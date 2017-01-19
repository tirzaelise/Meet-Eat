package nl.mprog.meeteat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    /** Shows the user's name in the fragment */
    private void putUsername() {
        TextView helloTextView = (TextView) rootView.findViewById(R.id.helloText);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);

        if (sharedPrefs.contains("username")) {
            String name = sharedPrefs.getString("username", "");
            helloTextView.setText("Hello, " + name + "!");
        } else {
            helloTextView.setText(R.string.hello);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joiningButton:
                JoinFragment joinFragment = new JoinFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, joinFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cookingButton:
                CookFragment cookFragment = new CookFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, cookFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
