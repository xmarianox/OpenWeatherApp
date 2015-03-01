package la.funka.openweather.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import la.funka.openweather.model.City;

public class ReadLocalJSON {

    private String json = "";
    private ArrayList<City> cities = new ArrayList<City>();
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;

    // Retorna la lista para el listView
    public ArrayList<City> getCities(Context context) {

        try {

            stringBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("cities.json")));

            String city = "";

            while ((city=bufferedReader.readLine()) != null) {
                stringBuilder.append(city);
            }

            bufferedReader.close();
            json = stringBuilder.toString();

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                City itemCity = new City();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                itemCity.setId(jsonObject.getInt("id"));
                itemCity.setName(jsonObject.getString("name"));
                itemCity.setCountry(jsonObject.getString("country"));
                itemCity.setLatitud(jsonObject.getDouble("lat"));
                itemCity.setLongitude(jsonObject.getDouble("lon"));

                cities.add(itemCity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "No se pudieron obtener datos", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "No se pudieron obtener datos", Toast.LENGTH_SHORT).show();
        }
        return cities;
    }

}
