package domain.dao.trip;

import domain.model.trip.Trip;
import domain.model.trip.TripDTO;
import domain.model.trip.Trips;
import java.util.Optional;

public interface TripDAO {

    Trips findAllTrips();

    Optional<Trip> findTripById(Integer tripId);

    Integer createTrip(TripDTO tripDTO);

    Integer updateTripById(Integer tripId, TripDTO tripDTO);

    boolean deleteTripById(Integer tripId);
}

