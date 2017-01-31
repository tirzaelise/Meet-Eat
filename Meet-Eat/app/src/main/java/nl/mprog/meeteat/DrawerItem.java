/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the DrawerItem object. This object consists of a title and an icon and is
 * used to add an image/icon next to the title of an item in the Navigation Drawer.
 */

package nl.mprog.meeteat;

class DrawerItem {
    private int icon;
    private String title;

    DrawerItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    int getIcon() {
        return icon;
    }

    String getTitle() {
        return title;
    }
}
