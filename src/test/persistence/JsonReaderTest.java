package persistence;

import model.Dish;
import model.RestaurantTracker;
import model.exceptions.RatingOutOfRangeException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static model.MealType.DESSERT;
import static model.MealType.DINNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Code inspired by JsonSerializationDemo from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            RestaurantTracker rt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        } catch (RatingOutOfRangeException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyTracker.json");
        try {
            RestaurantTracker rt = reader.read();
            assertEquals(0, rt.getRestaurants().size());
        } catch (Exception e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralTracker() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralTracker.json");
        try {
            RestaurantTracker rt = reader.read();
            assertEquals(2, rt.getRestaurants().size());
            List<Dish> dishes1 = rt.getRestaurants().get(0).getDishes();
            List<Dish> dishes2 = rt.getRestaurants().get(1).getDishes();
            checkRestaurant("Icy Bar", "Taiwanese", DESSERT, dishes1,
                    16, 4, 1, rt.getRestaurants().get(0));
            checkRestaurant("Danbo Ramen", "", DINNER, dishes2,
                    13, 5, 3, rt.getRestaurants().get(1));
        } catch (Exception e) {
            fail("Couldn't read from file");
        }
    }
}
