package com.wolf.fingerpos.utils;

import android.text.format.DateFormat;

/**
 * Created by Administrator on 12/7/2017.
 */

public class Utils {
    public static String DATE_FORMAT = "dd-MMM-yyyy";
    public static String TIME_FORMAT = "h:mm a";
    public static String DATE_TIME_FORMAT = "dd-MMM-yyyy h:mm a";
    public static long MONTH_MILLI = 30 * 86400000;

    public static String convertDate(long dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }


    public static int getYear(long dateInMilliseconds) {
        return Integer.valueOf(convertDate(dateInMilliseconds, "yyyy"));
    }

    public static int getMon(long dateInMilliseconds) {
        return Integer.valueOf(convertDate(dateInMilliseconds, "MM"));
    }


    public static int getDay(long dateInMilliseconds) {
        return Integer.valueOf(convertDate(dateInMilliseconds, "dd"));
    }
}
