package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class CookFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Activity activity;
    private String food;
    private String host;
    private String startTime;
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
        }
    }
    /** Adds a dinner to the database */
    public void searchRecipes() {
        food = ((EditText) rootView.findViewById(R.id.giveFood)).getText().toString();
        host = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE).
                getString("username", "");
        startTime = ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();
        freeSpacesString = ((EditText) rootView.findViewById(R.id.giveAmount)).getText()
                .toString();
        area = ((EditText) rootView.findViewById(R.id.giveArea)).getText().toString();

        if (!filledInAllFields()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!correctTimeFormat()) {
            Toast.makeText(activity, "Please enter a start time according to the format hh:mm",
                    Toast.LENGTH_SHORT).show();
        } else {
            food = food.replace(" ", "&nbsp;");
            toRecipeResultFragment();
        }
    }

    /** Checks if a start time corresponds to the format hh:mm */
    private boolean correctTimeFormat() {
        String[] splitTime = startTime.split(":");

        // If there is no ":" in the string, split returns an array containing the input at the
        // first place
        if (splitTime[0].equals(startTime)) {
            return false;
        } else {
            String[] splitHour = splitTime[0].split("");
            String[] splitMins = splitTime[1].split("");

            if ((splitHour[1].equals("0") || splitHour[1].equals("1") || splitHour[1].equals("2"))
                    && splitHour[2].matches("[0-9]+") && splitMins[1].matches("[0-5]+") &&
                    splitMins[2].matches("[0-9]+")) {
                return true;
            }
        }
        return false;
    }

    /** Checks if all fields were filled in */
    private boolean filledInAllFields() {
        return (!food.equals("") && !host.equals("") && !startTime.equals("") &&
                !freeSpacesString.equals("") && !area.equals(""));
    }

    /** Sends the user to the fragment where they can see the results of their recipe search */
    private void toRecipeResultFragment() {
        int freeSpaces = Integer.valueOf(freeSpacesString);
        RecipeResultFragment recipeResultFragment = new RecipeResultFragment();
        Bundle arguments = new Bundle();
        arguments.putString("food", food);
        arguments.putString("host", host);
        arguments.putString("startTime", startTime);
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
        }
    }
}
