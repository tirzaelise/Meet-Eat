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

import java.util.ArrayList;

class Dinner {
    private String title;
    private String id;
    private String hostId;
    private String hostName;
    private String date;
    private ArrayList<String> guests;
    private String area;
    private String ingredients;
    private boolean vegetarian;
    private boolean vegan;

    Dinner(String title, String id, String hostId, String hostName, String date,
           ArrayList<String> guests, String area, String ingredients, boolean vegetarian,
           boolean vegan) {
        this.title = title;
        this.id = id;
        this.hostId = hostId;
        this.hostName = hostName;
        this.date = date;
        this.guests = guests;
        this.area = area;
        this.ingredients = ingredients;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
    }

    public Dinner() {
    }

    void setGuests(ArrayList<String> guests) {
        this.guests = guests;
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

    String getDate() {
        return date;
    }

    ArrayList<String> getGuests() {
        return guests;
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
