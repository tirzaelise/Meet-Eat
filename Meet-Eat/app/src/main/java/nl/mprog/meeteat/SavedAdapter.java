/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the adapter for dinners that the user is hosting and has joined. It
 * displays the relevant information, namely the title, the date, the guests that are joining and,
 * optionally, the host. The user can also edit and delete their hosted dinners using this adapter.
 * Moreover, the user can remove themselves from a dinner that they joined.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class SavedAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Dinner> dinners;
    private LayoutInflater inflater;
    private boolean joining;

    SavedAdapter(Activity activity, ArrayList<Dinner> dinners, boolean joining) {
        this.activity = activity;
        this.dinners = dinners;
        this.joining = joining;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dinners.size();
    }

    @Override
    public Object getItem(int position) {
        return dinners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_saved_row, parent, false);
        }

        setDinnerInfo(view, position);
        setDinnerImage(view, position);
        setHostOptionally(view, position);

        ImageButton editButton = (ImageButton) view.findViewById(R.id.editButton);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        setClickListener(editButton, position);
        setClickListener(deleteButton, position);

        return view;
    }

    /** Sets all the information about a dinner in the ListView. */
    private void setDinnerInfo(View view, int position) {
        String title = this.dinners.get(position).getTitle();
        String date = activity.getString(R.string.date) + ": " +
                this.dinners.get(position).getDate();
        String guests = activity.getString(R.string.guests) + ": " +
                arrayToString(this.dinners.get(position).getGuestNames());
        String ingredients = activity.getString(R.string.ingredients) + ": " +
                this.dinners.get(position).getIngredients();

        ((TextView) view.findViewById(R.id.dinnerTitle)).setText(title);
        ((TextView) view.findViewById(R.id.dinnerDate)).setText(date);
        ((TextView) view.findViewById(R.id.dinnerGuests)).setText(guests);
        ((TextView) view.findViewById(R.id.dinnerIngredients)).setText(ingredients);
    }

    /**
     * If the adapter is being used to show the joined dinners, set the host name in the ListView.
     * Otherwise, set the visibility of the TextView to gone.
     */
    private void setHostOptionally(View view, int position) {
        if (joining) {
            String host = activity.getString(R.string.host) + ": "
                    + dinners.get(position).getHostName();
            ((TextView) view.findViewById(R.id.dinnerHost)).setText(host);
            view.findViewById(R.id.editButton).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.dinnerHost).setVisibility(View.GONE);
        }
    }

    /** Converts an array to a string. */
    private String arrayToString(ArrayList<String> array) {
        String string;

        if (array.get(0).equals("null")) {
            string = activity.getString(R.string.none);
        } else {
            string = array.get(0);
        }

        for (int i = 1; i < array.size(); i++) {
            if (!array.get(i).equals("null")) {
                string = string + ", " + array.get(i);
            } else {
                break;
            }
        }
        return string;
    }

    /** Sets the image of a dinner in the ImageView. */
    private void setDinnerImage(View view, int position) {
        ImageView dinnerImage = (ImageView) view.findViewById(R.id.dinnerImage);
        String id = this.dinners.get(position).getId();
        String url = "https://spoonacular.com/recipeImages/" + id + "-312x231.jpg";

        Picasso.with(activity).load(url).into(dinnerImage);
    }

    /** Sets a click listener on the edit and delete buttons. */
    private void setClickListener(ImageButton button, final int position) {
        final Dinner dinner = dinners.get(position);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.editButton:
                        alertDialog(activity.getString(R.string.dialogEdit), v.getId(),
                                dinner, position);
                        break;
                    case R.id.deleteButton:
                        if (joining) {
                            alertDialog(activity.getString(R.string.dialogUnjoin),
                                    v.getId(), dinner, position);
                            break;
                        } else {
                            alertDialog(activity.getString(R.string.dialogDelete), v.getId(),
                                    dinner, position);
                            break;
                        }
                }
            }
        });
    }

    /** Creates an alert dialog when a button is clicked. */
    private void alertDialog(String alert, final int buttonId, final Dinner dinner,
                             final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(alert);
        builder.setCancelable(true);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                clickedYes(buttonId, dinner, position);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /** Defines what to do when the user has clicked 'Yes' in the alert dialog. */
    private void clickedYes(int buttonId, Dinner dinner, int position) {
        switch (buttonId) {
            case R.id.editButton:
                goToEditFragment(dinner);
                break;
            case R.id.deleteButton:
                if (joining) {
                    unjoinDinner(dinner, position);
                    break;
                } else {
                    deleteDinner(dinner);
                    break;
                }
        }
    }

    /** Sends the user to the fragment where they can edit their dinner. */
    private void goToEditFragment(Dinner dinner) {
        EditDinnerFragment editDinnerFragment = new EditDinnerFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("dinner", dinner);
        editDinnerFragment.setArguments(arguments);

        FragmentManager manager = activity.getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.contentFrame, editDinnerFragment)
                .addToBackStack(null)
                .commit();
    }

    /** Removes a user from a dinner. */
    private void unjoinDinner(Dinner dinner, int position) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.removeGuest(dinner, activity, dinners, this, position);
    }

    /** Deletes a dinner from the database. */
    private void deleteDinner(Dinner dinner) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.deleteDinner(dinner, activity, this, dinners);
    }

    /** Gets the new list of dinners from the DatabaseHandler. */
    void setData(ArrayList<Dinner> dinners) {
        this.dinners = dinners;
    }
}
