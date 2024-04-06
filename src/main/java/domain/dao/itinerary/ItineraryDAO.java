package domain.dao.itinerary;

import domain.model.itinerary.Itineraries;
import domain.model.itinerary.ItineraryDTO;
import java.util.Optional;
import java.util.Map;

public interface ItineraryDAO {

    Optional<Map<Integer, Itineraries>> findAll();

    Integer createItinerary(Integer tripId, ItineraryDTO itineraryDTO);

    Itineraries findSelectedTripItineraries(Integer tripId);

    boolean deleteItinerary(Integer tripId, Integer itineraryId);

    Integer updateItineraryById(Integer tripId, ItineraryDTO itineraryDTO);

}
