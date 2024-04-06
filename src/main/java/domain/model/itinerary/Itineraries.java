package domain.model.itinerary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Itineraries {

    List<Itinerary> itineraries;

    public Itineraries() {
        itineraries = new ArrayList<>();
    }

    public Itineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public void addAll(List<Itinerary> list) {
        for (Itinerary itinerary : list) {
            addItinerary(itinerary);
        }
    }

    public void addItinerary(Itinerary itinerary) {
        itineraries.add(itinerary);
    }

    public Optional<Itinerary> getItinerary(Integer itineraryId) {
        for (Itinerary t : itineraries) {
            if (t.getItineraryId().equals(itineraryId)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public Itinerary[] toArray(){
        return itineraries.toArray(new Itinerary[0]);
    }

    public int size() {
        return itineraries.size();
    }

    public boolean isEmpty() {
        return itineraries.isEmpty();
    }
}
