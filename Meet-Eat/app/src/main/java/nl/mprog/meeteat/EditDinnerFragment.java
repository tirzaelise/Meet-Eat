/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This fragment allows the user to edit a dinner that they're hosting. They can edit the title,
 * date and ingredients of their dinner. This new information is saved in the database when the
 * Save button is clicked.
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDinnerFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private Activity activity;
    private Dinner dinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_dinner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getView();
        activity = getActivity();
        Bundle arguments = this.getArguments();

        if (rootView != null && arguments != null) {
            rootView.findViewById(R.id.saveButton).setOnClickListener(this);

            dinner = (Dinner) arguments.getSerializable("dinner");
            setOldInfo();
            calendarPopUp();
            timePopUp();
        }
    }

    /** Sets the old info about the dinner in the EditTexts. */
    private void setOldInfo() {
        String[] splitDate = dinner.getDate().split(" ");
        String date = splitDate[0];
        String time = splitDate[1];

        ((EditText) rootView.findViewById(R.id.newTitle)).setText(dinner.getTitle());
        ((EditText) rootView.findViewById(R.id.newDate)).setText(date);
        ((EditText) rootView.findViewById(R.id.newTime)).setText(time);
        ((EditText) rootView.findViewById(R.id.newIngredients)).setText(dinner.getIngredients());
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
        ((EditText) rootView.findViewById(R.id.newDate)).setText(date.format(calendar.getTime()));
    }

    /**
     * Sets a listener on the EditText so that a date can be picked when the it is clicked and
     * removes the option to choose dates before today.
     */
    private void setDateListener(final Calendar calendar,
                                 final DatePickerDialog.OnDateSetListener date) {
        rootView.findViewById(R.id.newDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(activity, date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
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
        ((EditText) rootView.findViewById(R.id.newTime)).setText(time.format(calendar.getTime()));
    }

    /** Sets a listener on the EditText o that a time can be picked when it is clicked. */
    private void setTimeListener(final Calendar calendar,
                                 final TimePickerDialog.OnTimeSetListener time) {
        rootView.findViewById(R.id.newTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    /** Updates the info about the dinner with the user's input. */
    private void updateInfo() {
        String title = ((EditText) rootView.findViewById(R.id.newTitle)).getText().toString();
        String date = ((EditText) rootView.findViewById(R.id.newDate)).getText().toString();
        String time = ((EditText) rootView.findViewById(R.id.newTime)).getText().toString();
        String ingredients = ((EditText) rootView.findViewById(R.id.newIngredients)).getText()
                .toString();

        dinner.setTitle(title);
        dinner.setDate(date + " " + time);
        dinner.setIngredients(ingredients);

        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.updateDinner(dinner, activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                updateInfo();
                break;
        }
    }
}
