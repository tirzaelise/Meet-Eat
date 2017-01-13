package nl.mprog.meeteat;

/**
 * Created by tirza on 10-1-17.
 */

class Dinner {
    private String food;
    private String host;
    private String startTime;
    private int amountOfPeople;
    private String cuisine;
    private String area;
    private String ingredients;

    Dinner(String food, String host, String startTime, int amountOfPeople, String cuisine, String
            area, String ingredients) {
        this.food = food;
        this.host = host;
        this.startTime = startTime;
        this.amountOfPeople = amountOfPeople;
        this.cuisine = cuisine;
        this.area = area;
        this.ingredients = ingredients;
    }

    public Dinner() {
    }
//
//    public void setFood(String food) {
//        this.food = food;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//
//    public void setAmountOfPeople(int amountOfPeople) {
//        this.amountOfPeople = amountOfPeople;
//    }
//
//    public void setCuisine(String cuisine) {
//        this.cuisine = cuisine;
//    }
//
//    public void setArea(String area) {
//        this.area = area;
//    }
//
//    public void setIngredients(String ingredients) {
//        this.ingredients = ingredients;
//    }

    String getFood() {
        return food;
    }

    String getHost() {
        return host;
    }

    String getStartTime() {
        return startTime;
    }

    int getAmountOfPeople() {
        return amountOfPeople;
    }

    String getCuisine() {
        return cuisine;
    }

    String getArea() {
        return area;
    }

    String getIngredients() {
        return ingredients;
    }
}
