package model;

import model.exceptions.RatingOutOfRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.MealType.DINNER;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    private Restaurant mcDonalds;
    private Dish burger;
    private Dish fries;
    private Dish iceCream;

    @BeforeEach
    public void setUp() {
        mcDonalds = new Restaurant("McDonalds");
        burger = new Dish("Burger", 8);
        fries = new Dish("Fries", 3);
        iceCream = new Dish("Ice Cream", 4);
    }

    @Test
    public void testConstructor() {
        assertEquals("McDonalds", mcDonalds.getName());
        assertEquals("", mcDonalds.getCuisine());
        assertEquals(DINNER, mcDonalds.getMealType());
        assertEquals(0, mcDonalds.getDishes().size());
        assertEquals(0, mcDonalds.getAvgPrice());
        assertEquals(5, mcDonalds.getRating());
        assertEquals(0, mcDonalds.getTimesVisited());
    }

    @Test
    public void testSetCuisine() {
        mcDonalds.setCuisine("American");
        assertEquals("American", mcDonalds.getCuisine());
    }

    @Test
    public void testSetMealType() {
        mcDonalds.setMealType(DINNER);
        assertEquals(DINNER, mcDonalds.getMealType());
    }

    @Test
    public void testSetRatingToOne() {
        try {
            mcDonalds.setRating(1);
        } catch (RatingOutOfRangeException e) {
            fail("RatingOutOfRangeException not expected");
        }
        assertEquals(1, mcDonalds.getRating());
    }

    @Test
    public void testSetRatingToThree() {
        try {
            mcDonalds.setRating(3);
        } catch (RatingOutOfRangeException e) {
            fail("RatingOutOfRangeException not expected");
        }
        assertEquals(3, mcDonalds.getRating());
    }

    @Test
    public void testSetRatingToFive() {
        try {
            mcDonalds.setRating(5);
        } catch (RatingOutOfRangeException e) {
            fail("RatingOutOfRangeException not expected");
        }
        assertEquals(5, mcDonalds.getRating());
    }

    @Test
    public void testSetRatingOutOfRangeZero() {
        try {
            mcDonalds.setRating(1);
            mcDonalds.setRating(0);
            fail("RatingOutOfRangeException was thrown");
        } catch (RatingOutOfRangeException e) {
            //Expected
        }
        assertEquals(1, mcDonalds.getRating());
    }

    @Test
    public void testSetRatingOutOfRangeSix() {
        try {
            mcDonalds.setRating(1);
            mcDonalds.setRating(6);
            fail("RatingOutOfRangeException was thrown");
        } catch (RatingOutOfRangeException e) {
            //Expected
        }
        assertEquals(1, mcDonalds.getRating());
    }

    @Test
    public void testVisitOnce() {
        mcDonalds.visitOnce();
        assertEquals(1, mcDonalds.getTimesVisited());
    }

    @Test
    public void testAddOneDish() {
        assertTrue(mcDonalds.addDish(burger));
        assertEquals(1, mcDonalds.getDishes().size());
        assertEquals(burger, mcDonalds.getDishes().get(0));
        assertEquals("Burger", mcDonalds.getDishNames().get(0));
    }

    @Test
    public void testAddSameDishTwice() {
        assertTrue(mcDonalds.addDish(burger));
        assertFalse(mcDonalds.addDish(burger));
        assertEquals(1, mcDonalds.getDishes().size());
        assertEquals(burger, mcDonalds.getDishes().get(0));
        assertEquals("Burger", mcDonalds.getDishNames().get(0));
    }

    @Test
    public void testAddMultipleDishes() {
        assertTrue(mcDonalds.addDish(burger));
        assertTrue(mcDonalds.addDish(fries));
        assertEquals(2, mcDonalds.getDishes().size());
        assertEquals(burger, mcDonalds.getDishes().get(0));
        assertEquals(fries, mcDonalds.getDishes().get(1));
        assertEquals("Burger", mcDonalds.getDishNames().get(0));
        assertEquals("Fries", mcDonalds.getDishNames().get(1));
    }

    @Test
    public void testGetAvgPriceNoDishes() {
        assertEquals(0, mcDonalds.getDishes().size());
        assertEquals(0, mcDonalds.getAvgPrice());
    }

    @Test
    public void testGetAvgPriceOneDish() {
        mcDonalds.addDish(burger);
        assertEquals(8, mcDonalds.getAvgPrice());
        assertEquals(1, mcDonalds.getDishes().size());
    }

    @Test
    public void testGetAvPriceMultipleDishes() {
        mcDonalds.addDish(burger);
        mcDonalds.addDish(fries);
        mcDonalds.addDish(iceCream);
        assertEquals(5, mcDonalds.getAvgPrice());
        assertEquals(3, mcDonalds.getDishes().size());

    }
}