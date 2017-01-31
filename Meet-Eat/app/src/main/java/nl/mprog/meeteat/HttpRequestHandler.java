/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class handles the requests that are made to the Spoonacular API. The result that is returned
 * is a string that contains everything on the web page.
 */

package nl.mprog.meeteat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequestHandler {

    /** Downloads the text from a web page and saves it in a string. */
    static synchronized String downloadFromApi(String... params) {
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
        result = readPage(url, result);

        return result;
    }

    /** Reads the text on a web page. */
    private static String readPage(URL url, String result) {
        String key = "uI9T1GTt8nmshcUUWJOjq8TQNGBgp1P9Zffjsn7dAbkmTSDt1k";

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
