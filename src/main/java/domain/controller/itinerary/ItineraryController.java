package domain.controller.itinerary;

import domain.model.itinerary.Itineraries;
import domain.model.itinerary.Itinerary;
import domain.model.itinerary.ItineraryDTO;
import domain.service.itinerary.ItineraryService;
import java.util.Map;

public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    public Map<Integer, Itineraries> findAllItinerary() {
        return itineraryService.findAll();
    }

    public Integer createItinerary(Integer tripId, Itinerary itinerary) {
        return itineraryService.createItinerary(tripId, itinerary);
    }

    public Itineraries findSelectedTripItineraries(Integer tripId) {
        return itineraryService.findSelectedTripItineraries(tripId);
    }

    public boolean deleteItinerary(Integer tripId, Integer itineraryId) {
        return itineraryService.deleteItinerary(tripId, itineraryId);
    }

    public Integer updateItinerary(Integer tripId, Itinerary oldItinerary, Itinerary newItinerary) {
        return itineraryService.updateItinerary(tripId, oldItinerary, newItinerary);
    }
}
