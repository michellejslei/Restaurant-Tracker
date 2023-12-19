package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// A restaurant tracker that contains a list of restaurants, where more restaurants
// can be added to the list
public class RestaurantTracker implements Writable {
    private List<Restaurant> restaurants;

    // EFFECTS: constructs a restaurant tracker with a name and an
    //          empty list of restaurants
    public RestaurantTracker() {
        restaurants = new ArrayList<>();
    }

    // getter
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    // MODIFIES: this, r
    // EFFECTS: adds r to the restaurant tracker if it is not already in
    //          the list and add 1 to the number of times visited, otherwise
    //          increment the number of times the restaurant has been visited
    public void addRestaurant(Restaurant r) {
        String name = r.getName();
        List<String> restaurantNames = getRestaurantNames();

        if (!restaurantNames.contains(name)) {
            restaurants.add(r);
            r.visitOnce();
            EventLog.getInstance().logEvent(new Event("Added restaurant: " + name));
        } else {
            int index = restaurantNames.indexOf(name);
            Restaurant existingRestaurant = this.restaurants.get(index);
            existingRestaurant.visitOnce();
        }
    }

    // MODIFIES: this
    // EFFECTS: if restaurant r is contained in restaurants list, remove r
    //          from the list, otherwise do nothing
    public void removeRestaurant(Restaurant r) {
        String name = r.getName();

        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getName().equals(name)) {
                restaurants.remove(restaurants.get(i));
                EventLog.getInstance().logEvent(new Event("Removed restaurant: " + name));
            }
        }
    }

    // EFFECTS: returns a filtered list of saved restaurants of the given
    //          meal type
    public List<Restaurant> filterByMealType(MealType mt) {
        List<Restaurant> filtered = new ArrayList<>();

        for (Restaurant r : restaurants) {
            if (r.getMealType().equals(mt)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    // EFFECTS: returns a list of restaurant names in list
    public List<String> getRestaurantNames() {
        List<String> names = new ArrayList<>();
        List<Restaurant> restaurants = getRestaurants();

        for (Restaurant r : restaurants) {
            names.add(r.getName());
        }
        return names;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("restaurants", restaurantsToJson());

        return json;
    }

    // EFFECTS: returns restaurants in this restaurant tracker as a JSON array
    private JSONArray restaurantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Restaurant r : restaurants) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }
}
