/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the adapter for the Navigation Drawer. It consists of a TextView, which is
 * the title of a fragment in the app, and an image, which corresponds to the title of the TextView.
 */


package nl.mprog.meeteat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class DrawerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<DrawerItem> data;

    DrawerAdapter(Context context, ArrayList<DrawerItem> data) {
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
        }

        DrawerItem currentItem = this.data.get(position);

        ((ImageView) view.findViewById(R.id.menuIcon)).setImageResource(currentItem.getIcon());
        ((TextView) view.findViewById(R.id.menuItem)).setText(currentItem.getTitle());

        return view;
    }
}
