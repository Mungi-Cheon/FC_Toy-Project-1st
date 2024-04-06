package domain.view;

import common.util.AppStatus;
import common.view.ConsoleView;
import common.view.MainMenu;

public class Exit implements ConsoleView {

	@Override
	public ConsoleView print() {

		System.out.println("프로그램을 종료합니다.");
		AppStatus.setStatus(false);

		return MainMenu.getInstance();
	}
}
