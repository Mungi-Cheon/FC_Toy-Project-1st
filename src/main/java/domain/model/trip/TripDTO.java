package domain.model.trip;

import domain.model.itinerary.Itinerary;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDTO {

    private int tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private List<Itinerary> itineraries;
}
