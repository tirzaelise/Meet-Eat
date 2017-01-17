package nl.mprog.meeteat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = getView();
        Bundle arguments = this.getArguments();

        if (arguments != null && rootView != null) showResult(arguments, rootView);
    }

    /** Shows the user's search result by reading from the Firebase database using DatabaseHelper */
    private void showResult(Bundle arguments, View rootView) {
        String area = arguments.getString("area", "");

        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
        ArrayList<Dinner> dinners = new ArrayList<>();
        DinnerAdapter adapter = new DinnerAdapter(this, dinners);
        listView.setAdapter(adapter);

        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.readDatabase(area, dinners, adapter, this);
    }
}