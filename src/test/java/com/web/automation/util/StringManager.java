package com.web.automation.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {

	public static String substringByRegex(String string, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		String matcherStr = "";
		while (matcher.find()) {
			if (!matcher.group().equals("")) {
				matcherStr = matcher.group();
				break;
			}
		}
		return matcherStr;
	}

	public static String substringByRegexMultiline(String string, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(string);
		String matcherStr = "";
		while (matcher.find()) {
			if (!matcher.group().equals("")) {
				matcherStr = matcher.group();
				break;
			}
		}
		return matcherStr;
	}

	public static List<String> getListMatcherByRegex(String string, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(string);
		List<String> matcherList = new LinkedList<String>();
		while (matcher.find()) {
			if (!matcher.group().isEmpty()) {
				matcherList.add(matcher.group());
			}
		}
		return matcherList;
	}

	public static boolean isListAlphabeticallyOrdered(String[] arrayStringList) {
		boolean alphabeticallyOrdered = true;
		int index = 0;
		for (String estado : arrayStringList) {
			if (index < arrayStringList.length - 1) {
				if (estado.compareTo(arrayStringList[index + 1]) > 0) {
					alphabeticallyOrdered = false;
					break;
				}
			}
			index++;
		}
		return alphabeticallyOrdered;
	}

	public static boolean isListAlphabeticallyOrdered(List<String> arrayStringList) {
		boolean alphabeticallyOrdered = true;
		int index = 0;
		for (String estado : arrayStringList) {
			if (index < arrayStringList.size() - 1) {
				if (estado.compareTo(arrayStringList.get(index + 1)) > 0) {
					alphabeticallyOrdered = false;
					break;
				}
			}
			index++;
		}
		return alphabeticallyOrdered;
	}
}
