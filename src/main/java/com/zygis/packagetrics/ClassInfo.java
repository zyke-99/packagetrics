package com.zygis.packagetrics;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassInfo {

	private String qualifiedName;
	private ReferenceType referenceType;
	private Set<ClassInfo> dependencies;
	private Set<ClassInfo> dependants;

	public ClassInfo(String qualifiedName, ReferenceType referenceType) {

		this.qualifiedName = qualifiedName;
		this.referenceType = referenceType;
		this.dependencies = new HashSet<>();
		this.dependants = new HashSet<>();
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public Set<ClassInfo> getDependencies() {
		return dependencies;
	}

	public Set<ClassInfo> getDependants() {
		return dependants;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClassInfo classInfo = (ClassInfo) o;
		return qualifiedName.equals(classInfo.qualifiedName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(qualifiedName);
	}

	@Override
	public String toString() {
		return "ClassInfo{" +
				"qualifiedName='" + qualifiedName + '\'' +
				", referenceType=" + referenceType +
				", dependencies=" + dependencies.stream().map(ClassInfo::getQualifiedName).collect(Collectors.toList()) +
				", dependants=" + dependants.stream().map(ClassInfo::getQualifiedName).collect(Collectors.toList()) +
				'}';
	}
}
