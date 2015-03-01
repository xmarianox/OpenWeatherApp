package la.funka.openweather;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class WeekForecastActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_forecast);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeekForecastFragment()).commit();
        }
    }

}
