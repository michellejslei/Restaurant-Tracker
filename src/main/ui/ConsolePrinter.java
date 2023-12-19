package ui;

import model.Event;
import model.EventLog;
import model.exceptions.LogException;

// Code inspired by https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
// a printer that prints event log events to the console
public class ConsolePrinter implements LogPrinter {

    // EFFECTS: constructs a new console printer
    public ConsolePrinter() {
    }

    // EFFECTS: prints a log of events to the console
    //          throws LogException if there is any error in printing the log
    @Override
    public void printLog(EventLog el) throws LogException {
        for (Event next : el) {
            System.out.println("\n" + next.toString());
        }
    }
}
