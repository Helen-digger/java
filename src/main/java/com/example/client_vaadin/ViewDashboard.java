package com.example.client_vaadin;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.WebBrowser;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;

class SelectCity {
    private static final HashMap<String, String> citycode = new HashMap<>();

    public static HashMap <String, String> getCity() {
        citycode.put("524901", "Москва");
        citycode.put("498817", "Санкт - Петербург");
        citycode.put("1496747", "Новосибирск");

        return citycode;
    }
}
    @SpringComponent

@SuppressWarnings("serial")


public class ViewDashboard extends Panel {

    private final VerticalLayout layout;
    private Label titleLabel;
    private CssLayout dashboardPanels;
    private Label dollar;


    public ViewDashboard() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(false);
        setContent(layout);
        Responsive.makeResponsive(layout);
        layout.addComponent(buildHeader());

        Component content = buildContent();
        layout.addComponent(content);
        layout.setExpandRatio(content, 1);
        layout.addComponent(buildInformashion());

    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        dashboardPanels.setWidth("100%");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildweather());
        dashboardPanels.addComponent(buildCurrency());
        dashboardPanels.addComponent(buildVisitation());

        return dashboardPanels;
    }


    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Dashboard");
        //titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);


        return header;
    }

        /**
         *
         */

        private Component buildInformashion() {

        HorizontalLayout inform = new HorizontalLayout();
        DateTimeField date = new DateTimeField();
            date.setValue(LocalDateTime.now());
        date.addValueChangeListener(event -> Notification.show("Value changed:",
                String.valueOf(event.getValue()),
                Notification.Type.TRAY_NOTIFICATION));

        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        Panel address = new Panel("IP address:" + webBrowser.getAddress() );
        Panel location = new Panel("Location" + webBrowser.getLocale().getDisplayName());

        inform.addComponent(address);
        inform.addComponent(location);
        inform.addComponent(date);

        return inform;
    }



    public Component buildweather() {



        VerticalLayout weatherlayuot = new VerticalLayout();

        Label weather = new Label("Погода");

        weather.setStyleName(ValoTheme.LABEL_H2);
        //HashMap <String, String> CityCode = SelectCity.getCity();
        ComboBox weatherbox = new ComboBox();

        //BeanItemContainer<SelectCity> CityCode= new BeanItemContainer(SelectCity.class, citycode);
        //for(Map.Entry<String,String> e :CityCode.entrySet()){

       //     weatherbox.addItem(e.getKey());
       //     weatherbox.setValue(e.getValue());
       // }
       // weatherbox.setPlaceholder("No city selected");
       // weatherbox.setItems("Москва","Санкт - Петербург","Новосибирск");
        weatherbox.setEmptySelectionAllowed(false);
      //  weatherbox.setInvalidAllowed(false);


        Label LabelToday = new Label();
        Label LabelTommorow = new Label();

        Button add = new Button("Обновить");
        add.setIcon(FontAwesome.REFRESH);
        add.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

            }
        });

        weatherlayuot.addComponents(weather,weatherbox,add);
        weatherlayuot.setComponentAlignment(weather, Alignment.MIDDLE_LEFT);
        weatherlayuot.setComponentAlignment(weatherbox, Alignment.MIDDLE_LEFT);
        weatherlayuot.setComponentAlignment(add, Alignment.MIDDLE_LEFT);
        weatherlayuot.addStyleName(ValoTheme.PANEL_BORDERLESS);
        weatherlayuot.setSizeFull();
        return weatherlayuot;
    }


    private Component buildCurrency(){
        VerticalLayout currencylayuot = new VerticalLayout();
        Label currency  = new Label("Валюта");
        CBRDailyRu WriteCBR = new CBRDailyRu();
        currency.setStyleName(ValoTheme.LABEL_H2);
        dollar = new Label("" + WriteCBR.showrate());
        Button add = new Button("Обновить");

        add.setIcon(FontAwesome.REFRESH);
        add.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                dollar.setValue("" + WriteCBR.showrate());
            }
        });

        currencylayuot.addComponents(currency,dollar,add);

        return currencylayuot;
    }

    private Component buildVisitation(){
        VerticalLayout visitationlayuot = new VerticalLayout();
        Label visitation  = new Label("Посещения");
        visitation.setStyleName(ValoTheme.LABEL_H2);
        Label quantity = new Label("Количество");
        visitationlayuot.addComponents(visitation,quantity);

        return visitationlayuot;
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        card.addComponent(content);
        slot.addComponent(card);
        return slot;

    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = layout.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }
}

