package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
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
            rootView.findViewById(R.id.signUpButton).setOnClickListener(this);
            rootView.findViewById(R.id.signOutButton).setOnClickListener(this);
            rootView.findViewById(R.id.accountAlready).setOnClickListener(this);
        }
    }

    /** Attaches listener to FirebaseAuth instance */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /** Removes listener from FirebaseAuth instance */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /** Checks if the user has been signed in or signed out */
    private void stateChanged() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                boolean loggedIn = user != null;
                if (loggedIn) {
                    signOutVisibility();
                } else {
                    signUpVisibility();
                }
            }
        };
    }

    private void signInVisibility() {
        rootView.findViewById(R.id.giveName).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.giveEmail).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signUpButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.accountAlready).setVisibility(View.INVISIBLE);
    }

    private void signUpVisibility() {
        rootView.findViewById(R.id.giveName).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.giveEmail).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signInButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signUpButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.accountAlready).setVisibility(View.VISIBLE);

    }

    private void signOutVisibility() {
        rootView.findViewById(R.id.giveName).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.giveEmail).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.givePassword).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signInButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signUpButton).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.accountAlready).setVisibility(View.INVISIBLE);

    }

    /** Creates an account in the database */
    private void createAccount() {
        String name = ((EditText) rootView.findViewById(R.id.giveName)).getText().toString();
        String email = ((EditText) rootView.findViewById(R.id.giveEmail)).getText().toString();
        String password = ((EditText) rootView.findViewById(R.id.givePassword)).getText().
                toString();

        if (!name.equals("") && !email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(activity, "Password must be at least 6 " +
                                            "characters", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(activity, "Invalid e-mail address",
                                            Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(activity, "User already exists",
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                saveUsername();
                                Toast.makeText(activity, "Registered successfully",
                                        Toast.LENGTH_SHORT).show();
                                hideKeyboard();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    /** Saves the user's name in the database */
    private void saveUsername() {
        FirebaseUser user = mAuth.getCurrentUser();
        String name = ((EditText) rootView.findViewById(R.id.giveName)).getText().toString();

        UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        if (user != null) {
            user.updateProfile(updateName);
        }
    }

    /** Hides keyboard */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /** Logs in the user */
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
                                Toast.makeText(activity, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Logged in successfully",
                                        Toast.LENGTH_SHORT).show();
                                hideKeyboard();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    /** Signs out the user */
    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                logIn();
                signInVisibility();
                break;
            case R.id.signUpButton:
                createAccount();
                break;
            case R.id.signOutButton:
                signOut();
                signUpVisibility();
                break;
            case R.id.accountAlready:
                signInVisibility();
                break;
        }
    }
}