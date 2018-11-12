package com.felix.util;

public class VectorMath {

	public static double[] computeDeltaVector(double[] values, int area) {
		double ret[] = new double[values.length + 2 * area];
		int j = 0;
		for (int i = area; i < ret.length - area; i++) {
			for (int k = 0; k < area; k++) {

			}
		}
		return null;
	}
	/**
	 * Expand or compress a series of integer values dependent on their location
	 * relative to mean value.
	 * 
	 * @param vals
	 *            The integer values.
	 * @param rate
	 *            The rate of displacement in percent (100 no change, values >
	 *            100 expand, values y 100 compress).
	 * @param mean
	 *            The central value for displacement.
	 * @return The displaced vector.
	 */
	public static int[] changeContrast(int vals[], int rate, double mean) {
		int newVal;
		int retVals[] = new int[vals.length];
		double Rate = (rate - 100) / 100.0;
		for (int i = 0; i < vals.length; i++) {
			// distance between pitchVal and Mean in %
			if (vals[i] != 0) {
				double dist = (double) vals[i] - mean;
				if (Rate > 0) { // enhance range
					retVals[i] = vals[i] + (int) (dist * Rate);
				} else { // reduce range
					if (vals[i] > mean) {
						if ((newVal = vals[i] + (int) (dist * Rate)) < mean) {
							retVals[i] = (int) mean;
						} else {
							retVals[i] = newVal;
						}
					} else {
						if ((newVal = vals[i] + (int) (dist * Rate)) > mean) {
							retVals[i] = (int) mean;
						} else {
							retVals[i] = newVal;
						}
					}

				}
			}
		}
		return retVals;
	}

	/**
	 * Assuming a linear interpolation calculate the next step for a series of
	 * values.
	 * 
	 * @param start
	 *            The start value of the series.
	 * @param end
	 *            The end value of the series.
	 * @param stepIndex
	 *            The index of the step value to compute.
	 * @param stepNum
	 *            The number of steps.
	 * @return The new value.
	 */
	public static int calcNextStep(int start, int end, int stepIndex,
			int stepNum) {
		int diff = end - start;
		double stepSize = (double) diff / (double) (stepNum + 1);
		return start + (int) (stepSize * stepIndex);
	}

	/**
	 * Given an array of integers, compute a series of integers as a linear
	 * interpolation between two values.
	 * 
	 * @param vec
	 *            The array that gives number of values.
	 * @param start
	 *            The start value for the linear interpolation.
	 * @param end
	 *            The end value for the linear interpolation.
	 * @return A new array containing the interpolated values.
	 */
	public static int[] interpolateLinear(int[] vec, int start, int end) {
		double step;
		int length = vec.length;
		int ret[] = new int[vec.length];
		if (start < end) {
			step = (double) (end - start) / (double) length;
			for (int i = 0; i < length; i++) {
				if (vec[i] != 0) {
					ret[i] = start + (int) (i * step);
				}
			}
		} else if (start > end) {
			step = (double) (start - end) / (double) length;
			for (int i = 0; i < length; i++)
				if (vec[i] != 0)
					ret[i] = start - (int) (i * step);
		} else
			for (int i = 0; i < length; i++)
				if (vec[i] != 0)
					ret[i] = start;
		return ret;
	}

}
