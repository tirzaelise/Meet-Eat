package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class OwnHostFragment extends Fragment {
    private View rootView;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_own_host, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            getHostingDinners();
//            rootView.findViewById(R.id.addButton).setOnClickListener(this);
        }
    }

    private void getHostingDinners() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ArrayList<Dinner> dinners = databaseHandler.getHostingDinners();
    }
}
