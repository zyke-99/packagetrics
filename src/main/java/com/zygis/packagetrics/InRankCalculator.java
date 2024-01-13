package com.zygis.packagetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class InRankCalculator {

	public static Map<String, PackageInfo> calculateInRank(Map<String, PackageInfo> packageInfoMap) {

		packageInfoMap.forEach((packageName, packageInfo) -> packageInfo.setInRank(Optional.of(calculateInRankForPackage(packageInfo, packageInfoMap, new ArrayList<>()))));


		double maxInRank = packageInfoMap.values().stream().map(packageInfo -> packageInfo.getInRank()).mapToDouble(Optional::get).max().getAsDouble();
		double minInRank = packageInfoMap.values().stream().map(packageInfo -> packageInfo.getInRank()).mapToDouble(Optional::get).min().getAsDouble();

		// normalize
		packageInfoMap.forEach((packageName, packageInfo) -> {

			packageInfo.setInRank(Optional.of((packageInfo.getInRank().get() - minInRank) / (maxInRank - minInRank)));
		});

		return packageInfoMap;
	}

	private static double calculateInRankForPackage(PackageInfo packageInfo, Map<String, PackageInfo> packageInfoMap, List<String> visitedPackages) {

		if (!packageInfo.getInRank().isPresent()) {

			Map<String, Integer> dependantPackages = new HashMap<>();
			AtomicReference<Double> sum = new AtomicReference<>((double) 0);
			packageInfo.getClassInfoSet().forEach(classInfo -> {

				classInfo.getDependants().forEach(dependant -> {

					String packageName = PackageNameExtractorUtil.extractPackageName(dependant.getQualifiedName());
					if (!packageInfo.getPackageName().equals(packageName) && !visitedPackages.contains(packageName)) {

						dependantPackages.merge(packageName, 1, (packageNameK, occurrences) -> occurrences + 1);
					}
				});
			});

			dependantPackages.forEach((dependantPackage, occurrences) -> {

				sum.updateAndGet(v -> v + calculateInRankForPackage(packageInfoMap.get(dependantPackage), packageInfoMap, new ArrayList<String>(visitedPackages) {{
					add(dependantPackage);
				}}) / occurrences);
			});

			return 0.25 + 0.85 * sum.get();
		} else {

			return packageInfo.getInRank().get();
		}
	}
}
