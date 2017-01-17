package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class JoinFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            resetLastSearch();
            rootView.findViewById(R.id.searchButton).setOnClickListener(this);
        }
    }


    /** Changes the text in the area and cuisine EditText to the most recent search */
    private void resetLastSearch() {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("lastSearch", Context.MODE_PRIVATE);

        if (sharedPrefs.contains("lastArea")) {
            String lastArea = sharedPrefs.getString("lastArea", "area");
            String lastCuisine = sharedPrefs.getString("lastCuisine", "cuisine");
            ((EditText) rootView.findViewById(R.id.giveArea)).setText(lastArea);
            ((EditText) rootView.findViewById(R.id.givePreference)).setText(lastCuisine);
        }
    }

    /** Searches for a dinner given the user's input */
    public void search() {
        String area = ((EditText) rootView.findViewById(R.id.giveArea)).getText().toString();
        String cuisine = ((EditText) rootView.findViewById(R.id.givePreference)).getText().toString();

        if (!area.equals("")) {
            saveLastSearch(area, cuisine);
            toResultFragment(area, cuisine);
        } else {
            Toast.makeText(activity, "Please enter the area you're in", Toast.LENGTH_SHORT).show();
        }
    }

    /** Saves the most recent search in Shared Preferences */
    private void saveLastSearch(String area, String cuisine) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("lastSearch", Context.MODE_PRIVATE)
                .edit();
        editor.putString("lastArea", area);
        editor.putString("lastCuisine", cuisine).apply();
    }

    /** Sends the user to ResultFragment to show his search results */
    private void toResultFragment(String area, String cuisine) {
        ResultFragment resultFragment = new ResultFragment();
        Bundle arguments = new Bundle();
        arguments.putString("area", area);
        arguments.putString("cuisine", cuisine);
        resultFragment.setArguments(arguments);

        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                search();
                break;
        }
    }
}
