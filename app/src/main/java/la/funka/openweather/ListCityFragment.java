package la.funka.openweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import la.funka.openweather.model.City;
import la.funka.openweather.utils.ReadLocalJSON;

public class ListCityFragment extends Fragment {

    private static final String LOG_TAG = ListCityFragment.class.getSimpleName();
    // recyclerview
    private RecyclerView recyclerView;
    private ArrayList<City> cities = new ArrayList<City>();
    private CityAdapter adapter;

    public ListCityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ReadLocalJSON readLocalJSON = new ReadLocalJSON();
        cities = readLocalJSON.getCities(getActivity());

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.list_cities);
        recyclerView.setHasFixedSize(true);

        adapter = new CityAdapter(cities, R.layout.item_city);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}