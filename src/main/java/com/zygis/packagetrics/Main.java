package com.zygis.packagetrics;

import static com.zygis.packagetrics.ClassInfoExtractor.extractClassInfo;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {

		printPackageStructure();

		try {
//			String path = "/Users/zygimantas.kersys@mambu.com/work/rankingtest/src/com/zygis";
//			String dotPath = "/Users/zygimantas.kersys@mambu.com/Desktop/zygis.dot";
			String path = "/Users/zygimantas.kersys@mambu.com/work/data-loader/src/main/java/com/mambu/dataloader";
			String dotPath = "/Users/zygimantas.kersys@mambu.com/Desktop/uni_final/package_by_feature/dataloader.dot";
			Map something = extractClassInfo(path);
//			System.out.println(something);
			List<ClassInfo> classInfoList = ClassInfoDotDependencyEnhancer.classInfoEnhancer(dotPath, "dataloader", something);
//			classInfoList.forEach(classInfo -> System.out.println(classInfo));
			Map<String, PackageInfo> packageInfoMap = PackageInfoBuilder.buildPackageMap(classInfoList);
			packageInfoMap = OutRankCalculator.calculateOutRank(packageInfoMap);
			packageInfoMap = InRankCalculator.calculateInRank(packageInfoMap);
			packageInfoMap = InRankAbsCalculator.calculateInRankAbs(packageInfoMap);
			packageInfoMap = CommonReuseCalculator.calculateCommonReuse(packageInfoMap);
			packageInfoMap = CommonClosureCalculator.calculateCommonClosure(packageInfoMap);
			FinalResultPrinter.printFinalResults(packageInfoMap);
//			packageInfoMap.forEach((k,packageInfo) -> System.out.println(packageInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printPackageStructure() throws IOException {
		// Replace "your/project/path" with the actual path to your Java project
		String projectPath = "/Users/zygimantas.kersys@mambu.com/work/data-loader/src/main/java/com/mambu";

		Path rootPath = Paths.get(projectPath);

		if (!Files.exists(rootPath) || !Files.isDirectory(rootPath)) {
			System.out.println("Invalid project path");
			return;
		}

		System.out.println("Package structure:");

		Files.walkFileTree(rootPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
			private int rootPathNameCount;

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (rootPathNameCount == 0) {
					rootPathNameCount = rootPath.getNameCount();
				}

				int indentLevel = dir.getNameCount() - rootPathNameCount;

				if (indentLevel > 0) {
					String packageName = dir.getFileName().toString();
					String indentation = getIndentation(indentLevel - 1);
					System.out.println(indentation + "├─ " + packageName);
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static String getIndentation(int indentLevel) {
		StringBuilder indentation = new StringBuilder();
		for (int i = 0; i < indentLevel; i++) {
			indentation.append("│  "); // Use │ for vertical line
		}
		return indentation.toString();
	}
}
