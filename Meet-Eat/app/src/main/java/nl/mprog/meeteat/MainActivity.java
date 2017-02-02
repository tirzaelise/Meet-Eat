/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the main activity of the application. The navigation drawer is defined, as
 * well as what should happen when it is closed, opened or an item is clicked. The user is sent to a
 * new fragment when they click an item.
 */

package nl.mprog.meeteat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerList = (ListView) findViewById(R.id.navigationList);
        createDrawerItems();
        setDrawerListener();
        setUpDrawer();

        if (savedInstanceState == null) {
            changeFragment(new MainFragment());
        }
    }

    /** Creates menu items for the navigation drawer and sets the adapter. */
    private void createDrawerItems() {
        ArrayList<DrawerItem> data = createDrawerData();
        DrawerAdapter adapter = new DrawerAdapter(this, data);
        drawerList.setAdapter(adapter);
    }

    /** Creates the data for the drawer adapter. */
    private ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();
        Resources resources = this.getResources();

        data.add(new DrawerItem(R.drawable.home_icon, resources.getString(R.string.home)));
        data.add(new DrawerItem(R.drawable.account_icon, resources.getString(R.string.account)));
        data.add(new DrawerItem(R.drawable.hosting_icon, resources.getString(R.string.hosting)));
        data.add(new DrawerItem(R.drawable.joining_icon, resources.getString(R.string.joined)));

        return data;
    }

    /** Sets a listener on the navigation drawer. */
    private void setDrawerListener() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

                switch (position) {
                    case 0:
                        drawerLayout.closeDrawers();
                        changeFragment(new MainFragment());
                        break;
                    case 1:
                        drawerLayout.closeDrawers();
                        changeFragment(new SignUpFragment());
                        break;
                    case 2:
                        checkLoggedIn(drawerLayout, new HostListFragment());
                        break;
                    case 3:
                        checkLoggedIn(drawerLayout, new JoinListFragment());
                        break;
                }
            }
        });
    }

    /** Changes the fragment in the content frame to the new fragment. */
    private void changeFragment(Fragment newFragment) {
        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
                .replace(R.id.contentFrame, newFragment)
                .addToBackStack(null)
                .commit();
    }

    /** Sends the user to the new fragment if they're logged in. */
    private void checkLoggedIn(DrawerLayout drawerLayout, Fragment newFragment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            drawerLayout.closeDrawers();

            changeFragment(newFragment);
        } else {
            Toast.makeText(MainActivity.this, R.string.logInRequired, Toast.LENGTH_SHORT).show();
        }
    }

    /** Sets up the navigation drawer. */
    private void setUpDrawer() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setElevation(0);

        defineDrawerToggle(actionBar);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /** Defines what happens when the Navigation Drawer is opened and closed. */
    private void defineDrawerToggle(final ActionBar actionBar) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer,
                R.string.closeDrawer) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (actionBar != null) setTitle(R.string.navigation);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (actionBar != null) actionBar.setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    /** Returns true if the touch event was handled. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /** Synchronises the state of the drawer with the drawer layout. */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /** Changes the configuration to the new configuration if it changes. */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /** Defines what to do to the fragments when the back button on the phone is pressed. */
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() <= 1){
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
