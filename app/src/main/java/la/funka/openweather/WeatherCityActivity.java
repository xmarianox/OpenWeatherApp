package la.funka.openweather;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class WeatherCityActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_city);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeatherCityFragment()).commit();
        }
    }
}
