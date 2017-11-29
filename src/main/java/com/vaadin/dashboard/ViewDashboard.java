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

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.vaadin.ui.Button.*;

/**
 *SelectCity contains a list of cities and their corresponding codes
 */
class SelectCity {
    private static final HashMap<String, String> citycode = new HashMap<>();

    public static HashMap<String, String> getCity() {
        citycode.put("524901", "Москва");
        citycode.put("498817", "Санкт - Петербург");
        citycode.put("1496747", "Новосибирск");
        citycode.put("0000000", "Error URL");

        return citycode;
    }
}


/**
 *  WeatherPannel contains components for displaying and updating the weather forecast
 */
final class WeatherPannel {
    Label TempNow;
    Label TempTomorrow;
    Image ImgNow;
    Image ImgTomorrow;
    OpenWeatherMapForecatsAPI weatherMap;

    WeatherPannel() {
        TempNow = new Label();
        TempTomorrow = new Label();
        ImgNow = new Image();
        ImgTomorrow = new Image();
        weatherMap = new OpenWeatherMapForecatsAPI();
    }

    /**
     * Update request weather forecast and fills in the appropriate fields
     * @param keyc - city code to request a weather forecast
     */
    void Update(String keyc) {
        weatherMap.getWeatherNow(keyc);
        weatherMap.getWeatherTomorrow(keyc);
        String WeatherErrorNow = weatherMap.getWeatherNow(keyc);
        String WeatherErrorTomorrow = weatherMap.getWeatherTomorrow(keyc);
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        if (WeatherErrorNow != null)
        {
            TempNow.setValue(WeatherErrorNow);
            ImgNow.setSource( new FileResource(new File(basepath + "/Pic/1304.gif")));
        }
        else {
            TempNow.setValue("Температура текущая: " + (weatherMap.weather.now.temp.intValue()) + "°C");
            ImgNow.setSource(new ExternalResource("http://openweathermap.org/img/w/" + weatherMap.weather.now.icon + ".png"));
        }
        if (WeatherErrorTomorrow != null)
        {
            TempTomorrow.setValue(WeatherErrorTomorrow);
            ImgTomorrow.setSource( new FileResource(new File(basepath + "/Pic/1304.gif")));
        }
        else {
            TempTomorrow.setValue("Температура на завтра: " + (weatherMap.weather.tomorrow.temp.intValue()) + "°C");
            ImgTomorrow.setSource(new ExternalResource("http://openweathermap.org/img/w/" + weatherMap.weather.tomorrow.icon + ".png"));

        }
    }
}


@SpringComponent
@SuppressWarnings("serial")
@UIScope
/**
 * ViewDashboard displays the weather forecast, currency rate, number of visits, server time and client IP address of the client,
 * allows you to update the content separately, at the click of a button
 */
public class ViewDashboard extends Panel implements View {

    Logger logger;

    private final VerticalLayout layoutSkeleton;
    private Label titleLabel;
    private HorizontalLayout MainPanels;
    public String keyc;
    private Label dateLabel;
    private WeatherPannel weatherPannel;

    public ViewDashboard() {

        logger  = LogManager.getLogger();

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        layoutSkeleton = new VerticalLayout();
        layoutSkeleton.setSizeFull();
        layoutSkeleton.setSpacing(true);
        setContent(layoutSkeleton);
        Responsive.makeResponsive(layoutSkeleton);
        layoutSkeleton.addComponent(buildHeader());
        Component content = placePanels();
        layoutSkeleton.addComponent(content);
        layoutSkeleton.setExpandRatio(content, 1);
        layoutSkeleton.addComponent(buildDateandIP());
    }

    /**
     * placePanels places information panels inside MainPanels
     * @return
     */
    public Component placePanels() {
        MainPanels = new HorizontalLayout();
        MainPanels.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        MainPanels.setSpacing(true);
        MainPanels.setMargin(true);
        MainPanels.setSizeFull();
        Responsive.makeResponsive(MainPanels);

        MainPanels.addComponent(buildWeather());
        MainPanels.addComponent(buildCurrency());
        MainPanels.addComponent(buildVisits());

        return MainPanels;
    }


