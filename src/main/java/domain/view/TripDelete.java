package domain.view;

import common.exception.IllegalFormatException;
import common.exception.TripIdNotFoundException;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.trip.TripController;
import domain.dao.trip.TripDAOImpl;
import domain.model.trip.Trips;
import domain.service.trip.TripServiceImpl;
import java.util.Arrays;

public class TripDelete implements ConsoleView {

    private final ScanUtil sc;
    private final ValidationUtil val;
    private final TripController tripController;

    public TripDelete() {
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
        this.tripController = new TripController(new TripServiceImpl(new TripDAOImpl()));
    }

    @Override
    public ConsoleView print() {
        Trips trips = tripController.findAllTrips();
        printAllTrips(trips);
        try {
            Integer tripId = inputTripId();
            tripController.deleteByTripId(tripId);
            System.out.println("\n" + tripId + "번 여행이 성공적으로 삭제되었습니다.\n");
            ScanUtil.printDivisionLine();
            keepDelete();
        } catch (TripIdNotFoundException e) {
            System.out.println(e.getMessage());
            navigateToMainMenu();
        }
        return MainMenu.getInstance();
    }

    private Integer inputTripId() {
        System.out.print("삭제할 여행 번호를 입력해주세요: ");
        return Integer.parseInt(sc.nextLine());
    }

    private void navigateToMainMenu() {
        String input = sc.nextLine();
        if (val.isYes(input)) {
            print();
        } else {
            System.out.println("메인 화면으로 돌아갑니다.");
        }
    }

    private void keepDelete() {
        try {
            System.out.print("계속 삭제하시겠습니까? (Y/N): ");
            navigateToMainMenu();
        } catch (IllegalFormatException e) {
            System.out.println(e.getMessage());
            keepDelete();
        }
    }

    private void printAllTrips(Trips trips) {
        System.out.println("전체 여행 목록");
        ScanUtil.printDivisionLine();
        System.out.printf(
            "%-1s[여행번호]%-10s[여행이름]%-15s[출발시간]%-20s[도착시간]%n",
            ScanUtil.EMPTY, ScanUtil.EMPTY, ScanUtil.EMPTY
            , ScanUtil.EMPTY);
        Arrays.stream(trips.toArray()).forEach(
            trip -> System.out.printf("%6d %23s %21s %28s\n",
                trip.getTripId(),
                trip.getTripName(),
                trip.getStartDate(),
                trip.getEndDate())
        );
        ScanUtil.printDivisionLine();
    }
}