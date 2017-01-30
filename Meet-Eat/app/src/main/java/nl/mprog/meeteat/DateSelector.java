package nl.mprog.meeteat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by tirza on 30-1-17.
 */

public class DateSelector {
    View view;
    Activity activity;

    DateSelector(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    /** Creates a pop up with a calendar so that the user can easily pick a date. */
    void calendarPopUp() {
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
        ((EditText) view.findViewById(R.id.giveDate)).setText(date.format(calendar.getTime()));
    }

    /**
     * Sets a listener on the EditText so that a date can be picked when the it is clicked and
     * removes the option to choose dates before today.
     */
    private void setDateListener(final Calendar calendar,
                                 final DatePickerDialog.OnDateSetListener date) {
        view.findViewById(R.id.giveDate).setOnClickListener(new View.OnClickListener() {
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
}
