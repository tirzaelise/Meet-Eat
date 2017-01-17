package nl.mprog.meeteat;

/**
 * Created by tirza on 10-1-17.
 */

class Dinner {
    private String title;
    private String id;
    private String host;
    private String startTime;
    private int freeSpaces;
    private String area;
    private String ingredients;
    private String imageUrl;
    private boolean vegetarian;
    private boolean vegan;

    Dinner(String title, String id, String host, String startTime, int freeSpaces, String
            area, String ingredients, String imageUrl, boolean vegetarian, boolean vegan) {
        this.title = title;
        this.id = id;
        this.host = host;
        this.startTime = startTime;
        this.freeSpaces = freeSpaces;
        this.area = area;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
    }

    public Dinner() {
    }

    void setFreeSpaces(int freeSpaces) {
        this.freeSpaces = freeSpaces;
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

    String getHost() {
        return host;
    }

    String getStartTime() {
        return startTime;
    }

    int getFreeSpaces() {
        return freeSpaces;
    }

    String getArea() {
        return area;
    }

    String getIngredients() {
        return ingredients;
    }

    String getImageUrl() {
        return imageUrl;
    }

    boolean isVegetarian() {
        return vegetarian;
    }

    boolean isVegan() {
        return vegan;
    }
}
