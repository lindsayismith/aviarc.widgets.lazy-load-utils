/*global
YAHOO
*/

(function () {
    YAHOO.namespace("lazyLoad");
    var lazyLoad = YAHOO.lazyLoad;
    var Toronto = YAHOO.com.aviarc.framework.toronto;

    lazyLoad.LazyLoad = function() {
        
    };

    YAHOO.lang.extend(lazyLoad.LazyLoad , Toronto.framework.DefaultWidgetImpl, {
        // The 'startup' method may be deleted if it is not required, the method from DefaultWidgetImpl will be used
        // Removing the superclass.startup method call may prevent your widget from functioning
        startup: function (widgetContext) {
            lazyLoad.LazyLoad .superclass.startup.apply(this, arguments);
        },

        // The 'bind' method may be deleted if it is not required, the method from DefaultWidgetImpl will be used
        // Removing the superclass.bind method call may prevent your widget from functioning
        bind: function (dataContext) {
            lazyLoad.LazyLoad .superclass.bind.apply(this, arguments);
        },

        refresh: function () {
            
        },
        
        isRendered: function() {
            return this.getContainerElement().childNodes.length > 0;
        }
    });
})();
