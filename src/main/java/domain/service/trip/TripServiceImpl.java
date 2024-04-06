package domain.service.trip;

import common.exception.TripIdNotFoundException;
import common.exception.ObjectEmptyException;
import common.exception.TripFileNotFoundException;
import domain.dao.trip.TripDAO;
import domain.model.trip.Trip;
import domain.model.trip.Trips;
import domain.model.trip.TripDTO;

import java.util.Optional;

public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;

    public TripServiceImpl(TripDAO tripDAO) {
        this.tripDAO = tripDAO;
    }

    @Override
    public Trips findAllTrips() {
        Trips trips = tripDAO.findAllTrips();
        if (trips.isEmpty()) {
            throw new ObjectEmptyException("기록된 여행 정보가 없습니다. 메인 화면으로 돌아갑니다.");
        }
        return trips;
    }

    @Override
    public Trip findTripById(Integer tripId) {
        Optional<Trip> tripOptional = tripDAO.findTripById(tripId);
        Trip trip = tripOptional.orElseThrow((
        ) -> new TripIdNotFoundException("해당 ID에 대한 여행 정보가 없습니다. 다시 입력하시겠습니까? (y/n)"));
        return trip;
    }

    @Override
    public Integer createTrip(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setTripName(trip.getTripName());
        tripDTO.setStartDate(trip.getStartDate());
        tripDTO.setEndDate(trip.getEndDate());
        tripDTO.setItineraries(trip.getItineraries());
        return tripDAO.createTrip(tripDTO);
    }

    @Override
    public Integer updateTripById(Trip selectedTrip, Trip updatedTrip) {
        TripDTO dto = new TripDTO();
        Integer tripId = selectedTrip.getTripId();

        dto.setTripId(tripId);
        dto.setItineraries(selectedTrip.getItineraries());

        if (selectedTrip.getTripName().equals(updatedTrip.getTripName())) {
            dto.setTripName(selectedTrip.getTripName());
        } else {
            dto.setTripName(updatedTrip.getTripName());
        }

        if (selectedTrip.getStartDate().equals(updatedTrip.getStartDate())) {
            dto.setStartDate(selectedTrip.getStartDate());
        } else {
            dto.setStartDate(updatedTrip.getStartDate());
        }

        if (selectedTrip.getEndDate().equals(updatedTrip.getEndDate())) {
            dto.setEndDate(selectedTrip.getEndDate());
        } else {
            dto.setEndDate(updatedTrip.getEndDate());
        }
        return tripDAO.updateTripById(tripId, dto);
    }

    @Override
    public boolean deleteTripById(Integer tripId) {
        try {
            findAllTrips();
        } catch (ObjectEmptyException e) {
            throw new TripFileNotFoundException("기록된 여행 정보가 없습니다. 메인 화면으로 돌아가겠습니다.");
        }
        return tripDAO.deleteTripById(findTripById(tripId).getTripId());
    }
}