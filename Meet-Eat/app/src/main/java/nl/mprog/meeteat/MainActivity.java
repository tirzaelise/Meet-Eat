package nl.mprog.meeteat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

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
            MainFragment mainFragment = new MainFragment();
            changeFragment(mainFragment);
        }
    }

    /** Creates menu items for the navigation drawer */
    private void createDrawerItems() {
        ArrayList<DrawerItem> data = createDrawerData();
        DrawerAdapter adapter = new DrawerAdapter(this, data);

        drawerList.setAdapter(adapter);
    }

    private ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem(R.drawable.home_icon, "Home"));
        data.add(new DrawerItem(R.drawable.account_icon, "Account"));
        data.add(new DrawerItem(R.drawable.hosting_icon, "Hosting dinners"));
        data.add(new DrawerItem(R.drawable.joining_icon, "Joined dinners"));

        return data;
    }

    /** Sets a listener on the navigation drawer */
    private void setDrawerListener() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawers();

                switch (position) {
                    case 0:
                        MainFragment mainFragment = new MainFragment();
                        changeFragment(mainFragment);
                        break;
                    case 1:
                        AccountFragment accountFragment = new AccountFragment();
                        changeFragment(accountFragment);
                        break;
                    case 2:
                        HostListFragment hostListFragment = new HostListFragment();
                        changeFragment(hostListFragment);
                        break;
                    case 3:
                        JoinListFragment joinListFragment = new JoinListFragment();
                        changeFragment(joinListFragment);
                        break;
                }
            }
        });
    }

    /** Changes the Fragment in the content frame to the new fragment. */
    private void changeFragment(Fragment newFragment) {
        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
                .replace(R.id.contentFrame, newFragment)
                .addToBackStack(null)
                .commit();
    }

    /** Sets up the navigation drawer */
    private void setUpDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setElevation(0);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
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
