package com.example.wind_turbine_tapes;

import android.content.Context;
import android.widget.TextView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecast {
    private ForecastConfiguration configuration;
    private Forecast forecast;
    private String tempActual;
    private String tempApparent;
    private String currentSummary;
    private String dailySummary;

    public WeatherForecast(Context mContext) {
        configuration =
                new ForecastConfiguration.Builder("091c99dcb62fc693cb74c0f83d2aae47")
                        .setCacheDirectory(mContext.getCacheDir())
                        .build();
        ForecastClient.create(configuration);
    }

    protected void getForecast(double lat, double lng, final String activity) {
        ForecastClient.getInstance()
                .getForecast(lat, lng, new Callback<Forecast>() {
                    @Override
                    public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                        if (response.isSuccessful()) {
                            forecast = response.body();
                            try {
                                double degreeAct = forecast.getCurrently().getTemperature();
                                int tempAct = (int) (degreeAct + 0.5);
                                tempActual = tempAct + " \u2109";

                                double degreeApp = forecast.getCurrently().getApparentTemperature();
                                int tempApp = (int) (degreeApp + 0.5);
                                tempApparent = tempApp + " \u2109";

                                currentSummary = forecast.getMinutely().getSummary();
                                dailySummary = forecast.getHourly().getSummary();
                                if (activity.equals("main")) {
                                    //MainActivity.weatherReport = new WeatherReport(tempActual, tempApparent, currentSummary, dailySummary);
                                    //MainActivity.updateTemperature();

                                }
                                else if (activity.equals("weather")) {
                                    //CurrentWeather.weatherReport = new WeatherReport(tempActual, tempApparent, currentSummary, dailySummary);
                                    //CurrentWeather.updateWeatherText();
                                }

                            }
                            catch (NullPointerException e) {
                                //degreeView.setText("N/A");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Forecast> forecastCall, Throwable t) {
                        // set all nulls
                    }
                });
    }
}
