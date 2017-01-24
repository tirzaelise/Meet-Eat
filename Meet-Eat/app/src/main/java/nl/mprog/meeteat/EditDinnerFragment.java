package nl.mprog.meeteat;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        }
    }

    /** Sets the old info about the dinner in the EditTexts. */
    private void setOldInfo() {
        ((EditText) rootView.findViewById(R.id.newTitle)).setText(dinner.getTitle());
        ((EditText) rootView.findViewById(R.id.newDate)).setText(dinner.getDate());
        ((EditText) rootView.findViewById(R.id.newIngredients)).setText(dinner.getIngredients());
    }

    /** Updates the info about the dinner with the user's input. */
    private void updateInfo() {
        String title = ((EditText) rootView.findViewById(R.id.newTitle)).getText().toString();
        String date = ((EditText) rootView.findViewById(R.id.newDate)).getText().toString();
        String ingredients = ((EditText) rootView.findViewById(R.id.newIngredients)).getText()
                .toString();

        dinner.setTitle(title);
        dinner.setDate(date);
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
