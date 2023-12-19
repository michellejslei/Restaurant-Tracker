package ui;

import model.EventLog;
import model.exceptions.LogException;

// Code inspired by https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
// Defines behaviours that event log printers must support
public interface LogPrinter {

    // EFFECTS: prints the event log el, throws LogException when
    //          printing fails for any reason
    void printLog(EventLog el) throws LogException;
}
