package nl.mprog.meeteat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tirza on 23-1-17.
 */

class SavedAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Dinner> dinners;
    private LayoutInflater inflater;
    private boolean joining;
    private Dinner dinner;
    private TextView dinnerTitle;
    private TextView dinnerDate;
    private TextView dinnerGuests;
    private TextView dinnerIngredients;
    private ImageView dinnerImage;

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

        if (view == null) {
            view = inflater.inflate(R.layout.layout_saved_row, parent, false);
            initialiseViewsAndRecipe(view, position);
            String date = "Date: " + dinner.getDate();
            String guests = "Guests: " + arrayToString(dinner.getGuestNames());
            String ingredients = "Ingredients: " + dinner.getIngredients();

            dinnerTitle.setText(dinner.getTitle());
            dinnerDate.setText(date);
            dinnerGuests.setText(guests);
            dinnerIngredients.setText(ingredients);

            if (joining) {
                String host = "Host: " + dinner.getHostName();
                ((TextView) view.findViewById(R.id.dinnerHost)).setText(host);
                view.findViewById(R.id.editButton).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.dinnerHost).setVisibility(View.GONE);
            }

            String url = "https://spoonacular.com/recipeImages/" + dinner.getId() + "-312x231.jpg";
            Picasso.with(activity).load(url).into(dinnerImage);

        } else {
            initialiseViewsAndRecipe(view, position);
            String date = "Date: " + dinners.get(position).getDate();
            String guests = "Guests: " + arrayToString(this.dinners.get(position).getGuestNames());
            String ingredients = "Ingredients: " + dinners.get(position).getIngredients();

            dinnerTitle.setText(dinners.get(position).getTitle());
            dinnerDate.setText(date);
            dinnerGuests.setText(guests);
            dinnerIngredients.setText(ingredients);

            if (joining) {
                String host = "Host: " + dinners.get(position).getHostName();
                ((TextView) view.findViewById(R.id.dinnerHost)).setText(host);
                view.findViewById(R.id.editButton).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.dinnerHost).setVisibility(View.GONE);
            }

            String url = "https://spoonacular.com/recipeImages/" +
                    this.dinners.get(position).getId() + "-312x231.jpg";
            Picasso.with(activity).load(url).into(dinnerImage);
        }

        ImageButton editButton = (ImageButton) view.findViewById(R.id.editButton);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        setClickListener(editButton, position);
        setClickListener(deleteButton, position);

        return view;
    }

    /** Sets a click listener on the buttons. */
    private void setClickListener(ImageButton button, final int position) {
        final Dinner dinner = dinners.get(position);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.editButton:
                        alertDialog("Are you sure you want to edit this dinner?", v.getId(),
                                dinner, joining, position);
                        break;
                    case R.id.deleteButton:
                        if (joining) {
                            alertDialog("Are you sure you no longer want to join this dinner?",
                                    v.getId(), dinner, joining, position);
                            break;
                        } else {
                            alertDialog("Are you sure you want to delete this dinner?", v.getId(),
                                    dinner, joining, position);
                            break;
                        }
                }
            }
        });
    }

    /** Creates an alert dialog when a button is clicked. */
    private void alertDialog(String alert, final int id, final Dinner dinner,
                             final boolean joining, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(alert);
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                switch (id) {
                    case R.id.editButton:
                        EditDinnerFragment editDinnerFragment = new EditDinnerFragment();
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("dinner", dinner);
                        editDinnerFragment.setArguments(arguments);

                        FragmentManager manager = activity.getFragmentManager();

                        manager.beginTransaction()
                                .replace(R.id.contentFrame, editDinnerFragment)
                                .addToBackStack(null)
                                .commit();
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
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /** Removes a user from a dinner. */
    private void unjoinDinner(Dinner dinner, int position) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.removeGuest(dinner, activity, dinners, this, position);
    }

    /** Gets the new list of dinners from the DatabaseHandler. */
    void setData(ArrayList<Dinner> dinners) {
        this.dinners = dinners;
    }

    /** Deletes a dinner from the database. */
    private void deleteDinner(Dinner dinner) {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.deleteDinner(dinner, activity, this, dinners);
    }


    /** Initialises the views in the row layout and a value for the recipe. */
    private void initialiseViewsAndRecipe(View view, int position) {
        dinner = dinners.get(position);
        dinnerTitle = (TextView) view.findViewById(R.id.dinnerTitle);
        dinnerDate = (TextView) view.findViewById(R.id.dinnerDate);
        dinnerGuests = (TextView) view.findViewById(R.id.dinnerGuests);
        dinnerIngredients = (TextView) view.findViewById(R.id.dinnerIngredients);
        dinnerImage = (ImageView) view.findViewById(R.id.dinnerImage);
    }

    /** Converts an array to a string. */
    private String arrayToString(ArrayList<String> array) {
        String string;

        if (array.get(0).equals("null")) {
            string = "none";
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
}
