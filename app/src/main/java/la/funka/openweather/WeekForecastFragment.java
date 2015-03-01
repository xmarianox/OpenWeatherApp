package la.funka.openweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeekForecastFragment extends Fragment {

    public WeekForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_forecast, container, false);

        Intent intent = getActivity().getIntent();
        final String CITY_ID = intent.getStringExtra("CITY_ID");
        final String CITY_NAME = intent.getStringExtra("CITY_NAME");

        ((ActionBarActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(CITY_NAME);

        return rootView;
    }
}
