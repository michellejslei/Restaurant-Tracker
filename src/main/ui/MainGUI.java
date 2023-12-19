package ui;

import ui.pages.LaunchPage;

import java.io.FileNotFoundException;

public class MainGUI {

    // Runs the TrackerApp (graphical console)
    public static void main(String[] args) {
        try {
            new LaunchPage();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
