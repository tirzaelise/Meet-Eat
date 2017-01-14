package nl.mprog.meeteat;

import android.app.FragmentManager;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private String[] menuArray;
    private DrawerLayout drawerLayout;
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
    }

    /** Creates menu items for the navigation drawer */
    private void createDrawerItems() {
        menuArray = new String[]{"Home", "Account"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                menuArray);
        drawerList.setAdapter(adapter);
    }

    /** Sets a listener on the navigation drawer */
    private void setDrawerListener() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        FragmentManager manager = getFragmentManager();
                        MainFragment mainFragment = new MainFragment();
                        manager.beginTransaction()
                                .replace(R.id.contentFrame, mainFragment)
                                .commit();
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, AccountFragment.class));
                        break;
                    default:
                        startActivity(new Intent(MainActivity.this, MainFragment.class));
                        break;
                }
            }
        });
    }

    /** Sets up the navigation drawer */
    private void setUpDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
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
