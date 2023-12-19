package persistence;

import org.json.JSONObject;

// Writable provides a framework for reader and writer of Json objects
public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
