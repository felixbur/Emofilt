package com.felix.util;

/**
 * Some methods around array calculations.
 * 
 * @author felix
 * 
 */
public class ArrayUtil {
	/**
	 * Return percentage value from fracture.
	 * 
	 * @param nom
	 * @param den
	 * @return Tpercentage value from fracture.
	 */
	public static int percent(int nom, int den) {
		return (int) Math.round(100 * ((double) nom / den));
	}

	/**
	 * Return sum of the ith row.
	 * 
	 * @param a
	 * @param rowIndex
	 * @return The sum of the ith row.
	 */
	public static int rowSum(int[][] a, int rowIndex) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (i == rowIndex) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return sum of values in a sub matrix.
	 * 
	 * @param a
	 * @param startCol
	 * @param endCol
	 * @param startRow
	 * @param endRow
	 * @return The sum of values in a sub matrix.
	 */
	public static int subMatrixSum(int[][] a, int startCol, int endCol,
			int startRow, int endRow) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (i >= startRow && i <= endRow && j >= startCol
						&& j <= endCol) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return a submatrix for a given matrix.
	 * 
	 * @param a
	 * @param startCol
	 * @param endCol
	 * @param startRow
	 * @param endRow
	 * @return A submatrix for a given matrix.
	 */
	public static int[][] subMatrix(int[][] a, int startCol, int endCol,
			int startRow, int endRow) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int[][] newAarry = new int[endRow - startRow + 1][endCol - startCol + 1];
		int newRow = 0, newCol = 0;
		for (int i = 0; i < rowNum; i++) {
			newCol = 0;
			for (int j = 0; j < colNum; j++) {
				if (i >= startRow && i <= endRow && j >= startCol
						&& j <= endCol) {
					newAarry[newRow][newCol] = a[i][j];
					newCol++;
				}
			}
			if (i >= startRow && i <= endRow) {
				newRow++;
			}
		}
		return newAarry;
	}

	/**
	 * Return the sum of a part of the ith row.
	 * 
	 * @param a
	 * @param rowIndex
	 * @param startCol
	 * @param endCol
	 * @return The sum of a part of the ith row.
	 */
	public static int partOfRowSum(int[][] a, int rowIndex, int startCol,
			int endCol) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (i == rowIndex && j >= startCol && j <= endCol) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return sum of the ith column.
	 * 
	 * @param a
	 * @param colIndex
	 * @return The sum of the ith column.
	 */
	public static int colSum(int[][] a, int colIndex) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (j == colIndex) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return sum of part of the ith column.
	 * 
	 * @param a
	 * @param colIndex
	 * @param startRow
	 * @param endRow
	 * @return The sum of part of the ith column.
	 */
	public static int partOfColSum(int[][] a, int colIndex, int startRow,
			int endRow) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (j == colIndex && i >= startRow && i <= endRow) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return the sum of a one dimensional array.
	 * 
	 * @param a
	 * @return The sum of a one dimensional array.
	 */
	public static int sum(int[] a) {
		int rowNum = a.length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Return the sum of all values in an array.
	 * 
	 * @param a
	 * @return The sum of all values in an array.
	 */
	public static int sum(int[][] a) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				sum += a[i][j];
			}
		}
		return sum;
	}

	/**
	 * Return the sum of all diagonal (i==j) elements.
	 * 
	 * @param a
	 * @return The sum of all diagonal (i==j) elements.
	 */
	public static int diagSum(int[][] a) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (i == j) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Return the sum of all elements except diagonal (i==j) ones.
	 * 
	 * @param a
	 * @return The sum of all elements except diagonal (i==j) ones.
	 */
	public static int noDiagSum(int[][] a) {
		int rowNum = a.length;
		int colNum = a[0].length;
		int sum = 0;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (i != j) {
					sum += a[i][j];
				}
			}
		}
		return sum;
	}

	/**
	 * Get zero filled array with row number == column number.
	 * 
	 * @param rowNum
	 * @return A zero filled array with row number == column number.
	 */
	public static int[][] getZeroQuadraticArray(int rowNum) {
		return getZeroArray(rowNum, rowNum);
	}

	/**
	 * Get zero filled array.
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return A zero filled array.
	 */
	public static int[][] getZeroArray(int rowNum, int colNum) {
		int[][] retA = new int[rowNum][colNum];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				retA[i][j] = 0;
			}
		}
		return retA;
	}

	/**
	 * Get formatted string representation.
	 * 
	 * @param a
	 * @return A formatted string representation.
	 */
	public static String toString(int[][] a) {
		int rowNum = a.length;
		int colNum = a[0].length;
		String ret = "";
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (colNum == 0) {
					ret += "\n";
				}
				ret += "\t" + a[i][j];
			}
		}
		return ret;
	}

	/**
	 * Get a formatted String version for a confusion matrix.
	 * 
	 * @param a
	 *            The matrix.
	 * @param cats
	 *            The names of categories (MUST be same dimension).
	 * @return A formatted String version for a confusion matrix.
	 */
	public static String toStringConfMatrix(int[][] a, String[] cats) {
		int rowNum = a.length;
		int colNum = a[0].length;
		String ret = "r\\p\t";
		for (int i = 0; i < cats.length; i++) {
			ret += cats[i] + "\t";
		}
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (j == 0) {
					ret += "\n" + cats[i];
				}
				ret += "\t" + a[i][j];
			}
		}
		return ret;
	}

	/**
	 * Get a formatted String version for a confusion matrix for relative
	 * values.
	 * 
	 * @param a
	 *            The matrix.
	 * @param cats
	 *            The names of categories (MUST be same dimension).
	 * @return A formatted String version for a confusion matrix for relative
	 * values.
	 */
	public static String toStringRelativeConfMatrix(int[][] a, String[] cats) {
		int rowNum = a.length;
		int colNum = a[0].length;
		String ret = "r\\p\t";
		for (int i = 0; i < cats.length; i++) {
			ret += cats[i] + "\t";
		}
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (j == 0) {
					ret += "\n" + cats[i];
				}
				ret += "\t" + percent(a[i][j], rowSum(a, i));
			}
		}
		return ret;
	}
}
