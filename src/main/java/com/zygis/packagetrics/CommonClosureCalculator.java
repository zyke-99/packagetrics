package com.zygis.packagetrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CommonClosureCalculator {

	public static Map<String, PackageInfo> calculateCommonClosure(Map<String, PackageInfo> packageInfoMap) {

		packageInfoMap.forEach((packageName, packageInfo) ->  {
			packageInfo.setCommonClosure(Optional.of(calculateCommonClosureForPackage(packageInfo, packageInfoMap)));
		});

		return packageInfoMap;
	}

	private static double calculateCommonClosureForPackage(PackageInfo packageInfo, Map<String, PackageInfo> packageInfoMap) {

		Map<ClassInfo, Set<PackageInfo>> classReachablePackageMap = new HashMap<>();
		packageInfo.getClassInfoSet().forEach(classInfo -> {

			classInfo.getDependants().stream()
					.filter(dependant -> !PackageNameExtractorUtil.extractPackageName(dependant.getQualifiedName()).equals(packageInfo.getPackageName()))
					.findFirst()
					.ifPresent(otherPackageDependant -> {
						Set<PackageInfo> reachablePackages = getReachablePackages(classInfo, packageInfoMap, new HashSet<>());
						// Same package considered to be self-reachable
						reachablePackages.add(packageInfoMap.get(PackageNameExtractorUtil.extractPackageName(classInfo.getQualifiedName())));
						classReachablePackageMap.put(classInfo, reachablePackages);
					});
		});

		Set<PackageInfo> intersection = SetUtil.findIntersection(classReachablePackageMap);
		Set<PackageInfo> union = SetUtil.findUnion(classReachablePackageMap);
		return ((double) intersection.size() + 1) / ((double) union.size() + 1);
	}

	private static Set<PackageInfo> getReachablePackages(ClassInfo classInfo, Map<String, PackageInfo> packageInfoMap, Set<ClassInfo> visitedClasses) {

		Set<PackageInfo> reachablePackages = new HashSet<>();
		if (visitedClasses.contains(classInfo)) {
			// Prevent infinite loop by avoiding re-visiting the same class
			return reachablePackages;
		}
		visitedClasses.add(classInfo);

		classInfo.getDependencies()
				.forEach(dependency -> {

					reachablePackages.add(packageInfoMap.get(PackageNameExtractorUtil.extractPackageName(dependency.getQualifiedName())));
					reachablePackages.addAll(getReachablePackages(dependency, packageInfoMap, visitedClasses));
				});

		return reachablePackages;
	}
}
