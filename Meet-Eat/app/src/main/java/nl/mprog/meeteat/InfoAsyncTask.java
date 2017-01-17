/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class makes a call to the Spoonacular API to get the ingredients from a recipe using the
 * recipe ID. It also retrieves whether the recipe is vegan or vegetarian.
 */

package nl.mprog.meeteat;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class InfoAsyncTask extends AsyncTask<String, Void, String> {
    private OnInfoRetrieved listener;
    private Dinner dinner;

    InfoAsyncTask(OnInfoRetrieved listener, Dinner dinner) {
        this.listener = listener;
        this.dinner = dinner;
    }

    interface OnInfoRetrieved {
        void onInfoRetrieved(Dinner dinner);
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
            boolean vegetarian = readObject.getBoolean("vegetarian");
            boolean vegan = readObject.getBoolean("vegan");
            JSONArray ingredientsObject = readObject.getJSONArray("extendedIngredients");
            String ingredients = "";

            for (int i = 0; i < ingredientsObject.length(); i++) {
                JSONObject dinnerObject = ingredientsObject.getJSONObject(i);
                String ingredient = dinnerObject.getString("name");
                if (ingredients.equals("")) {
                    ingredients = ingredient;
                } else {
                    ingredients = ingredients + ", " + ingredient;
                }
            }
            dinner.setIngredients(ingredients);
            dinner.setVegetarian(vegetarian);
            dinner.setVegan(vegan);
            listener.onInfoRetrieved(dinner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
