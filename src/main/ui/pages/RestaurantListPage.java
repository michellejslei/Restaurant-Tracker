package ui.pages;

import model.EventLog;
import model.MealType;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.LogException;
import persistence.JsonWriter;
import ui.ConsolePrinter;
import ui.LogPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.List;

// elements of code adapted from https://www.youtube.com/watch?v=Kmgo00avvEw&t=1355s
//A restaurant list page that allows to user to add, view, remove, and filter their restaurants
public class RestaurantListPage extends Page implements ActionListener, ItemListener {

    private JsonWriter jsonWriter;
    private static final String JSON_STORE = "./data/tracker.json";
    private RestaurantTracker tracker;

    private JPanel restaurantListPanel;
    private JScrollPane listScroller;
    private DefaultListModel<String> model;
    private JList<String> list;
    private JComboBox<String> mealTypeList;
    private List<Restaurant> restaurants;
    private Restaurant selectedRestaurant;

    // EFFECTS: constructs a JFrame for the tracker app restaurant list page
    public RestaurantListPage(RestaurantTracker tracker, JsonWriter jsonWriter) {
        super("My Restaurant Tracker");

        this.tracker = tracker;
        this.jsonWriter = jsonWriter;

        initializeMainPanel();

        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel and adds it to the JFrame
    public void initializeMainPanel() {
        restaurantListPanel = new JPanel();
        super.initializeMainPanel(restaurantListPanel);
    }

    // EFFECTS: initializes all the components for the main panel
    @Override
    public void initializePanelComponents() {
        initializeTitleMsg();
        initializeNewRestaurantButton();
        initializeFilterInstructions();
        initializeMealTypeFilter();
        initializeInstructionsMsg();
        initializeRestaurantList();
        initializeViewInfoButton();
        initializeRemoveButton();
        initializeSaveButton();
        initializeQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: constructs a title JLabel and adds it to the main panel
    private void initializeTitleMsg() {
        JLabel titleMsg = new JLabel("My Restaurants!");
        titleMsg.setBounds(70, 40, 250, 50);
        titleMsg.setFont(new Font("Georgia", Font.BOLD, 24));
        titleMsg.setForeground(Color.decode("#646361"));

        restaurantListPanel.add(titleMsg);
    }

    // MODIFIES: this
    // EFFECTS: constructs a new restaurant button and adds it to the main panel
    private void initializeNewRestaurantButton() {
        JButton newRestaurant = new JButton("add new restaurant");
        newRestaurant.setBounds(70, 95, 120, 20);
        newRestaurant.setFont(new Font("Georgia", Font.PLAIN, 12));
        newRestaurant.setForeground(Color.decode("#646361"));
        newRestaurant.setActionCommand("new");
        newRestaurant.addActionListener(this);

        restaurantListPanel.add(newRestaurant);
    }

    // MODIIFES: this
    // EFFECTS: constructs a JLabel with filter instructions and add it to the main panel
    private void initializeFilterInstructions() {
        JLabel filterInstructions = new JLabel("select an option to filter by type of meal");
        filterInstructions.setBounds(385, 75, 350, 15);
        filterInstructions.setFont(new Font("Georgia", Font.ITALIC, 10));
        filterInstructions.setForeground(Color.decode("#646361"));

        restaurantListPanel.add(filterInstructions);
    }

    // MODIFIES: this
    // EFFECTS: constructs a meal type filter and adds it to the main panel
    private void initializeMealTypeFilter() {
        String[] mealTypeStrings = {" ", "dinner", "lunch", "dessert", "breakfast", "brunch"};
        mealTypeList = new JComboBox<>(mealTypeStrings);
        mealTypeList.setBounds(385, 95, 150, 24);
        mealTypeList.setFont(new Font("Georgia", Font.PLAIN, 12));
        mealTypeList.setForeground(Color.decode("#646361"));
        mealTypeList.addItemListener(this);

        restaurantListPanel.add(mealTypeList);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JLabel for instructions and adds it to the main panel
    private void initializeInstructionsMsg() {
        JLabel restaurantInstructions = new JLabel("> select a restaurant below for more info");
        restaurantInstructions.setBounds(65, 125, 350, 20);
        restaurantInstructions.setFont(new Font("Georgia", Font.ITALIC, 12));
        restaurantInstructions.setForeground(Color.decode("#646361"));

        if (tracker.getRestaurants().size() == 0) {
            restaurantInstructions.setVisible(false);
        }

        restaurantListPanel.add(restaurantInstructions);
    }

    // MODIFIES: this
    // EFFECTS: constructs a list scroller for the list of restaurants saved in the
    //          tracker and adds it to the main panel
    private void initializeRestaurantList() {
        restaurants = tracker.getRestaurants();
        model = new DefaultListModel<>();

        for (Restaurant r : restaurants) {
            model.addElement(r.getName());
        }

        list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.setVisibleRowCount(5);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setBackground(Color.decode("#EAE5E4"));
        list.setFont(new Font("Georgia", Font.PLAIN, 13));
        list.setForeground(Color.decode("#646361"));
        list.setSelectionBackground(Color.decode("#938887"));

        list.addListSelectionListener(e -> selectedRestaurant = getRestaurantByName(list.getSelectedValue()));

        listScroller = new JScrollPane(list);
        listScroller.setBackground(Color.decode("#EAE5E4"));
        listScroller.setBounds(65, 155, 300, 120);
        listScroller.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        restaurantListPanel.add(listScroller);
    }

    // EFFECTS: returns the restaurant with the given name if found is restaurant is saved
    //          in the tracker, else return null
    private Restaurant getRestaurantByName(String name) {
        for (Restaurant r : restaurants) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: constructs a view info button and adds it to the main panel
    private void initializeViewInfoButton() {
        JButton viewButton = new JButton("view");
        viewButton.setBounds(65, 280, 50, 20);
        viewButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        viewButton.setForeground(Color.decode("#646361"));
        viewButton.setActionCommand("view");
        viewButton.addActionListener(this);

        restaurantListPanel.add(viewButton);
    }

    // MODIFIES: this
    // EFFECTS: constructs a remove restaurant button and adds it to the main panel
    private void initializeRemoveButton() {
        JButton removeButton = new JButton("remove");
        removeButton.setBounds(135, 280, 70, 20);
        removeButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        removeButton.setForeground(Color.decode("#646361"));
        removeButton.setActionCommand("remove");
        removeButton.addActionListener(this);

        restaurantListPanel.add(removeButton);
    }

    // MODIFIES: this
    // EFFECTS: constructs a save button and adds it to the main panel
    private void initializeSaveButton() {
        JButton saveButton = new JButton("save");
        saveButton.setBounds(35, 330, 50, 20);
        saveButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        saveButton.setForeground(Color.decode("#646361"));
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);

        restaurantListPanel.add(saveButton);
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

        restaurantListPanel.add(quitButton);
    }

    // code adapted from https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    // EFFECTS: constructs an input dialog that prompts the user to enter a restaurant name
    //          and adds that as a new restaurant to the tracker; lets user know if
    //          restaurant has already been added
    private void showNewRestaurantDialog() {
        JLabel msg = new JLabel("Please enter the restaurant name:");
        msg.setFont(new Font("Georgia", Font.PLAIN, 12));
        msg.setForeground(Color.decode("#646361"));

        JLabel name = new JLabel();
        name.setFont(new Font("Georgia", Font.PLAIN, 12));
        name.setForeground(Color.decode("#646361"));
        name.setText(JOptionPane.showInputDialog(this, msg, "New Restaurant",
                JOptionPane.PLAIN_MESSAGE));

        if (tracker.getRestaurantNames().contains(name.getText())) {
            showDuplicateRestaurantDialog();
        }

        tracker.addRestaurant(new Restaurant(name.getText()));
    }

    // EFFECTS: constructs a plain message dialog that notifies the user that their restaurant
    //          has already been added
    private void showDuplicateRestaurantDialog() {
        JLabel duplicate = new JLabel("This restaurant has already been added!");
        duplicate.setFont(new Font("Georgia", Font.PLAIN, 12));
        duplicate.setForeground(Color.decode("#646361"));
        JOptionPane.showMessageDialog(this, duplicate,
                "Duplicate restaurant", JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: creates error message dialog for when showFileNotFoundExceptionDialog is caught
    private void showFileNotFoundExceptionDialog() {
        JOptionPane.showMessageDialog(this,
                "Unable to write to file: " + JSON_STORE,
                "File Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: saves the tracker to file
    private void saveRestaurantTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(tracker);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            showFileNotFoundExceptionDialog();
        }
    }

    // EFFECTS: refreshes the list of restaurants stored in the model
    private void refreshModelList(List<Restaurant> restaurants) {
        model.clear();
        for (Restaurant r : restaurants) {
            model.addElement(r.getName());
        }
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
        if (e.getActionCommand().equals("save")) {
            saveRestaurantTracker();
        } else if (e.getActionCommand().equals("new")) {
            showNewRestaurantDialog();
            refreshModelList(restaurants);
        } else if (e.getActionCommand().equals("quit")) {
            printLogToConsole();
            System.exit(0);
        } else if (e.getActionCommand().equals("view")) {
            new RestaurantInfoPage(tracker, selectedRestaurant, jsonWriter);
            this.dispose();
        } else if (e.getActionCommand().equals("remove")) {
            Restaurant selected = selectedRestaurant;
            model.removeElement(selectedRestaurant.getName());
            tracker.removeRestaurant(selected);
        }
    }

    // EFFECTS: refreshes list of restaurants based on meal type filter
    @Override
    public void itemStateChanged(ItemEvent e) {
        String s = (String) mealTypeList.getSelectedItem();
        if (s.equals("dinner")) {
            List<Restaurant> filtered = tracker.filterByMealType(MealType.DINNER);
            refreshModelList(filtered);
        } else if (s.equals("lunch")) {
            List<Restaurant> filtered = tracker.filterByMealType(MealType.LUNCH);
            refreshModelList(filtered);
        } else if (s.equals("breakfast")) {
            List<Restaurant> filtered = tracker.filterByMealType(MealType.BREAKFAST);
            refreshModelList(filtered);
        } else if (s.equals("dessert")) {
            List<Restaurant> filtered = tracker.filterByMealType(MealType.DESSERT);
            refreshModelList(filtered);
        } else if (s.equals("brunch")) {
            List<Restaurant> filtered = tracker.filterByMealType(MealType.BRUNCH);
            refreshModelList(filtered);
        } else {
            refreshModelList(restaurants);
        }
    }
}
