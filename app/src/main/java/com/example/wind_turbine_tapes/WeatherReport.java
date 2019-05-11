package com.example.wind_turbine_tapes;

public class WeatherReport {
    private String tempActual;
    private String tempApparent;
    private String currentSummary;
    private String dailySummary;

    public WeatherReport(String tempAc, String tempAp, String curSum, String dailySum) {
        tempActual = tempAc;
        tempApparent = tempAp;
        currentSummary = curSum;
        dailySummary = dailySum;
    }

    public String getTempActual() {
        return tempActual;
    }

    public String getTempApparent() {
        return tempApparent;
    }

    public String getCurrentSummary() {
        return currentSummary;
    }

    public String getDailySummary() {
        return dailySummary;
    }
}
