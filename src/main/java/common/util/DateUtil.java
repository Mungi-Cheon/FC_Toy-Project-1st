package common.util;

import common.exception.IllegalFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static DateUtil instance;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat insDateFormat;
    private final ZoneId zoneId = ZoneId.of("Asia/Seoul");

    private DateUtil() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        insDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static DateUtil getInstance() {
        if (instance == null) {
            instance = new DateUtil();
        }
        return instance;
    }

    public String stringToInstant(String dateTime) {
        try {
            Instant instant = dateFormat.parse(dateTime).toInstant();

            return instant.atZone(zoneId)
                .format(DateTimeFormatter.ofPattern(insDateFormat.toPattern()));
        } catch (ParseException e) {
            throw new IllegalFormatException(e.getMessage());
        }
    }

    public String InstantToString(String dateTime) {
        try {
            Date date = insDateFormat.parse(dateTime);
            Instant instant = date.toInstant();
            return instant.atZone(zoneId)
                .format(DateTimeFormatter.ofPattern(dateFormat.toPattern()));
        } catch (ParseException e) {
            throw new IllegalFormatException(e.getMessage());
        }
    }

}
