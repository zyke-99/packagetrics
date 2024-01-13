package com.zygis.packagetrics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetUtil {

	public static <K, V> Set<V> findIntersection(Map<K, Set<V>> map) {

		if (map.values().isEmpty()) {

			return new HashSet<>();
		}

		// Start with the first set as the initial intersection
		Set<V> intersection = new HashSet<>(map.values().iterator().next());

		// Iterate over the remaining sets and retain the common elements
		for (Set<V> set : map.values()) {
			intersection.retainAll(set);
		}

		return intersection;
	}

	public static <K, V> Set<V> findUnion(Map<K, Set<V>> map) {

		// Start with the first set as the initial intersection
		Set<V> union = new HashSet<>();

		// Iterate over the remaining sets and retain the common elements
		for (Set<V> set : map.values()) {
			union.addAll(set);
		}

		return union;
	}
}
