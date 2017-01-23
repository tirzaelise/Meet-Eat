package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HostListFragment extends Fragment {
    private View rootView;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            ArrayList<Dinner> hostingDinners = new ArrayList<>();
            HostAdapter adapter = new HostAdapter(activity, hostingDinners);
            listView.setAdapter(adapter);
            getHostingDinners(adapter, hostingDinners);
        }
    }

    private void getHostingDinners(HostAdapter adapter, ArrayList<Dinner> dinners) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.getHostingDinners(adapter, dinners, activity);
    }
}
