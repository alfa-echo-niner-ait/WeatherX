package app.my.weatherx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.text.DecimalFormat;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private Context context;

    private RelativeLayout rl_home;
    private ProgressBar prog_loading;
    private TextView tv_cityName, tv_temp, tv_condition, tv_feel, tv_wind, tv_sunrise, tv_sunset;
    private TextInputEditText input_city;
    private ImageView img_bg, img_weather, img_search;
    private RecyclerView rv_forecast;

    private ArrayList<WeatherRCModal> weatherRCModalArrayList;
    private WeatherRCAdapter weatherRCAdapter;
    private CityDatabaseHelper databaseHelper;

    private LocationManager locationManager;
    private int PERMIT_CODE = 1;
    private String CITY_NAME;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Init all assets
        databaseHelper = new CityDatabaseHelper(requireContext());
        rl_home = rootView.findViewById(R.id.rl_home);
        prog_loading = rootView.findViewById(R.id.prog_loading);
        tv_cityName = rootView.findViewById(R.id.cityName);
        tv_temp = rootView.findViewById(R.id.count_temperature);
        tv_condition = rootView.findViewById(R.id.text_temperature);
        tv_feel = rootView.findViewById(R.id.text_feel);
        tv_wind = rootView.findViewById(R.id.text_wind);
        tv_sunrise = rootView.findViewById(R.id.tv_sunrise_time);
        tv_sunset = rootView.findViewById(R.id.tv_sunset_time);
        input_city = rootView.findViewById(R.id.cityInput);
        img_bg = rootView.findViewById(R.id.bgHomeBlack);
        img_weather = rootView.findViewById(R.id.img_temperature);
        img_search = rootView.findViewById(R.id.img_btn_search);
        rv_forecast = rootView.findViewById(R.id.rv_forecast);

        weatherRCModalArrayList = new ArrayList<>();
        weatherRCAdapter = new WeatherRCAdapter(requireContext(), weatherRCModalArrayList);
        rv_forecast.setAdapter(weatherRCAdapter);

        // Check if fine location permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the fine location permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMIT_CODE);
            getLocationAndGeocode();
        } else {
            // Fine location permission already granted, proceed with geocoding
            getLocationAndGeocode();
        }

        // Listener for search icon click
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = input_city.getText().toString();
                input_city.setText("");
                if (city.isEmpty()) {
                    Toast.makeText(context, "Please type city name first!", Toast.LENGTH_SHORT).show();
                } else {
                    get_weather_info(city);
                }
            }
        });

        return rootView;

    }

    private void getLocationAndGeocode() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMIT_CODE);
            }

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String location_info = decimalFormat.format(latitude) + "," + decimalFormat.format(longitude);

                get_weather_info(location_info);
            }
            else {
                Toast.makeText(context, "Last location unknown! Loading default.", Toast.LENGTH_SHORT).show();
                CITY_NAME = "Beijing"; // Set default location
                get_weather_info(CITY_NAME);
            }
        }
    }


    private void get_weather_info(String city_name) {
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
                            String name = response.getJSONObject("location").getString("name");
                            tv_cityName.setText(name);
                            databaseHelper.insertCity(name);

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
                            JSONObject forecast_astro = forecast_day.getJSONObject("astro");

                            String sunrise = forecast_astro.getString("sunrise");
                            String sunset = forecast_astro.getString("sunset");
                            tv_sunrise.setText(sunrise);
                            tv_sunset.setText(sunset);

                            JSONArray forecast_hour = forecast_day.getJSONArray("hour");
                            for (int i=0; i<forecast_hour.length(); i++) {
                                JSONObject data_hour = forecast_hour.getJSONObject(i);
                                String time = data_hour.getString("time");
                                String temp = data_hour.getString("temp_c");
                                // String cond = data_hour.getJSONObject("condition").getString("text");
                                String cond_img = data_hour.getJSONObject("condition").getString("icon");
                                String cond_txt = data_hour.getJSONObject("condition").getString("text");
                                String wind = data_hour.getString("wind_kph");
                                weatherRCModalArrayList.add(new WeatherRCModal(temp, time, cond_img, cond_txt, wind));
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