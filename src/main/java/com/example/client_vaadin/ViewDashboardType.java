package com.example.client_vaadin;
 import com.example.client_vaadin.ViewDashboard;
 import com.vaadin.navigator.View;
 import com.vaadin.server.FontAwesome;
 import com.vaadin.server.Resource;


public enum  ViewDashboardType {
    DASHBOARD("dashboard", ViewDashboard.class, FontAwesome.HOME, true);

    private final String viewName;
    //private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private ViewDashboardType(final String viewName,
                              final Class<ViewDashboard> viewClass, final Resource icon,
                              final boolean stateful) {
        this.viewName = viewName;
        //this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    //public Class<? extends View> getViewClass() {
     //   return viewClass;
    //}

    public Resource getIcon() {
        return icon;
    }

    public static ViewDashboardType getByViewName(final String viewName) {
        ViewDashboardType result = null;
        for (ViewDashboardType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
