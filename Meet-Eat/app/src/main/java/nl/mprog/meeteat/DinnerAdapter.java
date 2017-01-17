package nl.mprog.meeteat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tirza on 11-1-17.
 */

class DinnerAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private ArrayList<Dinner> dinners;

    DinnerAdapter(ResultFragment activity, ArrayList<Dinner> dinners) {
        inflater = LayoutInflater.from(activity.getActivity());
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
        convertView = inflater.inflate(R.layout.list_group, parentView, false);

        ((TextView) convertView.findViewById(R.id.dinnerTitle)).setText(dinner);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {
        final Dinner dinner = dinners.get(groupPosition);
        String dinnerId = dinner.getId();
        String host = "Host: " + dinner.getHost();
        String freeSpaces = "Free spaces: " + Integer.toString(dinner.getFreeSpaces());
        String startTime = "Start time: " + dinner.getStartTime();
        String ingredients = "Ingredients: " + dinner.getIngredients();
        convertView = inflater.inflate(R.layout.list_child, parentView, false);

        ((TextView) convertView.findViewById(R.id.host)).setText(host);
        ((TextView) convertView.findViewById(R.id.space)).setText(freeSpaces);
        ((TextView) convertView.findViewById(R.id.startTime)).setText(startTime);
        ((TextView) convertView.findViewById(R.id.ingredients)).setText(ingredients);
        ImageButton joinButton = (ImageButton) convertView.findViewById(R.id.joinButton);
        setClickListener(joinButton, dinnerId);

        return convertView;
    }

    private void setClickListener(ImageButton button, final String dinnerId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.updateFreeSpaces(dinnerId, v.getContext());
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
