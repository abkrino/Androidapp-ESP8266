package com.example.mehdi.esp_wifi;

import android.app.Activity;;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends Activity implements OnClickListener
{
	private Button buttonRoom1,buttonRoom2,buttonRoom3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* Assign buttons */
        buttonRoom1 = (Button)findViewById(R.id.buttonRoom1);
        buttonRoom2 = (Button)findViewById(R.id.buttonRoom2);
        buttonRoom3 = (Button)findViewById(R.id.buttonRoom3);

        /* Set button listener (this class) */
        buttonRoom1.setOnClickListener(this);
        buttonRoom2.setOnClickListener(this);
        buttonRoom3.setOnClickListener(this);

        /* Call the method by which weather info can be fetched
         first check the network to see if it is available. */
        if (CheckNetwork.isInternetAvailable(this)) {
            try {
                RetrieveWeatherinfo();
            } catch (IOException e) {
                Toast.makeText(this, "Error: IOException!", Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                Toast.makeText(this, "Error: NullPointException!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View view)
    {
        if (view.getId() == buttonRoom1.getId()) {
            startActivity(new Intent(getApplicationContext(), ActivityRoom1.class));
        } else if (view.getId() == buttonRoom2.getId()) {


        } else if (view.getId() == buttonRoom3.getId()) {


        }
    }


    /**
     * @name    RetrieveWeatherinfo
     * @brief   The method executes AsyncTask to get weather info based on URL given by user
     * @author  Mehdi
     *
     * @throws  IOException
     */
    private void RetrieveWeatherinfo() throws IOException
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Qazvin,IR&APPID=849562acb922a2f4f63113690698e74c";

        /* Make an object of weatherServiceAsync Activity */
        WeatherServiceAsync task = new WeatherServiceAsync(this);

        /* Execute Asynctask */
        task.execute(url);
    }


    /**
     * @name    SetDescription
     * @brief   The method called from WeatherServiceAsync to set display the weather condition
     * @author  Mehdi
     */
    public void SetDescription(String description){
        TextView view = (TextView) this.findViewById(R.id.textDescriptionValue);

        view.setText(description);
    }

    /**
     * @name    SetTemperature
     * @brief   The method called from WeatherServiceAsync to set display the temperature based on Celsius
     *          fetched from online weather forecasting website
     * @author  Mehdi
     */

    public void SetTemperature(double temperature,double tempMax, double tempMin)
    {
        TextView view  = (TextView) this.findViewById(R.id.textTemperatureValue);
        TextView viewM = (TextView) this.findViewById(R.id.txtTempMinMax);

        DecimalFormat decimalFormat  = new DecimalFormat("###.##");

        String formattedTemperature  = decimalFormat.format(temperature);

        view.setText(formattedTemperature + "°C");

        DecimalFormat decimalFormat2 = new DecimalFormat("###");

        String formattedTempMin      = decimalFormat2.format(tempMax);
        String formattedTempMax      = decimalFormat2.format(tempMin);

        viewM.setText("  H " + formattedTempMax + "°/ L " + formattedTempMin + "°");
    }

    /**
     * @name    SetPressure
     * @brief   The method called from WeatherServiceAsync to set show the pressure based on KPa
     *          fetched from online weather forcasting website
     * @author  Mehdi
     */

    public void SetPressure(double pressure)
    {
        TextView view               = (TextView) this.findViewById(R.id.textPressureValue);

        DecimalFormat decimalFormat = new DecimalFormat("####.#");

        String formattedPressure    = decimalFormat.format(pressure);

        view.setText(formattedPressure + " KPa");
    }

    /**
     * @name    SetHumidity
     * @brief   The method called from WeatherServiceAsync to set show the humidity based on percent
     *          fetched from online weather forcasting website
     * @author  Mehdi
     */

    public void SetHumidity(double humidity)
    {
        TextView view               = (TextView) this.findViewById(R.id.textHumidityValue);

        DecimalFormat decimalFormat = new DecimalFormat("##.#");

        String formattedHumidity    = decimalFormat.format(humidity);

        view.setText(formattedHumidity + " %");
    }

    /**
     * @name    SetWindSpeed
     * @brief   The method called from WeatherServiceAsync to set show the wind speed based on kmph
     *          fetched from online weather forcasting website
     * @author  Mehdi
     */

    public void SetWindSpeed(double windSpeed)
    {
        TextView view               = (TextView) this.findViewById(R.id.textWindSpeedValue);

        DecimalFormat decimalFormat = new DecimalFormat("##.###");

        String formattedWindSpeed   = decimalFormat.format(windSpeed);

        view.setText(formattedWindSpeed + " kmph");
    }

    /**
     * @name    ErrorHandler
     * @brief   The method called from WeatherServiceAsync any infor about the weather was not fetched.
     * @author  Mehdi
     */

    public void ErrorHandler()
    {
        Toast.makeText(this,"Data Was Not Fetched!!",Toast.LENGTH_LONG).show();
    }
}