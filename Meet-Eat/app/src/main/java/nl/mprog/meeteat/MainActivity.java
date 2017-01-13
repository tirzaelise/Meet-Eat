package nl.mprog.meeteat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient
        .OnConnectionFailedListener, View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        stateChange();
        googleSetUp();

        SignInButton signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);
        findViewById(R.id.joiningButton).setOnClickListener(this);
        findViewById(R.id.cookingButton).setOnClickListener(this);

        drawerList = (ListView) findViewById(R.id.navigationList);
        createDrawerItems();
        setDrawerListener();
        setUpDrawer();
    }

    /** Creates menu items for the navigation drawer */
    private void createDrawerItems() {
        String[] menuArray = {"Home", "Account"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                menuArray);
        drawerList.setAdapter(adapter);
    }

    /** Sets a listener on the navigation drawer */
    private void setDrawerListener() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Sets up the navigation drawer */
    private void setUpDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer,
                R.string.closeDrawer) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (actionBar != null) setTitle("Navigation");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (actionBar != null) actionBar.setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
    private void stateChange() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                boolean loggedIn = user != null;
                if (loggedIn) {
                    Log.wtf("signed in", "successfully");
                } else {
                    Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, "Authentication failed.",
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
        TextView helloText = (TextView) findViewById(R.id.helloText);
        int signedInView;
        int signedOutView;

        if (signedIn) {
            signedInView = View.VISIBLE;
            signedOutView = View.INVISIBLE;

            SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String name = sharedPrefs.getString("userName", "noName");
            String helloString = helloText.getText().toString();
            helloString = helloString.replace("username", name);
            helloText.setText(helloString);
        } else {
            signedInView = View.INVISIBLE;
            signedOutView = View.VISIBLE;
        }

        findViewById(R.id.introText).setVisibility(signedOutView);
        findViewById(R.id.signInButton).setVisibility(signedOutView);
        findViewById(R.id.cookOrJoin).setVisibility(signedInView);
        findViewById(R.id.cookingButton).setVisibility(signedInView);
        findViewById(R.id.joiningButton).setVisibility(signedInView);
        helloText.setVisibility(signedInView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 9001);
                break;
            case R.id.joiningButton:
                Intent joinIntent = new Intent(this, JoinActivity.class);
                startActivity(joinIntent);
                break;
            case R.id.cookingButton:
                Intent cookIntent = new Intent(this, CookActivity.class);
                startActivity(cookIntent);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}
