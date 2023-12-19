package persistence;

import model.Dish;
import model.MealType;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.RatingOutOfRangeException;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Code inspired by JsonSerializationDemo from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// represents a reader that reads restaurant tracker from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads restaurant tracker from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public RestaurantTracker read() throws IOException, RatingOutOfRangeException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRestaurantTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses restaurant tracker from JSON object and returns it
    private RestaurantTracker parseRestaurantTracker(JSONObject jsonObject) throws RatingOutOfRangeException {
        RestaurantTracker rt = new RestaurantTracker();
        addRestaurants(rt, jsonObject);
        return rt;
    }

    // MODIFIES: rt
    // EFFECTS: parses restaurants from JSON object and adds them to restaurant tracker
    private void addRestaurants(RestaurantTracker rt, JSONObject jsonObject) throws RatingOutOfRangeException {
        JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
        for (Object json : jsonArray) {
            JSONObject nextRestaurant = (JSONObject) json;
            addRestaurant(rt, nextRestaurant);
        }
    }

    // MODIFIES: rt
    // EFFECTS: parses restaurant from JSON object and adds it to the restaurant tracker
    private void addRestaurant(RestaurantTracker rt, JSONObject jsonObject) throws RatingOutOfRangeException {
        String name = jsonObject.getString("name");
        Restaurant r = new Restaurant(name);
        addDishes(r, jsonObject);
//        int avgPrice = jsonObject.getInt("average price");
        int rating = jsonObject.getInt("rating");
        int timesVisited = jsonObject.getInt("times visited");

        if (jsonObject.has("cuisine")) {
            String cuisine = jsonObject.getString("cuisine");
            r.setCuisine(cuisine);
        }

        if (jsonObject.has("meal type")) {
            MealType mealType = jsonObject.getEnum(MealType.class, "meal type");
            r.setMealType(mealType);
        }

        if (timesVisited > 1) {
            for (int i = 1; i < timesVisited; i++) {
                r.visitOnce();
            }
        }

        r.setAvgPrice();
        r.setRating(rating);

        rt.addRestaurant(r);
    }

    // MODIFIES: r
    // EFFECTS: parses dishes from JSON object and adds them to restaurant tracker
    private void addDishes(Restaurant r, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("dishes");
        for (Object json : jsonArray) {
            JSONObject nextDish = (JSONObject) json;
            addDish(r, nextDish);
        }
    }

    // MODIFIES: r
    // EFFECTS: parses dish from JSON object and adds it to the list of dishes ordered from r
    private void addDish(Restaurant r, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int price = jsonObject.getInt("price");
        Dish d = new Dish(name, price);
        boolean orderAgain = jsonObject.getBoolean("order again");

        if (orderAgain) {
            d.wouldOrderAgain();
        }

        r.addDish(d);
    }
}
