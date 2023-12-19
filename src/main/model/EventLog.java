package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Code inspired by https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
// Represents a log of restaurant tracker events
// We use the Singleton Design Pattern to ensure that there is only one EventLog
// in the system and that the system has global access to the single
// instance of the EventLog
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;
    private Collection<Event> events;

    private EventLog() {
        events = new ArrayList<Event>();
    }

    // EFFECTS: returns instance of EventLog - creates it if
    //          it doesn't already exist
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    // EFFECTS: adds event e to the event log
    public void logEvent(Event e) {
        events.add(e);
    }

    // EFFECTS: clears the event log and logs the event
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
