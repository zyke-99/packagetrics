package com.zygis.packagetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class FinalResultPrinter {

	public static void printFinalResults(Map<String, PackageInfo> packageInfoMap) {

		List<Double> instabilityList = new ArrayList<>();
		List<Double> distanceList = new ArrayList<>();
		List<Double> cohesionList = new ArrayList<>();
		List<Double> abstractnessList =packageInfoMap.values().stream().map(packageInfo -> packageInfo.getInRankAbs().get()).collect(Collectors.toList());
		packageInfoMap.forEach((packageName, packageInfo) -> {

			double instability = getInstability(packageInfo);
			double cohesion = getCohesion(packageInfo);
			double distance = getDistanceToMainSequence(packageInfo, instability);

			instabilityList.add(instability);
			distanceList.add(distance);
			cohesionList.add(cohesion);

			System.out.printf("%s: I=%f, A=%f, D=%f CH=%f%n", packageName, instability, packageInfo.getInRankAbs().get(), distance, cohesion);
//			System.out.println("common reuse: " + packageInfo.getCommonReuse().get() + " common closure: " + packageInfo.getCommonClosure().get());
		});

		System.out.println("\nProject statistics:");
		System.out.println(String.format("Instability mean: %f, standard deviation: %f", getMean(instabilityList), getStandardDeviation(instabilityList)));
		System.out.println(String.format("Abstraction mean: %f, standard deviation: %f", getMean(abstractnessList), getStandardDeviation(abstractnessList)));
		System.out.println(String.format("Distance mean: %f standard deviation: %f", getMean(distanceList), getStandardDeviation(distanceList)));
		System.out.println(String.format("Cohesion mean: %f standard deviation: %f", getMean(cohesionList), getStandardDeviation(cohesionList)));
	}

	private static double getInstability(PackageInfo packageInfo) {

		if (packageInfo.getPackageName().equals("com.mambu.dataloader.specialfileformat.utils")) {
			System.out.println(packageInfo.getInRank());
			System.out.println(packageInfo.getOutRank());
		}
		return packageInfo.getInRank().get() == 0 && packageInfo.getOutRank().get() == 0 ? 0 : packageInfo.getOutRank().get() / (packageInfo.getOutRank().get() + packageInfo.getInRank().get());
	}

	private static double getDistanceToMainSequence(PackageInfo packageInfo, double instability) {

		return Math.abs(packageInfo.getInRankAbs().get() + instability - 1);
	}

	private static double getCohesion(PackageInfo packageInfo) {

//		System.out.println(packageInfo.getCommonClosure());
//		System.out.println(packageInfo.getCommonReuse());
		return (Math.sqrt(2) - Math.sqrt(Math.pow((1 - packageInfo.getCommonReuse().get()), 2) + Math.pow((1 - packageInfo.getCommonClosure().get()), 2))) / Math.sqrt(2);
	}

	private static double getMean(List<Double> doubleList) {

		return doubleList.stream()
				.collect(Collectors.averagingDouble(Double::doubleValue));
	}

	private static double getStandardDeviation(List<Double> doubleList) {

		double sum = 0.0, standardDeviation = 0.0;
		int length = doubleList.toArray().length;

		for(double num : doubleList) {
			sum += num;
		}

		double mean = sum/length;

		for(double num: doubleList) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation/(length));
	}
}
