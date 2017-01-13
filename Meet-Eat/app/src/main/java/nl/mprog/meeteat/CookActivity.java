package nl.mprog.meeteat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
    }

    /** Adds a dinner to the database */
    public void addDinner(View view) {
        String food = ((EditText) findViewById(R.id.giveFood)).getText().toString();
        String host = getSharedPreferences("userInfo", Context.MODE_PRIVATE).
                getString("userName", "");
        String startTime = ((EditText) findViewById(R.id.giveTime)).getText().toString();
        String amountPeopleString = ((EditText) findViewById(R.id.giveAmount)).getText().toString();
        String cuisine = ((EditText) findViewById(R.id.giveCuisine)).getText().toString();
        String area = ((EditText) findViewById(R.id.giveArea)).getText().toString();
        String ingredients = ((EditText) findViewById(R.id.giveIngredients)).getText().toString();

        if (correctTimeFormat(startTime)) {
            createDinner(food, host, startTime, amountPeopleString, cuisine, area, ingredients);
        }
    }

    /** Checks if a start time corresponds to the format hh:mm */
    boolean correctTimeFormat(String time) {
        String[] splitTime = time.split(":");

        // If there is no ":" in the string, split returns an array containing the input at the
        // first place
        if (splitTime[0].equals(time)) {
            return false;
        } else {
            String[] splitHour = splitTime[0].split("");
            String[] splitMins = splitTime[1].split("");
            if ((splitHour[0].equals("0") || splitHour[0].equals("1") || splitHour[0].equals("2"))
                    && splitHour[1].matches("[0-9]+") && splitMins[0].matches("[0-5]+") &&
                    splitMins[1].matches("[0-9]+")) {
                return true;
            }
        }
        return false;
    }

    /** Creates a dinner object */
    private void createDinner(String food, String host, String startTime, String amountPeopleString,
                              String cuisine, String area, String ingredients) {
        if (!food.equals("") && !host.equals("") && !startTime.equals("") &&
                !amountPeopleString.equals("") && !cuisine.equals("") && !area.equals("") &&
                !ingredients.equals("")) {
            int amountOfPeople = Integer.valueOf(amountPeopleString);
            Dinner newDinner = new Dinner(food, host, startTime, amountOfPeople, cuisine, area,
                    ingredients);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.writeToDatabase(newDinner);
            Toast.makeText(this, "Added dinner successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
