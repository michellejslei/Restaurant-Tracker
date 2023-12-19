package ui.pages;

import model.*;
import model.exceptions.LogException;
import model.exceptions.RatingOutOfRangeException;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.ConsolePrinter;
import ui.LogPrinter;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

// elements of code adapted from https://www.youtube.com/watch?v=Kmgo00avvEw&t=1355s
//A restaurant tracker app launch page that allows user to load a tracker or create a new one
public class LaunchPage extends Page implements ActionListener {

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/tracker.json";
    private RestaurantTracker tracker;

    private JPanel launchPagePanel;

    // EFFECTS: constructs a JFrame for the tracker app launch page
    public LaunchPage() throws FileNotFoundException {
        super("Restaurant Tracker Console");

        tracker = new RestaurantTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        initializeMainPanel();

        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel and adds it to the JFrame
    public void initializeMainPanel() {
        launchPagePanel = new JPanel();
        super.initializeMainPanel(launchPagePanel);
    }

    // EFFECTS: initializes all the components for the main panel
    @Override
    public void initializePanelComponents() {
        initializeWelcomeLabel();
        initializeInstructionsLabel();
        initializeLoadButton();
        initializeNewButton();
        initializeQuitButton();
    }

    // EFFECTS: constructs a welcome JLabel and adds it to the main panel
    private void initializeWelcomeLabel() {
        JLabel welcomeMsg = new JLabel("Welcome!");
        welcomeMsg.setBounds(238, 50, 124, 50);
        welcomeMsg.setFont(new Font("Georgia", Font.BOLD, 24));
        welcomeMsg.setForeground(Color.decode("#646361"));

        launchPagePanel.add(welcomeMsg);
    }

    // EFFECTS: constructs a JLabel with launch page instructions and adds it to
    //          the main panel
    private void initializeInstructionsLabel() {
        JLabel welcomeInstructions = new JLabel("Please select from the following options:");
        welcomeInstructions.setBounds(170, 100, 250, 50);
        welcomeInstructions.setFont(new Font("Georgia", Font.PLAIN, 14));
        welcomeInstructions.setForeground(Color.decode("#646361"));

        launchPagePanel.add(welcomeInstructions);
    }

    // EFFECTS: constructs a load tracker button and adds it to the main panel
    private void initializeLoadButton() {
        JButton loadButton = new JButton("load tracker");
        loadButton.setBounds(166, 200, 84, 20);
        loadButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        loadButton.setForeground(Color.decode("#646361"));
        loadButton.setOpaque(true);
        loadButton.setBackground(Color.decode("#B5A3A3"));
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);

        launchPagePanel.add(loadButton);
    }

    // EFFECTS: constructs a new tracker button and adds it to the main panel
    private void initializeNewButton() {
        JButton newButton = new JButton("new tracker");
        newButton.setBounds(350, 200, 84, 20);
        newButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        newButton.setForeground(Color.decode("#646361"));
        newButton.setOpaque(true);
        newButton.setBackground(Color.decode("#B5A3A3"));
        newButton.setActionCommand("load");
        newButton.addActionListener(this);

        launchPagePanel.add(newButton);
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

        launchPagePanel.add(quitButton);
    }

    // EFFECTS: creates error message dialog for when IOException is caught
    private void showIOExceptionDialog() {
        JOptionPane.showMessageDialog(this,
                "Unable to read from file: " + JSON_STORE,
                "File Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: creates error message dialog for when RatingOutOfRangeException is caught
    private void showRatingOutOfRangeExceptionDialog() {
        JOptionPane.showMessageDialog(this,
                "Error with information from file: " + JSON_STORE
                        + " Please create a new tracker.",
                "File Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: loads tracker from file
    private void loadRestaurantTracker() {
        try {
            tracker = jsonReader.read();
            new RestaurantListPage(tracker, jsonWriter);
            this.dispose();
        } catch (IOException e) {
            showIOExceptionDialog();
        } catch (RatingOutOfRangeException e) {
            showRatingOutOfRangeExceptionDialog();
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

    // EFFECTS: performs given action is load tracker or new tracker buttons are pressed
    //          clears event log
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("load")) {
            loadRestaurantTracker();
            EventLog.getInstance().clear();
        } else if (e.getActionCommand().equals("new")) {
            new RestaurantListPage(tracker, jsonWriter);
            EventLog.getInstance().clear();
            this.dispose();
        } else if (e.getActionCommand().equals("quit")) {
            printLogToConsole();
            System.exit(0);
        }
    }
}



