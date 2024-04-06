package domain.view;

import common.util.AppStatus;
import common.util.DateUtil;
import common.view.ConsoleView;
import common.view.MainMenu;
import domain.controller.itinerary.ItineraryController;
import domain.dao.itinerary.ItineraryDAOImpl;
import domain.model.itinerary.Itineraries;
import domain.service.itinerary.ItineraryServiceImpl;
import java.util.Arrays;
import java.util.Map;

public class FindItineraryView implements ConsoleView {

    private static final String EMPTY = "";
    private final ItineraryController controller;

    private final DateUtil date;

    public FindItineraryView() {
        date = DateUtil.getInstance();
        controller = new ItineraryController(new ItineraryServiceImpl(new ItineraryDAOImpl()));
    }

    @Override
    public ConsoleView print() {
        displayItineraries();
        System.out.println("메인 메뉴로 돌아갑니다.");
        AppStatus.setStatus(true);
        return MainMenu.getInstance();
    }

    private void displayItineraries() {
        Map<Integer, Itineraries> trip2Itinerary = controller.findAllItinerary();
        for (Integer tripId : trip2Itinerary.keySet()) {
            Itineraries itineraries = trip2Itinerary.get(tripId);
            System.out.printf("\n%5d번 여행의 여정 목록 \n", tripId);
            printDivisionLine();
            System.out.printf(
                "%-1s[여정번호]%-15s[출발지]%-15s[도착지]%-15s[출발시간]%-20s[도착시간]%-20s[체크인]%-20s[체크아웃]%n",
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
            Arrays.stream(itineraries.toArray()).forEach(
                itinerary -> System.out.printf("%6d %23s %21s %29s %28s %27s %26s\n",
                    itinerary.getItineraryId(),
                    itinerary.getDeparturePlace(), itinerary.getDestination(),
                    date.InstantToString(itinerary.getDepartureTime()),
                    date.InstantToString(itinerary.getArrivalTime()),
                    date.InstantToString(itinerary.getCheckIn()),
                    date.InstantToString(itinerary.getCheckOut()))
            );
            printDivisionLine();
        }
    }

    private void printDivisionLine() {
        for (int i = 0; i < 175; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
