package orj.worf.util;

import java.text.ParseException;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * parse a string according to a date format
     */
    public static Date parse(final String pattern, final String date) throws ParseException {
        return DateFormatUtils.parse(pattern, date);
    }

    /**
     * format a date to a string
     */
    public static String format(final String pattern, final Date d) {
        return DateFormatUtils.format(pattern, d);
    }

}
