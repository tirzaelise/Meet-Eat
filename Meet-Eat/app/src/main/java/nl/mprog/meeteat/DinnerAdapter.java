/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the adapter for the ListView that shows the dinners in the area that the
 * user searched for. The adapter sets the information about the dinner in the ListView, namely the
 * host, the amount of free spaces, the date, the ingredients that will be used and the image of the
 * dinner. There is also a join button that the user can click if they want to join a dinner.
 */

package nl.mprog.meeteat;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

class DinnerAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Dinner> dinners;

    DinnerAdapter(Fragment fragment, ArrayList<Dinner> dinners) {
        inflater = LayoutInflater.from(fragment.getActivity());
        this.context = fragment.getActivity();
        this.dinners = dinners;
    }

    @Override
    public int getGroupCount() {
        return dinners.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dinners.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dinners.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parentView) {
        String dinner = dinners.get(groupPosition).getTitle();
        convertView = inflater.inflate(R.layout.layout_parent, parentView, false);

        ((TextView) convertView.findViewById(R.id.dinnerTitle)).setText(dinner);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_child, parentView, false);
        }

        setInfo(groupPosition, view);
        setVisibility(groupPosition, view);
        setImage(groupPosition, view);

        ImageButton joinButton = (ImageButton) view.findViewById(R.id.joinButton);
        setClickListener(joinButton, groupPosition);

        return convertView;
    }

    /**
     * Sets the info about a dinner in the ListView.
     */
    private void setInfo(int position, View view) {
        String host = "Host: " + this.dinners.get(position).getHostName();
        String guestsString = Arrays.toString(this.dinners.get(position).getGuestNames().toArray());
        String freeSpaces = "Free spaces: " +
                Integer.toString(StringUtils.countMatches(guestsString, "null"));
        String date = "Date: " + this.dinners.get(position).getDate();
        String ingredients = "Ingredients: " + this.dinners.get(position).getIngredients();

        ((TextView) view.findViewById(R.id.host)).setText(host);
        ((TextView) view.findViewById(R.id.space)).setText(freeSpaces);
        ((TextView) view.findViewById(R.id.date)).setText(date);
        ((TextView) view.findViewById(R.id.ingredients)).setText(ingredients);
    }

    /**
     * Sets the visibility of vegetarian and vegan TextViews according to their boolean values.
     */
    private void setVisibility(int position, View view) {
        boolean isVegetarian = this.dinners.get(position).isVegetarian();
        boolean isVegan = this.dinners.get(position).isVegan();
        TextView vegetarian = (TextView) view.findViewById(R.id.vegetarian);
        TextView vegan = (TextView) view.findViewById(R.id.vegan);

        booleanToVisibility(isVegetarian, isVegan, vegetarian, vegan);
    }

    /**
     * Sets the image of a dinner in the ListView.
     */
    private void setImage(int position, View view) {
        ImageView dinnerImage = (ImageView) view.findViewById(R.id.dinnerImage);

        String url = "https://spoonacular.com/recipeImages/" + this.dinners.get(position).getId() +
                "-312x231.jpg";
        Picasso.with(context).load(url).into(dinnerImage);
    }

    /**
     * Uses the boolean values to set visibility and text.
     */
    private void booleanToVisibility(boolean isVegetarian, boolean isVegan, TextView vegetarian,
                                     TextView vegan) {
        if (isVegan) {
            vegetarian.setVisibility(View.VISIBLE);
            String isVeganAndVegetarian = vegetarian.getText().toString() + ", ";
            vegetarian.setText(isVeganAndVegetarian);
            vegetarian.setVisibility(View.VISIBLE);
        } else if (isVegetarian) {
            vegetarian.setVisibility(View.VISIBLE);
            vegan.setVisibility(View.INVISIBLE);
        } else {
            vegetarian.setVisibility(View.INVISIBLE);
            vegan.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets a click listener on the join button so that the user can join a dinner.
     */
    private void setClickListener(ImageButton button, final int position) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    updateGuests(position);
                } else {
                    Toast.makeText(context, "Please sign in to join a dinner", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    /** Updates the list of guests in the database if there are free spaces. */
    private void updateGuests(int position) {
        Dinner dinner = this.dinners.get(position);
        String guestString = dinner.getGuestNames().toString();
        int freeSpaces = StringUtils.countMatches(guestString, "null");

        if (freeSpaces > 0) {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.updateFreeSpaces(context, dinner, position, this, dinners);
        } else {
            Toast.makeText(context, "There are no more free spaces for this dinner",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
