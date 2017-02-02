/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This fragment lets the user search for dinners in a specific area. When they hit the search
 * button, the city they searched for is saved in SharedPreferences. This city is also put in the
 * EditText to optimise user friendliness, since users will usually want to have dinner in the
 * same area (where they live).
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

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

    /** Changes the text in the area EditText to the most recent search. */
    private void resetLastSearch() {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("lastSearch",
                Context.MODE_PRIVATE);

        if (sharedPrefs.contains("lastArea")) {
            String lastArea = sharedPrefs.getString("lastArea", "area");
            ((EditText) rootView.findViewById(R.id.giveArea)).setText(lastArea);
        }
    }

    /** Searches for a dinner given the user's input. */
    public void search() {
        String area = ((EditText) rootView.findViewById(R.id.giveArea)).getText().toString();

        if (!area.equals("")) {
            area = capitalizeFully(area);
            saveLastSearch(area);
            toResultFragment(area);
        } else {
            Toast.makeText(activity, "Please enter an area", Toast.LENGTH_SHORT).show();
        }
    }

    /** Saves the most recent search in SharedPreferences. */
    private void saveLastSearch(String area) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("lastSearch",
                Context.MODE_PRIVATE).edit();
        editor.putString("lastArea", area).apply();
    }

    /** Sends the user to DinnerResultFragment to show his search results. */
    private void toResultFragment(String area) {
        DinnerResultFragment dinnerResultFragment = new DinnerResultFragment();
        Bundle arguments = new Bundle();
        arguments.putString("area", area);
        dinnerResultFragment.setArguments(arguments);

        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, dinnerResultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchButton) {
            search();
        }
    }
}
