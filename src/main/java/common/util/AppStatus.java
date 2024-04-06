package common.util;

public class AppStatus {

    private static boolean isRun = true;

    public static boolean isRun() {
        return isRun;
    }

    public static void setStatus(boolean status) {
        isRun = status;
    }
}
