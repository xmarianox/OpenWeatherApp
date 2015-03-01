package la.funka.openweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import la.funka.openweather.model.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    private static final String LOG_TAG = CityAdapter.class.getSimpleName();

    private ArrayList<City> cities;
    private int itemCity;

    public CityAdapter(ArrayList<City> data, int itemCity) {
        cities = data;
        this.itemCity = itemCity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView city_name;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            city_name = (TextView) itemView.findViewById(R.id.city_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(itemCity, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final City city = cities.get(position);

        final String curret_id = String.valueOf(city.getId());
        final String current_city = city.getName();
        final String current_country = city.getCountry();
        final String current_lat = String.valueOf(city.getLatitud());
        final String current_lon = String.valueOf(city.getLongitude());

        viewHolder.city_name.setText(current_city + ", " + current_country);
        viewHolder.itemView.setTag(city);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Click to open intentWeatherCity");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CITY_NAME", current_city);
                editor.putString("COUNTRY_NAME", current_country);
                editor.apply();

                // enviamos los datos al otro activity
                Intent intentWeatherCity = new Intent(v.getContext(), WeatherCityActivity.class);
                intentWeatherCity.putExtra("CITY_ID", curret_id);
                intentWeatherCity.putExtra("CITY_NAME", current_city);
                intentWeatherCity.putExtra("COUNTRY_NAME", current_country);
                intentWeatherCity.putExtra("CITY_LAT", current_lat);
                intentWeatherCity.putExtra("CITY_LON", current_lon);
                // start activity
                v.getContext().startActivity(intentWeatherCity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
