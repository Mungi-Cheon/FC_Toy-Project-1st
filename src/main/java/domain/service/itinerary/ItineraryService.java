package domain.service.itinerary;

import domain.model.itinerary.Itineraries;
import domain.model.itinerary.Itinerary;
import domain.model.itinerary.ItineraryDTO;
import java.util.Map;

public interface ItineraryService {

    Map<Integer, Itineraries> findAll();

    Integer createItinerary(Integer tripId, Itinerary itinerary);

    Itineraries findSelectedTripItineraries(Integer tripId);

    boolean deleteItinerary(Integer tripId, Integer itineraryId);

    Integer updateItinerary(Integer tripId, Itinerary oldItinerary, Itinerary newItinerary);

}
