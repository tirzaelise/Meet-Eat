/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the AsyncTask for the Spoonacular API.
 */

package nl.mprog.meeteat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class DinnerAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted listener;
    private String title;
    private String id;
    private String host;
    private String startTime;
    private int amountOfPeople;
    private String area;
    private String imageUrl;
    private ArrayList<Dinner> dinners;

    DinnerAsyncTask(CookFragment activity, OnTaskCompleted listener, String host, String startTime,
                    int amountOfPeople, String area) {
        this.context = activity.getActivity();
        this.listener = listener;
        this.host = host;
        this.startTime = startTime;
        this.amountOfPeople = amountOfPeople;
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
                dinners = new ArrayList<>();

                for (int i = 0; i < dinnersObject.length(); i++) {
                    JSONObject dinnerObject = dinnersObject.getJSONObject(i);
                    title = dinnerObject.getString("title");
                    id = dinnerObject.getString("id");
                    imageUrl = dinnerObject.getString("image");

                    Dinner dinner = new Dinner(title, id, host, startTime, amountOfPeople, area, "",
                            imageUrl, false, false);
                    dinners.add(dinner);

//                    InfoAsyncTask infoAsyncTask = new InfoAsyncTask(this);
//                    String query =  id + "/information";
//                    infoAsyncTask.execute(query);
//                    Log.wtf("size dinner meanwhile", Integer.toString(dinners.size()));
                }
//                Log.wtf("size", Integer.toString(dinners.size()));
                listener.onTaskCompleted(dinners);
            } else {
                Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    @Override
//    public void onTaskCompleted(Dinner dinner) {
//        Dinner dinner = new Dinner(title, id, host, startTime, amountOfPeople, area, ingredients,
//                imageUrl, vegetarian, vegan);
//        DinnerAsyncTask.this.dinners.add(dinner);
//        Log.wtf("size", Integer.toString(dinners.size()));
//    }
}
