package ui;

import model.Dish;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.RatingOutOfRangeException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static model.MealType.*;

//A restaurant tracker app with a console that allows to user to add, view, and edit their restaurants
public class TrackerApp {

    private static final String NEW_RESTAURANT_COMMAND = "new";
    private static final String VIEW_RESTAURANTS_COMMAND = "view";
    private static final String QUIT_COMMAND = "quit";
    private static final String BACK_COMMAND = "b";
    private static final String SAVE_COMMAND = "save";
    private static final String LOAD_COMMAND = "load";
    private static final String EDIT_CUISINE_COMMAND = "cuisine";
    private static final String EDIT_MEALTYPE_COMMAND = "mealtype";
    private static final String VIEW_DISHES_COMMAND = "dishes";
    private static final String EDIT_RATING_COMMAND = "rating";

    private boolean runProgram = true;

    private RestaurantTracker tracker;
    private Restaurant baanWasana;
    private Restaurant chickpea;
    private Restaurant baoguette;
    private Restaurant icyBar;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/tracker.json";

    // EFFECTS: runs the restaurant tracker application
    public TrackerApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        tracker = new RestaurantTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runTracker();
    }

    // EFFECTS: parses user input until user quits
    private void runTracker() {
        String command;

        initialize();

        System.out.println("\nWelcome to your Restaurant Tracker!"
                + "Please select from the following options:");
        displayMenu();

        while (runProgram) {
            if (input.hasNext()) {
                command = input.next();
                command = command.toLowerCase();
                parseInput(command);
            }
        }
        input.close();
    }

    // EFFECTS: displays users instructions to use tracker
    private void displayMenu() {
        System.out.println("\tEnter '" + NEW_RESTAURANT_COMMAND + "' to add a new restaurant.");
        System.out.println("\tEnter '" + VIEW_RESTAURANTS_COMMAND + "' to view saved restaurants.");
        System.out.println("\tEnter '" + SAVE_COMMAND + "' to save your changes.");
        System.out.println("\tEnter '" + LOAD_COMMAND + "' to load tracker from file.");
        System.out.println("\tEnter '" + QUIT_COMMAND + "' to quit at any time.");
    }

    // EFFECTS: prints menu options and info depends on user input
    private void parseInput(String command) {
        switch (command) {
            case NEW_RESTAURANT_COMMAND:
                makeNewRestaurantMenu();
                break;
            case VIEW_RESTAURANTS_COMMAND:
                viewSavedRestaurantsMenu();
                break;
            case SAVE_COMMAND:
                saveRestaurantTracker();
                break;
            case LOAD_COMMAND:
                loadRestaurantTracker();
                break;
            case QUIT_COMMAND:
                runProgram = false;
                break;
            default:
                System.out.println("Sorry, I didn't understand that command. Please try again.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new restaurant to restaurant tracker and display the new list of restaurants
    private void makeNewRestaurantMenu() {
        System.out.println("\nAdd a new restaurant!");
        System.out.println("Please enter a restaurant name: ");
        System.out.println("\nOr enter '" + BACK_COMMAND + "' to return to the main menu.");
        String name = input.next();

        if (name.equals(BACK_COMMAND)) {
            displayMenu();
        } else {
            Restaurant r = new Restaurant(name);
            tracker.addRestaurant(r);
            System.out.println(name + " has been saved to your restaurants: ");
            printRestaurantList();

            System.out.println("\nEnter '" + BACK_COMMAND + "' to return to the main menu.");

            String command = input.next();
            if (command.equals(BACK_COMMAND)) {
                displayMenu();
            } else {
                makeNewRestaurantMenu();
            }
        }
    }

    // EFFECTS: displays list of restaurant names saved in tracker and prompts user
    //          to select a restaurant from the list for more info
    private void viewSavedRestaurantsMenu() {
        List<Restaurant> restaurants = tracker.getRestaurants();

        System.out.println("To view or edit information, select a restaurant below: ");

        for (Restaurant r : restaurants) {
            System.out.println("\tEnter '" + restaurants.indexOf(r) + "' for " + r.getName());
        }
        System.out.println("\nOr enter '" + BACK_COMMAND + "' to return to the main menu.");

        String command = input.next();
        if (command.equals(BACK_COMMAND)) {
            displayMenu();
        } else {
            int value = Integer.parseInt(command);
            if (value < restaurants.size()) {
                editRestaurantInfo(restaurants.get(value));
            }
        }
    }

    // EFFECTS: displays list of restaurant info and menu of edit options and info
    //          depending on user input
    private void editRestaurantInfo(Restaurant r) {
        printRestaurantInfo(r);
        editRestaurantInfoMenu();

        String command = input.next();
        switch (command) {
            case EDIT_CUISINE_COMMAND:
                editRestaurantCuisine(r);
                break;
            case EDIT_MEALTYPE_COMMAND:
                editRestaurantMealType(r);
                break;
            case VIEW_DISHES_COMMAND:
                viewRestaurantDishes(r);
                break;
            case EDIT_RATING_COMMAND:
                editRestaurantRating(r);
                break;
            case BACK_COMMAND:
                viewSavedRestaurantsMenu();
                break;
            default:
                System.out.println("Sorry, I didn't understand that command. Please try again.");
                break;
        }
    }

    // EFFECTS: displays user instructions to edit restaurant info
    private void editRestaurantInfoMenu() {
        System.out.println("\nTo edit any of the information, please select an"
                + " option from below");
        System.out.println("\tEnter: '" + EDIT_CUISINE_COMMAND + "' to edit cuisine type");
        System.out.println("\tEnter: '" + EDIT_MEALTYPE_COMMAND + "' to edit meal type");
        System.out.println("\tEnter: '" + VIEW_DISHES_COMMAND + "' to view or add dishes ordered");
        System.out.println("\tEnter: '" + EDIT_RATING_COMMAND + "' to edit your rating");

        System.out.println("\nOr enter '" + BACK_COMMAND + "' to return to the previous menu.");
    }

    // EFFECTS: prints all info for the given restaurant to the screen
    private void printRestaurantInfo(Restaurant r) {
        System.out.println("\n" + r.getName());
        System.out.println("Cuisine: " + r.getCuisine());
        System.out.println("Meal type: " + r.getMealType());
        System.out.println("Dishes ordered: " + r.getDishNames());
        System.out.println("Average price: $" + r.getAvgPrice());
        System.out.println("My rating: " + r.getRating());
        System.out.println("Number of times visited: " + r.getTimesVisited());
    }

    // EFFECTS: prints list of saved restaurant names to the screen
    private void printRestaurantList() {
        List<String> restaurantNames = tracker.getRestaurantNames();

        for (String name : restaurantNames) {
            System.out.println(name);
        }
    }

    // REQUIRES: user input for cuisine is not an empty string
    // MODIFIES: this, r
    // EFFECTS: sets cuisine of restaurant r based on user input
    private void editRestaurantCuisine(Restaurant r) {
        System.out.println("\n" + r.getName() + "'s cuisine is currently set to: "
                + r.getCuisine() + "\nPlease enter the cuisine you would like to change to:");

        String cuisine = input.next();
        r.setCuisine(cuisine);

        System.out.println("\n" + r.getName() + "'s cuisine has been changed to: "
                + r.getCuisine());
        editRestaurantInfo(r);
    }

    // REQUIRES: user input for meal type is not an empty string
    // MODIFIES: this, r
    // EFFECTS: sets meal type of restaurant r based on user input
    private void editRestaurantMealType(Restaurant r) {
        editRestaurantMealTypeMenu(r);

        String mt = input.next();
        if (mt.equals("breakfast")) {
            r.setMealType(BREAKFAST);
        } else if (mt.equals("brunch")) {
            r.setMealType(BRUNCH);
        } else if (mt.equals("lunch")) {
            r.setMealType(LUNCH);
        } else if (mt.equals("dinner")) {
            r.setMealType(DINNER);
        } else if (mt.equals("dessert")) {
            r.setMealType(DESSERT);
        } else if (mt.equals(BACK_COMMAND)) {
            editRestaurantInfoMenu();
        } else {
            System.out.println("Sorry, I didn't understand that meal type. Please try again.");
        }
        System.out.println("\n" + r.getName() + "'s meal type has been changed to: " + r.getMealType());
        editRestaurantInfo(r);
    }

    // EFFECTS: displays menu of user instructions to edit restaurant meal type
    private void editRestaurantMealTypeMenu(Restaurant r) {
        System.out.println("\n" + r.getName() + "'s meal type is currently set to: "
                + r.getMealType() + "\nPlease follow the instructions to change the meal type:");
        System.out.println("\tEnter 'breakfast' to change meal type to breakfast");
        System.out.println("\tEnter 'brunch' to change meal type to brunch");
        System.out.println("\tEnter 'lunch' to change meal type to lunch");
        System.out.println("\tEnter 'dinner' to change meal type to dinner");
        System.out.println("\tEnter 'dessert' to change meal type to dessert");

        System.out.println("\nOr enter '" + BACK_COMMAND + "' to return to the previous menu.");
    }

    // REQUIRES: user input for new dish info is not an empty string
    // MODIFIES: this, r
    // EFFECTS: prints list of dishes ordered at restaurant r and allows user to add new dishes
    //          based on user input
    private void viewRestaurantDishes(Restaurant r) {
        printDishesList(r);

        System.out.println("\nTo a new dish, please enter the dish name: ");
        System.out.println("\nOr enter '" + BACK_COMMAND + "' to return to the previous menu.");
        String name = input.next();
        System.out.println("\nPlease enter " + name + "'s price: ");
        int price = Integer.parseInt(input.next());


        if (name.equals(BACK_COMMAND)) {
            editRestaurantInfoMenu();
        } else {
            Dish d = new Dish(name, price);
            r.addDish(d);
            System.out.println(name + " has been saved to your ordered dishes.");
            editRestaurantInfo(r);
        }
    }

    // REQUIRES: 1 <= user input for rating <= 5
    // MODIFIES: this, r
    // EFFECTS: sets rating of restaurant r based on user input
    private void editRestaurantRating(Restaurant r) {
        System.out.println("\nPlease enter a rating from 1-5 for " + r.getName());

        String rate = input.next();
        int rating = Integer.parseInt(rate);

        try {
            r.setRating(rating);
        } catch (RatingOutOfRangeException e) {
            System.out.println("\nPlease enter a valid rating from 1-5.");
        }

        System.out.println("\n" + r.getName() + "'s rating has been updated to: " + r.getRating());
        editRestaurantInfo(r);
    }

    // EFFECTS: prints list of ordered dishes from r to the screen
    private void printDishesList(Restaurant r) {
        List<Dish> dishes = r.getDishes();

        if (dishes.size() > 0) {
            for (Dish d : dishes) {
                System.out.println(d.getName());
            }
        } else {
            System.out.println("\nYou have not ordered any dishes from " + r.getName() + " yet.");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes restaurant tracker app
    private void initialize() {
        baanWasana = new Restaurant("Baan Wasana");
        chickpea = new Restaurant("Chickpea");
        baoguette = new Restaurant("Baoguette Vietnamese Bistro");
        icyBar = new Restaurant("Icy Bar");
        initializeRestaurants();

        tracker.addRestaurant(baanWasana);
        tracker.addRestaurant(chickpea);
        tracker.addRestaurant(baoguette);
        tracker.addRestaurant(icyBar);

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

//    // MODIFIES: this
//    // EFFECTS: initializes restaurants in restaurant tracker app
    private void initializeRestaurants() {
        baanWasana.setCuisine("Thai");
        baanWasana.setMealType(DINNER);
        baanWasana.addDish(new Dish("Thai Spring Rolls", 8));
        baanWasana.addDish(new Dish("Pad Thai", 18));

        chickpea.setCuisine("Mediterranean");
        chickpea.setMealType(LUNCH);
        chickpea.addDish(new Dish("Sabich Platter", 26));
        chickpea.addDish(new Dish("Falafel Pita", 17));

        baoguette.setCuisine("Vietnamese");
        baoguette.setMealType(LUNCH);
        baoguette.addDish(new Dish("Grilled Lemongrass Pork Baoguette", 9));
        baoguette.addDish(new Dish("Pho Dac Biet", 14));

        icyBar.setCuisine("Taiwanese");
        icyBar.setMealType(DESSERT);
        icyBar.addDish(new Dish("Mango Mochi Shaved Icy", 16));
    }

    // EFFECTS: saves the tracker to file
    private void saveRestaurantTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(tracker);
            jsonWriter.close();
            System.out.println("Saved tracker to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads tracker from file
    private void loadRestaurantTracker() {
        try {
            tracker = jsonReader.read();
            System.out.println("Loaded saved tracker from " + JSON_STORE);
            displayMenu();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (RatingOutOfRangeException e) {
            System.out.println("Error with information from file: " + JSON_STORE + " Please create a new tracker.");
        }
    }

}
