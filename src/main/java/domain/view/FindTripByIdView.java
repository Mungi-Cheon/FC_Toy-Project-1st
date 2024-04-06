package domain.view;

import common.exception.InvalidInputException;
import common.exception.TripFileNotFoundException;
import common.exception.TripIdNotFoundException;
import common.util.DateUtil;
import common.util.AppStatus;
import common.util.ScanUtil;
import common.util.ValidationUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.itinerary.ItineraryController;
import domain.dao.itinerary.ItineraryDAOImpl;
import domain.model.itinerary.Itineraries;
import domain.service.itinerary.ItineraryServiceImpl;
import java.util.Arrays;

public class FindTripByIdView implements ConsoleView {

    private final ItineraryController controller;
    private final ScanUtil sc;
    private final ValidationUtil val;
    private final DateUtil date;

    public FindTripByIdView() {
        sc = ScanUtil.getInstance();
        date = DateUtil.getInstance();
        controller = new ItineraryController(new ItineraryServiceImpl(new ItineraryDAOImpl()));
        val = ValidationUtil.getInstance();
    }

    @Override
    public ConsoleView print() {

        System.out.print("\n조회할 여행 ID를 입력해주세요: ");
        String tripId = sc.nextLine();
        // 0~9 아닐때 print()  근데 fsafa 를 친후 나중에 var.isYes() 에서 n을 치면 오류가 남
        if (!val.isValidInput(tripId, ValidationUtil.INT_REGEX)) {
                System.err.print("\n여행 ID를 잘 못 입력하셨습니다.\n");
                print();
        } else {  // 0~9 면 호출
            printTripById(Integer.parseInt(tripId));
        }

        return MainMenu.getInstance();
    }

    public void printTripById(Integer tripId) {  // << 얘는 무조건 0~9 사이였을때만 와야함

        try {                                   // id에 대한 json이 있을 때
            displayItineraries(tripId);

        } catch (TripFileNotFoundException e) {  // id에 대한 json이 없을 때

            System.err.println("입력하신 ID에 대한 여행 정보가 없습니다.");
            print();
        }

        System.out.print("계속해서 여행을 조회하시겠습니까? (y/n): ");

        try {
            isVaildInput();
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            isVaildInput();
        }


    }

    private void displayItineraries(Integer tripId) {
        Itineraries itineraries = controller.findSelectedTripItineraries(tripId);
        System.out.print("\n여정 목록 \n");
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

    private void isVaildInput() {
        String input = sc.nextLine();

        if (val.isYes(input)) {  // y
            print();
        } else {   // n
            AppStatus.setStatus(true);
        }
    }

}