package com.zygis.packagetrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class OutRankCalculator {

	public static Map<String, PackageInfo> calculateOutRank(Map<String, PackageInfo> packageInfoMap) {

		packageInfoMap.forEach((packageName, packageInfo) -> packageInfo.setOutRank(Optional.of(calculateOutRankForPackage(packageInfo, packageInfoMap, new ArrayList<>()))));

		double maxOutRank = packageInfoMap.values().stream().map(packageInfo -> packageInfo.getOutRank()).mapToDouble(Optional::get).max().getAsDouble();
		double minOutRank = packageInfoMap.values().stream().map(packageInfo -> packageInfo.getOutRank()).mapToDouble(Optional::get).min().getAsDouble();

		// normalize
		packageInfoMap.forEach((packageName, packageInfo) -> {

			packageInfo.setOutRank(Optional.of((packageInfo.getOutRank().get() - minOutRank) / (maxOutRank - minOutRank)));
		});

		return packageInfoMap;
	}

	private static double calculateOutRankForPackage(PackageInfo packageInfo, Map<String, PackageInfo> packageInfoMap, List<String> visitedPackages) {

		if (!packageInfo.getOutRank().isPresent()) {

			Set<String> dependencyPackages = new HashSet<>();
			AtomicReference<Double> sum = new AtomicReference<>((double) 0);
			packageInfo.getClassInfoSet().forEach(classInfo -> {

				classInfo.getDependencies().forEach(dependencyClass -> {

					String packageName = PackageNameExtractorUtil.extractPackageName(dependencyClass.getQualifiedName());
					if (!packageInfo.getPackageName().equals(packageName) && !visitedPackages.contains(packageName)) {

						dependencyPackages.add(packageName);
					}
				});
			});

			dependencyPackages.forEach(dependencyPackage -> {

				sum.updateAndGet(v -> v + calculateOutRankForPackage(packageInfoMap.get(dependencyPackage), packageInfoMap, new ArrayList<String>(visitedPackages) {{ add(dependencyPackage); }}));
			});

			return 0.25 + 0.85* sum.get();
		} else {

			return packageInfo.getOutRank().get();
		}
	}
}
