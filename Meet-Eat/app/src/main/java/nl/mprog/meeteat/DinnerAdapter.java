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

/**
 * Created by tirza on 11-1-17.
 */

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
        final Dinner dinner = dinners.get(groupPosition);

        String dinnerId = dinner.getId();
        String host = "Host: " + dinner.getHostName();
        String guestsString = Arrays.toString(dinner.getGuestNames().toArray());
        String freeSpaces = "Free spaces: "  +
                Integer.toString(StringUtils.countMatches(guestsString, "null"));
        String date = "Date: " + dinner.getDate();
        String ingredients = "Ingredients: " + dinner.getIngredients();
        convertView = inflater.inflate(R.layout.layout_child, parentView, false);

        ((TextView) convertView.findViewById(R.id.host)).setText(host);
        ((TextView) convertView.findViewById(R.id.space)).setText(freeSpaces);
        ((TextView) convertView.findViewById(R.id.date)).setText(date);
        TextView vegetarian = (TextView) convertView.findViewById(R.id.vegetarian);
        TextView vegan = (TextView) convertView.findViewById(R.id.vegan);
        booleanToVisibility(dinner.isVegetarian(), dinner.isVegan(), vegetarian, vegan);
        ((TextView) convertView.findViewById(R.id.ingredients)).setText(ingredients);
        ImageView dinnerImage = (ImageView) convertView.findViewById(R.id.dinnerImage);

        String url = "https://spoonacular.com/recipeImages/" +
                dinner.getId() + "-312x231.jpg";
        Picasso.with(context).load(url).into(dinnerImage);

        ImageButton joinButton = (ImageButton) convertView.findViewById(R.id.joinButton);
        setClickListener(joinButton, dinnerId, groupPosition);

        return convertView;
    }

    /** Uses the boolean values to set visibility and text. */
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

    private void setClickListener(ImageButton button, final String dinnerId, final int position) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    DatabaseHandler databaseHandler = new DatabaseHandler();
                    databaseHandler.updateFreeSpaces(dinnerId, v.getContext(), position, DinnerAdapter.this, dinners);
                } else {
                    Toast.makeText(context, "Please sign in to join a dinner", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void updateAdapter() {

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
