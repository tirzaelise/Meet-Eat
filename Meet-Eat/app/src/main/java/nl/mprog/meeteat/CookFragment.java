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
    public void addDinner() {
        String food = ((EditText) rootView.findViewById(R.id.giveFood)).getText().toString();
        String host = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE).
                getString("userName", "");
        String startTime = ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();
        String amountPeopleString = ((EditText) rootView.findViewById(R.id.giveAmount)).getText()
                .toString();
        String cuisine = ((EditText) rootView.findViewById(R.id.giveCuisine)).getText().toString();
        String area = ((EditText) rootView.findViewById(R.id.giveArea)).getText().toString();
        String ingredients = ((EditText) rootView.findViewById(R.id.giveIngredients)).getText()
                .toString();

        if (correctTimeFormat(startTime)) {
            createDinner(food, host, startTime, amountPeopleString, cuisine, area, ingredients);
        }
    }

    /** Checks if a start time corresponds to the format hh:mm */
    boolean correctTimeFormat(String time) {
        String[] splitTime = time.split(":");

        // If there is no ":" in the string, split returns an array containing the input at the
        // first place
        if (splitTime[0].equals(time)) {
            return false;
        } else {
            String[] splitHour = splitTime[0].split("");
            String[] splitMins = splitTime[1].split("");
            if ((splitHour[0].equals("0") || splitHour[0].equals("1") || splitHour[0].equals("2"))
                    && splitHour[1].matches("[0-9]+") && splitMins[0].matches("[0-5]+") &&
                    splitMins[1].matches("[0-9]+")) {
                return true;
            }
        }
        return false;
    }

    /** Creates a dinner object */
    private void createDinner(String food, String host, String startTime, String amountPeopleString,
                              String cuisine, String area, String ingredients) {
        if (!food.equals("") && !host.equals("") && !startTime.equals("") &&
                !amountPeopleString.equals("") && !cuisine.equals("") && !area.equals("") &&
                !ingredients.equals("")) {
            int amountOfPeople = Integer.valueOf(amountPeopleString);
            Dinner newDinner = new Dinner(food, host, startTime, amountOfPeople, cuisine, area,
                    ingredients);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.writeToDatabase(newDinner);
            Toast.makeText(activity, "Added dinner successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
            addDinner();
        }
    }
}
