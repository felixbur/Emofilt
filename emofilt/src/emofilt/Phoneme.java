/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;

import org.apache.log4j.*;

import emofilt.util.Util;

/**
 * Model of a Phoneme, i.e. a name, duration, a set of f0 values, manner of
 * articulation etc. Place of articulation is not modeled.
 * 
 * 
 * @author Felix Burkhardt
 */
public class Phoneme {
	public static final String noManner = "noManner";

	public static final String long_vowel = "long_vowel";

	public static final String short_vowel = "short_vowel";

	public static final String approximant = "approximant";

	public static final String nasal = "nasal";

	public static final String fricative_voiced = "fricative_voiced";

	public static final String fricative_voiceless = "fricative_voiceless";

	public static final String stop_voiced = "stop_voiced";

	public static final String stop_voiceless = "stop_voiceless";

	public static final String silence = "silence";

	public static final String EFFORT_NORMAL = "normal";

	public static final String EFFORT_SOFT = "soft";

	public static final String EFFORT_LOUD = "loud";

	public static final String VOWEL_NORMAL = "normal";

	public static final String VOWEL_OVERSHOOT = "overshoot";

	public static final String VOWEL_UNDERSHOOT = "undershoot";

	/**
	 * true if the phoneme is voiced;
	 */
	private boolean voiced = false;

	/**
	 * name of phoneme in mbrola language notation.
	 */
	private String name = null;

	private Logger debugLogger;

	private String variant;

	private boolean central = false;

	private boolean decentral = false;

	private boolean syllableStart = false;

	private boolean syllableStartUnstressed = true;

	private boolean syllableStartWordStressed = false;

	private boolean syllableStartFocusstressed = false;

	private Vector f0vals = null;
	private int dur;

	private String manner;

	/**
	 * Constructor
	 */
	public Phoneme() {
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
	}

	/**
	 * Test if the phoneme is nasal.
	 * 
	 * @return True if manner is nasal.
	 */
	public boolean isNasal() {
		if (manner.compareTo(nasal) == 0)
			return true;
		return false;
	}

	/**
	 * Test if the phoneme is fricative.
	 * 
	 * @return True if manner is fricative.
	 */
	public boolean isFricative() {
		if (manner.compareTo(fricative_voiced) == 0
				|| manner.compareTo(fricative_voiceless) == 0)
			return true;
		return false;
	}

	/**
	 * Test if the phoneme is obstruent.
	 * 
	 * @return True if manner is obstruent.
	 */
	public boolean isObstruent() {
		if (manner.compareTo(fricative_voiceless) == 0
				|| manner.compareTo(stop_voiceless) == 0)
			return true;
		return false;
	}

	/**
	 * Change the f0-contour by rate.
	 * 
	 * @param rate
	 *            Change factor in percent, 100 is no change.
	 */
	public void changeMeanF0(int rate) {
		if (!hasF0Description())
			return;
		double dr = rate / 100.0;
		for (Iterator iter = f0vals.iterator(); iter.hasNext();) {
			F0Val element = (F0Val) iter.next();
			element.setVal((int) (element.getVal() * dr));
		}
	}

	/**
	 * Change the duration of a phoneme.
	 * 
	 * @param rate
	 *            The rate of change in percent (100 stays the same).
	 */
	public void changeDuration(int rate) {
		dur = dur * rate / 100;
	}

	/**
	 * Return true if name is "_"
	 * 
	 * @return
	 */
	public boolean isSilence() {
		if (name.compareTo("_") == 0)
			return true;
		return false;
	}

	/**
	 * Expand or compress a series of integer values dependent on their location
	 * relative to mean value.
	 * 
	 * @param rate
	 *            The rate of change (100 no change, values > 100 expand, values
	 *            y 100 compress).
	 * @param mean
	 *            The central value for displacement.
	 */
	public void changeF0Range(int rate, double mean) {
		arrayToF0Vals(Util.changeContrast(f0ValsToArray(), rate, mean));
	}

	/**
	 * Change the vocal effort by selecting another phoneme.
	 * 
	 * @param effort
	 *            Effort-string that gets attached to the phoneme name.
	 */
	public void changeEffort(String effort) {
		if (name.compareTo("_") != 0 && name.compareTo("?") != 0) {
			name += "_" + effort;
		}
	}

