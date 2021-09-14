package com.web.automation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class TextManager {
  /**
   * Transform a string in a template for screenshots
   * 
   * @param text
   *          any string
   * @return adapted string
   */
  static String stringTreatment(String text) {
    return StringUtils.stripAccents(text).replace(" ", "_").replace("/", "-").replace(":", "").toLowerCase();
  }
  
  /**
   * Return the value of a String after regex
   * 
   * @param text
   *          any string
   * @param regex
   *          regex used to adapt the string
   * @return adapted string
   */
  public static String returnRegex(String text, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group(1) : "";
  }
}
