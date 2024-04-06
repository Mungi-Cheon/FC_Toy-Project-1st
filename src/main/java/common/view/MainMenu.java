package common.view;

import common.util.ScanUtil;
import common.util.ValidationUtil;

public class MainMenu implements ConsoleView {

    private static MainMenu instance;
    private final SimpleViewFactory svf;
    private final ScanUtil sc;
    private final ValidationUtil val;

    private MainMenu() {
        svf = new SimpleViewFactory();
        sc = ScanUtil.getInstance();
        val = ValidationUtil.getInstance();
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    @Override
    public ConsoleView print() {
        String menu;
        while (true) {
            System.out.println();
            ScanUtil.printDivisionLine();
            System.out.println(
                "여행/여정 기록(1), 여행/여정 수정(2), 여행 조회(3), 여정 조회(4), 여행 삭제(5), 여정 삭제(6), 종료(7)");
            ScanUtil.printDivisionLine();
            System.out.print("\n시작 할 메뉴 번호를 입력 하세요 : ");
            menu = sc.nextLine();
            if (val.isValidInput(menu, ValidationUtil.INT_REGEX)) {
                return svf.selectView(Integer.parseInt(menu));
            } else {
                System.err.println("\n유효하지 않은 입력입니다. 다시 입력해 주세요.");
            }
        }
    }
}
