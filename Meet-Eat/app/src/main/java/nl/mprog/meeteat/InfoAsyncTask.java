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

    /** Holds a dinner with all info from the API once the listener is alerted in OnPostExecute. */
    interface OnInfoRetrieved {
        void onInfoRetrieved(Dinner dinner);
    }

    /** Retrieves the info from a web page in the background. */
    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHandler.downloadFromApi(params);
    }

    /**
     * Updates standard values of the ingredients, vegetarian boolean and vegan boolean of a dinner
     * with the retrieved information.
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject readObject = new JSONObject(result);
            boolean vegetarian = readObject.getBoolean("vegetarian");
            boolean vegan = readObject.getBoolean("vegan");
            String ingredients = formIngredients(readObject);

            dinner.setIngredients(ingredients);
            dinner.setVegetarian(vegetarian);
            dinner.setVegan(vegan);
            listener.onInfoRetrieved(dinner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Forms a string of the ingredients that were retrieved using HttpRequestHandler. */
    private String formIngredients(JSONObject readObject) throws JSONException {
        JSONArray ingredientsObject = readObject.getJSONArray("extendedIngredients");
        String ingredients = "";

        for (int i = 0; i < ingredientsObject.length(); i++) {
            JSONObject ingredientObject = ingredientsObject.getJSONObject(i);
            String ingredient = ingredientObject.getString("name");

            if (ingredients.equals("")) {
                ingredients = ingredient;
            } else {
                ingredients = ingredients + ", " + ingredient;
            }
        }
        return ingredients;
    }
}
