package domain.dao.itinerary;

import common.constants.FilePathConstants;
import common.exception.TripFileNotFoundException;
import common.util.JsonUtil;
import domain.model.itinerary.Itineraries;
import domain.model.itinerary.Itinerary;
import domain.model.itinerary.ItineraryDTO;
import domain.model.trip.Trip;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ItineraryDAOImpl implements ItineraryDAO {

    private final JsonUtil jsonUtil = JsonUtil.getInstance();

    @Override
    public Optional<Map<Integer, Itineraries>> findAll() {
        Map<Integer, Itineraries> tripMap = new HashMap<>();
        File dir = new File(FilePathConstants.DIR_RESOURCE_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files == null) {
                throw new TripFileNotFoundException("json 파일이 존재하지 않습니다.");
            }
            List<File> jsonList = Arrays.stream(files)
                .filter(f -> f.isFile() && f.getName().endsWith(FilePathConstants.EXTENSION_JSON))
                .toList();
            for (int i = 0; i < jsonList.size(); i++) {
                List<Itinerary> itineraryList = jsonUtil.findAllItineraries(jsonList.get(i));
                itineraryList.sort(Comparator.comparingInt(Itinerary::getItineraryId));
                Itineraries itineraries = new Itineraries();
                itineraries.addAll(itineraryList);
                tripMap.put((i + 1), itineraries);
            }
            return Optional.of(tripMap);
        }
        return Optional.empty();
    }

    @Override
    public Integer createItinerary(Integer tripId, ItineraryDTO itineraryDTO) {
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        Optional<Trip> tripFound = jsonUtil.findTripById(tripId, filePath);
        int itineraryId = 1;
        if (tripFound.isPresent()) {
            itineraryId = tripFound.get().getItineraries().size() + 1;
        }
        return jsonUtil.createItinerary(itineraryId, filePath, itineraryDTO);
    }

    @Override
    public Itineraries findSelectedTripItineraries(Integer tripId) {
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new TripFileNotFoundException("해당 여행의 여정 정보를 찾을 수 없습니다.");
        }
        JsonUtil jsonUtil = JsonUtil.getInstance();
        return new Itineraries(jsonUtil.findAllItineraries(file));
    }

    @Override
    public boolean deleteItinerary(Integer tripId, Integer itineraryId) {
        return jsonUtil.deleteItineraryById(tripId, itineraryId);
    }

    @Override
    public Integer updateItineraryById(Integer tripId, ItineraryDTO itineraryDTO) {
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        return JsonUtil.getInstance()
            .updateItinerary(itineraryDTO.getItineraryId(), filePath, itineraryDTO);
    }
}
