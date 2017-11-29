package com.vaadin.dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * CBRCurrencyAPI receives information on exchange rates through the CentroBank API
 */



public class CBRCurrencyAPI {

    Logger logger;
    static final String URLDailyCBR = "https://www.cbr-xml-daily.ru/daily_json.js";
    public Double USD, EUR;

    /**
     * Get JSON data about currency exchange rates and call parser
     */
    public String GetRate() {

        logger  = LogManager.getLogger();
        logger.info("Request " + URLDailyCBR);

        String result = "";

        try {
            URL urlValute = new URL(URLDailyCBR);

            HttpURLConnection httpURLConnection = (HttpURLConnection) urlValute.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8193);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

                if (result.length() == 0) {
                    return "Server response empty";
                }

                String parseError = ParseResult(result);
                if (parseError != null) {
                    return "Server response invalid";
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
     * Parse received data
     * @param json - data received from https://www.cbr-xml-daily.ru/daily_json.js
     * @throws JSONException
     */
    private String ParseResult(String json) throws JSONException {

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject JSONObjectRate = jsonObject.getJSONObject("Valute");

            JSONObject JSONObjectUSD = JSONObjectRate.getJSONObject("USD");
            USD = JSONObjectUSD.getDouble("Value");

            JSONObject JSONObjectEUR = JSONObjectRate.getJSONObject("EUR");
            EUR = JSONObjectEUR.getDouble("Value");
        }catch (JSONException ex) {
            logger.error(ex.getMessage(), ex);
            return "Parse blablabla failed";
        }
        return null;
    }

}
