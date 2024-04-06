package domain.controller.trip;

import static org.junit.jupiter.api.Assertions.*;

import common.constants.FilePathConstants;
import common.util.JsonUtil;
import domain.dao.trip.TripDAOImpl;
import domain.model.itinerary.ItineraryDTO;
import domain.model.trip.TripDTO;
import domain.service.trip.TripService;
import domain.service.trip.TripServiceImpl;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripControllerTest {
    TripDTO dto;
    List<ItineraryDTO> itineraryList;
    @BeforeEach
    public void setUp() {
        dto = new TripDTO();
        dto.setTripId(2);
        dto.setTripName("서울 여행");
        dto.setStartDate("2024-04-02");
        dto.setEndDate("2024-04-03");
        int tripId = JsonUtil.getInstance().createTrip(2, String.format(FilePathConstants.FILE_PATH, 2), dto);
    }

    @AfterEach
    void complete(){
        String str = "resource/json/travel_%d.json";
        String filePath = String.format(str, dto.getTripId());
        File testFile = new File(filePath);
        if(testFile.exists()){
            assertTrue(testFile.delete());
        }
    }

    @Test
    void deleteByTripId() {
        TripService tripService = new TripServiceImpl(new TripDAOImpl());
        boolean result = tripService.deleteTripById(3);
        assertTrue(result);
    }
}