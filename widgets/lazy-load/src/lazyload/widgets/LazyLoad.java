package lazyload.widgets;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.aviarc.core.dataset.DatasetFieldName;
import com.aviarc.core.dataset.DatasetStack;
import com.aviarc.core.datatype.AviarcBoolean;
import com.aviarc.framework.toronto.screen.CompiledWidget;
import com.aviarc.framework.toronto.screen.RenderedNode;
import com.aviarc.framework.toronto.screen.ScreenRenderingContext;
import com.aviarc.framework.toronto.widget.DefaultDefinitionFile;
import com.aviarc.framework.toronto.widget.DefaultRenderedNodeFactory;
import com.aviarc.framework.toronto.widget.DefaultRenderedWidgetImpl;
import com.aviarc.framework.xml.compilation.ResolvedElementContext;

public class LazyLoad implements DefaultRenderedNodeFactory {
    private DefaultDefinitionFile _definition;
 
    /*
     * (non-Javadoc)
     * @see com.aviarc.framework.toronto.widget.DefaultRenderedNodeFactory#initialize(com.aviarc.framework.toronto.widget.DefaultDefinitionFile)
     */
    public void initialize(DefaultDefinitionFile definitionFile) {
        // Store the definition - our rendered node class requires it as it derives from DefaultRenderedNodeImpl
        this._definition = definitionFile;
    }
 
    /*
     * (non-Javadoc)
     * @see com.aviarc.framework.toronto.widget.RenderedNodeFactory#createRenderedNode(com.aviarc.framework.xml.compilation.ResolvedElementContext, com.aviarc.framework.toronto.screen.ScreenRenderingContext)
     */
    public RenderedNode createRenderedNode(ResolvedElementContext<CompiledWidget> elementContext,
                                           ScreenRenderingContext renderingContext) {
        return new LazyLoadImpl(elementContext, renderingContext, _definition);
    }
 
    /**
     * Our custom implementation of RenderedNode.
     * 
     * It derives from DefaultRenderedNodeImpl so that all the normal behaviour for widgets, e.g. javascript
     * constructors, requirements registering, required datasets etc are taken from the definition file.
     * 
     * We override the HTML generation method to provide our own markup.
     * 
     */
    public static class LazyLoadImpl extends DefaultRenderedWidgetImpl {
        private boolean _shouldRender;

        public LazyLoadImpl(ResolvedElementContext<CompiledWidget> resolvedContext,
                                            ScreenRenderingContext renderingContext, 
                                            DefaultDefinitionFile definition) {
            super(resolvedContext, renderingContext, definition, false);
            String action = this.getScreenRenderingContext().getCurrentState().getRequestState().getCurrentRequest().getAction();
            
            String lazyAttr = resolvedContext.getAttribute("lazy").getResolvedValue();
            
            this._shouldRender = "PartialRender".equals(action) || !AviarcBoolean.valueOf(lazyAttr).booleanValue();
            
            if (this._shouldRender) {
                this.addChildRenderedNodes();
            }
        }
 
        /**
         * Overridden to generate custom markup.
         */
        @Override
        public Node createXHTML(XHTMLCreationContext context) {
            // Use the current document to create an element
            Element div = context.getCurrentDocument().createElement("DIV");
            div.setAttribute("id", String.format("%1$s:container", getAttributeValue("name")));
            
            // Only actually render when we are asked to rerender
            if (this._shouldRender) {
                for (RenderedNode childWidget : getChildren()) {
                    Node childHtml = childWidget.createXHTML(context);
                    if (childHtml != null) {
                        div.appendChild(childHtml);
                    }
                }
            }

            
            return div;
        }
    }
}
