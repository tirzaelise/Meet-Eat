package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class JoinListFragment extends Fragment {

    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            ArrayList<Dinner> joinedDinners = new ArrayList<>();
            SavedAdapter adapter = new SavedAdapter(activity, joinedDinners, true);
            listView.setAdapter(adapter);
            getJoinedDinners(adapter, joinedDinners);
        }
    }

    private void getJoinedDinners(SavedAdapter adapter, ArrayList<Dinner> dinners) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.getJoinedDinners(adapter, dinners, activity);
    }
}
