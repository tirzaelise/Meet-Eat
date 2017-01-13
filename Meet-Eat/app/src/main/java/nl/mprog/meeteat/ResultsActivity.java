package nl.mprog.meeteat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String area = getIntent().getStringExtra("area");
        String cuisine = getIntent().getStringExtra("cuisine");
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ArrayList<Dinner> dinners = databaseHandler.readDatabase(area, cuisine);

        DinnerAdapter adapter = new DinnerAdapter(this, dinners);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}