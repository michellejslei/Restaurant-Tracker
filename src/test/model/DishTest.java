package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {
    private Dish pasta;

    @BeforeEach
    public void setUp() {
        pasta = new Dish("Pasta", 20);
    }

    @Test
    public void testConstructor() {
        assertEquals("Pasta", pasta.getName());
        assertEquals(20, pasta.getPrice());
        assertFalse(pasta.willOrderAgain());
    }

    @Test
    public void testWouldOrderAgain() {
        pasta.wouldOrderAgain();
        assertTrue(pasta.willOrderAgain());
    }
}
