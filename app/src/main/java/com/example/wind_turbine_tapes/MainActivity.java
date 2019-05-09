package com.example.wind_turbine_tapes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView degreeView;
    private TextView localView;
    private SlidingUpPanelLayout mLayout;
    private ForecastConfiguration configuration;
    private double prevLat;
    private double prevLng;
    private LinearLayout weather_view;
    private Forecast forecast;
    private String cityLocal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);

        weather_view = findViewById(R.id.weather_view_main);
        weather_view.setOnClickListener(this);
        degreeView = findViewById(R.id.degreeView);
        localView = findViewById(R.id.locationText);
        localView.setSelected(true);

        configuration =
                new ForecastConfiguration.Builder("091c99dcb62fc693cb74c0f83d2aae47")
                        .setCacheDirectory(getCacheDir())
                        .build();
        ForecastClient.create(configuration);


        //For Demo Testing
        ListView lv = findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);
        //End Demo Testing


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //when panel slides
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //when panel state changes
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                final double lat = location.getLatitude();
                                final double lng = location.getLongitude();
                                ForecastClient.getInstance()
                                        .getForecast(lat, lng, new Callback<Forecast>() {
                                            @Override
                                            public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                                                if (response.isSuccessful()) {
                                                    forecast = response.body();
                                                    try {
                                                        double degree = forecast.getCurrently().getApparentTemperature();
                                                        int temp = (int) (degree + 0.5);
                                                        String temperature = temp + " \u2109";
                                                        degreeView.setText(temperature);
                                                    }
                                                    catch (NullPointerException e) {
                                                        degreeView.setText("N/A");
                                                    }

                                                    Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
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
                                                        localView.setText(cityState);
                                                        cityLocal = cityState;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Forecast> forecastCall, Throwable t) {

                                            }
                                        });
                                prevLat = lat;
                                prevLng = lng;
                            }
                        }
                    });
        }
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    final double lat = location.getLatitude();
                    final double lng = location.getLongitude();
                    ForecastClient.getInstance()
                            .getForecast(lat, lng, new Callback<Forecast>() {
                                @Override
                                public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                                    if (response.isSuccessful()) {
                                        Forecast forecast = response.body();
                                        try {
                                            double degree = forecast.getCurrently().getApparentTemperature();
                                            int temp = (int) (degree + 0.5);
                                            String temperature = temp + " \u2109";
                                            degreeView.setText(temperature);
                                        }
                                        catch (NullPointerException e) {
                                            degreeView.setText("N/A");
                                        }

                                        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
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
                                            localView.setText(cityState);
                                            cityLocal = cityState;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Forecast> forecastCall, Throwable t) {

                                }
                            });
                    prevLat = lat;
                    prevLng = lng;
                }
            }
        };
        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CurrentWeather.class);
        if (forecast != null) {
            String feelsLike = "" + forecast.getCurrently().getApparentTemperature();
            String actualTemp = "" + forecast.getCurrently().getTemperature();
            String currentSummary = "" + forecast.getMinutely().getSummary();
            String dailySummary = "" + forecast.getHourly().getSummary();
            intent.putExtra("apparent", feelsLike);
            intent.putExtra("actual", actualTemp);
            intent.putExtra("summary", currentSummary);
            intent.putExtra("daily",dailySummary);
            intent.putExtra("city", cityLocal);
            intent.putExtra("valid", true);
        }
        else {
            intent.putExtra("valid", false);
        }
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
