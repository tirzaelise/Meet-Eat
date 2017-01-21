package nl.mprog.meeteat;

/**
 * Created by tirza on 21-1-17.
 */

class DrawerItem {
    private int icon;
    private String item;

    DrawerItem(int icon, String item) {
        this.icon = icon;
        this.item = item;
    }

    int getIcon() {
        return icon;
    }

    String getItem() {
        return item;
    }
}
