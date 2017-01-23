package nl.mprog.meeteat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tirza on 21-1-17.
 */

class DrawerAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DrawerItem> data;

    DrawerAdapter(Context context, ArrayList<DrawerItem> data) {
//        super(context, resource);

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_drawer_row, parent, false);
            ImageView  menuIcon = (ImageView) view.findViewById(R.id.menuIcon);
            TextView  menuItem = (TextView) view.findViewById(R.id.menuItem);
            DrawerItem currentItem = data.get(position);

            menuIcon.setImageResource(currentItem.getIcon());
            menuItem.setText(currentItem.getItem());
        } else {
            DrawerItem currentItem = this.data.get(position);

            ((ImageView) view.findViewById(R.id.menuIcon)).setImageResource(currentItem.getIcon());
            ((TextView) view.findViewById(R.id.menuItem)).setText(currentItem.getItem());
        }

        return view;
    }
}
