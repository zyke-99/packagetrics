package com.zygis.packagetrics;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class PackageInfo {

	private String packageName;
	private Set<ClassInfo> classInfoSet;
	private Optional<Double> inRank;
	private Optional<Double> outRank;
	private Optional<Double> inRankAbs;
	private Optional<Double> commonReuse;
	private Optional<Double> commonClosure;

	public PackageInfo(String packageName) {

		this.packageName = packageName;
		this.classInfoSet = new HashSet<>();
		this.inRank = Optional.empty();
		this.outRank = Optional.empty();
		this.inRankAbs = Optional.empty();
		this.commonReuse = Optional.empty();
		this.commonClosure = Optional.empty();
	}

	public void addClassInfo(ClassInfo classInfo) {

		classInfoSet.add(classInfo);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Set<ClassInfo> getClassInfoSet() {
		return classInfoSet;
	}

	public void setClassInfoSet(Set<ClassInfo> classInfoSet) {
		this.classInfoSet = classInfoSet;
	}

	public Optional<Double> getInRank() {
		return inRank;
	}

	public void setInRank(Optional<Double> inRank) {
		this.inRank = inRank;
	}

	public Optional<Double> getOutRank() {
		return outRank;
	}

	public void setOutRank(Optional<Double> outRank) {
		this.outRank = outRank;
	}

	public Optional<Double> getInRankAbs() {
		return inRankAbs;
	}

	public void setInRankAbs(Optional<Double> inRankAbs) {
		this.inRankAbs = inRankAbs;
	}

	public Optional<Double> getCommonReuse() {
		return commonReuse;
	}

	public void setCommonReuse(Optional<Double> commonReuse) {
		this.commonReuse = commonReuse;
	}

	public Optional<Double> getCommonClosure() {
		return commonClosure;
	}

	public void setCommonClosure(Optional<Double> commonClosure) {
		this.commonClosure = commonClosure;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PackageInfo that = (PackageInfo) o;
		return packageName.equals(that.packageName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(packageName);
	}

	@Override
	public String toString() {
		return "PackageInfo{" +
				"packageName='" + packageName + '\'' +
				", inRank=" + inRank +
				", outRank=" + outRank +
				", inRankAbs=" + inRankAbs +
				", CR=" + commonReuse +
				", CC=" + commonClosure +
				'}';
	}
}
