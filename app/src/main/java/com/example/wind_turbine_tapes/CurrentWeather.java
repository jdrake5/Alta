package com.example.wind_turbine_tapes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.Forecast;

public class CurrentWeather extends AppCompatActivity {
    private TextView currentLocal;
    private TextView feelsLike;
    private TextView actual;
    private TextView currentSummary;
    private TextView dailySummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_current_weather);

        currentLocal = findViewById(R.id.current_local);
        feelsLike = findViewById(R.id.feels_like_current_temp);
        actual = findViewById(R.id.actually_current_temp);
        currentSummary = findViewById(R.id.current_summary);
        dailySummary = findViewById(R.id.daily_summary);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getBoolean("valid")) {
            currentLocal.setText(bundle.getString("city"));
            feelsLike.setText(bundle.getString("apparent"));
            actual.setText(bundle.getString("actual"));
            currentSummary.setText(bundle.getString("summary"));
            dailySummary.setText(bundle.getString("daily"));
        }
        else {
            //make weather call
        }
    }
}
