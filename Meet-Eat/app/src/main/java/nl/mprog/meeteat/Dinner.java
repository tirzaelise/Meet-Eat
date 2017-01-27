/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the Dinner object, which consists of a title, an ID (retrieved from the
 * Spoonacular API), a host, a date, an amount of free spaces, an an area where it'll be held, a
 * string of ingredients, a boolean that says whether it's vegan and another boolean that says
 * whether it's vegetarian.
 */

package nl.mprog.meeteat;

import java.io.Serializable;
import java.util.ArrayList;

class Dinner implements Serializable {
    private String title;
    private String id;
    private String hostId;
    private String hostName;
    private String hostEmail;
    private String date;
    private ArrayList<String> guestIds;
    private ArrayList<String> guestNames;
    private String area;
    private String ingredients;
    private boolean vegetarian;
    private boolean vegan;

    Dinner(String title, String id, String hostId, String hostName, String hostEmail, String date,
           ArrayList<String> guestIds, ArrayList<String> guestNames, String area,
           String ingredients, boolean vegetarian, boolean vegan) {
        this.title = title;
        this.id = id;
        this.hostId = hostId;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.date = date;
        this.guestIds = guestIds;
        this.guestNames = guestNames;
        this.area = area;
        this.ingredients = ingredients;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
    }

    public Dinner() {
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setDate(String date) {
        this.date = date;
    }

    void setGuestIds(ArrayList<String> guestIds) {
        this.guestIds = guestIds;
    }

    void setGuestNames(ArrayList<String> guestNames) {
        this.guestNames = guestNames;
    }

    void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    String getTitle() {
        return title;
    }

    String getId() {
        return id;
    }

    String getHostId() {
        return hostId;
    }

    String getHostName() {
        return hostName;
    }

    String getHostEmail() {
        return hostEmail;
    }

    String getDate() {
        return date;
    }

    ArrayList<String> getGuestIds() {
        return guestIds;
    }

    ArrayList<String> getGuestNames() {
        return guestNames;
    }

    String getArea() {
        return area;
    }

    String getIngredients() {
        return ingredients;
    }

    boolean isVegetarian() {
        return vegetarian;
    }

    boolean isVegan() {
        return vegan;
    }
}
