package app.my.weatherx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private Context context;

    private RelativeLayout rl_home;
    private ProgressBar prog_loading;
    private TextView tv_cityName, tv_temp, tv_condition, tv_feel, tv_wind;
    private TextInputEditText input_city;
    private ImageView img_bg, img_weather, img_search;
    private RecyclerView rv_forecast;

    private ArrayList<WeatherRCModal> weatherRCModalArrayList;
    private WeatherRCAdapter weatherRCAdapter;

    private LocationManager locationManager;
    private int PERMIT_CODE = 1;
    private String CITY_NAME;

    public HomeFragment(Context cntex) {
        this.context = cntex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Init all assets
        rl_home = rootView.findViewById(R.id.rl_home);
        prog_loading = rootView.findViewById(R.id.prog_loading);
        tv_cityName = rootView.findViewById(R.id.cityName);
        tv_temp = rootView.findViewById(R.id.count_temperature);
        tv_condition = rootView.findViewById(R.id.text_temperature);
        tv_feel = rootView.findViewById(R.id.text_feel);
        tv_wind = rootView.findViewById(R.id.text_wind);
        input_city = rootView.findViewById(R.id.cityInput);
        img_bg = rootView.findViewById(R.id.bgHomeBlack);
        img_weather = rootView.findViewById(R.id.img_temperature);
        img_search = rootView.findViewById(R.id.img_btn_search);
        rv_forecast = rootView.findViewById(R.id.rv_forecast);

        weatherRCModalArrayList = new ArrayList<>();
        weatherRCAdapter = new WeatherRCAdapter(requireContext(), weatherRCModalArrayList);
        rv_forecast.setAdapter(weatherRCAdapter);

        // Location & Provider Check
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // The provider is enabled, proceed to get the last known location
            // Check if location permissions are granted
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permissions not granted, request them
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMIT_CODE);
            } else {
                // Permissions granted, get the last known location
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    Toast.makeText(context, "Couldn't get location! Loading default.", Toast.LENGTH_SHORT).show();
                    // Set default location
                    CITY_NAME = "Qingdao";
                    get_weather_info(CITY_NAME);
                    // Continue with your logic here
                } else {
                    // Handle the case when the last known location is not available
                    String get_city = get_city_name(location.getLatitude(), location.getLongitude());
                    if(get_city.isEmpty()) {
                        CITY_NAME = "Qingdao";
                        Toast.makeText(context, "Please check your location permission!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        CITY_NAME = get_city;
                        get_weather_info(CITY_NAME);
                    }
                }
            }
        } else {
            // Handle the case when the provider is not enabled
            CITY_NAME = "Qingdao";
            get_weather_info(CITY_NAME);
            Toast.makeText(context, "Last location unknown! Loading default.", Toast.LENGTH_SHORT).show();
        }

        // Listener for search icon click
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = input_city.getText().toString();
                if(city.isEmpty()) {
                    Toast.makeText(context, "Please type city name first!", Toast.LENGTH_SHORT).show();
                }
                else {
                    tv_cityName.setText(city);
                    get_weather_info(city);
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }

    private String get_city_name(double latitude, double longitude) {
        String city_name = "Not found!";
        Geocoder geocoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            for (Address add : addresses) {
                if(add != null) {
                    String city = add.getLocality();
                    if(city != null && !city.equals("")) {
                        city_name = city;
                    }
                    else {
                        Toast.makeText(context, "City not found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city_name;
    }

    private void get_weather_info(String city_name) {
        tv_cityName.setText(city_name);
        String url = "https://api.weatherapi.com/v1/forecast.json?key=4f4a5599137e4e86bf0163332232312&q=" + city_name + "&days=1&aqi=yes&alerts=yes";

        Toast.makeText(context, "Please wait data loading.", Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Make app visible
                        prog_loading.setVisibility(View.GONE);
                        rl_home.setVisibility(View.VISIBLE);
                        weatherRCModalArrayList.clear();

                        try {
                            String temperature = response.getJSONObject("current").getString("temp_c");
                            tv_temp.setText(temperature + "°C");

                            int is_day = response.getJSONObject("current").getInt("is_day");
                            String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            tv_condition.setText(condition);

                            String condition_icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("https:".concat(condition_icon)).resize(64, 64).into(img_weather);

                            String feel_like = response.getJSONObject("current").getString("feelslike_c");
                            tv_feel.setText("Feels Like " + feel_like +  "°C");

                            String wind_speed = response.getJSONObject("current").getString("wind_kph");
                            String wind_dir = response.getJSONObject("current").getString("wind_dir");
                            tv_wind.setText("Wind Speed " + wind_speed +  " km/h " + wind_dir);

                            // Load day/night background
                            // Get the device's display metrics
                            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                            // Calculate the target width and height based on screen size and density
                            int targetWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
                            int targetHeight = (int) (displayMetrics.heightPixels / displayMetrics.density);
                            if(is_day == 1) {
                                Picasso.get()
                                        .load(R.drawable.day)
                                        .resize(targetWidth, targetHeight)
                                        .centerCrop()
                                        .into(img_bg);
                            }
                            else {
                                Picasso.get()
                                        .load(R.drawable.night)
                                        .resize(targetWidth, targetHeight)
                                        .centerCrop()
                                        .into(img_bg);
                            }
                            // Get Forecast data
                            JSONObject forecast_object = response.getJSONObject("forecast");
                            JSONObject forecast_day = forecast_object.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray forecast_hour = forecast_day.getJSONArray("hour");
                            for (int i=0; i<forecast_hour.length(); i++) {
                                JSONObject data_hour = forecast_hour.getJSONObject(i);
                                String time = data_hour.getString("time");
                                String temp = data_hour.getString("temp_c");
                                // String cond = data_hour.getJSONObject("condition").getString("text");
                                String cond_img = data_hour.getJSONObject("condition").getString("icon");
                                String wind = data_hour.getString("wind_kph");
                                weatherRCModalArrayList.add(new WeatherRCModal(temp, time, cond_img, wind));
                            }
                            weatherRCAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.e("RESPONSE_DATA", "Error Status Code: " + error.networkResponse.statusCode);
                }
                Log.e("RESPONSE_DATA", "Error: " + error.toString());
                Toast.makeText(context, "Please type correct city name!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}