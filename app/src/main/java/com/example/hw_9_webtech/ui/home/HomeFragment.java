package com.example.hw_9_webtech.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw_9_webtech.NewsArticle;
import com.example.hw_9_webtech.R;
import com.example.hw_9_webtech.ui.RVCardAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private SwipeRefreshLayout r;
    private RequestQueue mqueue;
    private JSONArray results;
    private List<NewsArticle> all_news;
    private RVCardAdapter myRVAdapter;
    private RelativeLayout myProgressBar;
    private String cityName, stateName;
    private String weatherSummary;
    private int weatherTemperature;
    private CardView weatherCard;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        weatherCard = root.findViewById(R.id.weather_card_wrapper);
        weatherCard.setVisibility(View.GONE);

        mqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        try {
            populateLocationCard();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        weatherCard = root.findViewById(R.id.weather_card_wrapper);
//        weatherCard.setVisibility(View.GONE);
//        ((TextView) root.findViewById(R.id.weatherCity)).setText(cityName);
//        ((TextView) root.findViewById(R.id.weatherState)).setText(stateName);
//        ((TextView) root.findViewById(R.id.weatherSummary)).setText(weatherSummary);
//        String myTemperature = weatherTemperature + "\u2109C";
//        ((TextView) root.findViewById(R.id.weatherTemp)).setText(myTemperature);
//        switch (weatherSummary){
//            case "Clouds":
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/cloudy_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//                break;
//            case "Clear":
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/clear_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//                break;
//            case "Snow":
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/snowy_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//                break;
//            case "Rain":
//            case "Drizzle":
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/rainy_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//                break;
//            case "Thunderstorm":
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/thunder_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//                break;
//            default:
//                Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
//                        .load("https://csci571.com/hw/hw9/images/android/sunny_weather.jpg")
//                        .fit()
//                        .into((ImageView) root.findViewById(R.id.weatherBackground));
//        }

        myProgressBar = root.findViewById(R.id.progress_container);

        all_news = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.home_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        myRVAdapter = new RVCardAdapter(all_news);
        recyclerView.setAdapter(myRVAdapter);
        recyclerView.setLayoutManager(llm);

        jsonParse();

        r = root.findViewById(R.id.home_refresher);

        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myProgressBar.setVisibility(View.VISIBLE);
                weatherCard.setVisibility(View.GONE);
                jsonParse();
                r.setRefreshing(false);
            }
        });

        return root;
    }

    private void jsonParse() {
        String apiURL = "https://ivillega-nytimes-guardian.wl.r.appspot.com/guardian/home";
        all_news.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONObject("response")
                                    .getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                all_news.add(new NewsArticle(results.getJSONObject(i)));
                            }
                            myProgressBar.setVisibility(View.GONE);
                            weatherCard.setVisibility(View.VISIBLE);
                            myRVAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mqueue.add(request);
    }

    public void populateLocationCard() throws IOException {
        LocationManager locationManager = (LocationManager) root.getContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(root.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(root.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(provider);
        Double lat = lastLocation.getLatitude();
        Double lng = lastLocation.getLongitude();
        Geocoder geocoder = new Geocoder(root.getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        cityName = addresses.get(0).getLocality();
        stateName = addresses.get(0).getAdminArea();

        String encodedCity = "";
        try {
            encodedCity = URLEncoder.encode(cityName, "utf-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&APPID=6a456884808a093a6d213ad8d277e040&units=metric";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weatherSummary = response.getJSONArray("weather").getJSONObject(0).getString("main");
                            weatherTemperature = response.getJSONObject("main").getInt("temp");

                            ((TextView) root.findViewById(R.id.weatherCity)).setText(cityName);
                            ((TextView) root.findViewById(R.id.weatherState)).setText(stateName);
                            ((TextView) root.findViewById(R.id.weatherSummary)).setText(weatherSummary);
                            String myTemperature = weatherTemperature + "\u2103";
                            ((TextView) root.findViewById(R.id.weatherTemp)).setText(myTemperature);
                            switch (weatherSummary){
                                case "Clouds":
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/cloudy_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                                    break;
                                case "Clear":
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/clear_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                                    break;
                                case "Snow":
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/snowy_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                                    break;
                                case "Rain":
                                case "Drizzle":
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/rainy_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                                    break;
                                case "Thunderstorm":
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/thunder_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                                    break;
                                default:
                                    Picasso.with(root.findViewById(R.id.weatherBackground).getContext())
                                            .load("https://csci571.com/hw/hw9/images/android/sunny_weather.jpg")
                                            .fit()
                                            .into((ImageView) root.findViewById(R.id.weatherBackground));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mqueue.add(request);
    }

}
