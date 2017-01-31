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
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
            new DateSelector(rootView, activity).calendarPopUp();
            new TimeSelector(rootView, activity).timePopUp();
        }
    }

    /** Sets the old info about the dinner in the EditTexts. */
    private void setOldInfo() {
        String[] splitDate = dinner.getDate().split(" ");
        String date = splitDate[0];
        String time = splitDate[1];

        ((EditText) rootView.findViewById(R.id.newTitle)).setText(dinner.getTitle());
        ((EditText) rootView.findViewById(R.id.giveDate)).setText(date);
        ((EditText) rootView.findViewById(R.id.giveTime)).setText(time);
        ((EditText) rootView.findViewById(R.id.newIngredients)).setText(dinner.getIngredients());
    }

    /** Updates the info about the dinner in the database with the user's input. */
    private void updateInfo() {
        String title = ((EditText) rootView.findViewById(R.id.newTitle)).getText().toString();
        String date = ((EditText) rootView.findViewById(R.id.giveDate)).getText().toString();
        String time = ((EditText) rootView.findViewById(R.id.giveTime)).getText().toString();
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
