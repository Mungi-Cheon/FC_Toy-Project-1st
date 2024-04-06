package domain.view;

import common.exception.IllegalFormatException;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.trip.TripController;
import domain.dao.trip.TripDAOImpl;
import domain.model.trip.Trip;
import domain.service.trip.TripServiceImpl;

public class TripInput implements ConsoleView {

    private final ScanUtil sc;
    private final ValidationUtil val;
    private final TripController tripController;

    public TripInput() {
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
        tripController = new TripController(new TripServiceImpl(new TripDAOImpl()));
    }

    public Trip inputTripDetail() {
        ScanUtil.printDivisionLine();
        System.out.println("여행을 추가합니다.\n");

        System.out.print("여행 이름: ");
        String tripName = sc.nextLine();
        if (!val.isValidInput(tripName, ValidationUtil.ALL_WORDS_REGEX)) {
            String errMsg = String.format("입력 하신 여행 이름 [%s]은 잘못된 형식입니다", tripName);
            throw new IllegalFormatException(errMsg);
        }

        System.out.print("시작 날짜 (YYYY-MM-DD): ");
        String startDate = sc.nextLine();
        if (!val.isValidInput(startDate, ValidationUtil.DATE_REGEX)) {
            String errMsg = String.format("입력 하신 시작 날짜 [%s]은 잘못된 형식입니다", startDate);
            throw new IllegalFormatException(errMsg);
        }

        System.out.print("종료 날짜 (YYYY-MM-DD): ");
        String endDate = sc.nextLine();
        if (!val.isValidDate(startDate, endDate, ValidationUtil.DATE_REGEX)) {
            String errMsg = String.format("입력 하신 종료 날짜 [%s]은 잘못된 형식입니다", endDate);
            throw new IllegalFormatException(errMsg);
        }
        return new Trip(tripName, startDate, endDate);
    }

    @Override
    public ConsoleView print() {
        System.out.println();
        showTripInput();
        return MainMenu.getInstance();
    }

    public void showTripInput() {
        try {
            Trip tripObj = inputTripDetail();

            Integer tripId = tripController.createTrip(tripObj);
            System.out.printf("\n%d번 여행이 추가되었습니다.\n", tripId);
            ScanUtil.printDivisionLine();

            ItineraryInput itineraryInput = new ItineraryInput();
            itineraryInput.showItineraryInput(tripId, tripObj);
            ScanUtil.printDivisionLine();

            System.out.print("\n여행을 계속 추가하시겠습니까?(Y/N): ");
            String tripAnswer = sc.nextLine();
            if (val.isYes(tripAnswer)) {
                showTripInput();
            }
            ScanUtil.printDivisionLine();
            System.out.println("\n여행 기록을 종료하고 메인 화면으로 돌아갑니다.");
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            showTripInput();
        }
    }
}
