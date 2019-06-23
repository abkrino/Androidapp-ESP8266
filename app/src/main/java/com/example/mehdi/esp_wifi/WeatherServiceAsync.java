package com.example.mehdi.esp_wifi;


import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * @name    WeatherServiceAsync
 * @brief   The class tries to fetch weather info by using http request protocol
 *          from the URL given by the user in main activity, then desirable info
 *          from the received data are parsed out and display it based on desirable format
 * @author  Mehdi
 */

public class WeatherServiceAsync extends AsyncTask<String,Void,String> {

    private final MainActivity WeatherActivity;

    /* This constructor takes the activity as the parameter. */
    public WeatherServiceAsync(MainActivity mainActivity) {
        this.WeatherActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... urls) {

        /* This weather service method will be called after the service executes.
           It will run as a separate process, and will populate the activity in the onPostExecute
           method below */

        String response = "";

        /* Loop through the urls (there should only be one!) and call an http Get using the URL passed
           to this service */
        for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {

                /* make the http request for the weather data */
                HttpResponse execute = client.execute(httpGet);

                /* get the content of the result returned when the response comes back
                   it should be a json object */
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";

                /* populate the response string which will be passed later into the post execution */
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        String test = result;
        if (test != null) {
            try {
                /* parse the json result returned from the service */
                JSONObject jsonResult = new JSONObject(test);

                /* parse out the temperature from the JSON result */
                double temperature = jsonResult.getJSONObject("main").getDouble("temp");
                temperature = ConvertTemperatureToCentigrade(temperature);

                double tempMax = jsonResult.getJSONObject("main").getDouble("temp_max");
                tempMax = ConvertTemperatureToCentigrade(tempMax);

                double tempMin = jsonResult.getJSONObject("main").getDouble("temp_min");
                tempMin = ConvertTemperatureToCentigrade(tempMin);

                /* parse out the pressure from the JSON Result */
                double pressure = jsonResult.getJSONObject("main").getDouble("pressure");
                pressure = ConvertPressureToKPa(pressure);

                /* parse out the humidity from the JSON result */
                double humidity = jsonResult.getJSONObject("main").getDouble("humidity");

                /* parse out the humidity from the JSON result */
                double windSpeed = jsonResult.getJSONObject("wind").getDouble("speed");

                /* parse out the description from the JSON result */
                String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");

                /* set all the fields in the activity from the parsed JSON */
                this.WeatherActivity.SetDescription(description);
                this.WeatherActivity.SetTemperature(temperature,tempMax,tempMin);
                this.WeatherActivity.SetPressure(pressure);
                this.WeatherActivity.SetHumidity(humidity);
                this.WeatherActivity.SetWindSpeed(windSpeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            this.WeatherActivity.ErrorHandler();
        }
    }

    /**
     * @name    ConvertTemperatureToCentigrade
     * @brief   The method converts temperature from Kelvin to celsius
     * @author  Mehdi
     *
     */

    private double ConvertTemperatureToCentigrade(double temperature) {
        return (temperature - 273.15);
    }

    /**
     * @name    ConvertPressureToKPa
     * @brief   The method converts pressure fetched from given URL to KPa
     * @author  Mehdi
     *
     */

    private double ConvertPressureToKPa(double pressure){
        return (pressure * 0.1);
    }

}
