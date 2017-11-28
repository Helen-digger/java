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

final class DayForecast {
    Double temp;
    String icon;
}

final class Forecast {
    DayForecast now;
    DayForecast tomorrow;

    Forecast() {
        now = new DayForecast();
        tomorrow = new DayForecast();
    }
}

public class WeatherMap {
    static final String URL_preambul_now = "http://api.openweathermap.org/data/2.5/weather?id=";
    static final String URL_preambul_days = "http://api.openweathermap.org/data/2.5/forecast?id=";
    static final String APPID = "&APPID=79821d5373e0171e1b927d5e09e78578&units=metric";
    public Forecast weather = new Forecast();

    Logger logger;

    public String getWeatherNow(String city_code) {

        logger  = LogManager.getLogger();
        logger.info("!!!!!!!!!!!!!!weather!!!!!!!!!!!!!!!!!");
        String forecast_json = "";

        try {
            URL url_weather = new URL(URL_preambul_now+city_code+APPID);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    forecast_json += line;
                }
                bufferedReader.close();

                ParseNow(forecast_json);

            } else {
                logger.error("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "ERRROR!!!";
    }

    private String getWeatherTomorrow(String city_code) {
        String forecast_json = "";

        try {
            URL url_weather = new URL(URL_preambul_days+city_code+APPID);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    forecast_json += line;
                }
                bufferedReader.close();

                ParseDay(forecast_json);

            } else {
                logger.error("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "ERRROR!!!";
    }

    private void ParseNow(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject JSONObject_main = jsonObject.getJSONObject("main");
        weather.now.temp = JSONObject_main.getDouble("temp");

        JSONArray weather_list = jsonObject.getJSONArray("weather");
        JSONObject weather_list_item = weather_list.getJSONObject(0);
        weather.now.icon = weather_list_item.getString("icon");
    }

    private void ParseDay(String json) throws JSONException {
        logger.info(json);
        JSONObject jsonObject = new JSONObject(json);
        int cnt = jsonObject.getInt("cnt");
        JSONArray list = jsonObject.getJSONArray("list");
        JSONObject list_unit = list.getJSONObject(8);
        String first_unit_date = list_unit.getString("dt_txt");

        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String nextday =
        Date night = new Date()
        Date date = null;
        try {
            date = df.parse(first_unit_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info(df.format(date));*/

        JSONObject main = list_unit.getJSONObject("main");
        weather.tomorrow.temp = main.getDouble("temp");

        JSONArray json_weather = list_unit.getJSONArray("weather");
        JSONObject json_weather_unit = json_weather.getJSONObject(0);
        weather.tomorrow.icon = json_weather_unit.getString("icon");
    }

    public void ShowWeather() {
        WeatherMap map = new WeatherMap();

        logger.info("T " + map.weather.now.temp + " icon: " + map.weather.now.icon);
        logger.info("T " + map.weather.tomorrow.temp + " icon: " + map.weather.tomorrow.icon);
    }
}