    /**
     * buildHeader displays the head of the Dashboard
     * @return
     */
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

    /**
     * buildDateandIP displays the server time and IP address of the client
     * @return
     */
    private Component buildDateandIP() {

        HorizontalLayout bottomLayout = new HorizontalLayout();

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = date.format(formatter);
        dateLabel = new Label("Информация по состоянию на " + formatDateTime);

        Label clientIP = new Label("Ваш IPaddress: " + DashboardUI.ip);

        bottomLayout.addComponents( dateLabel, clientIP);
        bottomLayout.setComponentAlignment(dateLabel, Alignment.BOTTOM_LEFT);
        bottomLayout.setComponentAlignment(clientIP, Alignment.BOTTOM_RIGHT);
        bottomLayout.setSpacing(true);
        bottomLayout.setWidth("100%");

        return bottomLayout;
    }


    /**
     * buildWeather displays the weather forecast for the current time and for tomorrow, allows you to select a city from the list and update the weather forecast
     * @return
     */
    public Component buildWeather() {

        VerticalLayout weatherLayuot = new VerticalLayout();
        VerticalLayout layoutForecast = new VerticalLayout();
        HorizontalLayout layoutWeatherNow = new HorizontalLayout();
        HorizontalLayout layoutWeatherTomorrow = new HorizontalLayout();
        weatherLayuot.addStyleName(ValoTheme.LAYOUT_CARD);
        weatherLayuot.setWidth("100%");
        weatherLayuot.setHeight("100%");

        weatherPannel = new WeatherPannel();

        Label weather = new Label("Погода");
        weather.setStyleName(ValoTheme.LABEL_H2);

        ComboBox weatherbox = new ComboBox();

        HashMap<String, String> CityCode = SelectCity.getCity();
        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, String> e : CityCode.entrySet()) {
            list.add(e.getValue());
        }
        weatherbox.setItems(list);
        weatherbox.setPlaceholder("No city selected");
        weatherbox.setEmptySelectionAllowed(false);
        weatherbox.addValueChangeListener((HasValue.ValueChangeEvent event) -> {
            for (String key : CityCode.keySet()) {
                if (CityCode.get(key).equals(event.getValue())) {
                    logger.info(key);
                    keyc = key;
                }
            }
            logger.info("ComboBox: city select " +  "\"" + weatherbox.getValue() +  "\"");
            weatherPannel.Update(keyc);
            dateLabel.setValue("Информация по состоянию на " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        });

        Button refresh = new Button("Обновить");
        refresh.addStyleName(ValoTheme.BUTTON_SMALL);
        refresh.setIcon(FontAwesome.REFRESH);
        refresh.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("Button click: update weather forecast");
                weatherPannel.Update(keyc);
                dateLabel.setValue("Информация по состоянию на " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
        });

        layoutWeatherNow.addComponents(weatherPannel.TempNow, weatherPannel.ImgNow);
        layoutWeatherNow.setComponentAlignment(weatherPannel.TempNow, Alignment.MIDDLE_LEFT);
        layoutWeatherNow.setComponentAlignment(weatherPannel.ImgNow, Alignment.MIDDLE_LEFT);
        layoutWeatherTomorrow.addComponents(weatherPannel.TempTomorrow, weatherPannel.ImgTomorrow);
        layoutWeatherTomorrow.setComponentAlignment(weatherPannel.TempTomorrow, Alignment.MIDDLE_LEFT);
        layoutWeatherTomorrow.setComponentAlignment(weatherPannel.ImgTomorrow, Alignment.MIDDLE_LEFT);

        layoutForecast.setSpacing(false);
        layoutForecast.setMargin(false);
        layoutForecast.addComponents(layoutWeatherNow);
        layoutForecast.addComponent(layoutWeatherTomorrow);

        weatherLayuot.addComponents(weather, weatherbox);
        weatherLayuot.addComponent(layoutForecast);
        weatherLayuot.addComponent(refresh);
        weatherLayuot.setComponentAlignment(weather, Alignment.TOP_CENTER);
        weatherLayuot.setComponentAlignment(weatherbox, Alignment.MIDDLE_LEFT);
        weatherLayuot.setComponentAlignment(refresh, Alignment.BOTTOM_LEFT);

