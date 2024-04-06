package common.view;

import domain.view.Exit;
import domain.view.FindItineraryView;
import domain.view.FindTripView;
import domain.view.ItineraryDeleteView;
import domain.view.TripDelete;
import domain.view.TripInput;
import domain.view.TripUpdate;

public class SimpleViewFactory {

    public ConsoleView selectView(int menu) {
        return switch (menu) {
            case 1 -> new TripInput();
            case 2 -> new TripUpdate();
            case 3 -> new FindTripView();
            case 4 -> new FindItineraryView();
            case 5 -> new TripDelete();
            case 6 -> new ItineraryDeleteView();
            case 7 -> new Exit();
            default -> throw new IllegalArgumentException("잘못된 입력입니다");
        };
    }
}