/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the fragment where the user can give information about the dinner they
 * will be holding.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class CookFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Activity activity;
    private String food;
    private String date;
    private String freeSpacesString;
    private String area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cook, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();

        if (rootView != null) {
            rootView.findViewById(R.id.addButton).setOnClickListener(this);
            calendarPopUp();
            timePopUp();
        }
    }

    /** Creates a pop up with a calendar so that the user can easily pick a date. */
    private void calendarPopUp() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(calendar);
            }
        };
        setDateListener(calendar, date);
    }

    /** Updates the word 'Date' in the EditText to the date that was picked. */
    private void updateDate(Calendar calendar) {
        String format = "dd/MM/yy";
        SimpleDateFormat date = new SimpleDateFormat(format, Locale.UK);
        ((EditText) rootView.findViewById(R.id.giveDate)).setText(date.format(calendar.getTime()));
    }

    /** Sets a listener on the EditText so that a date can be picked when the it is clicked. */
    private void setDateListener(final Calendar calendar, 
                                 final DatePickerDialog.OnDateSetListener date) {
        rootView.findViewById(R.id.giveDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, date, calendar.get(Calendar.YEAR), 
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /** Creates a pop up with a clock so that the user can easily pick a time. */
    private void timePopUp() {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTime(calendar);
            }
        };
        setTimeListener(calendar, time);
    }

    /** Updates the time in the EditText to the selected time. */
    private void updateTime(Calendar calendar) {
        String format = "HH:mm";
        SimpleDateFormat time = new SimpleDateFormat(format, Locale.UK);
        ((EditText) rootView.findViewById(R.id.giveTime)).setText(time.format(calendar.getTime()));
    }

    /** Sets a listener on the EditText o that a time can be picked when it is clicked. */
    private void setTimeListener(final Calendar calendar,
                                 final TimePickerDialog.OnTimeSetListener time) {
        rootView.findViewById(R.id.giveTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    /**
     * Sends the user to the fragment where the search results will be displayed if everything is
     * filled in correctly.
     */
    public void searchRecipes() {
        food = ((EditText) rootView.findViewById(R.id.giveFood)).getText().toString();
        freeSpacesString = ((EditText) rootView.findViewById(R.id.giveAmount)).getText()
                .toString();
        area = capitalizeFully(((EditText) rootView.findViewById(R.id.giveArea)).getText()
                .toString());

        if (!filledInAllFields()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            food = food.replace(" ", "&nbsp;");
            date = ((EditText) rootView.findViewById(R.id.giveDate)).getText().toString() + " " +
                    ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();
            toRecipeResultFragment();
        }
    }

    /** Checks if all fields were filled in. */
    private boolean filledInAllFields() {
        String date = ((EditText) rootView.findViewById(R.id.giveDate)).getText().toString();
        String time = ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();

        return (!food.equals("") && !date.equals("") && !time.equals("") &&
                !freeSpacesString.equals("") && !area.equals(""));
    }

    /** Sends the user to the fragment where they can see the results of their recipe search. */
    private void toRecipeResultFragment() {
        int freeSpaces = Integer.valueOf(freeSpacesString);
        RecipeResultFragment recipeResultFragment = new RecipeResultFragment();
        Bundle arguments = new Bundle();
        arguments.putString("food", food);
        arguments.putString("date", date);
        arguments.putInt("freeSpaces", freeSpaces);
        arguments.putString("area", area);
        recipeResultFragment.setArguments(arguments);

        this.getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, recipeResultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                searchRecipes();
                break;
        }
    }
}
