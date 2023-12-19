package ui;

import java.io.FileNotFoundException;

// Runs the TrackerApp
public class Main {
    public static void main(String[] args) {
        try {
            new TrackerApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