	/**
	 * return a physical copy of phoneme.
	 */
	public Object clone() {
		Phoneme p = new Phoneme();
		p.setName(name);
		p.setManner(manner);
		p.copyVariant(this);
		p.setDur(dur);
		p.setVoiced(voiced);
		p.setSyllableStart(syllableStart);
		if (hasF0Description()) {
			Vector fs = new Vector();
			for (Iterator iter = f0vals.iterator(); iter.hasNext();) {
				F0Val element = (F0Val) iter.next();
				F0Val fv = new F0Val(element.getPos(), element.getVal());
				fs.add(fv);
			}
			p.setF0vals(fs);
		}
		return p;
	}

	/**
	 * Test whether a pitch-description exists for that phoneme.
	 * 
	 * @return True if the f0Vals exist and have at least one value.
	 */
	public boolean hasF0Description() {
		if (isVoiced() && f0vals != null && f0vals.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Test whether a pitch-description with non-zero exists for that phoneme.
	 * 
	 * @return True if the f0Vals exist and have at least one value above 0.
	 */
	public boolean hasVoicing() {
		if (f0vals != null && f0vals.size() > 0) {
			for (Iterator iter = f0vals.iterator(); iter.hasNext();) {
				F0Val element = (F0Val) iter.next();
				if (element.getVal() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Set a new f0-contour placed equidistantly.
	 * 
	 * @param vec
	 *            A vector of Integers that will be set as new f0-values.
	 */
	public void setF0ValueVector(Vector vec) {
		// System.err.println("tmpVec: "+Util.printVec(vec));
		Vector<F0Val> tmp = new Vector<F0Val>(vec.size());
		for (int i = 0; i < vec.size(); i++) {
			int pos = i * (100 / vec.size());
			tmp.add(new F0Val(pos, (Integer) vec.elementAt(i)));
		}
		setF0vals(tmp);
		// System.err.print(toString());
	}

	/**
	 * Model the F0-track by interpolation so that every frame has a f0-value.
	 * only valid for voiced phonemes, will crash otherwise! If no startValue
	 * (0%) is given modeling will start at lastF0. If no endValue (100%) is
	 * given modeling will end at nextF0.
	 * 
	 * @param prevF0
	 *            First value to start the interpolation if no 0% value is
	 *            given.
	 * @param nextF0
	 *            Target value to end the interpolation if no 100% value is
	 *            given;
	 */
	public void modelTrack(int prevF0, int nextF0) {
		if (f0vals == null || f0vals.size() == 0) {
			return;
		}
		F0Val firstVal = (F0Val) f0vals.firstElement();
		if (firstVal.getPos() > 0 && prevF0 > 0) {
			f0vals.add(0, new F0Val(0, prevF0));
		}
		F0Val lastVal = (F0Val) f0vals.lastElement();
		if (lastVal.getPos() < 100 && nextF0 > 0) {
			f0vals.add(new F0Val(100, nextF0));
		}
		// System.err.println(toString());
		// Util.printF0ValVec(f0vals, System.err);
		// System.err.println();
		modelTrack();
		// Util.printF0ValVec(f0vals, System.err);
		// System.err.println();
	}

	/**
	 * Model the F0-track by interpolation so that every frame has a f0-value.
	 */
	public void modelTrack() {
		if (!hasF0Description()) {
			return;
		}
		Vector tmpF0Vec = new Vector(); // vector with new f0Vals
		int valNum = getFrameNum() + 1;
		int stepSize = (int) ((100.0 * Constants.frameSize) / (double) dur);
		int percVal = 0;
		// first fill a temporary vector with empty f0-values at each frameSize.
		for (int i = 0; i < valNum; i++) {
			int newF0 = 0;
			// insert the target values
			for (int j = 0; j < f0vals.size(); j++) {
				F0Val f0Val = (F0Val) f0vals.elementAt(j);
				if (f0Val.getPos() >= percVal
						& f0Val.getPos() <= percVal + stepSize) {
					newF0 = f0Val.getVal();
					break;
				}
			}
			tmpF0Vec.add(new F0Val(percVal, newF0));
			percVal += stepSize;
		}
		// add the last element (100%) at the end
		if (((F0Val) f0vals.lastElement()).getPos() == 100) {
			tmpF0Vec.add(f0vals.lastElement());
		}
		// now fill in the missing values
		int start = 0;
		int stepNum = 0;
		for (int i = 0; i < tmpF0Vec.size(); i++) {
			F0Val f0Val = (F0Val) tmpF0Vec.elementAt(i);
			int end = 0;
			if (f0Val.getVal() == 0) {
				int stepIndex = 0;
				// search for next non-zero f0
				for (int j = i; j < tmpF0Vec.size(); j++) {
					F0Val f0Val2 = (F0Val) tmpF0Vec.elementAt(j);
					if (f0Val2.getVal() > 0) {
						end = f0Val2.getVal();
						break;
					}
					stepIndex++;
				}
				if (stepNum == 0) {
					stepNum = stepIndex;
				}
				if (start == 0) {
					start = end;
				}
				stepIndex = stepNum - stepIndex + 1;
				if (end > 0) {
					int nextStep = Util.calcNextStep(start, end, stepIndex,
							stepNum);
					// System.err.println("start " + start + " end " + end
					// + " stepIndex " + stepIndex + " of " + stepNum
					// + " steps, nextStep " + nextStep);
					f0Val.setVal(nextStep);
				} else {
					tmpF0Vec.removeElementAt(i);
				}
			} else {
				start = f0Val.getVal();
				stepNum = 0;
			}
		}
		f0vals = tmpF0Vec;
	}

	/**
	 * Retrieve the mean f0-value.
	 * 
	 * @return The mean f0-value or 0 if there are no f0-values.
	 */
	public double getMeanF0() {
		if (isVoiced() && hasF0Description()) {
			return Util.getF0Mean(f0vals);
		}
		return 0;
	}

	/**
	 * Return all information on this phoneme as a String.
	 * 
	 * @return Information on this phoneme.
	 */
	public String toString() {
		String ret = "";
		ret += name + ", " + manner;
		if (voiced)
			ret += ", voiced";
		if (syllableStart)
			ret += ", sylStart";
		ret += ", dur:" + dur;
		if (hasF0Description()) {
			ret += ", f0vals: ";
			ret += Util.printF0ValVec(f0vals);
		}
		if (central)
			ret += ", decentral variant:" + variant;
		if (decentral)
			ret += ", central variant:" + variant;
		return ret;
	}

	/**
	 * Will return first f0 value if there is one.
	 * 
	 * @return The first F0 value or 0 if no values are present.
	 */
	public int getFirstF0Val() {
		if (isVoiced() && hasF0Description()) {
			return ((F0Val) f0vals.firstElement()).getVal();
		}
		return 0;
	}

	/**
	 * Will return last f0 value if there is one.
	 * 
	 * @return The last F0 value or 0 if no f0 values were present.
	 */
	public int getLastF0Val() {
		if (isVoiced() && hasF0Description()) {
			return ((F0Val) f0vals.lastElement()).getVal();
		}
		return 0;
	}

	/**
	 * Set a new f0-contour with strting value "start" and end value "end".
	 * 
	 * @param start
	 *            The starting f0 value.
	 * @param end
	 *            The ending f0 value.
	 */
	public void interpolateF0Vec(int start, int end) {
		Vector<F0Val> newF0Vals = new Vector<F0Val>(2);
		newF0Vals.add(new F0Val(0, start));
		newF0Vals.add(new F0Val(100, end));
		f0vals = newF0Vals;
	}

	/**
	 * Simulate jitter by displacing all f0 values alternating by framesize up
	 * and down. The f0-contour should be modeled beforehand, i.e. existing a
	 * value for each framesize.
	 * 
	 * @param rate
	 *            The rate of displacement in percent.
	 */
	public void addJitter(int rate) {
		int[] ia = f0ValsToArray();
		for (int i = 0; i < ia.length; i++) {
			if (i % 2 == 0) {
				ia[i] += (int) ((double) ia[i] * rate / 100.0);
			} else {
				ia[i] -= (int) ((double) ia[i] * rate / 100.0);
			}
		}
		arrayToF0Vals(ia);
	}

	/**
	 * Test if the phoneme is a vowel.
	 * 
	 * @return True if the phoneme is vowel, false otherwise.
	 */
	public boolean isVowel() {
		if (manner.compareTo(long_vowel) == 0
				|| manner.compareTo(short_vowel) == 0)
			return true;
		return false;
	}

	/**
	 * Test if the phoneme is a stop.
	 * 
	 * @return True if the phoneme is stop, false otherwise.
	 */
	public boolean isStop() {
		if (manner.compareTo(stop_voiced) == 0
				|| manner.compareTo(stop_voiceless) == 0)
			return true;
		return false;
	}

	/**
	 * Get the duration of the phoneme in milliseconds.
	 * 
	 * @return The duration in msec.
	 */
	public int getDur() {
		return dur;
	}

	/**
	 * Set the duration of the phoneme in milliseconds.
	 * 
	 * @param dur
	 *            The duration in msec.
	 */
	public void setDur(int dur) {
		this.dur = dur;
	}

	/**
	 * Get the number of frames computed by duration as a rounded value.
	 * 
	 * @return The approximate (rounded) number of frames that the phoneme
	 *         lasts.
	 */
	public int getFrameNum() {
		return Math.round((float) dur / Constants.frameSize);
	}

	/**
	 * Retrieve the f0 contour.
	 * 
	 * @return The f0 values as a vector.
	 * @see F0Val
	 */
	public Vector getF0vals() {
		return f0vals;
	}

	/**
	 * Set the f0 contour.
	 * 
	 * @param newVals
	 *            The new f0 contour as a Vector of f0 values.
	 * @see F0Val
	 */
	public void setF0vals(Vector newVals) {
		this.f0vals = newVals;
	}

	/**
	 * Get the phoneme's name.
	 * 
	 * @return The name as a Sampa symbol.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the phoneme's name
	 * 
	 * @param name
	 *            The new name of the phoneme as a Sampa symbol.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Test if the phoneme is voiced.
	 * 
	 * @return True if the phoneme is voiced, false otherwise.
	 */
	public boolean isVoiced() {
		return voiced;
	}

	/**
	 * Set the information whether phoneme is voiced.
	 * 
	 * @param voiced
	 *            True if the phoneme is voiced, false otherwise,
	 */
	public void setVoiced(boolean voiced) {
		this.voiced = voiced;
	}

	/**
	 * Retrieve the manner of the phoneme.
	 * 
	 * @return The manner as definied by phoneme, e.g. "fricative".
	 */
	public String getManner() {
		return manner;
	}

	/**
	 * Set the phoneme manner String.
	 * 
	 * @param manner
	 *            The manner, e.g. "fricative".
	 */
	public void setManner(String manner) {
		this.manner = manner;
	}

	/**
	 * Test if the phoneme is a start phoneme of a syllable.
	 * 
	 * @return True if the phoneme is syllable start, false otherwise.
	 */
	public boolean isSyllableStart() {
		return syllableStart;
	}

	/**
	 * Set the syllable start information.
	 * 
	 * @param syllableStart
	 *            Set to treue if the phoneme is first of a syllable, false
	 *            otherwise.
	 */
	public void setSyllableStart(boolean syllableStart) {
		this.syllableStart = syllableStart;
	}

	/**
	 * Test if the phoneme is start of a focus stressed syllable.
	 * 
	 * @return True if the phoneme is the first one of a focus stressed
	 *         syllable, false otherwise.
	 */
	public boolean isSyllableStartFocusstressed() {
		return syllableStartFocusstressed;
	}

	/**
	 * Set the focus stressed syllable start information to true. Word- and
	 * unstressed syllable start information is set to false than.
	 * 
	 */
	public void setSyllableStartFocusstressed() {
		this.syllableStartFocusstressed = true;
		syllableStartWordStressed = syllableStartUnstressed = false;
	}

	/**
	 * Test if the phoneme is start of an unstressed syllable.
	 * 
	 * @return True if the phoneme is the first one of an unstressed syllable,
	 *         false otherwise.
	 */
	public boolean isSyllableStartUnstressed() {
		return syllableStartUnstressed;
	}

	/**
	 * Set the unstressed syllable start information to true. Word- and
	 * focus-stressed syllable start information is set to false than.
	 * 
	 */
	public void setSyllableStartUnstressed() {
		this.syllableStartUnstressed = true;
		syllableStartWordStressed = syllableStartFocusstressed = false;
	}

	/**
	 * Test if the phoneme is start of a word stressed syllable.
	 * 
	 * @return True if the phoneme is the first one of a word stressed syllable,
	 *         false otherwise.
	 */
	public boolean isSyllableStartWordStressed() {
		return syllableStartWordStressed;
	}

	/**
	 * Set the word stressed syllable start information to true. Focus- and
	 * unstressed syllable start information is set to false than.
	 * 
	 */
	public void setSyllableStartWordStressed() {
		this.syllableStartWordStressed = true;
		syllableStartFocusstressed = syllableStartUnstressed = false;
	}

	/**
	 * Return f0 values as array of ints.
	 * 
	 * @return The array of ints accrding to the f0 values or null if none were
	 *         present.
	 */
	public int[] f0ValsToArray() {
		if (!hasF0Description())
			return null;
		int retVals[] = new int[f0vals.size()];
		int i = 0;
		for (Iterator iter = f0vals.iterator(); iter.hasNext();) {
			retVals[i++] = ((F0Val) iter.next()).getVal();
		}
		return retVals;
	}

	/**
	 * Set the f0 contour according to an array of integers. Expects same size
	 * of f0 contour and array.
	 * 
	 * @param ia
	 *            The array of new f0-values.
	 */
	public void arrayToF0Vals(int ia[]) {

		if (ia.length != f0vals.size())
			debugLogger.error("found bug: ia.length != f0vals.size()");
		int i = 0;
		for (Iterator iter = f0vals.iterator(); iter.hasNext();) {
			((F0Val) iter.next()).setVal(ia[i++]);
		}
	}

	/**
	 * Set the centralized information to true and the decentralized feature to
	 * false. Change the phoneme's name to centralized variant. Shorten the
	 * duration of the phoneme as defined by Constants.
	 * 
	 * @see Constants
	 */
	public void centralize() {
		if (decentral) {
			central = true;
			decentral = false;
			name = variant;
			changeDuration(Constants.centralizedDurationChanged);
		}
	}

	/**
	 * Set the decentralized information to true and the centralized feature to
	 * false. Change the phoneme's name to decentralized variant. Enlarge the
	 * duration of the phoneme as defined by Constants.
	 * 
	 * @see Constants
	 */
	public void decentralize() {
		if (central) {
			central = false;
			decentral = true;
			name = variant;
			changeDuration(120);
		}
	}

	/**
	 * Set the phoneme's variant in case of centralization. Set the phoneme as
	 * decentralized.
	 * 
	 * @param variant
	 *            The centralized variant of the phoneme.
	 */
	public void setCentralVariant(String variant) {
		decentral = true;
		central = false;
		this.variant = variant;
	}

	/**
	 * Set the phoneme's variant in case of decentralization. Set the phoneme as
	 * centralized.
	 * 
	 * @param variant
	 *            The decentralized variant of the phoneme.
	 */
	public void setDecentralVariant(String variant) {
		central = true;
		decentral = false;
		this.variant = variant;
	}

	/**
	 * Retrieve the variant's name, i.e. Sampa symbol.
	 * 
	 * @return The variant's name.
	 */
	public String getVariant() {
		return variant;
	}

	/**
	 * Test if the phoneme is centralized.
	 * 
	 * @return True if the phoneme is contralized, false otherwise.
	 */
	public boolean isCentral() {
		if (central)
			return true;
		return false;
	}

	/**
	 * Test if the phoneme is decentralized.
	 * 
	 * @return True if the phoneme is decentralized, false otherwise.
	 */
	public boolean isDecentral() {
		if (decentral)
			return true;
		return false;
	}

	/**
	 * Set the centralisation or decentralisation as in another phoneme.
	 * 
	 * @param p
	 *            The phoneme which's centralization feature is copied.
	 */
	public void copyVariant(Phoneme p) {
		if (p.isCentral()) {
			setDecentralVariant(p.getVariant());
		}
		if (p.isDecentral()) {
			setCentralVariant(p.getVariant());
		}
	}
}