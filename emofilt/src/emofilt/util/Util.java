/*
 * Created on 09.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

import org.jdom.Element;

import emofilt.ElemNotFoundException;
import emofilt.Emofilt;
import emofilt.F0Val;
import emofilt.Phoneme;

/**
 * A loose collection of utility methods.
 * 
 * @author Felix Burkhardt
 */
public class Util {
	/**
	 * Test if two phonemes form an affricate, e.g. "pf" or "tS".
	 * 
	 * @param p1
	 *            First phoneme.
	 * @param p2
	 *            Second phoneme.
	 * @return True if they form an affricate, false otherwise.
	 */
	public static boolean isAffricate(Phoneme p1, Phoneme p2) {
		if ((p1.getManner().compareTo(Phoneme.stop_voiceless) == 0 && p2
				.getManner().compareTo(Phoneme.fricative_voiceless) == 0)
				|| (p1.getManner().compareTo(Phoneme.fricative_voiceless) == 0 && p2
						.getManner().compareTo(Phoneme.stop_voiceless) == 0))
			return true;
		return false;
	}

	/**
	 * Print element of a Vector to a Printstream.
	 * 
	 * @param vec
	 *            The vector.
	 * @param out
	 *            The printStream.
	 */
	public static void printVec(Vector vec, PrintStream out) {
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			out.print(element.toString() + " ");
		}
	}

	/**
	 * Print all elements of a vector in a row.
	 * 
	 * @param vec
	 *            The vector of elements.
	 * @return The output of toString() method in a row.
	 */
	public static String printVec(Vector vec) {
		String ret = "";
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			ret += element.toString() + " ";
		}
		return ret;
	}

	/**
	 * Print all f0-values of a vector in mbrola (time,f0) syntax to a
	 * printstream.
	 * 
	 * @param vec
	 *            The vector with F0Vals.
	 * @param out
	 *            The printstream.
	 * @see F0Val
	 */
	public static void printF0ValVec(Vector vec, PrintStream out) {
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			F0Val element = (F0Val) iter.next();
			out.print("(" + element.getPos() + "," + element.getVal() + ") ");
		}
	}

	/**
	 * Print all f0-values of a vector in mbrola (time,f0) syntax.
	 * 
	 * @param vec
	 *            The vector with F0Vals.
	 * @return The output of toString() method in a row.
	 * @see F0Val
	 */
	public static String printF0ValVec(Vector vec) {
		String ret = "";
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			F0Val element = (F0Val) iter.next();
			ret += "(" + element.getPos() + "," + element.getVal() + ") ";
		}
		return ret;
	}

	/**
	 * Get the mean F0 values of a series of F0 values.
	 * 
	 * @param vec
	 *            The vector with F0Vals.
	 * @return The arithmetic mean of all values.
	 * @see F0Val
	 */
	public static double getF0Mean(Vector vec) {
		int sum = 0;
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			F0Val element = (F0Val) iter.next();
			sum += element.getVal();
		}
		return sum / vec.size();
	}

	/**
	 * Check whether a f0-value is below a constant f0-value.
	 * 
	 * @param val
	 *            The value to check
	 * @return An F0-value with value set to minValue if the value was below or
	 *         the original value.
	 * @see emofilt.Constants
	 */
	public static F0Val checkMinPitch(F0Val val) {
		F0Val fv = new F0Val(val.getPos(), val.getVal());
		if (val.getVal() < emofilt.Constants.minF0Val) {
			fv.setVal(emofilt.Constants.minF0Val);
		}
		return fv;
	}

	/**
	 * Check whether a f0-value is below a constant f0-value.
	 * 
	 * @param val
	 *            The value to check
	 * @return An F0-value with value set to minValue if the value was below or
	 *         the original value.
	 * @see emofilt.Constants
	 */
	public static int checkMinPitch(int val) {
		if (val < emofilt.Constants.minF0Val) {
			return emofilt.Constants.minF0Val;
		}
		return val;
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

	/**
	 * Calculate a new f0-value in Hertz given a start f0 value, a time span and
	 * a gradient.
	 * 
	 * @param freq
	 *            The start value in Hertz.
	 * @param time
	 *            The time span.
	 * @param gradient
	 *            The gradient, interpreted as semi-tones per second (ST/S)
	 * @return The new F0-value.
	 */
	public static double gradient2Hz(int freq, int time, double gradient) {
		final double _12_LN2 = 17.31234;
		double _exp;
		double semiTone = (time * 0.001) * gradient;
		_exp = semiTone / _12_LN2;
		return Math.exp(_exp) * freq;
	}

	/**
	 * Return a hashmap from a filepath. Format is "<keyString> |
	 * <valueString>" (each line one value pair).
	 * 
	 * @param filename
	 *            The path to the file that containes the values.
	 * @return The hashmap.
	 */
	public static HashMap getValuesFromFile(String filename) {
		HashMap hm = new HashMap();
		try {
			String line = null;
			String key, value;
			StringTokenizer st = null;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
					st = new StringTokenizer(line, "|");
					key = st.nextToken();
					value = "";
					try {
						value = st.nextToken();
					} catch (Exception ex) {
						System.err.println("found no value for: " + key);

					}
					hm.put(key, value);
					// System.err.println("key: "+key+", val: "+value);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error reading config file: " + e);
		}
		return hm;
	}

	/**
	 * Return a hashmap from a buffered reader. Format is "<keyString> |
	 * <valueString> (each line one value pair).
	 * 
	 * @param br
	 *            The reader to read the values from.
	 * @return The hashmap containing the values.
	 */
	public static HashMap getValuesFromBufferedReader(BufferedReader br) {
		HashMap hm = new HashMap();
		try {
			String line = null;
			String key, value;
			StringTokenizer st = null;
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
					st = new StringTokenizer(line, "|");
					key = st.nextToken();
					value = "";
					try {
						value = st.nextToken();
					} catch (Exception ex) {
						System.err.println("found no value for: " + key);

					}
					hm.put(key, value);
					// System.err.println("key: "+key+", val: "+value);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error reading config file: " + e);
		}
		return hm;
	}

	/**
	 * Get a value of an emotion element.
	 * 
	 * @param emotion
	 *            The emotion descriptor as a jdom xml element.
	 * @param name
	 *            The name of the modification, e.g. "speechRate".
	 * @param type
	 *            The type of the modification, e.g. "duration".
	 * @param valName
	 *            The name of the value, e.g. "rate".
	 * @return The value as a String.
	 * @throws ElemNotFoundException
	 *             If the value was not contained.
	 */
	public static String getValueFromEmotion(Element emotion, String name,
			String type, String valName) throws ElemNotFoundException {
		Element typeElem = emotion.getChild(type);
		if (typeElem != null) {
			Element manElem = typeElem.getChild(name);
			if (manElem != null) {
				return manElem.getAttribute(valName).getValue();
			}
		}
		throw new ElemNotFoundException();
	}

	/**
	 * Set a value in an emotion-descriptor.
	 * 
	 * @param emotion
	 *            The original emotion-descriptor.
	 * @param name
	 *            The name of the modification.
	 * @param type
	 *            The type of the modification.
	 * @param valName
	 *            The name of the value.
	 * @param value
	 *            The new value.
	 * @param defaultVal
	 *            A default value. If value is equal to default, the descriptor
	 *            will be removed.
	 * @return The changed emotion.
	 */
	public static Element setValueInEmotion(Element emotion, String name,
			String type, String valName, String value, String defaultVal) {
		Element returnElem = emotion;
		boolean remove = value.compareTo(defaultVal) == 0 ? true : false;
		try {
			Element typeElem = returnElem.getChild(type);
			if (typeElem != null) {
				Element elem = typeElem.getChild(name);
				if (elem != null) {
					if (remove) {
						typeElem.removeContent(elem);
					} else {
						elem.setAttribute(valName, value);
					}
				} else if (!remove) {
					elem = new Element(name);
					elem.setAttribute(valName, value);
					typeElem.addContent(elem);
				}
			} else if (!remove) {
				typeElem = new Element(type);
				Element elem = new Element(name);
				elem.setAttribute(valName, value);
				typeElem.addContent(elem);
				returnElem.addContent(typeElem);
			}
			if (remove && typeElem != null && !typeElem.hasChildren()) {
				returnElem.removeContent(typeElem);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnElem;
	}

	/**
	 * Return a color from a key in Emofilt's initialisation values.
	 * 
	 * @param key
	 *            The key name for that color. Assumed are three values giving
	 *            integers for red, green and blue value.
	 * @return The color.
	 */
	public static Color getColorFromUidf(String key) {
		Color c = Color.WHITE;
		try {
//			try {
//				c = Colors.getColorByName(Emofilt._config.getString(key));
//				if (c != null) {
//					return c;
//				}				
//			} catch (Exception e) {
//				// not existent
//			}

			int red = Integer.parseInt(Emofilt._config.getString(key + ".red"));
			int green = Integer
					.parseInt(Emofilt._config.getString(key + ".green"));
			int blue = Integer.parseInt(Emofilt._config.getString(key + ".blue"));
			c = new Color(red, green, blue);
		} catch (Exception e) {
			System.err.println("undefined Color: " + key);
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * Return a color from a hashmap given a key.
	 * 
	 * @param hm
	 *            The hashmap.
	 * @param key *
	 *            The key name for that color. Assumed are three values giving
	 *            integers for red, green and blue value.
	 * 
	 * @return The color.
	 */
	public static Color getColorFromValues(HashMap hm, String key) {
		Color c = Color.WHITE;
		try {
			c = Colors.getColorByName((String) hm.get(key));
			if (c != null) {
				return c;
			}
			int red = Integer.parseInt((String) hm.get(key + ".red"));
			int green = Integer.parseInt((String) hm.get(key + ".green"));
			int blue = Integer.parseInt((String) hm.get(key + ".blue"));
			c = new Color(red, green, blue);
		} catch (Exception e) {
			System.err.println("undefined Color: " + key);
			e.printStackTrace();
		}
		return c;
	}
}