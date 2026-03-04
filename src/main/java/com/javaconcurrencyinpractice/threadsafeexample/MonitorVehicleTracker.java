package com.javaconcurrencyinpractice.threadsafeexample;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// This is thread safe
public class MonitorVehicleTracker {
    private Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        synchronized (this) {
            this.locations = deepCopy(locations);
        }
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        var loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        var location = locations.get(id);
        if (location != null) {
            location.x = x;
            location.y = y;
        } else {
            var map = new HashMap<String, MutablePoint>();
            for (String key : locations.keySet()) {
                map.put(key, new MutablePoint(locations.get(key)));
            }

            map.put(id, new MutablePoint(x, y));

            this.locations = Collections.unmodifiableMap(map);
        }
    }


    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        var map = new HashMap<String, MutablePoint>();
        for (String key : locations.keySet()) {
            map.put(key, new MutablePoint(locations.get(key)));
        }

        return Collections.unmodifiableMap(map);
    }
}
