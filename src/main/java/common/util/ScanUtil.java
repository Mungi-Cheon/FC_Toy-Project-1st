package common.util;

import java.util.Scanner;

public class ScanUtil {

    public static final String EMPTY = "";
    private static ScanUtil instance;
    private final Scanner sc;

    private ScanUtil() {
        sc = new Scanner(System.in);
    }

    public static ScanUtil getInstance() {
        if (instance == null) {
            instance = new ScanUtil();
        }
        return instance;
    }

    public String nextLine() {
        return sc.nextLine();
    }

    public String next() {
        return sc.next();
    }

    public static void printDivisionLine() {
        for (int i = 0; i < 175; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
