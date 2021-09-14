package com.web.automation.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalculator {
  private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
  
  /**
   * Get current date
   *
   * @return current date
   */
  public static Date getCurrentDate() {
    Calendar date = new GregorianCalendar();
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    return date.getTime();
  }
  
  /**
   * Get a new date using the actual date plus a number of days with pattern
   * passed as parameter
   *
   * @param days
   *          number of the days counting from today
   * @return corresponding date after calculation
   */
  public static String getDate(SimpleDateFormat sdfDate, int days) {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DATE, days);
    return sdfDate.format(calendar.getTime());
  }
  
  /**
   * Get a new date using the actual date plus a number of days with pattern
   * dd/MM/yyyy
   *
   * @param days
   *          number of the days counting from today
   * @return corresponding date after calculation
   */
  public static String getDate(int days) {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DATE, days);
    return df.format(calendar.getTime());
  }
  
  /**
   * Format a date using characters
   *
   * @param date
   *          date to be formatted
   * @return corresponding date after formatation
   */
  public static String formatDate(String date) {
    String[] array = date.split("/");
    switch (Integer.parseInt(array[1])) {
      case 1:
        array[1] = "Jan";
        break;
      case 2:
        array[1] = "Feb";
        break;
      case 3:
        array[1] = "Mar";
        break;
      case 4:
        array[1] = "Apr";
        break;
      case 5:
        array[1] = "May";
        break;
      case 6:
        array[1] = "Jun";
        break;
      case 7:
        array[1] = "Jul";
        break;
      case 8:
        array[1] = "Aug";
        break;
      case 9:
        array[1] = "Sep";
        break;
      case 10:
        array[1] = "Oct";
        break;
      case 11:
        array[1] = "Nov";
        break;
      case 12:
        array[1] = "Dec";
        break;
    }
    date = array[0] + "-" + array[1] + "-" + array[2];
    return date;
  }
}