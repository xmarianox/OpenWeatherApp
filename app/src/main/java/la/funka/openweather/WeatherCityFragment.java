package la.funka.openweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import la.funka.openweather.utils.Utility;

public class WeatherCityFragment extends Fragment {

    private static final String LOG_TAG = WeatherCityActivity.class.getSimpleName();

    private static ImageView select_city_weather_image;
    private static TextView select_city_name;
    private static TextView select_city_weather;
    private static TextView select_city_current_temp;
    private static TextView select_city_min_temp;
    private static TextView select_city_max_temp;
    private static Button btn_open_forecast;
    private static String CITY_ID;
    private static String CITY_NAME;
    private static String COUNTRY_NAME;
    private static String CITY_LAT;
    private static String CITY_LON;
    ProgressDialog progressDialog;

    public WeatherCityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_city, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CITY_NAME = preferences.getString("CITY_NAME", "");
        COUNTRY_NAME = preferences.getString("COUNTRY_NAME","");

        Intent intent = getActivity().getIntent();
        CITY_ID = intent.getStringExtra("CITY_ID");
        //CITY_NAME = intent.getStringExtra("CITY_NAME");
        //COUNTRY_NAME = intent.getStringExtra("COUNTRY_NAME");
        CITY_LAT = intent.getStringExtra("CITY_LAT");
        CITY_LON = intent.getStringExtra("CITY_LON");

        ((ActionBarActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(CITY_NAME);

        select_city_name = (TextView) rootView.findViewById(R.id.select_city_name);
        select_city_weather = (TextView) rootView.findViewById(R.id.select_city_weather);
        select_city_current_temp = (TextView) rootView.findViewById(R.id.select_city_current_temp);
        select_city_min_temp = (TextView) rootView.findViewById(R.id.select_city_min_temp);
        select_city_max_temp = (TextView) rootView.findViewById(R.id.select_city_max_temp);
        select_city_weather_image = (ImageView) rootView.findViewById(R.id.select_city_weather_image);
        btn_open_forecast = (Button) rootView.findViewById(R.id.btn_get_forecast);

        select_city_name.setText(CITY_NAME+", "+ COUNTRY_NAME);

        String URL_BASE = "http://api.openweathermap.org/data/2.5/weather?q=";
        String URL = null;

        try {
            URL = URL_BASE + URLEncoder.encode(CITY_NAME.toLowerCase() + "," + COUNTRY_NAME.toLowerCase(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new GetWeatherTask().execute(URL);

        // Open the forecast activity
        btn_open_forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForecast = new Intent(v.getContext(), WeekForecastActivity.class);
                // enviamos los datos al otro activity
                intentForecast.putExtra("CITY_ID", CITY_ID);
                intentForecast.putExtra("CITY_NAME", CITY_NAME);
                intentForecast.putExtra("COUNTRY_NAME", COUNTRY_NAME);
                // start activity
                v.getContext().startActivity(intentForecast);
            }
        });

        return rootView;
    }

    /**
     * API Weather.
     * * * */
    public class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog = ProgressDialog.show(getActivity(), "Descargando datos del clima", "Espere un momento...", true);
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

            progressDialog.dismiss();

            try {

                JSONObject jsonObject = new JSONObject(resultado);

                JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");

                for (int i = 0; i < jsonArrayWeather.length(); i++) {
                    JSONObject objWeather = jsonArrayWeather.getJSONObject(i);

                    // set items
                    String weather = objWeather.getString("description");

                    String icon = objWeather.getString("icon");
                    String urlIcon = "http://openweathermap.org/img/w/"+ icon +".png";

                    Picasso.with(getActivity()).load(urlIcon).into(select_city_weather_image);

                    // set texts
                    select_city_weather.setText(weather);
                }

                JSONObject objTemp = jsonObject.getJSONObject("main");

                // Format Temperature
                String current_temp = Utility.formatTemperature(objTemp.getDouble("temp")) + "º";
                String current_temp_min = "Min " + Utility.formatTemperature(objTemp.getDouble("temp_min")) + "º";
                String current_temp_max = "Max " + Utility.formatTemperature(objTemp.getDouble("temp_max")) + "º";

                // Set Temperature
                select_city_current_temp.setText(current_temp);
                select_city_min_temp.setText(current_temp_min);
                select_city_max_temp.setText(current_temp_max);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error JSONException e: ", e);
            }
        }
    }
}


