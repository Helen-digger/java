package com.example.client_vaadin;

import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;


@SpringUI
public class DashboardUI extends UI {

    private CssLayout root;

    @Autowired
    ViewDashboard viewDashboard;

    @Override
    protected void init(VaadinRequest request) {
        setupLayuot();
        addHeader();
        //addForm();

    }

    private void setupLayuot() {
        root = new CssLayout();
        root.setSizeFull();
        setContent(root);


    }


    private void addHeader() {
        // root.addComponent(new Label("Погода"));
        root.addComponent(viewDashboard);

    }

}
