/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the fragment where the user can create an account and log out. This
 * is done using Firebase. The user is also saved in the Firebase database after creating an account
 * to save their name, e-mail address and and user ID.
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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        mAuth = FirebaseAuth.getInstance();
        stateChanged();

        if (rootView != null) {
            rootView.findViewById(R.id.signUpButton).setOnClickListener(this);
            rootView.findViewById(R.id.signOutButton).setOnClickListener(this);
            rootView.findViewById(R.id.accountAlready).setOnClickListener(this);
        }
    }

    /** Attaches listener to FirebaseAuth instance. */
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
                    signUpVisibility();
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
     * up.
     */
    private void signUpVisibility() {
        rootView.findViewById(R.id.giveName).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.giveEmail).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signUpButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.accountAlready).setVisibility(View.VISIBLE);

    }

    /**
     * Changes the visibility of views so that the right views are shown when the user has to sign
     * out.
     */
    private void signOutVisibility() {
        rootView.findViewById(R.id.giveName).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.giveEmail).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signUpButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.accountAlready).setVisibility(View.INVISIBLE);

    }

    /** Creates an account in the database. */
    private void createAccount() {
        final String name = ((EditText) rootView.findViewById(R.id.giveName)).getText().toString();
        String email = ((EditText) rootView.findViewById(R.id.giveEmail)).getText().toString();
        String password = ((EditText) rootView.findViewById(R.id.givePassword)).getText().
                toString();

        if (!name.equals("") && !email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                catchSignUpException(task);
                            } else {
                                Toast.makeText(activity, R.string.successRegister,
                                        Toast.LENGTH_SHORT).show();
                                saveUser(name);
                                hideKeyboard();
                                toMainFragment();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.allFields, Toast.LENGTH_SHORT).show();
        }
    }

    /** Tries to catch the exception to find out why an account could not be created. */
    private void catchSignUpException(Task <AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            Toast.makeText(activity, R.string.failPassword, Toast.LENGTH_SHORT).show();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            Toast.makeText(activity, R.string.failEmail, Toast.LENGTH_SHORT).show();
        } catch (FirebaseAuthUserCollisionException e) {
            Toast.makeText(activity, R.string.failUserExists, Toast.LENGTH_SHORT).show();
        } catch (FirebaseNetworkException e) {
            Toast.makeText(activity, R.string.failNetwork, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a user to the Firebase database after the user has created an account to keep track
     * of the user's name.
     */
    private void saveUser(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            String email = firebaseUser.getEmail();
            User user = new User(userId, username, email);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.saveUser(user, activity);
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

    /** Sends the user back to the main fragment after signing up. */
    private void toMainFragment() {
        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new MainFragment())
                .addToBackStack(null)
                .commit();
    }

    /** Sends the user to the fragment to log in instead of creating an account. */
    private void toSignInFragment() {
        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new SignInFragment())
                .addToBackStack(null)
                .commit();
    }

    /** Signs out the user. */
    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                createAccount();
                break;
            case R.id.signOutButton:
                signOut();
                signUpVisibility();
                break;
            case R.id.accountAlready:
                toSignInFragment();
                break;
        }
    }
}