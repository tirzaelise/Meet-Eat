package nl.mprog.meeteat;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
            FragmentManager manager = getFragmentManager();
            MainFragment mainFragment = new MainFragment();
            manager.beginTransaction()
                    .replace(R.id.contentFrame, mainFragment)
                    .commit();
        }
    }

    /** Creates menu items for the navigation drawer */
    private void createDrawerItems() {
        String[] menuArray = {"Home", "Account", "Hosting dinners", "Joined dinners"};
//        ArrayList<DrawerItem> data = createDrawerData();

//        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.layout_drawer, data);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                menuArray);
        drawerList.setAdapter(adapter);
    }

    private ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem(R.drawable.home_icon, "Home"));
        data.add(new DrawerItem(R.drawable.account_icon, "Account"));
        data.add(new DrawerItem(R.drawable.hosting_icon, "Hosting dinners"));
        data.add(new DrawerItem(R.drawable.joining_icon, "Joining dinners"));

        return data;
    }

    /** Sets a listener on the navigation drawer */
    private void setDrawerListener() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();

                switch (position) {
                    case 0:
                        MainFragment mainFragment = new MainFragment();
                        manager.beginTransaction()
                                .replace(R.id.contentFrame, mainFragment)
                                .commit();

                        break;
                    case 1:
                        AccountFragment accountFragment = new AccountFragment();
                        manager.beginTransaction()
                                .replace(R.id.contentFrame, accountFragment)
                                .commit();
                        break;
                    case 2:
                        HostListFragment hostListFragment = new HostListFragment();
                        manager.beginTransaction()
                                .replace(R.id.contentFrame, hostListFragment)
                                .commit();
                        break;
                    case 3:
                        JoinListFragment joinListFragment = new JoinListFragment();
                        manager.beginTransaction()
                                .replace(R.id.contentFrame, joinListFragment)
                                .commit();
                        break;
                }
            }
        });
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
}
