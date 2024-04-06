package domain.model.itinerary;

import lombok.Getter;

@Getter
public class Itinerary {

    private final Integer itineraryId;
    private final String departurePlace;
    private final String destination;
    private final String departureTime;
    private final String arrivalTime;
    private final String checkIn;
    private final String checkOut;

    public Itinerary(Integer itineraryId, String departurePlace, String destination,
        String departureTime, String arrivalTime, String checkIn, String checkOut) {
        this.itineraryId = itineraryId;
        this.departurePlace = departurePlace;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
