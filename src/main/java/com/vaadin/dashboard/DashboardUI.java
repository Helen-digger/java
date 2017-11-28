package com.vaadin.dashboard;

import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import java.io.IOException;

@Title("Dashboard")
//@Theme("dashboard")
@SpringUI

public class DashboardUI extends UI {
    Logger logger;

    private CssLayout root;


   // @Autowired
    ViewDashboard viewDashboard;


    @Autowired
    private CounterRepository repository;
    static String ip;
    static String wip;
    static String geo;
    static String count;


    @Override
    protected void init(VaadinRequest request) {
        logger  = LogManager.getLogger();

        addStyleName(ValoTheme.UI_WITH_MENU);
        Responsive.makeResponsive(this);
        IpAddress(request);

        Counter cnt = repository.findByPage("all");
        Integer counter = new Integer(cnt.value);
        counter++;
        cnt.value = counter.toString();
        repository.save(cnt);
        count= counter.toString();

        logger.info(cnt);

        wip = getUI().getPage().getWebBrowser().getAddress();
        geo = getUI().getPage().getWebBrowser().getLocale().getDisplayName();
        logger.info(wip);
        logger.info(geo);

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
        viewDashboard = new ViewDashboard();
        root.addComponent(viewDashboard);
    }
}
