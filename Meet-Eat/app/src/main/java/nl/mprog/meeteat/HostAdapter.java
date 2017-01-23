package nl.mprog.meeteat;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tirza on 23-1-17.
 */

class HostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Dinner> dinners;
    private LayoutInflater inflater;
    private Dinner dinner;
    private TextView dinnerTitle;
    private TextView dinnerDate;
    private TextView dinnerGuests;
    private TextView dinnerIngredients;
    private ImageView dinnerImage;

    HostAdapter(Context context, ArrayList<Dinner> dinners) {
        this.context = context;
        this.dinners = dinners;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = inflater.inflate(R.layout.layout_host_row, parent, false);
            initialiseViewsAndRecipe(view, position);
            String date = "Date: " + dinner.getDate();
            String guests = "Guests: " + arrayToString(dinner.getGuestNames());
            String ingredients = "Ingredients: " + dinner.getIngredients();

            dinnerTitle.setText(dinner.getTitle());
            dinnerDate.setText(date);
            dinnerGuests.setText(guests);
            dinnerIngredients.setText(ingredients);

            String url = "https://spoonacular.com/recipeImages/" + dinner.getId() + "-312x231.jpg";
            Picasso.with(context).load(url).into(dinnerImage);
        } else {
            initialiseViewsAndRecipe(view, position);
            String date = "Date: " + dinners.get(position).getDate();
            String guests = "Guests: " + arrayToString(this.dinners.get(position).getGuestNames());
            String ingredients = "Ingredients: " + dinners.get(position).getIngredients();

            dinnerTitle.setText(dinners.get(position).getTitle());
            dinnerDate.setText(date);
            dinnerGuests.setText(guests);
            dinnerIngredients.setText(ingredients);

            String url = "https://spoonacular.com/recipeImages/" +
                    this.dinners.get(position).getId() + "-312x231.jpg";
            Picasso.with(context).load(url).into(dinnerImage);
        }
        return view;
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
        String string = array.get(0);

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
