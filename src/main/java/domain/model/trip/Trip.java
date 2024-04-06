package domain.model.trip;

import domain.model.itinerary.Itinerary;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Trip {

    private Integer tripId;
    private final String tripName;
    private final String startDate;
    private final String endDate;
    private final List<Itinerary> itineraries;

    public Trip(String tripName, String tripStartDate, String tripEndDate) {
        this.tripName = tripName;
        this.startDate = tripStartDate;
        this.endDate = tripEndDate;
        this.itineraries = new ArrayList<>();
    }

    public Trip(Integer tripId, String tripName, String startDate, String endDate,
        List<Itinerary> itineraries) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itineraries = itineraries;
    }
}