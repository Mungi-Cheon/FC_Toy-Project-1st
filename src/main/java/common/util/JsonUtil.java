package common.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.constants.FilePathConstants;
import common.exception.IllegalFormatException;
import common.exception.TripFileNotFoundException;
import common.exception.TripIdNotFoundException;
import domain.model.itinerary.Itinerary;
import domain.model.itinerary.ItineraryDTO;
import domain.model.trip.Trip;
import domain.model.trip.TripDTO;
import domain.model.trip.Trips;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JsonUtil {

    private static JsonUtil instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private JsonUtil() {
    }

    public static JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public int createTrip(Integer tripId, String filePath, TripDTO dto) {
        try (FileWriter fw = new FileWriter(filePath)) {
            Trip trip = new Trip(
                tripId,
                dto.getTripName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getItineraries()
            );

            gson.toJson(trip, fw);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return tripId;
    }

    public Integer createItinerary(Integer itineraryId, String filePath, ItineraryDTO dto) {
        try (FileReader reader = new FileReader(filePath)) {
            Trip trip = gson.fromJson(reader, Trip.class);
            Itinerary newItinerary = new Itinerary(
                itineraryId,
                dto.getDeparturePlace(),
                dto.getDestination(),
                dto.getDepartureTime(),
                dto.getArrivalTime(),
                dto.getCheckIn(),
                dto.getCheckOut()
            );

            trip.getItineraries().add(newItinerary);

            try (FileWriter fw = new FileWriter(filePath)) {
                gson.toJson(trip, fw);
                return itineraryId;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Integer updateItinerary(Integer itineraryId, String filePath, ItineraryDTO dto) {
        try (FileReader reader = new FileReader(filePath)) {
            Trip trip = gson.fromJson(reader, Trip.class);
            Itinerary newItinerary = new Itinerary(
                itineraryId,
                dto.getDeparturePlace(),
                dto.getDestination(),
                dto.getDepartureTime(),
                dto.getArrivalTime(),
                dto.getCheckIn(),
                dto.getCheckOut()
            );

            trip.getItineraries().set(itineraryId - 1, newItinerary);

            try (FileWriter fw = new FileWriter(filePath)) {
                gson.toJson(trip, fw);
                return itineraryId;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int getLatestTripId() {
        int id = 1;
        File dir = new File(FilePathConstants.DIR_RESOURCE_PATH);
        if (!dir.exists()) {
            return id;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            id = (int) Arrays.stream(files)
                .filter(f -> f.isFile() && f.getName().endsWith(FilePathConstants.EXTENSION_JSON))
                .count() + 1;
        }
        return id;
    }

    public Trips findAllTrips() {
        List<Trip> tripList = new ArrayList<>();
        File dir = new File(FilePathConstants.DIR_RESOURCE_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files == null) {
                throw new TripFileNotFoundException("json 파일이 존재하지 않습니다.");
            }
            List<File> fileList = Arrays.stream(files)
                .filter(f -> f.isFile() && f.getName().endsWith(FilePathConstants.EXTENSION_JSON))
                .toList();

            for (File file : fileList) {
                if (!file.exists()) {
                    continue;
                }
                try (FileReader reader = new FileReader(file)) {
                    Trip trip = gson.fromJson(reader, Trip.class);
                    tripList.add(trip);
                } catch (IOException e) {
                    String msg = "[%s] 파일은 잘못된 JSON 형식입니다.";
                    throw new IllegalFormatException(msg, file.getName());
                }
            }
            tripList.sort(Comparator.comparingInt(Trip::getTripId));
        }
        return new Trips(tripList);
    }

    public Optional<Trip> findTripById(Integer tripId, String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Trip trip = gson.fromJson(reader, Trip.class);

            if (trip != null) {
                return Optional.of(trip);
            }
        } catch (IOException e) {
            String msg = "선택한 여행 번호 " + tripId + "은/는 존재하지 않은 번호입니다.\n다시 입력하시겠습니까? (Y/N): ";
            throw new TripIdNotFoundException(msg);
        }
        return Optional.empty();
    }

    public List<Itinerary> findAllItineraries(File jsonFile) {
        List<Itinerary> itineraryList;
        try (FileReader reader = new FileReader(jsonFile)) {
            TripDTO trip = gson.fromJson(reader, TripDTO.class);
            itineraryList = new ArrayList<>(trip.getItineraries());

        } catch (IOException e) {
            String msg = "[%s] 파일은 잘못된 JSON 형식입니다.";
            throw new IllegalFormatException(msg, jsonFile.getName());
        }
        return itineraryList;
    }

    public boolean deleteTripById(Integer tripId) {
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        File file = new File(filePath);

        if (!file.exists()) {
            throw new TripFileNotFoundException("파일이 존재하지 않습니다.");
        }
        return file.delete();
    }

    public boolean deleteItineraryById(Integer tripId, Integer itineraryId) {
        String filePath = String.format(FilePathConstants.FILE_PATH, tripId);
        try (FileReader reader = new FileReader(filePath)) {
            Trip trip = gson.fromJson(reader, Trip.class);
            boolean isDeleted = false;
            for (Itinerary itinerary : trip.getItineraries()) {
                if (Objects.equals(itinerary.getItineraryId(), itineraryId)) {
                    isDeleted = trip.getItineraries().remove(itinerary);
                    break;
                }
            }
            if (!isDeleted) {
                return false;
            }
            try (FileWriter fw = new FileWriter(filePath)) {
                gson.toJson(trip, fw);
            }
        } catch (IOException e) {
            String msg = "[%s] 파일은 잘못된 JSON 형식입니다.";
            throw new IllegalFormatException(msg,
                String.format(FilePathConstants.FILE_NAME, tripId));
        }
        return true;
    }
}