package nl.mprog.meeteat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tirza on 15-1-17.
 */

public class HttpRequestHandler {

    static synchronized String downloadFromApi(String... params) {
        String key = "uI9T1GTt8nmshcUUWJOjq8TQNGBgp1P9Zffjsn7dAbkmTSDt1k";
//        String key = "EV3I1nY4m3msh9pEO51dECigTna1p1CP3TmjsnOpBnaLR4wcfD";
        String urlToApi = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
        String query = params[0];
        String urlRequest = urlToApi + query;
        String result = "";
        URL url;

        try {
            url = new URL(urlRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return result;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Mashape-Key", key);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            Integer responseCode = connection.getResponseCode();

            if (200 >= responseCode && responseCode <= 299) {
                InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
