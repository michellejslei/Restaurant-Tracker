package ui.pages;

import model.Dish;
import model.EventLog;
import model.Restaurant;
import model.RestaurantTracker;
import model.exceptions.LogException;
import persistence.JsonWriter;
import ui.ConsolePrinter;
import ui.LogPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// elements of code adapted from https://www.youtube.com/watch?v=Kmgo00avvEw&t=1355s
//A page that allows to user to add and view the list of dishes ordered from a restaurant
public class DishListPage extends Page implements ActionListener {

    private RestaurantTracker tracker;
    private JsonWriter jsonWriter;
    private Restaurant restaurant;
    private JPanel dishListPanel;
    private DefaultListModel<String> model;
    private List<Dish> dishes;
    private JList<String> list;

    private int selectedIndex;

    // EFFECTS: constructs a JFrame for the tracker app dish list page
    public DishListPage(RestaurantTracker tracker, Restaurant r, JsonWriter writer) {
        super(r.getName() + " - Dishes Ordered");

        this.tracker = tracker;
        this.jsonWriter = writer;
        this.restaurant = r;

        initializeMainPanel();

        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel and adds it to the JFrame
    private void initializeMainPanel() {
        dishListPanel = new JPanel();
        super.initializeMainPanel(dishListPanel);
    }

    // EFFECTS: initializes all the components for the main panel
    @Override
    public void initializePanelComponents() {
        initializeDishHeader();
        initializeDishInstructions();
        initializeDishesList();
        initializeAddDishButton();
        initializeBackButton();
        initializeQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: constructs a dish header JLabel and adds it to the main panel
    private void initializeDishHeader() {
        JLabel dishListTitle = new JLabel("Dishes ordered:");
        dishListTitle.setHorizontalAlignment(JLabel.LEFT);
        dishListTitle.setVerticalTextPosition(JLabel.CENTER);
        dishListTitle.setBounds(75, 40, 400, 50);
        dishListTitle.setFont(new Font("Georgia", Font.BOLD, 24));
        dishListTitle.setForeground(Color.decode("#646361"));

        dishListPanel.add(dishListTitle);
    }

    // MODIFIES: this
    // EFFECTS: constructs a list scroller for the list of dishes saved in the
    //          tracker for the restaurant and adds it to the main panel
    private void initializeDishInstructions() {
        JLabel dishInstructions = new JLabel("You have not ordered any dishes. "
                + "Please add a new dish below.");
        dishInstructions.setBounds(65, 105, 500, 20);
        dishInstructions.setFont(new Font("Georgia", Font.ITALIC, 12));
        dishInstructions.setForeground(Color.decode("#646361"));

        if (restaurant.getDishes().size() > 0) {
            dishInstructions.setVisible(false);
        }

        dishListPanel.add(dishInstructions);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JScrollPane for the list of dishes ordered from the restaurant
    //          and adds it to the main panel
    private void initializeDishesList() {
        dishes = restaurant.getDishes();
        model = new DefaultListModel<>();

        for (Dish d : dishes) {
            model.addElement(d.getName() + ": $" + d.getPrice());
        }

        initializeList(model);

        list.addListSelectionListener(e -> selectedIndex = list.getSelectedIndex());

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setBackground(Color.decode("#EAE5E4"));
        listScroller.setBounds(65, 110, 300, 120);
        listScroller.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        dishListPanel.add(listScroller);
    }

    // EFFECTS: constructs a JList to display the list of dishes
    private void initializeList(DefaultListModel<String> m) {
        list = new JList<>(m);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.setVisibleRowCount(5);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setBackground(Color.decode("#EAE5E4"));
        list.setFont(new Font("Georgia", Font.PLAIN, 13));
        list.setForeground(Color.decode("#646361"));
        list.setSelectionBackground(Color.decode("#938887"));
    }

    // MODIFIES: this
    // EFFECTS: constructs a add dish button and adds it to the main panel
    private void initializeAddDishButton() {
        JButton addDish = new JButton("add a new dish");
        addDish.setBounds(65, 235, 100, 20);
        addDish.setFont(new Font("Georgia", Font.PLAIN, 12));
        addDish.setForeground(Color.decode("#646361"));
        addDish.setActionCommand("add");
        addDish.addActionListener(this);

        dishListPanel.add(addDish);
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

        dishListPanel.add(backButton);
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

        dishListPanel.add(quitButton);
    }

    // EFFECTS: constructs an input dialog that prompts the user to enter a dish name
    //          and dish price and adds that to the list of dishes ordered from the
    //          restaurant
    public void showNewDishDialog() {
        JLabel nameMsg = new JLabel("Please enter the dish name:");
        nameMsg.setFont(new Font("Georgia", Font.PLAIN, 12));
        nameMsg.setForeground(Color.decode("#646361"));

        JLabel name = new JLabel();
        name.setFont(new Font("Georgia", Font.PLAIN, 12));
        name.setForeground(Color.decode("#646361"));
        name.setText(JOptionPane.showInputDialog(this, nameMsg, "New Dish",
                JOptionPane.PLAIN_MESSAGE));

        JLabel priceMsg = new JLabel("Please enter " + name.getText() + "'s price");
        priceMsg.setFont(new Font("Georgia", Font.PLAIN, 12));
        priceMsg.setForeground(Color.decode("#646361"));

        JLabel price = new JLabel();
        price.setFont(new Font("Georgia", Font.PLAIN, 12));
        price.setForeground(Color.decode("#646361"));
        price.setText(JOptionPane.showInputDialog(this, priceMsg, "New Dish",
                JOptionPane.PLAIN_MESSAGE));

        restaurant.addDish(new Dish(name.getText(), Integer.parseInt(price.getText())));
    }

    // EFFECTS: refreshes the list of dishes stored in the model
    private void refreshModelList(List<Dish> dishes) {
        model.clear();
        for (Dish d : dishes) {
            model.addElement(d.getName() + ": $" + d.getPrice());
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
        if (e.getActionCommand().equals("back")) {
            new RestaurantInfoPage(tracker, restaurant, jsonWriter);
            this.dispose();
        } else if (e.getActionCommand().equals("quit")) {
            printLogToConsole();
            System.exit(0);
        } else if (e.getActionCommand().equals("add")) {
            showNewDishDialog();
            refreshModelList(dishes);
        }
    }
}
