package com.github.wongph.automated.shell.execution.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class FormattingUtils {
	
	
	public static String getLastNonEmptyLine(String str) throws Exception {
		String result = null;
		if(str == null) {
			return str;
		}
		// get the index of last new line character
		int startIndex = StringUtils.lastIndexOf(str, "\n");

		// see if its valid index then just substring it to the end from that
		if(startIndex < 0 || startIndex == str.length()) {
			return str;
		}
		
		if(startIndex!=-1 && startIndex!= str.length()){
			result = str.substring(startIndex+1);
			if(StringUtils.isBlank(result)) {
				
			}
		}
		
		throw new Exception("Error in getting the last line of string");
	}
	
	public static String getLastNonBlankLine(String str) throws Exception {
		String[] lines = StringUtils.split(str, "\n");
	    for (int i = lines.length - 1; i >= 0; i--) {
	        if (!StringUtils.isBlank(lines[i])) {
	            return StringUtils.trim(lines[i]);
	        }
	    }
	    throw new Exception("Error in getting the last line of string");
	}
	
	public static List<String> getJMSRunCommands(String str) {
		List<String> runCommands = new ArrayList<>();
		String[] lines = StringUtils.split(str, "\n");
		for (int i = 0; i < lines.length; i++) {
			if(StringUtils.startsWith(lines[i], "nohup")) {
				runCommands.add(StringUtils.trim(lines[i]));
			}
		}
		
		return runCommands;
	}
	
	public static boolean containsKeyWordsInLastNLines(String str, int NumberofLine, String... keywords) {
		String[] lines = StringUtils.split(str, "\n");
		if(lines.length < NumberofLine) {
			NumberofLine = lines.length;
		}
		int endOfSearch = lines.length - NumberofLine;
		for (int i = lines.length - 1; i >= endOfSearch; i--) {
			if(StringUtils.containsAny(lines[i], keywords)) {
				return true;
			}	
		}
		return false;
		
	}
}
