/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This fragment shows the user the dinners that he has joined. This is done using DatabaseHandler,
 * which retrieves all dinners where the ID of the current user is in the list of guest IDS of a
 * dinner. This data is shown in a ListView.
 */

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
    private View rootView;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            ArrayList<Dinner> joinedDinners = new ArrayList<>();
            SavedAdapter adapter = new SavedAdapter(activity, joinedDinners, true);
            listView.setAdapter(adapter);
            getJoinedDinners(adapter, joinedDinners);
        }
    }

    /** Retrieves the dinners that the user has joined using DatabaseHandler. */
    private void getJoinedDinners(SavedAdapter adapter, ArrayList<Dinner> dinners) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.getJoinedDinners(adapter, dinners, activity, rootView);
    }
}
