package com.zygis.packagetrics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageNameExtractorUtil {

	public static String extractPackageName(String qualifiedName) {

		// Define a regular expression pattern
		Pattern pattern = Pattern.compile("^(.*?\\.)[^.]+$");

		// Create a matcher with the pattern
		Matcher matcher = pattern.matcher(qualifiedName);

		// Check if the matcher finds a match
		if (matcher.find()) {
			return matcher.group(1).substring(0, matcher.group(1).length() - 1);
		} else {
			return ""; // No match found
		}
	}
}
