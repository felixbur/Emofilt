package com.felix.util;

/**
 * Methods around statistic calculations.
 * 
 * @author felix
 * 
 */
public class StatsUtil {
	/**
	 * Return the f1 value.
	 * 
	 * @param recall
	 * @param precision
	 * @return
	 */
	public static double f1(double recall, double precision) {
		return (2 * recall * precision) / (precision + recall);
	}
}
