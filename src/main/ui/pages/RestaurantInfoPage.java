package ui.pages;

import model.EventLog;
import model.MealType;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.LogException;
import model.exceptions.RatingOutOfRangeException;
import persistence.JsonWriter;
import ui.ConsolePrinter;
import ui.LogPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// elements of code adapted from https://www.youtube.com/watch?v=Kmgo00avvEw&t=1355s
//A restaurant info page that allows to user to view and edit the given restaurant
public class RestaurantInfoPage extends Page implements ActionListener, ItemListener {

    private JsonWriter jsonWriter;

    private RestaurantTracker tracker;
    private Restaurant restaurant;

    private JComboBox<String> mealTypeList;
    private int mealTypeIndex;
    private JComboBox<String> ratingList;
    private int ratingIndex;
    private JPanel restaurantInfoPanel;
    private JLabel restaurantName;
    private static final ImageIcon ONE_STAR = new ImageIcon("./data/images/oneStar.png");
    private static final ImageIcon TWO_STAR = new ImageIcon("./data/images/twoStar.png");
    private static final ImageIcon THREE_STAR = new ImageIcon("./data/images/threeStar.png");
    private static final ImageIcon FOUR_STAR = new ImageIcon("./data/images/fourStar.png");
    private static final ImageIcon FIVE_STAR = new ImageIcon("./data/images/fiveStar.png");
    private ImageIcon starRating;

    // EFFECTS: constructs a JFrame for the tracker app restaurant info page
    public RestaurantInfoPage(RestaurantTracker tracker, Restaurant restaurant, JsonWriter writer) {
        super(restaurant.getName());

        this.tracker = tracker;
        this.restaurant = restaurant;
        this.jsonWriter = writer;

        initializeMainPanel();

        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel and adds it to the JFrame
    private void initializeMainPanel() {
        restaurantInfoPanel = new JPanel();
        super.initializeMainPanel(restaurantInfoPanel);
    }

    // EFFECTS: initializes all the components for the main panel
    @Override
    public void initializePanelComponents() {
        initializeRestaurantHeader();
        initializeRatingInfo();
        initializeCuisineInfo();
        initializeMealTypeInfo();
        initializePriceInfo();
        initializeDishesInfo();
        initializeBackButton();
        initializeQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant header JLabel and adds it to the main panel
    private void initializeRestaurantHeader() {
        restaurantName = new JLabel(restaurant.getName());

        restaurantName.setIcon(resizeStarRating());
        restaurantName.setHorizontalTextPosition(JLabel.LEFT);
        restaurantName.setBounds(75, 40, 525, 50);
        restaurantName.setFont(new Font("Georgia", Font.BOLD, 24));
        restaurantName.setForeground(Color.decode("#646361"));

        restaurantInfoPanel.add(restaurantName);
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant rating JComboBox and adds it to the main panel
    private void initializeRatingInfo() {
        JLabel rating = new JLabel("Rating: ");
        rating.setBounds(75, 110, 55, 20);
        rating.setFont(new Font("Georgia", Font.PLAIN, 14));
        rating.setForeground(Color.decode("#646361"));

        setRatingIndex(restaurant.getRating());

        String[] ratingStrings = {"1", "2", "3", "4", "5"};
        ratingList = new JComboBox<>(ratingStrings);
        ratingList.setSelectedIndex(getRatingIndex());
        ratingList.setBounds(125, 110, 75, 25);
        ratingList.setFont(new Font("Georgia", Font.PLAIN, 14));
        ratingList.setForeground(Color.decode("#646361"));
        ratingList.addItemListener(this);

        restaurantInfoPanel.add(rating);
        restaurantInfoPanel.add(ratingList);
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant cuisine JTextField and adds it to the main panel
    private void initializeCuisineInfo() {
        JLabel cuisine = new JLabel("Cuisine: ");
        cuisine.setBounds(75, 150, 60, 20);
        cuisine.setFont(new Font("Georgia", Font.PLAIN, 14));
        cuisine.setForeground(Color.decode("#646361"));

        JTextField cuisineText = new JTextField(restaurant.getCuisine());
        cuisineText.setBounds(135, 149, 100, 24);
        cuisineText.setFont(new Font("Georgia", Font.PLAIN, 14));
        cuisineText.setForeground(Color.decode("#646361"));
        cuisineText.setBackground(Color.decode("#EAE5E4"));
        cuisineText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        cuisineText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JTextField textField = (JTextField) e.getSource();
                    String text = textField.getText();
                    restaurant.setCuisine(text);
                }
            }
        });

        restaurantInfoPanel.add(cuisine);
        restaurantInfoPanel.add(cuisineText);
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant meal type JComboBox and adds it to the main panel
    private void initializeMealTypeInfo() {
        JLabel mealType = new JLabel("Meal: ");
        mealType.setBounds(75, 190, 40, 20);
        mealType.setFont(new Font("Georgia", Font.PLAIN, 14));
        mealType.setForeground(Color.decode("#646361"));

        setMealTypeIndex(restaurant.getMealType());

        String[] mealTypeStrings = {"dinner", "lunch", "dessert", "breakfast", "brunch", ""};
        mealTypeList = new JComboBox<>(mealTypeStrings);
        mealTypeList.setSelectedIndex(getMealTypeIndex());
        mealTypeList.setBounds(115, 190, 150, 25);
        mealTypeList.setFont(new Font("Georgia", Font.PLAIN, 14));
        mealTypeList.setForeground(Color.decode("#646361"));
        mealTypeList.addItemListener(this);

        restaurantInfoPanel.add(mealType);
        restaurantInfoPanel.add(mealTypeList);
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant price JLabel and adds it to the main panel
    private void initializePriceInfo() {
        JLabel price = new JLabel("Avg Price: $" + restaurant.getAvgPrice());
        price.setBounds(75, 230, 350, 20);
        price.setFont(new Font("Georgia", Font.PLAIN, 14));
        price.setForeground(Color.decode("#646361"));

        restaurantInfoPanel.add(price);
    }

    // MODIFIES: this
    // EFFECTS: constructs a restaurant dishes button and adds it to the main panel
    private void initializeDishesInfo() {
        JLabel dishes = new JLabel("Dishes ordered: ");
        dishes.setBounds(75, 270, 105, 20);
        dishes.setFont(new Font("Georgia", Font.PLAIN, 14));
        dishes.setForeground(Color.decode("#646361"));

        JButton viewDishes = new JButton("view");
        viewDishes.setBounds(180, 270, 65, 25);
        viewDishes.setFont(new Font("Georgia", Font.PLAIN, 14));
        viewDishes.setForeground(Color.decode("#646361"));
        viewDishes.setActionCommand("view");
        viewDishes.addActionListener(this);

        restaurantInfoPanel.add(dishes);
        restaurantInfoPanel.add(viewDishes);
    }

    // MODIFIES: this
    // EFFECTS: constructs a back button and adds it to the main panel
    private void initializeBackButton() {
        JButton backButton = new JButton("back");
        backButton.setBounds(35, 330, 50, 20);
        backButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        backButton.setForeground(Color.decode("#646361"));
        backButton.setActionCommand("back");
        backButton.addActionListener(this);

        restaurantInfoPanel.add(backButton);
    }

    // MODIFIES: this
    // EFFECTS: constructs a quit button and adds it to the main panel
    private void initializeQuitButton() {
        JButton quitButton = new JButton("quit");
        quitButton.setBounds(515, 330, 50, 20);
        quitButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        quitButton.setForeground(Color.decode("#646361"));
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(this);

        restaurantInfoPanel.add(quitButton);
    }

    // EFFECTS: gets the ratingIndex
    private int getRatingIndex() {
        return ratingIndex;
    }

    // EFFECTS: sets the rating of the given rating
    private void setRatingIndex(int rating) {
        if (rating == 1) {
            ratingIndex = 0;
        } else if (rating == 2) {
            ratingIndex = 1;
        } else if (rating == 3) {
            ratingIndex = 2;
        } else if (rating == 4) {
            ratingIndex = 3;
        } else {
            ratingIndex = 4;
        }
    }

    // EFFECTS: gets the ratings out of 5 stars
    private ImageIcon getStarRating() {
        if (restaurant.getRating() == 1) {
            return ONE_STAR;
        } else if (restaurant.getRating() == 2) {
            return TWO_STAR;
        } else if (restaurant.getRating() == 3) {
            return THREE_STAR;
        } else if (restaurant.getRating() == 4) {
            return FOUR_STAR;
        } else {
            return FIVE_STAR;
        }
    }

    // EFFECTS: resizes the star rating icons to fit next to restaurant header
    private ImageIcon resizeStarRating() {
        starRating = getStarRating();
        Image ratingImage = starRating.getImage();
        Image resizedRating = ratingImage.getScaledInstance(256 / 2, 60 / 2,  java.awt.Image.SCALE_SMOOTH);
        starRating = new ImageIcon(resizedRating);
        return starRating;
    }

    // EFFECTS: prints event log to console
    public void printLogToConsole() {
        LogPrinter lp = new ConsolePrinter();
        try {
            lp.printLog(EventLog.getInstance());
        } catch (LogException e) {
            System.out.println("System error. Could not print event log.");
        }
    }

    // EFFECTS: performs and action based on the action command
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("back")) {
            new RestaurantListPage(tracker, jsonWriter);
            this.dispose();
        } else if (e.getActionCommand().equals("quit")) {
            printLogToConsole();
            System.exit(0);
        } else if (e.getActionCommand().equals("view")) {
            new DishListPage(tracker, restaurant, jsonWriter);
            this.dispose();
        }
    }

