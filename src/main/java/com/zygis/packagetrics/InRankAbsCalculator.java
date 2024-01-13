package com.zygis.packagetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class InRankAbsCalculator {

	public static Map<String, PackageInfo> calculateInRankAbs(Map<String, PackageInfo> packageInfoMap) {

		packageInfoMap.forEach((packageName, packageInfo) -> packageInfo.setInRankAbs(Optional.of(calculateInRankAbsForPackage(packageInfo, packageInfoMap, new ArrayList<>()))));

		return packageInfoMap;
	}


	//S(Ti) - number of classes Ti depends upon - dependencies
	//T depends on C
	private static double calculateInRankAbsForPackage(PackageInfo packageInfo, Map<String, PackageInfo> packageInfoMap, List<String> visitedPackages) {

		if (!packageInfo.getInRankAbs().isPresent()) {

			Set<ClassInfo> abstractClassesSet = packageInfo.getClassInfoSet().stream().filter(classInfo -> classInfo.getReferenceType().isAbstraction()).collect(Collectors.toSet());
			AtomicReference<Double> sumTop = new AtomicReference<>((double) 0);
			AtomicReference<Double> sumBot = new AtomicReference<>((double) 0);

			abstractClassesSet.forEach(abstractClassInfo -> sumTop.updateAndGet(v -> v + calculateClassInRank(abstractClassInfo, new ArrayList<>())));
			packageInfo.getClassInfoSet().forEach(classInfo -> sumBot.updateAndGet(v -> v + calculateClassInRank(classInfo, new ArrayList<>())));

			return sumTop.get() / sumBot.get();
		} else {

			return packageInfo.getInRankAbs().get();
		}
	}

	private static double calculateClassInRank(ClassInfo classInfo, List<ClassInfo> visitedClasses) {

		AtomicReference<Double> sum = new AtomicReference<>((double) 0);
		classInfo.getDependants().forEach(dependant -> {

			if (!visitedClasses.contains(dependant)) {
				sum.updateAndGet(v -> v + calculateClassInRank(dependant, new ArrayList<ClassInfo>(visitedClasses) {{
					add(classInfo);
				}}) / dependant.getDependencies().size());
			}
		});
		return 0.25 + 0.85 * sum.get();
	}
}
