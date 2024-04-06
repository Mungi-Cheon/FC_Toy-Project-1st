package domain.view;

import common.exception.TripFileNotFoundException;
import common.exception.TripIdNotFoundException;
import common.util.DateUtil;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.itinerary.ItineraryController;
import domain.controller.trip.TripController;
import domain.dao.itinerary.ItineraryDAOImpl;
import domain.dao.trip.TripDAOImpl;
import domain.model.itinerary.Itineraries;
import domain.model.trip.Trip;
import domain.model.trip.Trips;
import domain.service.itinerary.ItineraryServiceImpl;
import domain.service.trip.TripServiceImpl;
import java.util.Arrays;

public class ItineraryDeleteView implements ConsoleView {

    private final ScanUtil sc;
    private final ItineraryController itineraryController;
    private final TripController tripController;
    private final ValidationUtil val;
    private final DateUtil date;

    public ItineraryDeleteView() {
        this.itineraryController = new ItineraryController(
            new ItineraryServiceImpl(new ItineraryDAOImpl()));
        this.tripController = new TripController(new TripServiceImpl(new TripDAOImpl()));
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
        date = DateUtil.getInstance();
    }

    @Override
    public ConsoleView print() {
        Trips trips = tripController.findAllTrips();
        printAllTrips(trips);

        while (true) {
            Integer tripId = inputTripId();
            if (!selectTrip(tripId)) {
                break;
            }
        }
        return MainMenu.getInstance();
    }

    private void printAllTrips(Trips trips) {
        System.out.println("전체 여행 목록");
        ScanUtil.printDivisionLine();
        System.out.printf(
            "%-1s[여행번호]%-10s[여행이름]%-15s[출발시간]%-20s[도착시간]%n",
            ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY);
        Arrays.stream(trips.toArray()).forEach(
            trip -> System.out.printf("%6d %23s %21s %28s\n",
                trip.getTripId(),
                trip.getTripName(),
                trip.getStartDate(),
                trip.getEndDate())
        );
        ScanUtil.printDivisionLine();
    }

    private void displayItineraries(Integer tripId) {
        Itineraries itineraries = itineraryController.findSelectedTripItineraries(tripId);
        System.out.printf("\n%5d번 여행의 여정 목록 \n", tripId);
        ScanUtil.printDivisionLine();
        System.out.printf(
            "%-1s[여정번호]%-15s[출발지]%-15s[도착지]%-15s[출발시간]%-20s[도착시간]%-20s[체크인]%-20s[체크아웃]%n",
            ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY
            , ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY);
        Arrays.stream(itineraries.toArray()).forEach(
            itinerary -> System.out.printf("%6d %23s %21s %29s %28s %27s %26s\n",
                itinerary.getItineraryId(),
                itinerary.getDeparturePlace(), itinerary.getDestination(),
                date.InstantToString(itinerary.getDepartureTime()),
                date.InstantToString(itinerary.getArrivalTime()),
                date.InstantToString(itinerary.getCheckIn()),
                date.InstantToString(itinerary.getCheckOut()))
        );
        ScanUtil.printDivisionLine();
    }

    private Integer inputTripId() {
        System.out.print("삭제할 여정이 속한 여행 번호를 입력해주세요: ");
        return Integer.parseInt(sc.nextLine());
    }

    private boolean selectTrip(Integer tripId) {
        try {
            Trip trip = tripController.findTripById(tripId);
        } catch (TripIdNotFoundException e) {
            System.out.println(e.getMessage());
            String userInput = sc.nextLine();

            if (val.isYes(userInput)) {
                print();
            } else {
                return false;
            }
        }

        displayItineraries(tripId);

        while (true) {
            Integer itineraryId = inputItineraryId();
            try {
                boolean deleteResult = deleteItinerary(tripId, itineraryId);
                if (deleteResult) {
                    System.out.println("\n" + itineraryId + "번 여정이 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("여정 삭제에 실패했습니다. 입력한 번호에 해당하는 여정이 존재하지 않습니다.");
                }
                ScanUtil.printDivisionLine();
                System.out.print(deleteResult ? "\n계속 삭제하시겠습니까? (Y/N): " : "\n다시 시도하시겠습니까? (Y/N): ");
                String retry = sc.nextLine();
                if (retry.equalsIgnoreCase("y")) {
                    displayItineraries(tripId);
                } else {
                    System.out.println("메인 화면으로 돌아갑니다.");
                    return false;
                }
            } catch (TripFileNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Integer inputItineraryId() {
        System.out.print("삭제할 여정의 번호를 입력해주세요: ");
        return Integer.parseInt(sc.nextLine());
    }

    private boolean deleteItinerary(Integer tripId, Integer itineraryId)
        throws TripFileNotFoundException {
        return itineraryController.deleteItinerary(tripId, itineraryId);
    }
}