package domain.dao.trip;

import common.constants.FilePathConstants;
import common.util.JsonUtil;
import domain.model.trip.Trip;
import domain.model.trip.TripDTO;
import domain.model.trip.Trips;
import java.io.File;
import java.util.Optional;

public class TripDAOImpl implements TripDAO {

    private static final String JSON_FILE_PATH = "trips.json";

    @Override
    public Trips findAllTrips() {
        return JsonUtil.getInstance().findAllTrips();
    }

    @Override
    public Optional<Trip> findTripById(Integer tripId) {
        String theFilePath = String.format(FilePathConstants.FILE_PATH, tripId);
        return JsonUtil.getInstance().findTripById(tripId, theFilePath);
    }

    @Override
    public Integer createTrip(TripDTO tripDTO) {

        File dir = new File(FilePathConstants.DIR_RESOURCE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        JsonUtil jsonUtil = JsonUtil.getInstance();
        Integer tripId = jsonUtil.getLatestTripId();
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        return jsonUtil.createTrip(tripId, filePath, tripDTO);
    }

    @Override
    public Integer updateTripById(Integer tripId, TripDTO tripDTO) {
        JsonUtil jsonUtil = JsonUtil.getInstance();
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        return jsonUtil.createTrip(tripId, filePath, tripDTO);
    }

    @Override
    public boolean deleteTripById(Integer tripId) {
        return JsonUtil.getInstance().deleteTripById(tripId);
    }
}