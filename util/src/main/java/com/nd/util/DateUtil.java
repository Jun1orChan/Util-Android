package com.nd.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author cwj
 */
public class DateUtil {

    /**
     * 日期转为字符串
     *
     * @param date         日期
     * @param formatString 格式
     * @return
     */
    public static String dateToString(Date date, String formatString) {
        if (formatString == null || formatString.equals("")) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            return sdf.format(date);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
    }

    /**
     * 将日期转换成当天8:00的字符串
     *
     * @param date         日期
     * @param formatString 转换格式
     * @return
     */
    public static String dateToString8AM(Date date, String formatString) {
        if (formatString == null || formatString.equals("")) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            Date newDate = new Date(date.getYear(), date.getMonth(),
                    date.getDate(), 8, 0, 0);
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            return sdf.format(newDate);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
    }

    /**
     * 字符串转为日期 按照yyyy-MM-dd HH:mm:ss格式
     *
     * @param strDate      日期字符串
     * @param defaultValue 默认值
     * @return
     */
    public static Date stringToDate(String strDate, Date defaultValue) {
        return stringToDate(strDate, "yyyy-MM-dd HH:mm:ss", defaultValue);
    }


    /**
     * 字符串转为日期
     *
     * @param strDate      日期字符串
     * @param formatString 格式化字符串 如果为空则按照yyyy-MM-dd HH:mm:ss格式
     * @param defaultValue
     * @return
     */
    public static Date stringToDate(String strDate, String formatString,
                                    Date defaultValue) {
        if (formatString == null || formatString.equals("")) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            return sdf.parse(strDate);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换日期格式
     *
     * @param strDate      日期字符串
     * @param beforeFormat 日期格式
     * @param afterFormat  结果日期格式
     * @return
     */
    public static String changeDateFormat(String strDate, String beforeFormat,
                                          String afterFormat) {
        String afterStr = null;
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(beforeFormat);
            Date date = sdf.parse(strDate);
            SimpleDateFormat after = new SimpleDateFormat(afterFormat);
            afterStr = after.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterStr;
    }
}
