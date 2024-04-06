package domain.model.trip;

import domain.model.itinerary.Itinerary;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Trips {

    List<Trip> trips;

    public Trips() {
        trips = new ArrayList<>();
    }

    public Trips(List<Trip> trips) {
        this.trips = trips;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public Optional<Trip> getTrip(Integer tripId) {
        for (Trip t : trips) {
            if (t.getTripId().equals(tripId)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public Trip [] toArray(){
        return trips.toArray(new Trip[0]);
    }
    public Integer size() {
        return trips.size();
    }

    public boolean isEmpty() {
        return trips.isEmpty();
    }
}
