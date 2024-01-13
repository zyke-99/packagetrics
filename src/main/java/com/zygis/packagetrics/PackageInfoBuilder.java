package com.zygis.packagetrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageInfoBuilder {

	public static Map<String, PackageInfo> buildPackageMap(List<ClassInfo> classInfoList) {

		Map<String, PackageInfo> packageInfoMap = new HashMap<>();
		classInfoList.forEach(classInfo -> {

			String packageName = PackageNameExtractorUtil.extractPackageName(classInfo.getQualifiedName());
			packageInfoMap.computeIfPresent(packageName, (k, v) -> {
				v.addClassInfo(classInfo);
				return v;
			});
			packageInfoMap.computeIfAbsent(packageName, k -> {
				PackageInfo packageInfo = new PackageInfo(k);
				packageInfo.addClassInfo(classInfo);
				return packageInfo;
			});
		});

		return packageInfoMap;
	}
}
