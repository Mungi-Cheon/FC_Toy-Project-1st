package domain.view;

import common.exception.IllegalFormatException;
import common.exception.ObjectEmptyException;
import common.exception.TripIdNotFoundException;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.trip.TripController;
import domain.dao.trip.TripDAOImpl;
import domain.model.trip.Trip;
import domain.model.trip.Trips;
import domain.service.trip.TripServiceImpl;
import java.util.Arrays;

public class TripUpdate implements ConsoleView {

    private static final String SUB_MENU_TRIP = "1";
    private static final String SUB_MENU_ITINERARY = "2";
    private static final String RETURN = "9";
    private final ScanUtil sc;
    private final ValidationUtil val;
    private final TripController tripController;

    public TripUpdate() {
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
        tripController = new TripController(
            new TripServiceImpl(new TripDAOImpl()));
    }

    public Trip updateTripDetail() {
        System.out.println("선택한 여행을 수정합니다.\n");

        System.out.print("여행 이름: ");
        String updatedTripName = sc.nextLine();
        if (!val.isValidInput(updatedTripName, ValidationUtil.ALL_WORDS_REGEX)) {
            String errMsg = String.format("입력 하신 여행 이름 [%s]은 잘못된 형식입니다", updatedTripName);
            throw new IllegalFormatException(errMsg);
        }
        System.out.println();
        System.out.print("시작 날짜 (YYYY-MM-DD): ");
        String updatedStartDate = sc.nextLine();
        if (!val.isValidInput(updatedStartDate, ValidationUtil.DATE_REGEX)) {
            String errMsg = String.format("입력 하신 시작 날짜 [%s]은 잘못된 형식입니다", updatedStartDate);
            throw new IllegalFormatException(errMsg);
        }
        System.out.println();
        System.out.print("종료 날짜 (YYYY-MM-DD): ");
        String updatedEndDate = sc.nextLine();
        if (!val.isValidDate(updatedStartDate, updatedEndDate,
            ValidationUtil.DATE_REGEX)) {
            String errMsg = String.format("입력 하신 종료 날짜 [%s]은 잘못된 형식입니다", updatedEndDate);
            throw new IllegalFormatException(errMsg);
        }

        return new Trip(updatedTripName, updatedStartDate, updatedEndDate);
    }

    @Override
    public ConsoleView print() {
        String subMenu;
        while (true) {
            ScanUtil.printDivisionLine();
            System.out.println("여행 수정(1), 여정 수정(2), 메인 화면(9)");
            ScanUtil.printDivisionLine();
            System.out.print("\n시작 할 서브 메뉴 번호를 입력 하세요 : ");
            subMenu = sc.nextLine();
            if (val.isValidInput(subMenu, ValidationUtil.INT_REGEX)) {
                break;
            } else {
                System.err.println("\n서브 메뉴 번호를 다시 입력해 주세요.");
            }
        }

        switch (subMenu) {
            case SUB_MENU_TRIP -> {
                try {
                    updateTrip(subMenu);
                } catch (TripIdNotFoundException e) {
                    return MainMenu.getInstance();
                }
            }
            case SUB_MENU_ITINERARY -> {
                try {
                    updateItinerary(subMenu);
                } catch (TripIdNotFoundException e) {
                    return MainMenu.getInstance();
                }
            }
            case RETURN -> {
                System.out.println("\n메인 화면으로 돌아갑니다.");
                return MainMenu.getInstance();
            }
            default -> {
                System.err.println("\n다시 입력해 주세요.");
                print();
            }
        }

        return MainMenu.getInstance();
    }

    private void updateTrip(String subMenu) {
        ScanUtil.printDivisionLine();
        System.out.println("여 행 수 정");
        try {
            printTrips(tripController.findAllTrips());
            tripController.updateTripById(inputTripId(), updateTripDetail());
            System.out.println("여행 내용 수정이 완료되었습니다.\n");
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            updateTrip(subMenu);
        } catch (TripIdNotFoundException e) {
            throw e;
        } catch (ObjectEmptyException e) {
            System.out.print("\n" + e.getMessage() + "\n");
            return;
        }
        askYorN(subMenu);
    }

    private void printTrips(Trips trips) {
        System.out.println("\n현재 저장된 여행들입니다.");
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

    private void updateItinerary(String subMenu) {
        System.out.println("\n여 정 수 정");
        try {
            printTrips(tripController.findAllTrips());
            ItineraryUpdate itineraryUpdate = new ItineraryUpdate();
            itineraryUpdate.updateItinerary(inputTripId());
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            updateItinerary(subMenu);
        } catch (TripIdNotFoundException e) {
            throw e;
        } catch (ObjectEmptyException e) {
            System.out.print("\n" + e.getMessage() + "\n");
            return;
        }
        askYorN(subMenu);
    }

    private Trip inputTripId() {
        System.out.print("여행 ID를 입력해주세요: ");

        String selectedTripId = sc.nextLine();
        if (!val.isValidInput(selectedTripId, ValidationUtil.INT_REGEX)) {
            String errMsg = String.format("입력 하신 여행 ID [%s]은 잘못된 형식입니다", selectedTripId);
            System.err.println(errMsg);
        }

        try {
            return tripController.findTripById(Integer.parseInt(selectedTripId));
        } catch (TripIdNotFoundException e) {
            System.out.println(e.getMessage());
            String input = sc.nextLine();
            if (val.isYes(input)) {
                return inputTripId();
            } else {
                throw e;
            }
        }
    }

    private void askYorN(String subMenu) {
        try {
            ScanUtil.printDivisionLine();
            System.out.print("\n계속 수정하시겠습니까? (y/n)");
            String answer = sc.nextLine();
            if (!val.isYes(answer)) {
                System.out.println("\n메인 화면으로 돌아갑니다.");
                return;
            }

            if (subMenu.equals(SUB_MENU_TRIP)) {
                updateTrip(subMenu);
            } else {
                updateItinerary(subMenu);
            }

        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            askYorN(subMenu);
        }
    }
}
