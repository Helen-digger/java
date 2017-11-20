package com.example.client_vaadin;

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



public class CBRDailyRu {


    static final String URL_DailyCBR = "https://www.cbr-xml-daily.ru/daily_json.js";

    static public String showrate() {

        String result = "";

        try {
            URL url_valute = new URL(URL_DailyCBR);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_valute.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader =
                        new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader, 8193);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

                return ParseResult(result);

            } else {
                System.out.println("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(CBRDailyRu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CBRDailyRu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CBRDailyRu.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "ERRROR!!!";
    }
    static public String ParseResult(String json) throws JSONException {

        String parsedResult = "";


        JSONObject jsonObject = new JSONObject(json);

        parsedResult += "Number of object = " + jsonObject.length() + "\n\n";

        JSONObject JSONObject_Valute = jsonObject.getJSONObject("Valute");

        ////"USD"
       // String result_USD = JSONObject_Valute.getString("USD");
        JSONObject JSONObject_USD = JSONObject_Valute.getJSONObject("USD");
        Double result_Val_USD = JSONObject_USD.getDouble("Value");
        Double result_Prev_USD = JSONObject_USD.getDouble("Previous");

        //"EUR"
        //String result_EUR = JSONObject_Valute.getString("EUR");
        JSONObject JSONObject_EUR = JSONObject_Valute.getJSONObject("EUR");
        Double result_Val_EUR = JSONObject_EUR.getDouble("Value");
        Double result_Prev_EUR = JSONObject_EUR.getDouble("Previous");


        return "USD:" + result_Val_USD + "\n" + "EUR:" +  result_Val_EUR + "\n";
    }

}
