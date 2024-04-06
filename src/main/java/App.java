import common.util.AppStatus;
import common.view.ConsoleView;
import common.view.MainMenu;

public class App {

    public static void main(String[] args) {
        run();
    }

    private static void run() {

        ConsoleView consoleView = MainMenu.getInstance();
        while (AppStatus.isRun()) {
            consoleView = consoleView.print();
        }

    }
}
