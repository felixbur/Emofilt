package com.felix.util;

public class NLPUtil {
	public static double computeWER(int total, int substitutions,
			int deletions, int insertions) {
		return ((double) substitutions + deletions + insertions) / total;
	}
}
