package model;

import model.exceptions.RatingOutOfRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTrackerTest {
    private RestaurantTracker testRestaurantTracker;
    private Restaurant mcDonalds;
    private Restaurant cactusClub;

    @BeforeEach
    public void setUp() {
        testRestaurantTracker = new RestaurantTracker();
        mcDonalds = new Restaurant("McDonalds");
        cactusClub = new Restaurant("Cactus Club");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testRestaurantTracker.getRestaurants().size());
    }

    @Test
    public void testAddOneRestaurant() {
        testRestaurantTracker.addRestaurant(mcDonalds);
        assertEquals(1, testRestaurantTracker.getRestaurants().size());
        assertEquals("McDonalds", testRestaurantTracker.getRestaurantNames().get(0));
        assertEquals(1, mcDonalds.getTimesVisited());
    }

    @Test
    public void testAddSameRestaurantTwice() {
        testRestaurantTracker.addRestaurant(mcDonalds);
        testRestaurantTracker.addRestaurant(mcDonalds);
        assertEquals(1, testRestaurantTracker.getRestaurants().size());
        assertEquals("McDonalds", testRestaurantTracker.getRestaurantNames().get(0));
        assertEquals(2, mcDonalds.getTimesVisited());
    }

    @Test
    public void testAddMultipleRestaurants() {
        testRestaurantTracker.addRestaurant(mcDonalds);
        testRestaurantTracker.addRestaurant(cactusClub);
        assertEquals(2, testRestaurantTracker.getRestaurants().size());
        assertEquals("McDonalds", testRestaurantTracker.getRestaurantNames().get(0));
        assertEquals("Cactus Club", testRestaurantTracker.getRestaurantNames().get(1));
        assertEquals(1, mcDonalds.getTimesVisited());
        assertEquals(1, cactusClub.getTimesVisited());
    }

    @Test
    public void testRemoveRestaurant() {
        testRestaurantTracker.addRestaurant(mcDonalds);
        testRestaurantTracker.addRestaurant(cactusClub);
        assertEquals(2, testRestaurantTracker.getRestaurants().size());
        assertEquals("McDonalds", testRestaurantTracker.getRestaurantNames().get(0));
        assertEquals("Cactus Club", testRestaurantTracker.getRestaurantNames().get(1));
        testRestaurantTracker.removeRestaurant(mcDonalds);
        assertEquals(1, testRestaurantTracker.getRestaurants().size());
        assertFalse(testRestaurantTracker.getRestaurantNames().contains("McDonalds"));
    }

    @Test
    public void testRemoveRestaurantNoRestaurant() {
        testRestaurantTracker.addRestaurant(cactusClub);
        assertEquals(1, testRestaurantTracker.getRestaurants().size());
        assertFalse(testRestaurantTracker.getRestaurantNames().contains("McDonalds"));
        assertEquals("Cactus Club", testRestaurantTracker.getRestaurantNames().get(0));
        testRestaurantTracker.removeRestaurant(mcDonalds);
        assertEquals(1, testRestaurantTracker.getRestaurants().size());
        assertFalse(testRestaurantTracker.getRestaurantNames().contains("McDonalds"));
        assertEquals("Cactus Club", testRestaurantTracker.getRestaurantNames().get(0));
    }

    @Test
    public void testFilterByMealTypeDinner() {
        mcDonalds.setMealType(MealType.BREAKFAST);
        cactusClub.setMealType(MealType.DINNER);
        testRestaurantTracker.addRestaurant(mcDonalds);
        testRestaurantTracker.addRestaurant(cactusClub);
        List<Restaurant> filtered = testRestaurantTracker.filterByMealType(MealType.DINNER);
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(cactusClub));
        assertFalse(filtered.contains(mcDonalds));
    }

    @Test
    public void testFilterByMealTypeNoMatch() {
        mcDonalds.setMealType(MealType.BREAKFAST);
        cactusClub.setMealType(MealType.DINNER);
        testRestaurantTracker.addRestaurant(mcDonalds);
        testRestaurantTracker.addRestaurant(cactusClub);
        List<Restaurant> filtered = testRestaurantTracker.filterByMealType(MealType.DESSERT);
        assertEquals(0, filtered.size());
    }
}
