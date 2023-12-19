package persistence;

import model.Dish;
import model.MealType;
import model.Restaurant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Code inspired by JsonSerializationDemo from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkRestaurant(String name, String cuisine, MealType mealType,
                                   List<Dish> dishes, int avgPrice, int rating, int timesVisited,
                                   Restaurant r) {
        assertEquals(name, r.getName());
        assertEquals(cuisine, r.getCuisine());
        assertEquals(mealType, r.getMealType());
        assertEquals(dishes, r.getDishes());
        assertEquals(avgPrice, r.getAvgPrice());
        assertEquals(rating, r.getRating());
        assertEquals(timesVisited, r.getTimesVisited());
    }
}
