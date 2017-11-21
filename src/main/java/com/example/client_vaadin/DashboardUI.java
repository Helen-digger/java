package com.example.client_vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.io.IOException;


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
        ClientAddressServlet clientAddressServlet = new ClientAddressServlet();
        try {
            Label ip = new Label(""+ clientAddressServlet.doPost(request));
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
