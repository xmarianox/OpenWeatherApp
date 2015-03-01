package la.funka.openweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import la.funka.openweather.model.Forecast;
import la.funka.openweather.utils.Utility;

public class WeekForecastFragment extends Fragment {

    private static final String LOG_TAG = WeekForecastFragment.class.getSimpleName();
    // Recycler
    private RecyclerView listaForecast;
    private ArrayList<Forecast> datasetForecast = new ArrayList<Forecast>();
    private ForecastAdapter forecastAdapter;
    // Refresh
    private SwipeRefreshLayout swipeRefreshLayout = null;

    public WeekForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_forecast, container, false);

        Intent intent = getActivity().getIntent();
        final String CITY_ID = intent.getStringExtra("CITY_ID");
        final String CITY_NAME = intent.getStringExtra("CITY_NAME");

        // Set Title
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(CITY_NAME);

        // Load Data
        final String URL_API = "http://api.openweathermap.org/data/2.5/forecast/daily?id="+ CITY_ID +"&units=metric&cnt=7";
        new GetForecastTask().execute(URL_API);

        // Refresh Forecast
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetForecastTask().execute(URL_API);
            }
        });

        // Set RecyclerView
        listaForecast = (RecyclerView) rootView.findViewById(R.id.list_forecast);
        listaForecast.setHasFixedSize(true);

        forecastAdapter = new ForecastAdapter(datasetForecast, R.layout.item_forecast, getActivity());
        listaForecast.setAdapter(forecastAdapter);

        listaForecast.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaForecast.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    /**
     * API Weather.
     * * * */
    public class GetForecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(urls[0]));
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = buffer.readLine()) != null)
                        result += line;

                    inputStream.close();
                } else {
                    // ERROR;
                    Log.e(LOG_TAG, "Error");
                }

            } catch (Exception e) {
                // ERROR;
                Log.e(LOG_TAG, "Error ", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultado) {

            try {
                JSONObject jsonObject = new JSONObject(resultado);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i ++) {
                    Forecast forecast = new Forecast();

                    JSONObject jsonList = jsonArray.getJSONObject(i);

                    JSONObject jsonTemp = jsonList.getJSONObject("temp");

                    // min & max
                    String min = "Min " + String.valueOf(jsonTemp.getDouble("min")) + "º";
                    String max = "Max " + String.valueOf(jsonTemp.getDouble("max")) + "º";

                    JSONArray jsonWeather = jsonList.getJSONArray("weather");

                    for (int a = 0; a < jsonWeather.length(); a++) {
                        JSONObject objWeather = jsonWeather.getJSONObject(a);

                        String weatherString = objWeather.getString("description");
                        String icon_ref = "http://openweathermap.org/img/w/"+ objWeather.getString("icon") + ".png";

                        forecast.setIcon(icon_ref);
                        forecast.setWeather(weatherString);
                    }
                    forecast.setMin_temp(min);
                    forecast.setMax_temp(max);
                    datasetForecast.add(forecast);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                forecastAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
