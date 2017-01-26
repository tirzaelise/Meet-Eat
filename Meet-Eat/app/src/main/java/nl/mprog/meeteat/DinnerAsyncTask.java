/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the AsyncTask for the Spoonacular API. It gets everything from the web page
 * and uses this to create ArrayList<Dinner> of all the recipes that were found given the type of
 * food the user put in. This AsyncTask retrieves the title and ID of a dinner.
 */

package nl.mprog.meeteat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

class DinnerAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted listener;
    private String date;
    private int freeSpaces;
    private String area;

    DinnerAsyncTask(RecipeResultFragment activity, OnTaskCompleted listener, String date,
                    int freeSpaces, String area) {
        this.context = activity.getActivity();
        this.listener = listener;
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
                SharedPreferences sharedPrefs = context.getSharedPreferences("userInfo",
                        Context.MODE_PRIVATE);
                String hostId = sharedPrefs.getString("userId", "");
                String hostName = sharedPrefs.getString("username", "");

                for (int i = 0; i < dinnersObject.length(); i++) {
                    JSONObject dinnerObject = dinnersObject.getJSONObject(i);
                    Dinner dinner = createDinnerObject(dinnerObject, hostId, hostName);
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

    /** Creates a new dinner object from the JSONObject. */
    private Dinner createDinnerObject(JSONObject dinner, String hostId, String hostName)
            throws JSONException {
        String title = dinner.getString("title");
        String id = dinner.getString("id");
        ArrayList<String> guestNames = createEmptyGuests(freeSpaces);
        ArrayList<String> guestIds = createEmptyGuests(freeSpaces);

        return new Dinner(title, id, hostId, hostName, date, guestIds, guestNames, area, "", false,
                false);
    }

    /** Creates an ArrayList<String> of the size of the amount of spaces that was given. */
    private ArrayList<String> createEmptyGuests(int spaces) {
        String[] guests = new String[spaces];
        Arrays.fill(guests, "null");
        return new ArrayList<>(Arrays.asList(guests));
    }
}