        return weatherLayuot;
    }


    /**
     *  buildCurrency displays the exchange rates of the dollar and euro and update the data
     * @return
     */
    public Component buildCurrency() {
        VerticalLayout currencyLayuot = new VerticalLayout();
        VerticalLayout layoutExchangeRates = new VerticalLayout();
        HorizontalLayout layoutUSD = new HorizontalLayout();
        HorizontalLayout layoutEUR = new HorizontalLayout();
        currencyLayuot.addStyleName(ValoTheme.LAYOUT_CARD);
        currencyLayuot.setWidth("100%");
        currencyLayuot.setHeight("100%");

        Label currency = new Label("Курсы валют");
        currency.setStyleName(ValoTheme.LABEL_H2);

        CBRCurrencyAPI WriteCBR = new CBRCurrencyAPI();

        WriteCBR.GetRate();
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        Image usdImg = new Image();
        Image eurImg = new Image();
        String CurrencyErorr = WriteCBR.GetRate();
        Label dollar = new Label();
        Label euro = new Label();
        if (CurrencyErorr != null)
        {
            dollar.setValue(CurrencyErorr);
            euro.setValue(CurrencyErorr);
        } else {
            dollar.setValue("" + WriteCBR.USD);
            usdImg.setSource(new FileResource(new File(basepath + "/Pic/dollar.png")));
            euro.setValue("" + WriteCBR.EUR);
            eurImg.setSource(new FileResource(new File(basepath + "/Pic/euro.png")));
        }

        Button refresh = new Button("Обновить");
        refresh.addStyleName(ValoTheme.BUTTON_SMALL);

        refresh.setIcon(FontAwesome.REFRESH);
        refresh.addClickListener(event -> {
            logger.info("Button click: update exchange rates");
            WriteCBR.GetRate();
            dollar.setValue(String.valueOf(WriteCBR.USD));
            euro.setValue(String.valueOf(WriteCBR.EUR));
            dateLabel.setValue("Информация по состоянию на " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        });

        layoutUSD.addComponents(usdImg, dollar);
        layoutUSD.setComponentAlignment(usdImg, Alignment.MIDDLE_LEFT);
        layoutUSD.setComponentAlignment(dollar, Alignment.MIDDLE_LEFT);
        layoutEUR.addComponents(eurImg, euro);
        layoutEUR.setComponentAlignment(eurImg, Alignment.MIDDLE_LEFT);
        layoutEUR.setComponentAlignment(euro, Alignment.MIDDLE_LEFT);

        layoutExchangeRates.addComponents(layoutUSD, layoutEUR);
        layoutExchangeRates.setSpacing(false);
        layoutExchangeRates.setMargin(false);

        currencyLayuot.addComponents(currency, layoutExchangeRates, refresh);
        currencyLayuot.setComponentAlignment(currency, Alignment.TOP_CENTER);
        currencyLayuot.setComponentAlignment(refresh, Alignment.BOTTOM_LEFT);

        return currencyLayuot;
    }

    /**
     *  buildVisits displays the number of visits to the site
     * @return
     */
    public Component buildVisits() {
        VerticalLayout visitsLayout = new VerticalLayout();
        visitsLayout.addStyleName(ValoTheme.LAYOUT_CARD);
        visitsLayout.setWidth("100%");
        visitsLayout.setHeight("100%");

        Label visitsHeader = new Label("Счетчик посещений");
        visitsHeader.setStyleName(ValoTheme.LABEL_H2);

        Label quantity = new Label("" + DashboardUI.count);
        quantity.addStyleName(ValoTheme.LABEL_H1);

        visitsLayout.addComponents(visitsHeader, quantity);
        visitsLayout.setComponentAlignment(visitsHeader, Alignment.TOP_CENTER);
        visitsLayout.setComponentAlignment(quantity, Alignment.TOP_CENTER);

        return visitsLayout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        layoutSkeleton.addComponent(new Label());
    }

}

