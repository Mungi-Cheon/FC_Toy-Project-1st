package domain.controller.trip;

import domain.model.trip.Trip;
import domain.model.trip.TripDTO;
import domain.model.trip.Trips;
import domain.service.trip.TripService;

public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    public Trips findAllTrips() {
        return tripService.findAllTrips();
    }

    public Trip findTripById(Integer tripId) {
        return tripService.findTripById(tripId);
    }

    public Integer createTrip(Trip trip) {
        return tripService.createTrip(trip);
    }

    public Integer updateTripById(Trip selectedTrip, Trip updatedTrip) {
        return tripService.updateTripById(selectedTrip, updatedTrip);
    }

    public boolean deleteByTripId(Integer tripId) {
        return tripService.deleteTripById(tripId);
    }
}
