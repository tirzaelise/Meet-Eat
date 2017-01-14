package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class ResultFragment extends Fragment {
    View rootView;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            String area = arguments.getString("area", "");
            String cuisine = arguments.getString("cuisine", "");

            DatabaseHandler databaseHandler = new DatabaseHandler();
            ArrayList<Dinner> dinners = databaseHandler.readDatabase(area, cuisine);
            DinnerAdapter adapter = new DinnerAdapter(this,  dinners);
            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
    }
}