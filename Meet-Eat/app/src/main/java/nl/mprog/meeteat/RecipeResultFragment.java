/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This fragment shows the user's search results for recipes given a food that they put in. It uses
 * the DinnerAsyncTask and InfoAsyncTask for this. Firstly, the ID and title of a recipe are
 * retrieved using DinnerAsyncTask and then InfoAsyncTask is used to obtain the ingredients and
 * whether the recipe is vegan and/or vegetarian. These results are then displayed in this fragment
 * in a ListView.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeResultFragment extends Fragment implements DinnerAsyncTask.OnTaskCompleted,
        InfoAsyncTask.OnInfoRetrieved {
    private View rootView;
    private Activity activity;
    private String food;
    private String date;
    private int freeSpaces;
    private String area;
    private RecipeAdapter adapter;
    private ArrayList<Dinner> dinners;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();
        Bundle arguments = this.getArguments();

        if (arguments != null && rootView != null) {
            dinners = new ArrayList<>();
            getArguments(arguments);
            getDinnerInfo();
            initialiseAdapter(rootView);
        }
    }

    /**
     * Gets the arguments that were passed to this fragment to search for recipes and create a
     * Dinner object.
     */
    private void getArguments(Bundle arguments) {
        food = arguments.getString("food");
        date = arguments.getString("date");
        freeSpaces = arguments.getInt("freeSpaces");
        area = arguments.getString("area");
    }

    /**
     * Gets the basic information about a dinner, namely the title and its ID on Spoonacular.
     */
    private void getDinnerInfo() {
        String query = "search?&query=" + food;
        DinnerAsyncTask dinnerAsyncTask = new DinnerAsyncTask(this, this, date, freeSpaces, area,
                rootView);
        dinnerAsyncTask.execute(query);
    }

    /** Initialises the RecipeAdapter for the ListView. */
    private void initialiseAdapter(View rootView) {
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new RecipeAdapter(activity, dinners);
        listView.setAdapter(adapter);
    }

    /**
     * Starts InfoAsyncTask once DinnerAsyncTask has completed to get extra information about a
     * recipe. Namely, its ingredients and whether it's vegetarian and vegan.
     */
    @Override
    public void onTaskCompleted(ArrayList<Dinner> retrievedDinners) {
        for (int i = 0; i < retrievedDinners.size(); i++) {
            Dinner dinner = retrievedDinners.get(i);
            InfoAsyncTask infoAsyncTask = new InfoAsyncTask(this, dinner);
            String query =  dinner.getId() + "/information";
            infoAsyncTask.execute(query);
        }
    }

    /** Adds a complete dinner to the ArrayList<Dinner> once all information is retrieved. */
    @Override
    public void onInfoRetrieved(Dinner dinner) {
        dinners.add(dinner);
        adapter.notifyDataSetChanged();
    }
}
