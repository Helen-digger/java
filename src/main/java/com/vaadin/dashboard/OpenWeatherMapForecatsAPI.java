package com.vaadin.dashboard;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DayForecast contains the temperature and icon fields
 */
final class DayForecast {
    Double temp;
    String icon;
}

/**
 * Forecast contains the weather forecast for the moment and for tomorrow
 */
final class Forecast {
    DayForecast now;
    DayForecast tomorrow;

    Forecast() {

        now = new DayForecast();
        tomorrow = new DayForecast();
    }
}

/**
 * OpenWeatherMapForecatsAPI receives information about weather forecast for the moment and for tomorrow
 */
public class OpenWeatherMapForecatsAPI {
    static final String URLPreambulNow = "http://api.openweathermap.org/data/2.5/weather?id=";
    static final String URLPreambulDays = "http://api.openweathermap.org/data/2.5/forecast?id=";
    static final String APPID = "&APPID=79821d5373e0171e1b927d5e09e78578&units=metric";
    public Forecast weather = new Forecast();

    Logger logger;

    /**
     * Get JSON data about weather forecast for the moment and call parser
     * @param cityCode - city identifier from list of city ID
     * @return
     */
    public String getWeatherNow(String cityCode) {

        logger  = LogManager.getLogger();
        logger.info("Request " + URLPreambulNow + cityCode + APPID);
        String forecastJson = "";

        try {
            URL url_weather = new URL(URLPreambulNow + cityCode + APPID);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    forecastJson += line;
                }
                bufferedReader.close();

                if (forecastJson.length() == 0) {
                    return "Server response empty";
                }

                ParseNow(forecastJson);

            } else {
                logger.error("Error in httpURLConnection.getResponseCode()!!!");
                return "httpURLConnection.getResponseCode failed";
            }

        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Get JSON data about weather forecast or tomorrow and call parser
     * @param cityCode - city identifier from list of city ID
     * @return
     */
    public String getWeatherTomorrow(String cityCode) {
        logger  = LogManager.getLogger();
        logger.info("Request " + URLPreambulNow + cityCode + APPID);
        String forecastJson = "";

        try {
            URL url_weather = new URL(URLPreambulDays + cityCode + APPID);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

                String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        forecastJson += line;
                    }
                    bufferedReader.close();

                    ParseTomorrow(forecastJson);
                if (forecastJson.length() == 0) {
                    return "Server response empty";
                }


            } else {
                logger.error("Error in httpURLConnection.getResponseCode()!!!");
                return "httpURLConnection.getResponseCode failed";
            }

        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     *  Parse received data about weather forecast for the moment
     * @param json
     * @throws JSONException
     */
    private void ParseNow(String json) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject JSONObjectMain = jsonObject.getJSONObject("main");
            weather.now.temp = JSONObjectMain.getDouble("temp");

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject weatherListItem = jsonArray.getJSONObject(0);
            weather.now.icon = weatherListItem.getString("icon");
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     *  Parse received data about weather forecast or tomorrow
     * @param json
     * @throws JSONException
     */
    private void ParseTomorrow(String json) throws JSONException {

        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("list");
            JSONObject listUnit = list.getJSONObject(8);
            JSONObject main = listUnit.getJSONObject("main");
            weather.tomorrow.temp = main.getDouble("temp");

            JSONArray jsonWeather = listUnit.getJSONArray("weather");
            JSONObject jsonWeatherUnit = jsonWeather.getJSONObject(0);
            weather.tomorrow.icon = jsonWeatherUnit.getString("icon");
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
