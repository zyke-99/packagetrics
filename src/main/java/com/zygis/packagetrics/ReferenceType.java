package com.zygis.packagetrics;

import java.util.Arrays;
import java.util.List;

public enum ReferenceType {

	CLASS,
	ABSTRACT_CLASS,
	INTERFACE,
	RECORD,
	ENUM;

	private static final List<ReferenceType> ABSTRACTIONS = Arrays.asList(INTERFACE, ABSTRACT_CLASS);

	public boolean isAbstraction() {

		return ABSTRACTIONS.contains(this);
	}
}
