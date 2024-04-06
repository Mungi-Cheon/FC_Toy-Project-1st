package common.util;

import common.exception.IllegalFormatException;
import common.exception.InvalidInputException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.PatternSyntaxException;

public class ValidationUtil {

    public static final String INT_REGEX = "[0-9]+";
    public static final String ALL_WORDS_REGEX = "[0-9a-zA-Zㄱ-ㅎ가-힣 ]*";
    public static final String DATE_REGEX = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-([123]0|[012][1-9]|31)";
    public static final String DATE_TIME_REGEX =
        DATE_REGEX + " ([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    private static final String YES_REGEX = "[y|Y]";
    private static final String NO_REGEX = "[n|N]";
    private static ValidationUtil instance;

    private ValidationUtil() {

    }

    public static ValidationUtil getInstance() {
        if (instance == null) {
            instance = new ValidationUtil();
        }
        return instance;
    }

    public boolean isValidInput(String input, String regex) {
        try {
            if (input.isEmpty()) {
                return false;
            }
            return input.matches(regex);
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public boolean isValidDate(String start, String end, String regex) {
        try {
            if (!isValidInput(end, regex)) {
                return false;
            } else if (start.equals(end)) {
                return true;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate;
            if (start.length() > 10) {
                startDate = formatter.parse(start.substring(0, 10));
            } else {
                startDate = formatter.parse(start);
            }

            Date endDate;
            if (end.length() > 10) {
                endDate = formatter.parse(end.substring(0, 10));
            } else {
                endDate = formatter.parse(end);
            }
            return endDate.after(startDate);
        } catch (PatternSyntaxException | ParseException e) {
            return false;
        }
    }

    public boolean isValidDateTime(String start, String end, String regex) {
        if (!isValidInput(end, regex)) {
            return false;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = dateFormat.parse(start);
            Date endTime = dateFormat.parse(end);
            return endTime.after(startTime);
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isYes(String input) {
        try {
            if (input.matches(YES_REGEX)) {
                return true;
            } else if (input.matches(NO_REGEX)) {
                return false;
            } else {
                throw new InvalidInputException("Y 또는 N을 입력해주세요 ");
            }
        } catch (PatternSyntaxException e) {
            throw new IllegalFormatException("Y 또는 N을 입력해주세요 ");
        }
    }
}
