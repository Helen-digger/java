package com.example.client_vaadin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OpenWeatherMap {

    static final String URL_OpenWeatherMap_weather = "http://api.openweathermap.org/data/2.5/weather?id=";
            //"http://api.openweathermap.org/data/2.5/forecast?q=Novosibirsk&APPID=79821d5373e0171e1b927d5e09e78578";
           // "http://api.openweathermap.org/data/2.5/group?id=524901,1496747,498817&units=metric&APPID=79821d5373e0171e1b927d5e09e78578";
    //static public String URL_OpenWeatherMap_code = "";
    static final String URL_OpenWeatherMap_ID = "&APPID=79821d5373e0171e1b927d5e09e78578&units=metric";



    public static String Weather(String URL_OpenWeatherMap_code) {

        String result = "";

        try {
            URL url_weather = new URL(URL_OpenWeatherMap_weather+URL_OpenWeatherMap_code+URL_OpenWeatherMap_ID);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader =
                        new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader, 8192);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

                String weatherResult = ParseResult(result);
                System.out.println(result);
                return ParseResult(result);

            } else {
                System.out.println("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(OpenWeatherMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OpenWeatherMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(OpenWeatherMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ERRROR!!!";
    }
    static public String ParseResult(String json) throws JSONException {

        String parsedResult = "";

        JSONObject jsonObject = new JSONObject(json);

        parsedResult += "Number of object = " + jsonObject.length() + "\n\n";


        JSONObject JSONObject_main = jsonObject.getJSONObject("main");
        Double result_temp = JSONObject_main.getDouble("temp");



        return
                "temp: " + result_temp ;
    }
}
