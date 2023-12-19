package model;

import org.json.JSONObject;
import persistence.Writable;

// A dish that is ordered from a restaurant with a name, price,
// and whether it would be ordered again
public class Dish implements Writable {
    private final String name;
    private int price;
    private boolean orderAgain;

    // EFFECTS: constructs a dish ordered with a name, price ($)
    //          rounded to the nearest dollar, and whether the
    //          dish would be ordered again to false
    public Dish(String name, int price) {
        this.name = name;
        this.price = price;
        this.orderAgain = false;
    }

    // getters
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean willOrderAgain() {
        return orderAgain;
    }

    // MODIFIES: this
    // EFFECTS: changes whether this dish would be ordered again to "true"
    public void wouldOrderAgain() {
        orderAgain = true;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);
        json.put("order again", orderAgain);
        return json;
    }
}
