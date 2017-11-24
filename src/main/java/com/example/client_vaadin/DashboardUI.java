package com.example.client_vaadin;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.client_vaadin.ViewDashboard;

import javax.servlet.ServletException;
import java.io.IOException;

@Title("Dashboard")
@Theme("valo")
@SpringUI

public class DashboardUI extends UI {

    private CssLayout root;

    @Autowired
    ViewDashboard viewDashboard;
    private CounterRepository repository;

    @Override
    protected void init(VaadinRequest request) {
        addStyleName(ValoTheme.UI_WITH_MENU);
        Responsive.makeResponsive(this);
        setupLayuot();
        addHeader();
        //addForm();

        ClientAddressServlet clientAddressServlet = new ClientAddressServlet();
        try {
            Label ip = new Label("" + clientAddressServlet.doPost(request));
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Counter cnt  = repository.findByPage("all");
        Integer counter = new Integer(cnt.value);
        counter++;
        cnt.value =  counter.toString();
        repository.save(cnt);
        //System.out.println(cnt);

    }


    private void setupLayuot() {
        root = new CssLayout();
        root.setSizeFull();
        setContent(root);
        //root.addStyleName("dashboard");


    }



    private void addHeader() {
        root.addComponent(viewDashboard);
    }

}
