package la.funka.openweather.utils;

/**
 * Created by RetinaPro on 28/2/15.
 */
public class Utility {

    public static String formatTemperature(double temperature) {
        double temp;

        temp = Math.round(10*(temperature -273.15)) / 10;

        return String.format("%.0f", temp);
    }
}
