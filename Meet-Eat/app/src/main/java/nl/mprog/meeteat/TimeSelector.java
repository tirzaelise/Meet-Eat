/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the time selector. The user can select the time of their dinner here using
 * a TimePickerDialog. The selected time is automatically set in the EditText.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class TimeSelector {
    View view;
    Activity activity;

    TimeSelector(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }


    /** Creates a pop up with a clock so that the user can easily pick a time. */
    void timePopUp() {
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
        ((EditText) view.findViewById(R.id.giveTime)).setText(time.format(calendar.getTime()));
    }

    /** Sets a listener on the EditText o that a time can be picked when it is clicked. */
    private void setTimeListener(final Calendar calendar,
                                 final TimePickerDialog.OnTimeSetListener time) {
        view.findViewById(R.id.giveTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }
}
