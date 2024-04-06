package domain.service.trip;

import domain.model.trip.Trip;
import domain.model.trip.Trips;
import domain.model.trip.TripDTO;

public interface TripService {

    Trips findAllTrips();

    Trip findTripById(Integer tripId);

    Integer createTrip(Trip trip);

    Integer updateTripById(Trip selectedTripObj, Trip updatedTripObj);

    boolean deleteTripById(Integer tripId);
}