    // EFFECTS: sets the meal type index based on the given meal type
    private void setMealTypeIndex(MealType mt) {
        if (mt.equals(MealType.DINNER)) {
            mealTypeIndex = 0;
        } else if (mt.equals(MealType.LUNCH)) {
            mealTypeIndex = 1;
        } else if (mt.equals(MealType.DESSERT)) {
            mealTypeIndex = 2;
        } else if (mt.equals(MealType.BREAKFAST)) {
            mealTypeIndex = 3;
        } else if (mt.equals(MealType.BRUNCH)) {
            mealTypeIndex = 4;
        } else {
            mealTypeIndex = 5;
        }
    }

    // EFFECTS: gets the mealTypeIndex
    private int getMealTypeIndex() {
        return mealTypeIndex;
    }

    // EFFECTS: sets meal type based or sets rating based on the given item state change
    @Override
    public void itemStateChanged(ItemEvent e) {
        String s = (String) mealTypeList.getSelectedItem();
        if (s.equals("dinner")) {
            restaurant.setMealType(MealType.DINNER);
        } else if (s.equals("lunch")) {
            restaurant.setMealType(MealType.LUNCH);
        } else if (s.equals("breakfast")) {
            restaurant.setMealType(MealType.BREAKFAST);
        } else if (s.equals("dessert")) {
            restaurant.setMealType(MealType.DESSERT);
        } else if (s.equals("brunch")) {
            restaurant.setMealType(MealType.BRUNCH);
        }

        String r = (String) ratingList.getSelectedItem();
        try {
            restaurant.setRating(Integer.parseInt(r));
            restaurantName.setIcon(resizeStarRating());
        } catch (RatingOutOfRangeException ex) {
            System.out.println("Rating must be between 1-5");
        }
    }
}
