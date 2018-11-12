package com.felix.util;

/**
 * Filter an input String by replacing any integers by a german number-string,
 * e.g. "apple 1007 i-book" --> "apple eintausendundsieben i-book"
 * 
 * @see
 * @author fburkhardt, tziroff
 * @author $Author: felixbur $
 * @version $Revision: 1.2 $
 * @since 07.10.2003
 */
public class NumberToWord {
	private int i;
	private int offset;
	private String ret = "";
	private int enumerationCounter = 1;
	private String locale = "de-DE";

	/**
	 * Constructor for the NumberToWord object
	 */
	public NumberToWord() {
		i = 0;
		offset = 0;
		enumerationCounter = 1;
		ret = "";
	}

	/**
     *
     */
	public void setResetEnumerationCounter() {
		enumerationCounter = 1;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getNextEnumeration() {
		String retValue = this.getEnumeration(enumerationCounter);
		enumerationCounter++;

		return retValue;
	}

	/**
	 * Filter an input String by replacing any integers by a german
	 * number-string, e.g. "apple 1007 i-book" --> "apple eintausendundsieben
	 * i-book"
	 * 
	 * @param in
	 *            Description of the Parameter
	 * @return The converted String.
	 */
	public String filtNum(String in) {
		offset = 0;
		ret = "";

		// remove leading 0
		if ((in.length() == 2) && in.startsWith("0")) {
			in = in.substring(1, 2);
		}

		for (i = 0; i < in.length(); i++) {
			if (!Character.isDigit(in.charAt(i))) {
				ret += in.charAt(i);
			} else {
				offset = checkDigits(in.substring(i, in.length()));
				i = i + offset;
			}
		}

		if (in.length() >= 1) {
			char lastOne = in.charAt(in.length() - 1);

			if (Character.isDigit(lastOne)) {
				if (in.length() >= 2) {
					char beforelastOne = in.charAt(in.length() - 2);

					if (!Character.isDigit(beforelastOne)) {
						ret += (" " + digitToWord(lastOne));
					}
				} else {
					ret += (" " + digitToWord(lastOne));
				}
			}
		}

		return ret;
	}

	/**
	 * Description of the Method
	 * 
	 * @param dg
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private int checkDigits(String dg) {
		String nums = "";
		String word = "";
		int i;
		int j;
		int k;

		for (i = 0; i < dg.length(); i++) {
			if ((nums.length() == 0) && (dg.charAt(i) == '0')) {
				continue;
			}

			if (!Character.isDigit(dg.charAt(i))) {
				break;
			}

			nums += dg.substring(i, i + 1);
		}

		if (nums.length() == 0) {
			nums = "0";
		}

		k = nums.length();

		if (k > 12) {
			for (j = 0; j < k; j++) {
				ret += digitToWord(dg.charAt(j));
			}

			return i - 1;
		}

		if (locale.equalsIgnoreCase("en-US")) {
			if (nums.length() > 1) {
				for (j = 0; j < nums.length(); j++) {
					word = digitToWord(nums.charAt(j));

					if ((k > 1) && (nums.charAt(j) == '1')) {
						word = "one";
					}

					switch (k) {
					case 12:

						if (nums.charAt(j) != '0') {
							ret += (word + "hundred ");
						}

						break;

					case 11: {
						ret += (tenToNintynine(nums.charAt(j), nums
								.charAt(j + 1)) + " billion ");
						j++;
						k--;
					}

						break;

					case 10: {
						if (nums.length() == 10) {
							if (word.compareTo("one") == 0) {
								ret += " one billion ";
							} else {
								ret += (" " + word + " billions ");
							}
						}

						break;
					}

					case 9:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + " hundred ");
						}

						break;

					case 8: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " million ");
						j++;
						k--;
					}

						break;

					case 7: {
						if (nums.length() == 7) {
							if (word.compareTo("one") == 0) {
								ret += " one million ";
							} else {
								ret += (" " + word + " millions ");
							}
						}
					}

						break;

					case 6:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + "hundred ");
						}

						break;

					case 5: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " thousand ");
						j++;
						k--;
					}

						break;

					case 4: {
						if (nums.length() == 4) {
							ret += (" " + word + " thousand ");
						}
					}

						break;

					case 3:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + " hundred ");
						}

						break;

					case 2: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " ");
						j = j + 2;
					}

						break;
					}

					k--;
				}
			}
		} else {
			if (nums.length() > 1) {
				for (j = 0; j < nums.length(); j++) {
					word = digitToWord(nums.charAt(j));

					if ((k > 1) && (nums.charAt(j) == '1')) {
						word = "ein";
					}

					switch (k) {
					case 12:

						if (nums.charAt(j) != '0') {
							ret += (word + "hundert ");
						}

						break;

					case 11: {
						ret += (tenToNintynine(nums.charAt(j), nums
								.charAt(j + 1)) + " Milliarden ");
						j++;
						k--;
					}

						break;

					case 10: {
						if (nums.length() == 10) {
							if (word.compareTo("ein") == 0) {
								ret += " eine Miliarde ";
							} else {
								ret += (" " + word + " Millionen ");
							}
						}

						break;
					}

					case 9:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + " hundert ");
						}

						break;

					case 8: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " Millionen ");
						j++;
						k--;
					}

						break;

					case 7: {
						if (nums.length() == 7) {
							if (word.compareTo("ein") == 0) {
								ret += " eine Million ";
							} else {
								ret += (" " + word + " Millionen ");
							}
						}
					}

						break;

					case 6:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + "hundert ");
						}

						break;

					case 5: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " tausend ");
						j++;
						k--;
					}

						break;

					case 4: {
						if (nums.length() == 4) {
							ret += (" " + word + " tausend ");
						}
					}

						break;

					case 3:

						if (nums.charAt(j) != '0') {
							ret += (" " + word + " hundert ");
						}

						break;

					case 2: {
						ret += (" "
								+ tenToNintynine(nums.charAt(j), nums
										.charAt(j + 1)) + " ");
						j = j + 2;
					}

						break;
					}

					k--;
				}
			}
		}

		addSpecialChar(dg, nums, i);

		return i - 1;
	}

	/**
	 * Adds a feature to the SpecialChar attribute of the NumberToWord object
	 * 
	 * @param dg
	 *            The feature to be added to the SpecialChar attribute
	 * @param nums
	 *            The feature to be added to the SpecialChar attribute
	 * @param i
	 *            The feature to be added to the SpecialChar attribute
	 */
	private void addSpecialChar(String dg, String nums, int i) {
		// is a special char possible
		if ((dg.length() > 1) && (dg.length() > i)) {
			// is the current char a .
			if ((dg.charAt(i) == '.')) {
				// then append a word if its the last one ( 123. ), a part of a
				// sentence ( Karl der 1. lebte von ) or it follows a new line
				if ((dg.length() == (i + 1))
						|| ((dg.length() > (i + 1)) && ((dg.charAt(i + 1) == ' ') || (dg
								.charAt(i + 1) == '\n')))) {
					// ( ( dg.charAt( i+1 ) == ' ' ) || || ( dg.charAt( i+1 ) ==
					// '\n' ) )
					if (locale.equalsIgnoreCase("en-US")) {
						if (nums.compareTo("1") == 0) {
							ret += " first ";
						} else if (nums.length() == 1) {
							ret += (digitToWord(nums.charAt(0)) + "ly ");
						} else {
							ret += "ly ";
						}
					} else {
						if (nums.compareTo("1") == 0) {
							ret += " erstens ";
						} else if (nums.compareTo("3") == 0) {
							ret += " drittens ";
						} else if (nums.compareTo("7") == 0) {
							ret += " siebtens ";
						} else if (nums.length() == 1) {
							ret += (digitToWord(nums.charAt(0)) + "tens ");
						} else {
							ret += "tens ";
						}
					}
				}
			} else {
				if (nums.length() == 1) {
					ret += (" " + digitToWord(nums.charAt(0)) + " ");
				}
			}

			if (locale.equalsIgnoreCase("en-US")) {
				if (dg.length() > (i + 1)) {
					if ((dg.charAt(i) == '.')
							&& (Character.isDigit(dg.charAt(i + 1)))) {
						ret += " dot ";
					}

					if ((dg.charAt(i) == ',')
							&& (Character.isDigit(dg.charAt(i + 1)))) {
						ret += " comma ";
					}
				}
			} else {
				if (dg.length() > (i + 1)) {
					if ((dg.charAt(i) == '.')
							&& (Character.isDigit(dg.charAt(i + 1)))) {
						ret += " Punkt ";
					}

					if ((dg.charAt(i) == ',')
							&& (Character.isDigit(dg.charAt(i + 1)))) {
						ret += " Komma ";
					}
				}
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param c
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String digitToWord(int c) {
		if (locale.equalsIgnoreCase("en-US")) {
			switch (c) {
			case '0':
				return "zero";

			case '1':
				return "one";

			case '2':
				return "two";

			case '3':
				return "three";

			case '4':
				return "four";

			case '5':
				return "five";

			case '6':
				return "six";

			case '7':
				return "seven";

			case '8':
				return "eight";

			case '9':
				return "nine";
			}
		} else {
			switch (c) {
			case '0':
				return "null";

			case '1':
				return "eins";

			case '2':
				return "zwei";

			case '3':
				return "drei";

			case '4':
				return "vier";

			case '5':
				return "fünf";

			case '6':
				return "sechs";

			case '7':
				return "sieben";

			case '8':
				return "acht";

			case '9':
				return "neun";
			}
		}

		return "error: c>9";
	}

	/**
	 * Description of the Method
	 * 
	 * @param c1
	 *            Description of the Parameter
	 * @param c2
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String tenToNintynine(int c1, int c2) {
		String german_number = "";

		if (locale.equalsIgnoreCase("en-US")) {
			if ((c1 == '1') && (c2 == '1')) {
				german_number = "eleven";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '2')) {
				german_number = "twelve";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '3')) {
				german_number = "thirteen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '4')) {
				german_number = "fourteen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '5')) {
				german_number = "fifteen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '6')) {
				german_number = "sixteen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '7')) {
				german_number = "seventeen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '8')) {
				german_number = "eighteen";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '9')) {
				german_number = "nineteen";

				return (german_number);
			}

			switch (c1) {
			case '1':
				german_number += "ten";

				break;

			case '2':
				german_number += "twenty";

				break;

			case '3':
				german_number += "thirty";

				break;

			case '4':
				german_number += "fourty";

				break;

			case '5':
				german_number += "fifty";

				break;

			case '6':
				german_number += "sixty";

				break;

			case '7':
				german_number += "seventy";

				break;

			case '8':
				german_number += "eighty";

				break;

			case '9':
				german_number += "ninety";

				break;
			}

			if (c2 != '0') {
				german_number += digitToWord(c2);
			}
		} else {
			if ((c1 == '1') && (c2 == '1')) {
				german_number = "elf";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '2')) {
				german_number = "zw�lf";

				return (german_number);
			}

			if ((c1 == '1') && (c2 == '7')) {
				german_number = "siebzehn";

				return (german_number);
			}

			// sprintf(german_number,"\0");
			if (c2 != '0') {
				if ((c2 == '1') && (c1 != '0')) {
					german_number = "ein";
				} else {
					german_number = digitToWord(c2);
				}
			}

			if ((c1 > '1') && (c2 != '0')) {
				german_number += "und";
			}

			switch (c1) {
			case '1':
				german_number += "zehn";

				break;

			case '2':
				german_number += "zwanzig";

				break;

			case '3':
				german_number += "drei�ig";

				break;

			case '4':
				german_number += "vierzig";

				break;

			case '5':
				german_number += "fünfzig";

				break;

			case '6':
				german_number += "sechzig";

				break;

			case '7':
				german_number += "siebzig";

				break;

			case '8':
				german_number += "achtzig";

				break;

			case '9':
				german_number += "neunzig";

				break;
			}
		}

		return (german_number);
	}

	/**
	 * returns the number as enumeration word e.g. 12 = zw�lfte
	 * 
	 * @param number
	 *            the input value
	 * @return the german enumeration word
	 */
	public String getEnumeration(int number) {
		if (this.locale.equalsIgnoreCase("en-US")) {
			if (number == 1) {
				return " first";
			} else if (number == 3) {
				return " second";
			} else if (number == 3) {
				return " third";
			} else if (number < 10) {
				return digitToWord(String.valueOf(number).charAt(0)).trim()
						+ "th";
			} else {
				return filtNum(String.valueOf(number)).trim() + "th";
			}
		}

		if (number == 1) {
			return " erste";
		} else if (number == 3) {
			return " dritte";
		} else if (number == 7) {
			return " siebte";
		} else if (number == 16) {
			return " sechzehnte";
		} else if (number < 10) {
			return digitToWord(String.valueOf(number).charAt(0)).trim() + "te";
		} else {
			return filtNum(String.valueOf(number)).trim() + "te";
		}
	}

	/**
	 * @return Returns the locale.
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NumberToWord nf = new NumberToWord();
			System.out.println(nf.filtNum("a1"));
			/*
			 * System.out.println("ouput: " + nf.filtNum("11"));
			 * System.out.println("ouput: " + nf.filtNum("21"));
			 * System.out.println("ouput: " + nf.filtNum("31"));
			 * System.out.println("ouput: " + nf.filtNum("031"));
			 * System.out.println("ouput: " + nf.filtNum("00"));
			 * System.out.println("ouput: " + nf.filtNum("06"));
			 * System.out.println("ouput: " + nf.filtNum("6"));
			 * System.out.println("ouput: " + nf.filtNum("0"));
			 * System.out.println("ouput: " + nf.filtNum(" Sabrina - Total
			 * verhext! ")); System.out.println("ouput: " + nf.filtNum("
			 * Sabri1na - Total verhext! ")); System.out.println("ouput: " +
			 * nf.filtNum("1 Sabrina - Total verhext! "));
			 * System.out.println("ouput: " + nf.filtNum("p1"));
			 * System.out.println("ouput: " + nf.filtNum("Sabrina - Total
			 * verhext!"));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
