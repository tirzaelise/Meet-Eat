package nl.mprog.meeteat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }

    /** Searches for a dinner given the user's input */
    public void search(View view) {
        String area = ((EditText) findViewById(R.id.giveArea)).getText().toString();
        String cuisine = ((EditText) findViewById(R.id.givePreference)).getText().toString();

        if (!area.equals("")) {
            Intent results = new Intent(this, ResultsActivity.class);
            results.putExtra("area", area);
            results.putExtra("cuisine", cuisine);
            startActivity(results);
        } else {
            Toast.makeText(this, "Please enter the area you're in", Toast.LENGTH_SHORT).show();
        }
    }
}
