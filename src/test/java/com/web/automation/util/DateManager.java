package com.web.automation.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateManager {
	

	private static DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static LocalDate currentDate = LocalDate.now();
	
	
	public static String convertToPatternDate(String value) throws Exception {
		if (value.contains("atual+")) {
			String[] values = value.split(Pattern.quote("+"));
			String daysToAdd = values[1].replace("d", "");
			value = currentDate.plusDays(Integer.parseInt(daysToAdd)).format(pattern1).toString();
		} else if (value.contains("atual-")) {
			String[] values = value.split(Pattern.quote("-"));
			String daysToSubtract = values[1].replace("d", "");
			value = currentDate.minusDays(Integer.parseInt(daysToSubtract)).format(pattern1).toString();
		} else if (value.equalsIgnoreCase("hoje")) {
			value = currentDate.format(pattern1).toString();
		} else if (value.equalsIgnoreCase("ontem")) {
			value = currentDate.minusDays(1).format(pattern1).toString();
		} else {
			throw new Exception("Invalid date!");
		}
		
		return value;
	}
	

}
