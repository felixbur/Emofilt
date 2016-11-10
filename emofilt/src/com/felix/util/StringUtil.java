package com.felix.util;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Methods around string processing.
 * 
 * @author felix
 * 
 */
public class StringUtil {
	/**
	 * Return the whitespace separated tokens of a string.
	 * 
	 * @param s
	 * @return
	 */
	public static Vector<String> stringToVector(String s) {
		Vector<String> ret = new Vector<String>();
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			ret.add(st.nextToken());
		}
		return ret;
	}

	/**
	 * Check if a string ocntains any alphanumeric characters.
	 * 
	 * @param in
	 *            The string to check.
	 * @return
	 */
	public static boolean checkIfWordsContained(String in) {
		StringTokenizer st = new StringTokenizer(in);
		while (st.hasMoreTokens()) {
			if (st.nextToken().matches("\\p{Alnum}+"))
				return true;
		}
		return false;
	}

	/**
	 * This method checks if a String contains only numbers
	 */
	public static boolean containsOnlyNumbers(String str) {

		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;
	}

	/**
	 * Return the tokens of a string.
	 * 
	 * @param s
	 *            The String.
	 * @param sep
	 *            The separatpr String.
	 * @return
	 */
	public static Vector<String> stringToVector(String s, String sep) {
		Vector<String> ret = new Vector<String>();
		StringTokenizer st = new StringTokenizer(s, sep);
		while (st.hasMoreTokens()) {
			ret.add(st.nextToken());
		}
		return ret;
	}

	/**
	 * Get a vector of strings as string array.
	 * 
	 * @param v
	 *            The vector.
	 * @return The string array.
	 */
	public static String[] vectorToStringArray(Vector<String> v) {
		String ret[] = new String[v.size()];
		int i = 0;
		for (String s : v) {
			ret[i++] = s;
		}
		return ret;
	}

	/**
	 * Get all words from a string except the first one.
	 * 
	 * @param a
	 *            The input, e.g. "a b"
	 * @return The result, e.g. "b"
	 */
	public static String getAllButFirst(String s) {
		return (Util.arrayToString(getAllButFirst(stringToArray(s, " "))));
	}

	/**
	 * Get all words from a string except the first one.
	 * 
	 * @param a
	 *            The input, e.g. "a b"
	 * @return The result, e.g. "a"
	 */
	public static String getAllButLast(String s) {
		return (Util.arrayToString(getAllButLast(stringToArray(s, " "))));
	}

	/**
	 * Get the number of blank separated tokens in a String.
	 * 
	 * @param t
	 *            The input, e.g. "foo bar"
	 * @return The output, e.g. "2"
	 */
	public static int numWords(String t) {
		return new StringTokenizer(t).countTokens();
	}

	/**
	 * Get all words from a string array except the first one.
	 * 
	 * @param a
	 *            The input, e.g. {"a", "b"}
	 * @return The result, e.g. {"b"}
	 */
	public static String[] getAllButFirst(String[] a) {
		if (a == null || a.length == 0)
			return null;
		String[] ret = new String[a.length - 1];
		for (int i = 1; i < a.length; i++) {
			ret[i - 1] = a[i];
		}
		return ret;
	}

	/**
	 * Get all words from a string array except the last one.
	 * 
	 * @param a
	 *            The input, e.g. {"a", "b"}
	 * @return The result, e.g. {"a"}
	 */
	public static String[] getAllButLast(String[] a) {
		if (a == null || a.length == 0)
			return null;
		String[] ret = new String[a.length - 1];
		for (int i = 0; i < a.length - 1; i++) {
			ret[i] = a[i];
		}
		return ret;
	}

	/**
	 * Search a complete string in vector elements (vector:string ==
	 * search:string).
	 * 
	 * @param s
	 * @param v
	 * @return
	 */
	public static boolean isStringInVector(String s, Vector<String> v) {
		if (v != null && s != null) {
			for (Iterator<String> iterator = v.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				if (s.trim().compareTo(string.trim()) == 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Get last tokens of string tokenizer separated by blank.
	 * 
	 * @param st
	 *            The Tokenizer.
	 * 
	 * @return The String.
	 */
	public static String getRestOfLine(StringTokenizer st) {
		String ret = "";
		while (st.hasMoreTokens()) {
			ret += st.nextToken() + " ";
		}
		return ret.trim();
	}

	/**
	 * Search a complete string ignoring case in vector elements (vector:string
	 * == search:string).
	 * 
	 * @param s
	 * @param v
	 * @return
	 */
	public static boolean isStringInVectorIgnoreCase(String s, Vector<String> v) {
		if (v != null && s != null) {
			for (Iterator<String> iterator = v.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				if (s.trim().toLowerCase().compareTo(
						string.trim().toLowerCase()) == 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Test if a trimmed string is element of an array.
	 * 
	 * @param s
	 * @param sa
	 * @return
	 */
	public static boolean isStringInArray(String s, String[] sa) {
		if (sa != null && s != null) {
			for (int i = 0; i < sa.length; i++) {
				String string = sa[i];
				if (s.trim().compareTo(string.trim()) == 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Test if a trimmed string ignoring case is element of an array.
	 * 
	 * @param s
	 * @param sa
	 * @return
	 */
	public static boolean isStringInArrayIgnoreCase(String s, String[] sa) {
		if (sa != null && s != null) {
			for (int i = 0; i < sa.length; i++) {
				String string = sa[i];
				if (s.trim().toLowerCase().compareTo(
						string.trim().toLowerCase()) == 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Remove all Tags (xml-elements) inside a string.
	 * 
	 * @param in
	 *            The string.
	 * @return The sting without tags.
	 */
	public static String stripTags(String in) {
		String temp = in;
		return temp.replaceAll("<[^>]*>", "");
	}

	/**
	 * Remove all newlines inside a string.
	 * 
	 * @param in
	 *            The string.
	 * @return The sting without tags.
	 */
	public static String stripNewlines(String in) {
		String temp = in;
		return temp.replaceAll("\n", "");
	}

	/**
	 * Get a String without whitespace but blank separated tokens.
	 * 
	 * @param in
	 *            The String.
	 * @return The String.
	 */
	public static String stripWhiteSpce(String in) {
		String ret = "";
		StringTokenizer st = new StringTokenizer(in);
		while (st.hasMoreTokens()) {
			ret += st.nextToken() + " ";
		}
		return ret.trim();
	}

	/**
	 * Count number of newlines in a string.
	 * 
	 * @param in
	 *            The String.
	 * @return The number of newlines.
	 */
	public static int countNewlines(String in) {
		int ret = 0;
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if (ch == '\n')
				ret++;
		}
		return ret;
	}

	/**
	 * Remove all Tags (xml-elements) inside a part of a string.
	 * 
	 * @param string
	 *            the String
	 * @param start
	 *            The start position.
	 * @param end
	 *            The end position.
	 * @return
	 */
	public static String stripTagsAtPosition(String string, int start, int end) {
		String temp = string.substring(start, end);
		temp = stripTags(temp);
		return string.substring(0, start) + temp
				+ string.substring(end, string.length());
	}

	/**
	 * Remove the last xml tag in a string.
	 * 
	 * @param string
	 *            The string, e.g <bla>foo</bla>
	 * @return The result, e.g. "<bla>foo"
	 */
	public static String stripLastTag(String string) {
		int start = string.lastIndexOf("<");
		if (start < 1)
			return string;
		int end = string.length();
		String temp = string.substring(start, end);
		temp = stripTags(temp);
		return string.substring(0, start) + temp
				+ string.substring(end, string.length());
	}

	/**
	 * Sourround a string by xml-tags
	 * 
	 * @param string
	 *            The string, e.g. foo
	 * @param tag
	 *            The tag, e.g. bar
	 * @return The return, e.g. <bar>foo</bar>
	 */
	public static String tagString(String string, String tag) {
		return "<" + tag + ">" + string + "</" + tag + ">";

	}

	/**
	 * Surround a string by xml-tags, attributes are taken into account.
	 * 
	 * @param string
	 *            The string, e.g. blafoobla
	 * @param tag
	 *            The tag, e.g. bar color="es a"
	 * @param start
	 *            The beginning, e.g. 3
	 * @param end
	 *            The end, e.g. 6
	 * 
	 * @return The return, e.g. bla<bar color="es a">foo</bar>bla
	 */
	public static String tagStringAtPosition(String string, String tag,
			int start, int end) {
		StringTokenizer st = new StringTokenizer(tag);
		String tagname = "", attribute = "";
		tagname = st.nextToken();
		while (st.hasMoreTokens()) {
			attribute += st.nextToken() + " ";
		}
		if (attribute.length() > 0)
			return string.substring(0, start) + "<" + tagname + " "
					+ attribute.trim() + ">" + string.substring(start, end)
					+ "</" + tagname + ">"
					+ string.substring(end, string.length());
		else
			return string.substring(0, start) + "<" + tagname + ">"
					+ string.substring(start, end) + "</" + tagname + ">"
					+ string.substring(end, string.length());
	}

	/**
	 * Return the last position of a key string in another string (like
	 * "indexOf", but searching backwards).
	 * 
	 * @param string
	 *            The String to search.
	 * @param key
	 *            The keyString.
	 * @param endSearch
	 *            The last position.
	 * @return The position of 0 if not found.
	 */
	public static int lastPositionOf(String string, String key, int endSearch) {
		int start = 0, ind = 0, laststart = 0;
		while (ind < endSearch) {
			start = string.substring(0, endSearch - 1).indexOf(key, ind++);
			if (start > -1) {
				laststart = start;
			}
		}
		start = laststart;
		if (start > 0) {
			start += 1;
		}
		return start;
	}

	/**
	 * Test if a String is not null and contains characters besides whitespace.
	 * 
	 * @param s
	 *            The String.
	 * @return True or False.
	 */
	public static boolean isFilled(String s) {
		if (s != null && s.trim().length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Return the blank separated tokens of a string.
	 * 
	 * @param s
	 * @return
	 */
	public static String[] stringToArray(String s) {
		String ret[] = null;
		StringTokenizer st = new StringTokenizer(s);
		ret = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			ret[i++] = st.nextToken();
		}
		return ret;
	}

	/**
	 * Capitalize one word.
	 * 
	 * @param inputWord
	 *            The input, e.g. "aBa"
	 * @return The outpu, e.g "Aba"
	 */
	public static String capitalizeOneWord(String inputWord) {
		String firstLetter = inputWord.substring(0, 1); // Get first letter
		String remainder = inputWord.substring(1); // Get remainder of word.
		return firstLetter.toUpperCase() + remainder.toLowerCase();
	}

	/**
	 * Capitalize a number of words.
	 * 
	 * @param in
	 *            The input, e.g. "aBa hJk-sdf"
	 * @return The output, e.g. "Aba Hjk-Sdf"
	 */
	public static String capitalizeMultipleWords(String in) {
		String ret = "";
		StringTokenizer st = new StringTokenizer(in, " ");
		while (st.hasMoreTokens()) {
			String t = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(t, "-");
			while (st2.hasMoreTokens()) {
				ret += capitalizeOneWord(st2.nextToken()) + "-";
			}
			ret = ret.substring(0, ret.length() - 1) + " ";
		}
		return ret.trim();
	}

	/**
	 * Capitalize a named entitiy.
	 * 
	 * @param in
	 *            The input, e.g. "werner von siemens"
	 * @param stopwrods
	 *            The stopwords, e.g. "de la von van für"
	 * @return The output, e.g. "Werner von Siemens"
	 */
	public static String capitalizeNamedEntities(String in, String[] stopwords) {
		String ret = "";
		StringTokenizer st = new StringTokenizer(in);
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			if (Util.isStringContainedInArray(word, stopwords)) {
				ret += word + " ";
			} else {
				ret += capitalizeOneWord(word) + " ";
			}
		}
		return ret.trim();
	}

	/**
	 * Return the blank separated tokens of a string.
	 * 
	 * @param s
	 * @return
	 */
	public static String[] stringToArray(String s, String seperator) {
		String ret[] = null;
		StringTokenizer st = new StringTokenizer(s, seperator);
		ret = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			ret[i++] = st.nextToken();
		}
		return ret;
	}

	/**
	 * Print something to System.out.
	 * 
	 * @param msg
	 */
	public static void printOut(String msg) {
		System.out.println(msg);
	}

	/**
	 * Return a string surrounded by two other strings.
	 * 
	 * @param target
	 *            The containing string, e.g. "bla[[foo]]bar".
	 * @param front
	 *            The front trigger, e.g. "[[".
	 * @param rear
	 *            The rear trigger, e.g. "]]".
	 * @return The contained String (if any), e.g. "foo" or null if not found
	 */
	public static String getStringBetween(String target, String front,
			String rear) {
		int pos1 = target.indexOf(front) + front.length();
		int pos2 = target.substring(pos1).indexOf(rear);
		if (pos1 > 0 && pos2 > 0) {
			return target.substring(pos1, pos2 + pos1);
		} else {
			return null;
		}
	}

	/**
	 * Get the string between two other strgins, where the back string will be
	 * chosen from an array of possible string, and selecting the shortest
	 * possibility.
	 * 
	 * @param target
	 *            The target, e.g. "asda bla blobli foo faa ; asd"
	 * @param front
	 *            The first string, e.g. "bla"
	 * @param rears
	 *            The back possibilites, e.g. {"foo", ";", "faa"}
	 * @return e.g. " bloblo " or null if not found.
	 */
	public static String getStringBetween(String target, String front,
			String[] rears) {
		int pos1 = target.indexOf(front) + front.length();
		int minRearPos = target.length();
		for (String r : rears) {
			int pos2 = target.substring(pos1).indexOf(r);
			if (pos2 < 0)
				continue;
			if (pos2 < minRearPos)
				minRearPos = pos2;
		}
		if (pos1 > 0 && minRearPos > 0 && minRearPos < target.length()) {
			return target.substring(pos1, minRearPos + pos1);
		} else {
			return null;
		}
	}

	public static String getStringBetween(String target, String[] fronts,
			String[] rears) {

		int minFrontPos = target.length();
		int minRearPos = target.length();
		for (String f : fronts) {
			int pos1 = target.indexOf(f) + f.length();
			if (pos1 - f.length() < 0)
				continue;
			if (pos1 < minFrontPos)
				minFrontPos = pos1;
			for (String r : rears) {
				int pos2 = target.substring(minFrontPos).indexOf(r);
				if (pos2 < 0)
					continue;
				if (pos2 < minRearPos)
					minRearPos = pos2;
			}
		}
		if (minFrontPos > 0 && minFrontPos < target.length() && minRearPos > 0
				&& minRearPos < target.length()) {
			return target.substring(minFrontPos, minRearPos + minFrontPos);
		} else {
			return null;
		}
	}

	public static String[] removeLastStopwords(String[] query,
			String[] stopwords) {
		Vector<String> ret = new Vector<String>();
		boolean stopped = false;
		for (int i = query.length - 1; i >= 0; i--) {
			String word = query[i];
			if (!isStringInArray(word, stopwords)) {
				stopped = true;
				ret.add(0, word);
			} else {
				if (stopped)
					ret.add(0, word);
			}
		}
		return vectorToStringArray(ret);
	}

	public static String[] removeFirstStopwords(String[] query,
			String[] stopwords) {
		Vector<String> ret = new Vector<String>();
		boolean stopped = false;
		for (String word : query) {
			if (!isStringInArray(word, stopwords)) {
				stopped = true;
				ret.add(word);
			} else {
				if (stopped)
					ret.add(word);
			}
		}
		return vectorToStringArray(ret);
	}

	public static String[] removeOuterStopwords(String[] query,
			String[] stopwords) {
		String[] temp = removeFirstStopwords(query, stopwords);
		return removeLastStopwords(temp, stopwords);
	}

	/**
	 * Remove stopwords from a query.
	 * 
	 * @param query
	 *            The query, e.g. "foo fong bar fong"
	 * @param stopwords
	 *            The stopwords, e.g. "fong"
	 * @return The result, e.g. "foo bar"
	 */
	public static String[] removeStopwords(String[] query, String[] stopwords) {
		Vector<String> ret = new Vector<String>();
		for (String word : query) {
			if (!isStringInArray(word, stopwords)) {
				ret.add(word);
			}
		}
		return vectorToStringArray(ret);
	}

	/**
	 * Insert a String sequence before a trigger String inside a target String.
	 * 
	 * @param insertion
	 *            The String to insert before the key.
	 * @param key
	 *            The key or trigger String.
	 * @param target
	 *            The target String containing the key.
	 * @return The result String.
	 */
	public static String insertBefore(String insertion, String key,
			String target) {
		int start = 0;
		String temp = target;
		while (start > -1) {
			start = temp.indexOf(key, start);
			if (start > -1) {
				// int end = start + orig.length();
				try {
					temp = temp.substring(0, start) + insertion
							+ temp.substring(start, temp.length());
				} catch (Exception e) {
					System.err.println("ERROR processing " + key + ", "
							+ insertion + ", " + target);
					e.printStackTrace();
				}
				start = start + insertion.length() + key.length();
			}
		}
		return temp;
	}

	/**
	 * Insert a String sequence before a trigger String inside a target String
	 * ignoring case.
	 * 
	 * @param insertion
	 *            The String to insert before the key.
	 * @param key
	 *            The key or trigger String.
	 * @param target
	 *            The target String containing the key.
	 * @return The result String.
	 */
	public static String insertBeforeIgnoreCase(String insertion, String key,
			String target) {
		int start = 0;
		String temp = target;
		while (start > -1) {
			start = temp.toLowerCase().indexOf(key.toLowerCase(), start);
			if (start > -1) {
				// int end = start + orig.length();
				try {
					temp = temp.substring(0, start) + insertion
							+ temp.substring(start, temp.length());
				} catch (Exception e) {
					System.err.println("ERROR processing " + key + ", "
							+ insertion + ", " + target);
					e.printStackTrace();
				}
				start = start + insertion.length() + key.length();
			}
		}
		return temp;
	}

	/**
	 * Insert a String sequence after a trigger String inside a target String.
	 * 
	 * @param insertion
	 *            The String to insert before the key.
	 * @param key
	 *            The key or trigger String.
	 * @param target
	 *            The target String containing the key.
	 * @return The result String.
	 */
	public static String insertAfter(String insertion, String key, String target) {
		int start = 0;
		String temp = target;
		while (start > -1) {
			start = temp.indexOf(key, start);
			if (start > -1) {
				int end = start + key.length();
				try {
					temp = temp.substring(0, end) + insertion
							+ temp.substring(end, temp.length());
				} catch (Exception e) {
					System.err.println("ERROR processing " + key + ", "
							+ insertion + ", " + target);
					e.printStackTrace();
				}
				start = end;
			}
		}
		return temp;
	}

	/**
	 * Insert a String sequence after a trigger String inside a target String
	 * ignoring case.
	 * 
	 * @param insertion
	 *            The String to insert before the key.
	 * @param key
	 *            The key or trigger String.
	 * @param target
	 *            The target String containing the key.
	 * @return The result String.
	 */
	public static String insertAfterIgnoreCase(String insertion, String key,
			String target) {
		int start = 0;
		String temp = target;
		while (start > -1) {
			start = temp.toLowerCase().indexOf(key.toLowerCase(), start);
			if (start > -1) {
				int end = start + key.length();
				try {
					temp = temp.substring(0, end) + insertion
							+ temp.substring(end, temp.length());
				} catch (Exception e) {
					System.err.println("ERROR processing " + key + ", "
							+ insertion + ", " + target);
					e.printStackTrace();
				}
				start = end;
			}
		}
		return temp;
	}

	/**
	 * Count the occurrences of a String in another String.
	 * 
	 * @param input
	 *            The containing String.
	 * @param token
	 *            The contained String.
	 * @return The number.
	 */
	public static int countTokens(String input, String token) {
		int start = 0;
		int ret = 0;
		while (start > -1) {
			start = input.indexOf(token, start);
			if (start > -1) {
				start = start + token.length();
				ret++;
			}
		}
		return ret;
	}

	/**
	 * Return list of strings in a single string separated by a separator
	 * string.
	 * 
	 * @param list
	 *            The list.
	 * @param separator
	 *            The separator.
	 * @return The new String.
	 */
	public static String stringList2String(List<String> list, String separator) {
		String ret = "";
		for (String s : list) {
			ret += s + separator;
		}
		return ret.substring(0, ret.length() - separator.length());
	}

	/**
	 * Retrieve the parts of a String array as a single String separated by a
	 * separator.
	 * 
	 * @param stringArray
	 *            The string array, e.g. {"a", "a"}
	 * @param separator
	 *            The separator, e.g. ", "
	 * @return The String, e.g. "a, a"
	 */
	public static String stringArray2String(String[] stringArray,
			String separator) {
		if (stringArray == null)
			return null;
		String ret = "";
		for (String s : stringArray) {
			ret += s + separator;
		}
		return ret.substring(0, ret.length() - separator.length());
	}

	/**
	 * Count the occurrences of a String in another String ignoring case.
	 * 
	 * @param input
	 *            The containing String.
	 * @param token
	 *            The contained String.
	 * @return The number.
	 */
	public static int countTokensIgnoreCase(String input, String token) {
		int start = 0;
		int ret = 0;
		while (start > -1) {
			start = input.toLowerCase().indexOf(token.toLowerCase(), start);
			if (start > -1) {
				start = start + token.length();
				ret++;
			}
		}
		return ret;
	}

	/**
	 * Main program used for testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(stripLastTag("<bar>foo</bar>"));
		System.out.println(getAllButLast("das ist ein test."));
		String[] testA = { "foo", "bar", "foo", "foo", "bar", "foo" };
		String[] stops = { "foo" };
		for (String s : removeOuterStopwords(testA, stops)) {
			System.out.println("s: " + s);
		}

		System.out.println(capitalizeMultipleWords("karl-heinz rumennigge"));
		// String test =
		// "Manfred Albrecht Freiherr von Richthofen geboren am	 2. Mai 1892 in Breslau; gestorben am 21. April 1918 bei Vaux-sur-Somme war ein deutscher Jagdflieger im Ersten Weltkrieg";
		String test = "Albert Einstein geboren am	 14. März 1879 in Ulm, Königreich Württemberg Deutsches Reich; gestorben am 18. April 1955 in Princeton, USA war ein theoretischer Physiker.";
		String[] fronts = { "in", "bei" };
		String[] rears = { ";", "war", "gehörte" };
		System.out.println(getStringBetween(test.substring(test
				.indexOf("gestorben")), fronts, rears));

		test = "Hans Fallada geboren am	 21. Juli 1893 in Greifswald; gestorben am 5. Februar 1947 in Berlin; eigentlich Rudolf";
		System.out.println(getStringBetween(test, "geboren am", "in"));

		String insertion = "</font>";
		String key = "ein";
		// String target = "hxhx";
		String target = "das ist ein test ein test";
		System.out.println("Ausgabe von insertBefore() für " + target + ", "
				+ key + ", " + insertion);
		System.out.println(insertBefore(insertion, key, target));
		System.out.println("Ausgabe von insertAfter() für " + target + ", "
				+ key + ", " + insertion);
		System.out.println(insertAfter(insertion, key, target));

		String countTokensTestInput = "lohallo thlo bralo da";
		String countTokensTestToken = "lo";
		System.out.println("Ausgabe von countTokens(\"" + countTokensTestInput
				+ "\", \"" + countTokensTestToken + "\")");
		System.out.println(countTokens(countTokensTestInput,
				countTokensTestToken));
	}
}