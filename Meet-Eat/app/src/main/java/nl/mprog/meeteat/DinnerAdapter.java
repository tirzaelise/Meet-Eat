package nl.mprog.meeteat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tirza on 11-1-17.
 */

class DinnerAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private ArrayList<Dinner> dinners;

    DinnerAdapter(ResultsActivity activity, ArrayList<Dinner> dinners) {
        inflater = LayoutInflater.from(activity);
        this.dinners = dinners;
    }

    @Override
    public int getGroupCount() {
        return dinners.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 5;
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
        String dinner = dinners.get(groupPosition).getFood();
        convertView = inflater.inflate(R.layout.list_group, parentView, false);

        ((TextView) convertView.findViewById(R.id.dinnerTitle)).setText(dinner);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {
        final Dinner dinner = dinners.get(groupPosition);
        String host = "Host: " + dinner.getHost();
        String amountOfPeople = "Free spaces: " + Integer.toString(dinner.getAmountOfPeople());
        String startTime = "Start time: " + dinner.getStartTime();
        String cuisine = "Cuisine: " + dinner.getCuisine();
        String ingredients = "Ingredients: " + dinner.getIngredients();
        convertView = inflater.inflate(R.layout.list_child, parentView, false);

        ((TextView) convertView.findViewById(R.id.host)).setText(host);
        ((TextView) convertView.findViewById(R.id.space)).setText(amountOfPeople);
        ((TextView) convertView.findViewById(R.id.startTime)).setText(startTime);
        ((TextView) convertView.findViewById(R.id.cuisine)).setText(cuisine);
        ((TextView) convertView.findViewById(R.id.ingredients)).setText(ingredients);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
