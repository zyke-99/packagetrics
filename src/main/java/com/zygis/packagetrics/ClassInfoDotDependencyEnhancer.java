package com.zygis.packagetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ClassInfoDotDependencyEnhancer {

	public static List<ClassInfo> classInfoEnhancer(String filePath, String mainPackage, Map<String, ClassInfo> classInfoMap) throws IOException {
		List<ClassInfo> enhancedClassInfos = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			// Define a regular expression pattern
			Pattern pattern = Pattern.compile("\"(.*?)\"\\s+->\\s+\"(.*?)\\s+\\((.*?)\\)\"");

			// Iterate through lines and print the class names and dependencies
			lines.forEach(line -> {
				// Create a matcher with the pattern
				Matcher matcher = pattern.matcher(line);

				// Check if the line matches the pattern
				if (matcher.find()) {
					String className = matcher.group(1).replaceAll("\\$.+", "");
					String dependencyName = matcher.group(2).replaceAll("\\$.+", "");
					String dependencyPackage = matcher.group(3);

					// Check if the dependency package is relevant
					if (mainPackage.equals(dependencyPackage) && !className.equals(dependencyName)) {

//						System.out.println(className);
						classInfoMap.get(className).getDependencies().add(classInfoMap.get(dependencyName));
						classInfoMap.get(dependencyName).getDependants().add(classInfoMap.get(className));
					}
				}
			});
		}
		return new ArrayList<>(classInfoMap.values());
	}
}
