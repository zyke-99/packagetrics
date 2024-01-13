package com.zygis.packagetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonReuseCalculator {

	public static Map<String, PackageInfo> calculateCommonReuse(Map<String, PackageInfo> packageInfoMap) {

		packageInfoMap.forEach((packageName, packageInfo) ->  {
			packageInfo.setCommonReuse(Optional.of(calculateCommonReuseForPackage(packageInfo)));
		});

		return packageInfoMap;
	}

	private static double calculateCommonReuseForPackage(PackageInfo packageInfo) {

		Map<ClassInfo, Set<ClassInfo>> inInterfaceHubMap = new HashMap<>();
		packageInfo.getClassInfoSet().forEach(classInfo -> {

			classInfo.getDependants().stream()
					.filter(dependant -> !PackageNameExtractorUtil.extractPackageName(dependant.getQualifiedName()).equals(packageInfo.getPackageName()))
					.findFirst()
					.ifPresent(otherPackageDependant -> {
						Set<ClassInfo> samePackageClasses = packageInfo.getClassInfoSet().stream()
								.collect(Collectors.toSet());
						Set<ClassInfo> reachableClasses = getReachableClassesInSamePackage(classInfo, samePackageClasses, new HashSet<>());
						// Same class considered to be self-reachable
						reachableClasses.add(classInfo);
						inInterfaceHubMap.put(classInfo, reachableClasses);
					});
		});

		Set<ClassInfo> intersection = SetUtil.findIntersection(inInterfaceHubMap);
		return (double) intersection.size() / (double) packageInfo.getClassInfoSet().size();
	}

	private static Set<ClassInfo> getReachableClassesInSamePackage(ClassInfo classInfo, Set<ClassInfo> samePackageClasses, Set<ClassInfo> visitedClasses) {

		Set<ClassInfo> reachableClasses = new HashSet<>();
		if (visitedClasses.contains(classInfo)) {
			// Prevent infinite loop by avoiding re-visiting the same class
			return reachableClasses;
		}
		visitedClasses.add(classInfo);

		classInfo.getDependencies()
				.stream().filter(samePackageClasses::contains)
				.forEach(samePackageDependency -> {

					reachableClasses.add(samePackageDependency);
					reachableClasses.addAll(getReachableClassesInSamePackage(samePackageDependency, samePackageClasses, visitedClasses));
				});
//		classInfo.getDependants()
//				.forEach(dependant -> {
//
//					if (samePackageClasses.contains(dependant)) {
//
//						reachableClasses.add(dependant);
//					} else {
//
//						System.out.println(visitedClasses.stream().map(ClassInfo::getQualifiedName).collect(Collectors.toSet()));
//						//System.out.println(classInfo.getQualifiedName() + " going deeper for dependant " + dependant.getQualifiedName());
//						reachableClasses.addAll(getReachableClassesInSamePackage(dependant, samePackageClasses, visitedClasses));
//					}
//				});

		return reachableClasses;
	}
}
