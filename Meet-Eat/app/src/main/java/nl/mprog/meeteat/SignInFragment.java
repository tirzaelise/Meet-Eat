/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the fragment where the user can log in and log out. This is done using
 * Firebase.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        mAuth = FirebaseAuth.getInstance();
        stateChanged();

        if (rootView != null) {
            rootView.findViewById(R.id.signInButton).setOnClickListener(this);
            rootView.findViewById(R.id.signOutButton).setOnClickListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /** Removes listener from FirebaseAuth instance. */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /** Keeps track of the user signing in and out. */
    private void stateChanged() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    signOutVisibility();
                } else {
                    signInVisibility();
                    SharedPreferences.Editor editor = activity.getSharedPreferences("userInfo",
                            Context.MODE_PRIVATE).edit();
                    editor.remove("userId");
                    editor.remove("username");
                    editor.remove("userEmail").apply();
                }
            }
        };
    }


    /**
     * Changes the visibility of views so that the right views are shown when the user has to sign
     * in.
     */
    private void signInVisibility() {
        rootView.findViewById(R.id.giveEmail).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Changes the visibility of views so that the right views are shown when the user has to sign
     * out.
     */
    private void signOutVisibility() {
        rootView.findViewById(R.id.giveEmail).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signInButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
    }


    /** Logs in the user. */
    private void logIn() {
        String email = ((EditText) rootView.findViewById(R.id.giveEmail)).getText().toString();
        String password = ((EditText) rootView.findViewById(R.id.givePassword)).getText().
                toString();

        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(activity, R.string.failAuth, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(activity, R.string.successLogIn, Toast.LENGTH_SHORT)
                                        .show();
                                hideKeyboard();
                                getUsername();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.allFields, Toast.LENGTH_SHORT).show();
        }
    }

    /** Hides keyboard. */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** Gets the user's username from the database. */
    private void getUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.getUsername(user.getUid(), activity);
        }
    }

    /** Signs out the user. */
    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                logIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }
}
