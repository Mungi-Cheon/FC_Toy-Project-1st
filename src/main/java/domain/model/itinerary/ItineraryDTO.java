package domain.model.itinerary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItineraryDTO {

    private Integer itineraryId;
    private String departurePlace;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String checkIn;
    private String checkOut;
}
