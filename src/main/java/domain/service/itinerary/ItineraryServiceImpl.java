package domain.service.itinerary;

import common.exception.ItineraryNoSuchException;
import domain.dao.itinerary.ItineraryDAO;
import domain.model.itinerary.Itineraries;
import domain.model.itinerary.Itinerary;
import domain.model.itinerary.ItineraryDTO;
import java.util.Map;
import java.util.Optional;

public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryDAO itineraryDAO;

    public ItineraryServiceImpl(ItineraryDAO itineraryDAO) {
        this.itineraryDAO = itineraryDAO;
    }

    @Override
    public Map<Integer, Itineraries> findAll() {
        Optional<Map<Integer, Itineraries>> tripMapOptional = itineraryDAO.findAll();
        if (tripMapOptional.isEmpty()) {
            throw new ItineraryNoSuchException("여정 정보가 없습니다.");
        }
        return tripMapOptional.get();
    }

    @Override
    public Integer createItinerary(Integer tripId, Itinerary itinerary) {
        ItineraryDTO itineraryDTO = new ItineraryDTO();
        itineraryDTO.setDeparturePlace(itinerary.getDeparturePlace());
        itineraryDTO.setDestination(itinerary.getDestination());
        itineraryDTO.setDepartureTime(itinerary.getDepartureTime());
        itineraryDTO.setArrivalTime(itinerary.getArrivalTime());
        itineraryDTO.setCheckIn(itinerary.getCheckIn());
        itineraryDTO.setCheckOut(itinerary.getCheckOut());
        return itineraryDAO.createItinerary(tripId, itineraryDTO);
    }

    @Override
    public Itineraries findSelectedTripItineraries(Integer tripId) {
        return itineraryDAO.findSelectedTripItineraries(tripId);
    }

    @Override
    public boolean deleteItinerary(Integer tripId, Integer itineraryId) {
        return itineraryDAO.deleteItinerary(tripId, itineraryId);
    }

    @Override
    public Integer updateItinerary(Integer tripId, Itinerary oldItinerary, Itinerary newItinerary) {
        ItineraryDTO dto = new ItineraryDTO();
        dto.setItineraryId(oldItinerary.getItineraryId());

        if (oldItinerary.getDeparturePlace().equals(newItinerary.getDeparturePlace())) {
            dto.setDeparturePlace(oldItinerary.getDeparturePlace());
        } else {
            dto.setDeparturePlace(newItinerary.getDeparturePlace());
        }

        if (oldItinerary.getDestination().equals(newItinerary.getDestination())) {
            dto.setDestination(oldItinerary.getDestination());
        } else {
            dto.setDestination(newItinerary.getDestination());
        }

        if (oldItinerary.getDepartureTime().equals(newItinerary.getDepartureTime())) {
            dto.setDepartureTime(oldItinerary.getDepartureTime());
        } else {
            dto.setDepartureTime(newItinerary.getDepartureTime());
        }

        if (oldItinerary.getArrivalTime().equals(newItinerary.getArrivalTime())) {
            dto.setArrivalTime(oldItinerary.getArrivalTime());
        } else {
            dto.setArrivalTime(newItinerary.getArrivalTime());
        }

        if (oldItinerary.getCheckIn().equals(newItinerary.getCheckIn())) {
            dto.setCheckIn(oldItinerary.getCheckIn());
        } else {
            dto.setCheckIn(newItinerary.getCheckIn());
        }

        if (oldItinerary.getCheckOut().equals(newItinerary.getCheckOut())) {
            dto.setCheckIn(oldItinerary.getCheckIn());
        } else {
            dto.setCheckOut(newItinerary.getCheckOut());
        }
        return itineraryDAO.updateItineraryById(tripId, dto);

    }
}
