package org.jun1or.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 日期转为字符串
     *
     * @param date         日期
     * @param formatString 格式
     * @return
     */
    public static String dateToString(Date date, String formatString) {
        if (formatString == null || formatString.equals(""))
            formatString = "yyyy-MM-dd HH:mm:ss";
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
        if (formatString == null || formatString.equals(""))
            formatString = "yyyy-MM-dd HH:mm:ss";

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
     * 将日期转换成某天的某一时刻的字符串
     *
     * @param date         日期
     * @param formatString 日期格式
     * @param hourse       时
     * @param min          分
     * @param sec          秒
     * @return
     */
    public static String dateToString(Date date, String formatString,
                                      int hourse, int min, int sec) {
        if (formatString == null || formatString.equals(""))
            formatString = "yyyy-MM-dd HH:mm:ss";

        try {
            Date newDate = new Date(date.getYear(), date.getMonth(),
                    date.getDate(), hourse, min, sec);
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            return sdf.format(newDate);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
    }

    /**
     * 1 date1>date2
     * 0 date1=date2
     * -1 date1<date2
     *
     * @param dateStr1
     * @param formatString1
     * @param dataStr2
     * @param formatString2
     * @return
     */
    public static int compareData(String dateStr1, String formatString1, String dataStr2, String formatString2) {
        Date date1 = stringToDate(dateStr1, formatString1, null);
        Date date2 = stringToDate(dataStr2, formatString2, null);
        if (date1 == null && date2 == null)
            return 0;
        if (date1 == null)
            return -1;
        if (date2 == null)
            return 1;
        return date1.compareTo(date2);

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
        if (formatString == null || formatString.equals(""))
            formatString = "yyyy-MM-dd HH:mm:ss";
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
     * @param strdate     日期字符串
     * @param beferformat 日期格式
     * @param afterformat 结果日期格式
     * @return
     */
    public static String changeDateFormat(String strdate, String beferformat,
                                          String afterformat) {
        Date date = new Date();
        String afterStr = null;
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(beferformat);
            date = (Date) sdf.parse(strdate);
            SimpleDateFormat after = new SimpleDateFormat(afterformat);
            afterStr = after.format(date);
            // System.out.println(afterStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return afterStr;
    }
}
