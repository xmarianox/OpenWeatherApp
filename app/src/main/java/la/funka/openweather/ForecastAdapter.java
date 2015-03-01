package la.funka.openweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import la.funka.openweather.model.Forecast;

/**
 * Created by RetinaPro on 1/3/15.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>{

    private static final String LOG_TAG = ForecastAdapter.class.getSimpleName();

    private ArrayList<Forecast> dataset;
    private int itemForecast;
    Context context;

    public ForecastAdapter(ArrayList<Forecast> data, int itemForecast, Context context) {
        dataset = data;
        this.itemForecast = itemForecast;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon_forecast;
        public TextView weather_forecast;
        public TextView min_temp_forecast;
        public TextView max_temp_forecast;

        public ViewHolder(View itemView) {
            super(itemView);

            icon_forecast = (ImageView) itemView.findViewById(R.id.icon_forecast);
            weather_forecast = (TextView) itemView.findViewById(R.id.weather_forecast);
            min_temp_forecast = (TextView) itemView.findViewById(R.id.min_temp_forecast);
            max_temp_forecast = (TextView) itemView.findViewById(R.id.max_temp_forecast);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(itemForecast, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Forecast forecast = dataset.get(position);
        final String icon_ref = forecast.getIcon();
        final String weather = forecast.getWeather();
        final String temp_min = forecast.getMin_temp();
        final String temp_max = forecast.getMax_temp();

        Picasso.with(context).load(icon_ref).into(viewHolder.icon_forecast);

        viewHolder.weather_forecast.setText(weather);
        viewHolder.min_temp_forecast.setText(temp_min);
        viewHolder.max_temp_forecast.setText(temp_max);

        viewHolder.itemView.setTag(forecast);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
