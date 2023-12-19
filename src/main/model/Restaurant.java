package model;

import model.exceptions.RatingOutOfRangeException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// A restaurant that has a name, cuisine, meal type, a list of dishes ordered,
// an average price, a user rating, and a tally of the number of times visited.
// The list of dishes ordered can be added to.
public class Restaurant implements Writable {
    private final String name;
    private String cuisine;
    private MealType mealType;
    private List<Dish> dishes;
    private int avgPrice;
    private int rating;
    private int timesVisited;

    // EFFECTS: constructs a restaurant with a name, cuisine type, a dinner meal type,
    //          an empty list of dishes ordered, an average price of 0 ($),
    //          a 5 (star) rating, and 0 number of times visited
    public Restaurant(String name) {
        this.name = name;
        this.cuisine = "";
        this.mealType = MealType.DINNER;
        dishes = new ArrayList<>();
        avgPrice = 0;
        rating = 5;
        timesVisited = 0;
    }

    // getters
    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public MealType getMealType() {
        return mealType;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public int getAvgPrice() {
        setAvgPrice();
        return avgPrice;
    }

    public int getRating() {
        return rating;
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    // MODIFIES: this
    // EFFECTS: sets cuisine type to c
    public void setCuisine(String c) {
        cuisine = c;
        EventLog.getInstance().logEvent(new Event("Set " + name + "'s cuisine to: " + c));
    }

    // MODIFIES: this
    // EFFECTS: sets meal type to mt
    public void setMealType(MealType mt) {
        mealType = mt;
    }

    // REQUIRES: 1 <= rating <= 5
    // MODIFIES: this
    // EFFECTS: sets restaurant rating
    //          if rating is < 1 or > 4, throws RatingOutOfRangeException
    public void setRating(int rating) throws RatingOutOfRangeException {
        if (rating < 1 || rating > 5) {
            throw new RatingOutOfRangeException();
        }
        this.rating = rating;
        EventLog.getInstance().logEvent(
                new Event("Set " + name + "'s rating to: " + rating));
    }

    // EFFECTS: returns list of dish names in dishes
    public List<String> getDishNames() {
        List<String> names = new ArrayList<>();

        for (Dish d : dishes) {
            names.add(d.getName());
        }
        return names;
    }

    // MODIFIES: this
    // EFFECTS: adds one to the number of times visited
    public void visitOnce() {
        timesVisited++;
    }

    // MODIFIES: this
    // EFFECTS: if d is not already in list of dishes, add d to list and
    //          return true; otherwise do not add and return false
    //          list of dishes maintains dishes in the order they were added
    public boolean addDish(Dish d) {
        if (!dishes.contains(d)) {
            dishes.add(d);
            EventLog.getInstance().logEvent(
                    new Event("Added dish: " + d.getName() + " to " + name));
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates the average price ($) rounded to the nearest dollar,
    //          of the dishes ordered from the restaurant, or returns 0 if
    //          the list of dishes is empty
    public void setAvgPrice() {
        int totalPrice = 0;
        if (dishes.isEmpty()) {
            this.avgPrice = 0;
        } else {
            for (Dish d : dishes) {
                totalPrice += d.getPrice();
            }
            this.avgPrice = totalPrice / dishes.size();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("cuisine", cuisine);
        json.put("meal type", mealType);
        json.put("dishes", dishesToJson());
        json.put("average price", avgPrice);
        json.put("rating", rating);
        json.put("times visited", timesVisited);
        return json;
    }

    // EFFECTS: returns dishes in this restaurant as a JSON array
    private JSONArray dishesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Dish d : dishes) {
            jsonArray.put(d.toJson());
        }

        return jsonArray;
    }
}
