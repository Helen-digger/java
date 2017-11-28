package com.vaadin.dashboard;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.vaadin.ui.Button.*;


class SelectCity {
    private static final HashMap<String, String> citycode = new HashMap<>();

    public static HashMap<String, String> getCity() {
        citycode.put("524901", "Москва");
        citycode.put("498817", "Санкт - Петербург");
        citycode.put("1496747", "Новосибирск");

        return citycode;
    }
}

@SpringComponent
@SuppressWarnings("serial")
@UIScope


public class ViewDashboard extends Panel implements View {

    Logger logger;

    private final VerticalLayout layout;
    private Label titleLabel;
    private HorizontalLayout dashboardPanels;
    private Label dollar;
    private Label RemouteAddr;
    public String keyc;


    public ViewDashboard() {

        logger  = LogManager.getLogger();

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(true);
        setContent(layout);
        Responsive.makeResponsive(layout);
        layout.addComponent(buildHeader());

        Component content = buildContent();


        layout.addComponent(content);
        layout.setExpandRatio(content, 1);

        layout.addComponent(buildInformashion());
    }



    public Component buildContent() {
        dashboardPanels = new HorizontalLayout();
        dashboardPanels.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        dashboardPanels.setSpacing(true);
        dashboardPanels.setMargin(true);
        dashboardPanels.setSizeFull();
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildweather());
        dashboardPanels.addComponent(buildCurrency());
        dashboardPanels.addComponent(buildVisitation());

        return dashboardPanels;
    }


    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);
        header.setMargin(true);

        titleLabel = new Label("Dashboard");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        return header;
    }

    private Component buildInformashion() {
        HorizontalLayout inform = new HorizontalLayout();
        inform.setSpacing(true);
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Event event = null;
        String formatDateTime = date.format(formatter);

        Label dateLabel = new Label(formatDateTime);


        Panel address = new Panel("IP address:" + DashboardUI.ip);
        Panel addressw = new Panel("IP address:" + DashboardUI.wip);
        Panel geol = new Panel("IP address:" + DashboardUI.geo);

        inform.addComponents( dateLabel, address, addressw, geol);
        return inform;
    }


    public Component buildweather() {

        VerticalLayout weatherlayuot = new VerticalLayout();
        weatherlayuot.addStyleName(ValoTheme.LAYOUT_CARD);
        weatherlayuot.setWidth("100%");

        Label weather = new Label("Погода");
        WeatherMap weatherMap = new WeatherMap();

        weather.setStyleName(ValoTheme.LABEL_H2);
        HashMap<String, String> CityCode = SelectCity.getCity();
        Image image = new Image();
        ComboBox weatherbox = new ComboBox();

        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, String> e : CityCode.entrySet()) {
            list.add(e.getValue());
        }
        weatherbox.setItems(list);

        Label weath = new Label();
        weatherbox.setPlaceholder("No city selected");
        weatherbox.setEmptySelectionAllowed(false);
        weatherbox.addValueChangeListener((HasValue.ValueChangeEvent event) -> {
            for (String key : CityCode.keySet()) {
                if (CityCode.get(key).equals(event.getValue())) {
                    logger.info(key);
                    keyc = key;
                }
            }
            Notification.show("" + weatherMap.getWeatherNow(keyc),
                    String.valueOf(event.getValue()),
                    Notification.Type.TRAY_NOTIFICATION);
            weatherbox.getValue();
            weath.setValue(String.valueOf(weatherMap.weather.now.temp));
            image.setSource(new ExternalResource("http://openweathermap.org/img/w/" + weatherMap.weather.now.icon + ".png"));

        });


        weatherlayuot.addComponent(weatherbox);

        Button add = new Button("Обновить");
        add.addStyleName(ValoTheme.BUTTON_SMALL);
        add.setIcon(FontAwesome.REFRESH);
        add.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                weath.setValue(String.valueOf(weatherMap.weather.now.temp));
                image.setSource(new ExternalResource("http://openweathermap.org/img/w/" + weatherMap.weather.now.icon + ".png"));

            }
        });


        weatherlayuot.addComponents(weather, weatherbox, weath);
        weatherlayuot.addComponents(image);
        weatherlayuot.addComponent(add);

        weatherlayuot.setComponentAlignment(weather, Alignment.MIDDLE_LEFT);
        weatherlayuot.setComponentAlignment(weatherbox, Alignment.MIDDLE_LEFT);
        weatherlayuot.setComponentAlignment(add, Alignment.MIDDLE_LEFT);
        return weatherlayuot;
    }


    public Component buildCurrency() {
        VerticalLayout currencylayuot = new VerticalLayout();
        currencylayuot.addStyleName(ValoTheme.LAYOUT_CARD);
        currencylayuot.setWidth("100%");
        Label currency = new Label("Валюта");
        CBRDailyRu WriteCBR = new CBRDailyRu();
        currency.setStyleName(ValoTheme.LABEL_H2);
        dollar = new Label("" + WriteCBR.showrate());


        Button add = new Button("Обновить");
        add.addStyleName(ValoTheme.BUTTON_SMALL);

        add.setIcon(FontAwesome.REFRESH);
        add.addClickListener(event -> {
            dollar.setValue(WriteCBR.showrate());
            // date.setValue(LocalDateTime.now());
        });

        currencylayuot.addComponents(currency, dollar, add);

        return currencylayuot;
    }

    public Component buildVisitation() {
        VerticalLayout visitationlayuot = new VerticalLayout();
        visitationlayuot.addStyleName(ValoTheme.LAYOUT_CARD);
        visitationlayuot.setWidth("100%");
        Label visitation = new Label("Посещения");
        visitation.setStyleName(ValoTheme.LABEL_H2);
        Label quantity = new Label("Количество" + DashboardUI.count);
        visitationlayuot.addComponents(visitation, quantity);

        return visitationlayuot;
    }


    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        layout.addComponent(new Label());
    }

}

