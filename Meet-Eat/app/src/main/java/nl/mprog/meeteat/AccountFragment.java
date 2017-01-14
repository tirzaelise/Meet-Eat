package nl.mprog.meeteat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AccountFragment extends FragmentActivity implements GoogleApiClient
        .OnConnectionFailedListener, View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Context context;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_account);
        super.onCreate(savedInstanceState);

//        mAuth = FirebaseAuth.getInstance();
//        stateChange();
//        googleSetUp();


//        context = getApplicationContext();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);
    }

//    /** Attaches listener to FirebaseAuth instance */
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    /** Removes listener from FirebaseAuth instance */
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

    /** Checks if the user has been signed in or signed out */
    private void stateChange() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                boolean loggedIn = user != null;
                if (loggedIn) {
                    Log.wtf("signed in", "successfully");
                } else {
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show();
                }
                hideOrShowViews(loggedIn);
            }
        };
    }


    /** Configures what information to get from user's Google account and builds GoogleApiClient */
    private void googleSetUp() {
        String token = "377678081222-adcrfr3msab4j9qu6dkrknn32n97mso2.apps.googleusercontent.com";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Gets an ID token from a GoogleSignInAccount object, exchanges it for a Firebase credential
     * and uses it to authenticate with Firebase.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("", "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /** Signs out the user */
    private void signOut() {
        mAuth.signOut();
    }

    /** Hides or shows views, depending on whether the user is signed in or signed out */
    private void hideOrShowViews(boolean signedIn) {

        Button signOutButton = (Button) activity.findViewById(R.id.signOutButton);

        if (signedIn) signOutButton.setVisibility(View.VISIBLE);
        else signInButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 9001);
                break;
            case R.id.signOutButton:
                signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}