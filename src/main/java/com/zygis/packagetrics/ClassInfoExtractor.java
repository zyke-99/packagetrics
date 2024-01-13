package com.zygis.packagetrics;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

public class ClassInfoExtractor {

	public static Map<String, ClassInfo> extractClassInfo(String projectPath) throws IOException {
		Map<String, ClassInfo> classInfoMap = new HashMap<>();
		Files.walkFileTree(Paths.get(projectPath), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (file.toString().endsWith(".java")) {
					parseJavaFile(file).ifPresent(classInfo -> classInfoMap.put(classInfo.getQualifiedName(), classInfo));
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return classInfoMap;
	}

	private static Optional<ClassInfo> parseJavaFile(Path filePath) {
		try {
			ParserConfiguration parserConfiguration = new ParserConfiguration();
			parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
			ParseResult<CompilationUnit> result = new JavaParser(parserConfiguration).parse(filePath);
			if (result.isSuccessful()) {
				CompilationUnit cu = result.getResult().get();
				return Optional.of(cu.accept(new ClassInfoVisitor(), null));
			} else {
				System.err.println("Parsing failed for: " + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
