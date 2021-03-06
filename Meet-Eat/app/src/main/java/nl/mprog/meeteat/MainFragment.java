/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This fragment is the home screen of the app. This is where the user decides whether they want to
 * join or cook a dinner. The user's name is also put on the screen if they're logged in.
 */

package nl.mprog.meeteat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment implements View.OnClickListener {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();

        if (rootView != null) {
            rootView.findViewById(R.id.joiningButton).setOnClickListener(this);
            rootView.findViewById(R.id.cookingButton).setOnClickListener(this);
            putUsername();
        }
    }

    /** Shows the user's name in the fragment. */
    private void putUsername() {
        TextView helloTextView = (TextView) rootView.findViewById(R.id.helloText);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);

        if (sharedPrefs.contains("username")) {
            String name = sharedPrefs.getString("username", "");
            helloTextView.setText(getString(R.string.hello) + " " + name +
                    getString(R.string.exclamation));
        } else {
            helloTextView.setText(getString(R.string.helloExcl));
        }
    }

    /** Changes the fragment to the fragment that the user has selected. */
    private void changeFragment(Fragment newFragment) {
        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joiningButton:
                changeFragment(new JoinFragment());
                break;
            case R.id.cookingButton:
                changeFragment(new CookFragment());
                break;
        }
    }
}
