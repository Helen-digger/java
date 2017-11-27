package com.example.client_vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Title("Dashboard")
//@Theme("dashboard")
@SpringUI

public class DashboardUI extends UI {

    private CssLayout root;


    @Autowired
    ViewDashboard viewDashboard;
    @Autowired
    private CounterRepository repository;
    static String ip;
    static String wip;
    static String geo;


    @Override
    protected void init(VaadinRequest request) {
        addStyleName(ValoTheme.UI_WITH_MENU);
        Responsive.makeResponsive(this);
        IpAddress(request);

        Counter cnt = repository.findByPage("all");
        Integer counter = new Integer(cnt.value);
        counter++;
        cnt.value = counter.toString();
        repository.save(cnt);
        System.out.println(cnt);

        wip = getUI().getPage().getWebBrowser().getAddress();
        geo = getUI().getPage().getWebBrowser().getLocale().getDisplayName();
        System.out.println(wip);
        System.out.println(geo);

        setupLayuot();
        addHeader();
    }

    private String IpAddress(VaadinRequest request){
        ClientAddressServlet clientAddressServlet = new ClientAddressServlet();
        try {
            String ipAddr =  clientAddressServlet.doPost(request);
            ip=ipAddr;
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERRROR!!!";
    }

    private void setupLayuot() {
        root = new CssLayout();
        root.setSizeFull();
        setContent(root);
    }

    private void addHeader() {
        root.addComponent(viewDashboard);
    }
}
