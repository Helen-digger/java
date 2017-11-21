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
            //"http://api.openweathermap.org/data/2.5/weather?q=Novosibirsk&APPID=79821d5373e0171e1b927d5e09e78578";
           // "http://api.openweathermap.org/data/2.5/group?id=524901,1496747,498817&units=metric&APPID=79821d5373e0171e1b927d5e09e78578";
    //static public String URL_OpenWeatherMap_code = "";
    static final String URL_OpenWeatherMap_ID = "&APPID=79821d5373e0171e1b927d5e09e78578";

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
    static private String ParseResult(String json) throws JSONException {

        String parsedResult = "";

        JSONObject jsonObject = new JSONObject(json);

        parsedResult += "Number of object = " + jsonObject.length() + "\n\n";

        //JSONObject jsonObject_list = jsonObject.getJSONObject("list");


        //"coord"
        JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
        Double result_lon = JSONObject_coord.getDouble("lon");
        Double result_lat = JSONObject_coord.getDouble("lat");

        //"sys"
        JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
        String result_country = JSONObject_sys.getString("country");
        int result_sunrise = JSONObject_sys.getInt("sunrise");
        int result_sunset = JSONObject_sys.getInt("sunset");

        //"weather"
        String result_weather;
        JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
        if (JSONArray_weather.length() > 0) {
            JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
            int result_id = JSONObject_weather.getInt("id");
            String result_main = JSONObject_weather.getString("main");
            String result_description = JSONObject_weather.getString("description");
            String result_icon = JSONObject_weather.getString("icon");

            result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: " + result_description + "\ticon: " + result_icon;
        } else {
            result_weather = "weather empty!";
        }

        //"base"
        String result_base = jsonObject.getString("base");

        //"main"
        JSONObject JSONObject_main = jsonObject.getJSONObject("main");
        Double result_temp = JSONObject_main.getDouble("temp");
        Double result_pressure = JSONObject_main.getDouble("pressure");
        Double result_humidity = JSONObject_main.getDouble("humidity");
        Double result_temp_min = JSONObject_main.getDouble("temp_min");
        Double result_temp_max = JSONObject_main.getDouble("temp_max");

        //"wind"
        JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
        Double result_speed = JSONObject_wind.getDouble("speed");
        //Double result_gust = JSONObject_wind.getDouble("gust");
        Double result_deg = JSONObject_wind.getDouble("deg");
        String result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

        //"clouds"
        JSONObject JSONObject_clouds = jsonObject.getJSONObject("clouds");
        int result_all = JSONObject_clouds.getInt("all");

        //"dt"
        int result_dt = jsonObject.getInt("dt");

        //"id"
        int result_id = jsonObject.getInt("id");

        //"name"
        String result_name = jsonObject.getString("name");

        //"cod"
        int result_cod = jsonObject.getInt("cod");

        return
                "coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n" +
                        "sys\tcountry: " + result_country + "\tsunrise: " + result_sunrise + "\tsunset: " + result_sunset + "\n" +
                        result_weather + "\n" +
                        "base: " + result_base + "\n" +
                        "main\ttemp: " + result_temp + "\thumidity: " + result_humidity + "\tpressure: " + result_pressure + "\ttemp_min: " + result_temp_min + "\ttemp_max: " + result_temp_min + "\n" +
                        result_wind + "\n" +
                        "clouds\tall: " + result_all + "\n" +
                        "dt: " + result_dt + "\n" +
                        "id: " + result_id + "\n" +
                        "name: " + result_name + "\n" +
                        "cod: " + result_cod + "\n" +
                        "\n";
    }
}
