package domain.view;

import common.exception.IllegalFormatException;
import common.util.DateUtil;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.itinerary.ItineraryController;
import domain.dao.itinerary.ItineraryDAOImpl;
import domain.model.itinerary.Itinerary;
import domain.model.trip.Trip;
import domain.service.itinerary.ItineraryServiceImpl;

public class ItineraryInput implements ConsoleView {

    private final ScanUtil sc;
    private final ValidationUtil val;
    private final ItineraryController itineraryController;
    private final DateUtil date;

    public ItineraryInput() {
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
        date = DateUtil.getInstance();
        itineraryController = new ItineraryController(
            new ItineraryServiceImpl(new ItineraryDAOImpl()));
    }

    private Itinerary inputItineraryDetail(Trip trip) {
        System.out.println("\n여정을 추가합니다.\n");
        System.out.print("출발지: ");
        String departurePlace = sc.nextLine();
        if (!val.isValidInput(departurePlace, ValidationUtil.ALL_WORDS_REGEX)) {
            String errMsg = String.format("입력 하신 출발지 [%s]은 잘못된 형식입니다", departurePlace);
            throw new IllegalFormatException(errMsg);
        }

        System.out.print("도착지: ");
        String destination = sc.nextLine();
        if (!val.isValidInput(destination, ValidationUtil.ALL_WORDS_REGEX)) {
            String errMsg = String.format("입력 하신 도착지 [%s]은 잘못된 형식입니다", destination);
            throw new IllegalFormatException(errMsg);
        }

        System.out.print("출발 시간 (yyyy-MM-dd HH:MM:SS): ");
        String departureTime = sc.nextLine();
        checkDepartureTime(trip.getStartDate(), trip.getEndDate(), departureTime);

        System.out.print("도착 시간 (yyyy-MM-dd HH:MM:SS): ");
        String arrivalTime = sc.nextLine();
        checkArrivalTime(trip.getStartDate(), trip.getEndDate(), departureTime,
            arrivalTime);

        System.out.print("체크인 (yyyy-MM-dd HH:MM:SS): ");
        String checkIn = sc.nextLine();
        checkCheckIn(trip.getEndDate(), arrivalTime, checkIn);

        System.out.print("체크아웃 (yyyy-MM-dd HH:MM:SS): ");
        String checkOut = sc.nextLine();
        checkCheckOut(trip.getEndDate(), checkIn, checkOut);

        return new Itinerary(null, departurePlace, destination
            , date.stringToInstant(departureTime), date.stringToInstant(arrivalTime)
            , date.stringToInstant(checkIn), date.stringToInstant(checkOut));
    }

    public void showItineraryInput(Integer tripId, Trip createdTrip) {
        try {
            Itinerary itineraryObj = inputItineraryDetail(createdTrip);
            Integer itineraryId = itineraryController.createItinerary(tripId, itineraryObj);

            System.out.printf("\n%d번 여행의 %d번 여정을 추가했습니다.\n", tripId, itineraryId);
            ScanUtil.printDivisionLine();

            System.out.print("\n여정을 계속 추가하시겠습니까?(Y/N): ");
            String itineraryAnswer = sc.nextLine();

            if (val.isYes(itineraryAnswer)) {
                showItineraryInput(tripId, createdTrip);
            }
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            showItineraryInput(tripId, createdTrip);
        }
    }

    private void checkDepartureTime(String startDate, String endDate, String target) {
        String start = startDateTimeSet(startDate);
        String end = endDateTimeSet(endDate);

        if (!val.isValidInput(target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 출발 시간 [%s]은 잘못된 형식입니다", target);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(start, target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 출발 시간(%s)이 시작 날짜(%s)보다 이전입니다", target, start);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(target, end, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 출발 시간(%s)이 종료 날짜(%s)보다 이후입니다", target, end);
            throw new IllegalFormatException(errMsg);
        }
    }

    private void checkArrivalTime(String startDate, String endDate, String departure,
        String target) {
        String start = startDateTimeSet(startDate);
        String end = endDateTimeSet(endDate);

        if (!val.isValidDateTime(departure, target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 도착 시간 [%s]은 잘못된 형식입니다", target);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(start, target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 도착 시간(%s)이 시작 날짜(%s)보다 이전입니다", target, start);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(target, end, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 도착 시간(%s)이 종료 날짜(%s)보다 이후입니다", target, end);
            throw new IllegalFormatException(errMsg);
        }
    }

    private void checkCheckIn(String endDate, String arrivalTime, String target) {
        String end = endDateTimeSet(endDate);

        if (!val.isValidInput(target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크인 [%s]은 잘못된 형식입니다", target);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(target, end, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크인(%s)이 종료 날짜(%s)보다 이후입니다", target, end);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(arrivalTime, target, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크인(%s)이 도착 시간(%s)보다 이전입니다", target, arrivalTime);
            throw new IllegalFormatException(errMsg);
        }
    }

    private void checkCheckOut(String endDate, String checkIn, String checkOut) {
        String end = endDateTimeSet(endDate);

        if (!val.isValidInput(checkOut, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크아웃 [%s]은 잘못된 형식입니다", checkOut);
            throw new IllegalFormatException(errMsg);
        } else if (!val.isValidDateTime(checkIn, checkOut, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크아웃(%s)이 체크인(%s)보다 이전입니다", checkOut, checkIn);
            throw new IllegalFormatException(errMsg);
        } else if (val.isValidDateTime(end, checkOut, ValidationUtil.DATE_TIME_REGEX)) {
            String errMsg = String.format("입력 하신 체크아웃(%s)이 종료 날짜(%s)보다 이후입니다", checkOut, end);
            throw new IllegalFormatException(errMsg);
        }
    }

    private String startDateTimeSet(String start) {
        if (start.length() <= 10) {
            start += " 00:00:00";
        }
        return start;
    }

    private String endDateTimeSet(String end) {
        if (end.length() <= 10) {
            end += " 23:59:59";
        }
        return end;
    }

    @Override
    public ConsoleView print() {
        return MainMenu.getInstance();
    }
}
