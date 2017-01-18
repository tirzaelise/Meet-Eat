package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
//import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
//    private GoogleApiClient mGoogleApiClient;
//    private SignInButton signInButton;
//    private Context context;
    private Activity activity;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.fragment_account);
//        super.onCreate(savedInstanceState);
//
//        mAuth = FirebaseAuth.getInstance();
//        stateChanged();
////        googleSetUp();
//
//
////        context = getApplicationContext();
//
//        signInButton = (SignInButton) findViewById(R.id.signInButton);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
//        signInButton.setOnClickListener(this);
//    }

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
                    Log.wtf("signed in", "successfully");
                } else {
                    Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show();
                }
//                hideOrShowViews(loggedIn);
            }
        };
    }

    /** Creates an account in the database */
    private void createAccount() {
        String email = ((EditText) rootView.findViewById(R.id.giveEmail)).getText().toString();
        String password = ((EditText) rootView.findViewById(R.id.givePassword)).getText().
                toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT)
                                    .show();
                            Log.wtf("exception", task.getException());
                        } else {
                            Toast.makeText(activity, "Registered successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /** Logs in the user */
    private void logIn() {
        String email = ((EditText) rootView.findViewById(R.id.giveEmail)).getText().toString();
        String password = ((EditText) rootView.findViewById(R.id.givePassword)).getText().
                toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }


//    /** Configures what information to get from user's Google account and builds GoogleApiClient */
//    private void googleSetUp() {
//        String token = "377678081222-adcrfr3msab4j9qu6dkrknn32n97mso2.apps.googleusercontent.com";
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
//                .DEFAULT_SIGN_IN)
//                .requestIdToken(token)
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 9001) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//            if (result.isSuccess()) {
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            } else {
//                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    /**
//     * Gets an ID token from a GoogleSignInAccount object, exchanges it for a Firebase credential
//     * and uses it to authenticate with Firebase.
//     */
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d("", "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w("", "signInWithCredential", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    /** Signs out the user */
    private void signOut() {
        mAuth.signOut();
    }

//    /** Hides or shows views, depending on whether the user is signed in or signed out */
//    private void hideOrShowViews(boolean signedIn) {
//
//        Button signOutButton = (Button) activity.findViewById(R.id.signOutButton);
//
//        if (signedIn) signOutButton.setVisibility(View.VISIBLE);
//        else signInButton.setVisibility(View.INVISIBLE);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                logIn();
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, 9001);
                break;
            case R.id.signUpButton:
                createAccount();
                break;
//            case R.id.signOutButton:
//                signOut();
        }
    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
//    }
}