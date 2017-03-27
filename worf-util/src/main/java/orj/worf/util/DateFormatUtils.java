package orj.worf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_HH_MM_SS_24H = "yyyy-MM-dd HH:mm:ss";

    /**
     * parse a string according to a date format
     */
    public static Date parse(final String pattern, final String date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat fmt = getDateFormat(pattern);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            ParseException ex = new ParseException(e.getMessage() + " - expected '" + pattern + '\'',
                    e.getErrorOffset());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    /**
     * format a date to a string
     */
    public static String format(final String pattern, final Date d) {
        if (d == null) {
            return null;
        }
        return getDateFormat(pattern).format(d);
    }

    /**
     * Uses Thread local storage - holds a map of date formatters. Should consider weak references to allow garbage
     * collection?
     */
    private static final ThreadLocal<ConcurrentMap<String, SimpleDateFormat>> locals = new ThreadLocal<ConcurrentMap<String, SimpleDateFormat>>();

    /**
     * Locate Thread local formatter for given type of Date format. This is safe for use by calling thread only - its
     * reference should NOT be used in code that could share it with other threads
     *
     * @param pattern The date format
     * @return formatter thread local, no synchronize is needed for it in the calling thread.
     */
    public static SimpleDateFormat getDateFormat(final String pattern) {
        ConcurrentMap<String, SimpleDateFormat> map = locals.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, SimpleDateFormat>();
            locals.set(map);
        }
        SimpleDateFormat df = map.get(pattern);
        if (df == null) {
            df = new SimpleDateFormat(pattern);
            map.put(pattern, df);
        }
        return df;
    }

}
