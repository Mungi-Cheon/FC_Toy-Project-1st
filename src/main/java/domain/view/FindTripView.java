package domain.view;

import common.exception.ObjectEmptyException;
import common.util.AppStatus;
import common.util.ScanUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.trip.TripController;
import domain.dao.trip.TripDAOImpl;
import domain.model.trip.Trips;
import domain.service.trip.TripServiceImpl;
import java.util.Arrays;

public class FindTripView implements ConsoleView {

    private final TripController tripController;
    private final TripInput tripInput;
    private final FindTripByIdView findTripByIdView;
    private final ScanUtil sc;

    public FindTripView() {
        tripController = new TripController(
            new TripServiceImpl(new TripDAOImpl()));
        tripInput = new TripInput();
        findTripByIdView = new FindTripByIdView();
        sc = ScanUtil.getInstance();
    }

    @Override
    public ConsoleView print() {

        try {
            System.out.println();
            ScanUtil.printDivisionLine();

            System.out.println("\n여행 목록");

            Trips trips = tripController.findAllTrips();
            printTrips(trips);

            findTripByIdView.print();

        } catch (ObjectEmptyException e) {
            System.out.println(e.getMessage());

            askForCreatTrip();
        }

        return MainMenu.getInstance();
    }

    private void printTrips(Trips trips) {
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

	private void askForCreatTrip() {
		String input = sc.nextLine();

		if (input.equalsIgnoreCase("y")) {
			tripInput.print();
		} else if (input.equalsIgnoreCase("n")) { // 메인으로
			AppStatus.setStatus(true);
		} else {
			System.out.println("잘못된 입력입니다. (Y/N)으로만 작성해주세요");
			askForCreatTrip();
		}
	}

}
