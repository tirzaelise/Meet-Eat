package nl.mprog.meeteat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tirza on 21-1-17.
 */

class DrawerAdapter extends ArrayAdapter<DrawerItem> {
    private Context context;
    private int resource;
    private ArrayList<DrawerItem> data;

    DrawerAdapter(Context context, int resource, ArrayList<DrawerItem> data) {
        super(context, resource);

        this.context = context;
        this.resource= resource;
        this.data = data;
    }

    @Override
    public int getCount(){
        return data.size();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            ImageView  menuIcon = (ImageView) view.findViewById(R.id.menuIcon);
            TextView  menuItem = (TextView) view.findViewById(R.id.menuItem);
            DrawerItem currentItem = data.get(position);

            menuIcon.setImageResource(currentItem.getIcon());
            menuItem.setText(currentItem.getItem());
        } else {
            Log.wtf("not in null", "aasdsad");
            DrawerItem currentItem = this.data.get(position);
            ((ImageView) view.findViewById(R.id.menuIcon)).setImageResource(currentItem.getIcon());
            ((TextView) view.findViewById(R.id.menuItem)).setText(currentItem.getItem());
        }

        return convertView;
    }
}
