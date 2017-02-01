/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the fragment where the user can give information about the dinner they
 * will be holding. After the user has filled in all fields, he is sent to the next fragment where
 * his search results will be displayed.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class CookFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Activity activity;
    private String food;
    private String date;
    private String freeSpacesString;
    private String area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cook, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            rootView.findViewById(R.id.addButton).setOnClickListener(this);
            new TimeSelector(view, activity).timePopUp();
            new DateSelector(view, activity).calendarPopUp();
        }
    }

    /**
     * Sends the user to the fragment where the search results will be displayed if everything is
     * filled in correctly.
     */
    public void searchRecipes() {
        food = ((EditText) rootView.findViewById(R.id.giveFood)).getText().toString();
        freeSpacesString = ((EditText) rootView.findViewById(R.id.giveAmount)).getText()
                .toString();
        area = capitalizeFully(((EditText) rootView.findViewById(R.id.giveArea)).getText()
                .toString());

        if (!filledInAllFields()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!area.matches(".*[a-zA-Z].*[a-zA-Z].*")) {
            Toast.makeText(activity, "Please fill in a valid area", Toast.LENGTH_SHORT).show();
        } else {
            food = food.replace(" ", "&nbsp;");
            date = ((EditText) rootView.findViewById(R.id.giveDate)).getText().toString() + " " +
                    ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();

            getFreeSpaces();
        }
    }

    /** Checks if all EditText fields were filled in. */
    private boolean filledInAllFields() {
        String date = ((EditText) rootView.findViewById(R.id.giveDate)).getText().toString();
        String time = ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();

        return (!food.equals("") && !date.equals("") && !time.equals("") &&
                !freeSpacesString.equals("") && !area.equals(""));
    }

    /** Creates an integer from the number that the user gave in as a string. */
    private void getFreeSpaces() {
        try {
            int freeSpaces = Integer.valueOf(freeSpacesString);

            if (freeSpaces > 0) {
                toRecipeResultFragment(freeSpaces);
            } else {
                Toast.makeText(activity, "Please allow more than 0 people to join",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(activity, "Please fill in a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    /** Sends the user to the fragment where they can see the results of their recipe search. */
    private void toRecipeResultFragment(int freeSpaces) {
        RecipeResultFragment recipeResultFragment = new RecipeResultFragment();
        Bundle arguments = new Bundle();
        arguments.putString("food", food);
        arguments.putString("date", date);
        arguments.putInt("freeSpaces", freeSpaces);
        arguments.putString("area", area);
        recipeResultFragment.setArguments(arguments);

        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, recipeResultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                searchRecipes();
                break;
        }
    }
}
