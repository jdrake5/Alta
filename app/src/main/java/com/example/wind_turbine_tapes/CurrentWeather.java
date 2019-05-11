package com.example.wind_turbine_tapes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.Forecast;

//ToDO add updating location services to CurrentWeather activity
//ToDO fix memory leaks mentioned below

public class CurrentWeather extends AppCompatActivity {
    //private static TextView currentLocal;
    //private static TextView feelsLike;
    //private static TextView actual;
    //private static TextView currentSummary;
    //private static TextView dailySummary;
    public static String cityLocalWeather;
    public static WeatherReport weatherReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_current_weather);

        //currentLocal = findViewById(R.id.current_local);
        //feelsLike = findViewById(R.id.feels_like_current_temp);
        //actual = findViewById(R.id.actually_current_temp);
        //currentSummary = findViewById(R.id.current_summary);
        //dailySummary = findViewById(R.id.daily_summary);

        Intent intent = getIntent();
        WeatherForecast wf = new WeatherForecast(CurrentWeather.this);
        wf.getForecast(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0), "weather");

        LocationRetrieval lr = new LocationRetrieval();
        lr.getLocation(CurrentWeather.this, intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0), "weather");
    }

    public static void updateWeatherText() {
        if (weatherReport != null) {
            //feelsLike.setText(weatherReport.getTempApparent());
            //actual.setText(weatherReport.getTempActual());
            //currentSummary.setText(weatherReport.getCurrentSummary());
            //dailySummary.setText(weatherReport.getDailySummary());
        }
    }

    public static void updateWeatherLocation() {
        //if (cityLocalWeather != null && currentLocal != null) {
            //currentLocal.setText(cityLocalWeather);
        //}
    }
}
