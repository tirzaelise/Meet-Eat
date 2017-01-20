/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the AsyncTask for the Spoonacular API.
 */

package nl.mprog.meeteat;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

class DinnerAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted listener;
    private String host;
    private String date;
    private int freeSpaces;
    private String area;

    DinnerAsyncTask(RecipeResultFragment activity, OnTaskCompleted listener, String host, String
            date, int freeSpaces, String area) {
        this.context = activity.getActivity();
        this.listener = listener;
        this.host = host;
        this.date = date;
        this.freeSpaces = freeSpaces;
        this.area = area;
    }

    interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<Dinner> dinners);
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHandler.downloadFromApi(params);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject readObject = new JSONObject(result);
            if (!readObject.getString("totalResults").equals("0")) {
                JSONArray dinnersObject = readObject.getJSONArray("results");
                ArrayList<Dinner> dinners = new ArrayList<>();

                for (int i = 0; i < dinnersObject.length(); i++) {
                    JSONObject dinnerObject = dinnersObject.getJSONObject(i);
                    String title = dinnerObject.getString("title");
                    String id = dinnerObject.getString("id");
                    ArrayList<String> guests = createEmptyGuests(freeSpaces);

                    Dinner dinner = new Dinner(title, id, host, date, guests, area, "", false,
                            false);
                    dinners.add(dinner);
                }
                listener.onTaskCompleted(dinners);
            } else {
                Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /** Creates an ArrayList<String> of the size of the amount of spaces that was given */
    private ArrayList<String> createEmptyGuests(int spaces) {
        String[] guests = new String[freeSpaces];
        Arrays.fill(guests, "null");
        return new ArrayList<>(Arrays.asList(guests));
    }
}
