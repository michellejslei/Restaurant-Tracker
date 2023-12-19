package persistence;

import model.Dish;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.RatingOutOfRangeException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.util.List;

import static model.MealType.DINNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Code inspired by JsonSerializationDemo from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            RestaurantTracker rt = new RestaurantTracker();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testWriterEmptyTracker() {
        try {
            RestaurantTracker rt = new RestaurantTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyTracker.json");
            writer.open();
            writer.write(rt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyTracker.json");
            rt = reader.read();
            assertEquals(0, rt.getRestaurants().size());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        } catch (RatingOutOfRangeException e) {
            fail("RatingOutOfRangeException should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralTracker() {
        try {
            RestaurantTracker rt = new RestaurantTracker();
            Restaurant baanWasana = new Restaurant("Baan Wasana");
            baanWasana.setCuisine("Thai");
            baanWasana.addDish(new Dish("Thai Spring Rolls", 8));
            baanWasana.addDish(new Dish("Pad Thai", 18));
            baanWasana.setMealType(DINNER);
            rt.addRestaurant(baanWasana);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralTracker.json");
            writer.open();
            writer.write(rt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralTracker.json");
            rt = reader.read();
            assertEquals(1, rt.getRestaurants().size());
            List<Dish> dishes = rt.getRestaurants().get(0).getDishes();
            checkRestaurant("Baan Wasana", "Thai", DINNER, dishes,
                    13, 5, 1, rt.getRestaurants().get(0));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        } catch (RatingOutOfRangeException e) {
            fail("RatingOutOfRangeException should not have been thrown");
        }
    }
}
