package com.example.wind_turbine_tapes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationRetrieval {


    public void getLocation(Context mContext, double lat, double lng, String activity) {
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea().substring(0,2).toUpperCase();
            String cityState = city + ", " + state;
            if (activity.equals("main")) {
                //MainActivity.cityLocal = cityState;
                //MainActivity.updateLocation();
            }
            else if (activity.equals("weather")) {
                //CurrentWeather.cityLocalWeather = cityState;
                //CurrentWeather.updateWeatherLocation();
            }
        }
    }
}
