package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CookFragment extends Fragment implements View.OnClickListener,
        DinnerAsyncTask.OnTaskCompleted, InfoAsyncTask.OnInfoRetrieved {
    View rootView;
    Activity activity;
    private ArrayList<Dinner> dinners;

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
        String freeSpacesString = ((EditText) rootView.findViewById(R.id.giveAmount)).getText()
                .toString();
        String area = ((EditText) rootView.findViewById(R.id.giveArea)).getText().toString();

        if (correctTimeFormat(startTime)) {
            food = food.replace(" ", "&nbsp;");
            createDinner(food, host, startTime, freeSpacesString, area);
        } else {
            Toast.makeText(activity, "Please enter a start time according to the format hh:mm",
                    Toast.LENGTH_SHORT).show();
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

            if ((splitHour[1].equals("0") || splitHour[1].equals("1") || splitHour[1].equals("2"))
                    && splitHour[2].matches("[0-9]+") && splitMins[1].matches("[0-5]+") &&
                    splitMins[2].matches("[0-9]+")) {
                return true;
            }
        }
        return false;
    }

    /** Creates a dinner object */
    private void createDinner(String food, String host, String startTime, String freeSpacesString,
                              String area) {

        // TODO: HOST CHANGE
        host = "Tirza";
        if (!food.equals("") && !host.equals("") && !startTime.equals("") &&
                !freeSpacesString.equals("") && !area.equals("")) {
            int freeSpaces = Integer.valueOf(freeSpacesString);
            getDinnerInfo(food, host, startTime, freeSpaces, area);
//            DatabaseHandler databaseHandler = new DatabaseHandler();
//            databaseHandler.writeToDatabase(newDinner);
//            Toast.makeText(activity, "Added dinner successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDinnerInfo(String food, String host, String startTime, int freeSpaces,
                               String area) {
        String query = "search?&query=" + food;
        DinnerAsyncTask dinnerAsyncTask = new DinnerAsyncTask(this, this, host, startTime,
                freeSpaces, area);
        dinnerAsyncTask.execute(query);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
            addDinner();
        }
    }

    @Override
    public void onTaskCompleted(ArrayList<Dinner> retrievedDinners) {
        dinners = new ArrayList<>();

        for (int i = 0; i < retrievedDinners.size(); i++) {
            Dinner dinner = retrievedDinners.get(i);
            InfoAsyncTask infoAsyncTask = new InfoAsyncTask(this, dinner);
            String query =  dinner.getId() + "/information";
            infoAsyncTask.execute(query);
        }

    }

    @Override
    public void onInfoRetrieved(Dinner dinner) {
        dinners.add(dinner);
        // Serializable?
    }
}
