/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;
import emofilt.util.Util;

/**
 * A Syllable is a set of Phonemes with has a stress-type.
 * 
 * @author Felix Burkhardt
 */
public class Syllable {
	private boolean unStressed;
	private boolean wordStressed;
	private boolean focusStressed;
	private Vector phonemes;

	/**
	 * Constructor. The phonemes are initialized.
	 */
	public Syllable() {
		phonemes = new Vector();
		unStressed = true;
		wordStressed = false;
		focusStressed = false;
	}

	/**
	 * Get a new syllable object with copied values.
	 * 
	 * @return A copy of the syllable object.
	 */
	public Object clone() {
		Syllable syl = new Syllable();
		if (focusStressed)
			syl.setFocusStressed();
		if (wordStressed)
			syl.setWordStressed();
		Vector ps = new Vector();
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			ps.add(element.clone());
		}
		syl.setPhonemes(ps);
		return syl;
	}

	/**
	 * Retrieve the total rounded frame number of all phonemes.
	 * 
	 * @return The total rounded frame number.
	 * @see Constants
	 */
	public int getFrameNum() {
		int ret = 0;
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme p = (Phoneme) iter.next();
			ret += p.getFrameNum();
		}
		return ret;
	}

	/**
	 * Set all phonemes to centralized variants. Remove them of the duration
	 * gets too short.
	 * 
	 * @see Phoneme
	 */
	public void centralize() {
		for (int i = 0; i < phonemes.size(); i++) {
			Phoneme pho = (Phoneme) phonemes.elementAt(i);
			pho.centralize();
			if (pho.getDur() < Constants.frameSize && !pho.isSilence()) {
				phonemes.remove(pho);
				i--;
			}
		}
	}

	/**
	 * Set all phonemes to decentralized variant.
	 * 
	 * @see Phoneme
	 */
	public void decentralize() {
		for (int i = 0; i < phonemes.size(); i++) {
			((Phoneme) phonemes.elementAt(i)).decentralize();
		}
	}

	/**
	 * Retrieve a String representation of the syllable.
	 */
	public String toString() {
		String ret = "[";
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			ret += element.getName();
			if (iter.hasNext()) {
				ret += " ";
			}
		}
		ret += "] stress: " + printStress() + ", f0: " + getF0Mean();
		return ret;
	}

	/**
	 * Print all phoneme information.
	 * 
	 * @return String with phoneme information.
	 */
	public String printPhonemes() {
		String ret = toString();
		ret += "] stress: " + printStress() + ", f0: " + getF0Mean() + "\n";
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			ret += "\t" + element.toString();
			if (iter.hasNext())
				ret += "\n";
		}
		return ret;
	}

	/**
	 * Expand or compress the f0 values dependent on their location relative to
	 * mean value.
	 * 
	 * @param rate
	 *            The rate of change (100 no change, values > 100 expand, values <
	 *            100 compress).
	 * @param mean
	 *            The central value for displacement.
	 */
	public void changeF0Range(int rate, double mean) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description())
				element.changeF0Range(rate, mean);
		}
	}

	/**
	 * Add Jitter (F0-flutter) to all voiced phonemes.
	 * 
	 * @param rate
	 *            The rate of change.
	 */
	public void addJitter(int rate) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description())
				element.addJitter(rate);
		}
	}

	/**
	 * Change the vocal effort for all phonemes.
	 * 
	 * @param effort
	 *            The new vocal effort identifier String.
	 */
	public void changeEffort(String effort) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			((Phoneme) iter.next()).changeEffort(effort);
		}
	}

	/**
	 * Change the contour of the whole syllable.
	 * <ul>
	 * <li>for a straight contour all f0 values are set to the mean f0 value,
	 * regardless of the gradient.
	 * <li>for a rising contour it is modeled as a linear line between the
	 * first f0 value and a last value computed by the value given by the
	 * gradient and duration of the syllable as semitones per second.
	 * <li>for a falling contour it is modeled as a linear line between the
	 * mean f0 value of the first phoneme and a last value computed by the value
	 * given by the gradient and duration of the syllable as semitones per
	 * second.
	 * </ul>
	 * 
	 * @param contour
	 *            A contour identifier (straight, rise of fall).
	 * @param gradient
	 *            The steepness of the change. Sensible values range from 0-200.
	 */
	public void changeContour(String contour, int gradient) {
		int start = 0, end;
		boolean found = false;
		if (contour.compareTo(Constants.CONTOUR_STRAIGHT) == 0) {
			start = (int) getF0Mean();
			for (int phoi = 0; phoi < phonemes.size(); phoi++) {
				Phoneme p = (Phoneme) phonemes.elementAt(phoi);
				if (p.hasF0Description()) {
					p.interpolateF0Vec(start, start);
				}
			}
		} else if (contour.compareTo(Constants.CONTOUR_RISE) == 0) {
			found = false;
			for (int phoi = 0; phoi < phonemes.size(); phoi++) {
				Phoneme p = (Phoneme) phonemes.elementAt(phoi);
				if (p.hasF0Description()) {
					start = p.getFirstF0Val();
					break;
				}
			}
			for (int phoi = 0; phoi < phonemes.size(); phoi++) {
				Phoneme p = (Phoneme) phonemes.elementAt(phoi);
				if (p.hasF0Description()) {
					end = (int) Util.gradient2Hz(start, p.getDur(), gradient);
					p.interpolateF0Vec(start, end);
					start = end;
				}
			}
		} else if (contour.compareTo(Constants.CONTOUR_FALL) == 0) {
			for (int phoi = 0; phoi < phonemes.size(); phoi++) {
				Phoneme p = (Phoneme) phonemes.elementAt(phoi);
				if (p.hasF0Description()) {
					start = (int) p.getMeanF0();
					break;
				}
			}
			for (int phoi = 0; phoi < phonemes.size(); phoi++) {
				Phoneme p = (Phoneme) phonemes.elementAt(phoi);
				if (p.hasF0Description()) {
					end = start
							- ((int) Util.gradient2Hz(start, p.getDur(),
									gradient) - start);
					if (end < Constants.minF0Val) {
						end = Constants.minF0Val;
					}
					p.interpolateF0Vec(start, end);
					start = end;
				}
			}
		}
	}

	/**
	 * Add variability to the f0-contour by changing the range of all phoneme f0
	 * contours with syllable mean value as reference.
	 * 
	 * @param rate
	 *            The rate of change.
	 */
	public void changeVariability(int rate) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description())
				element.changeF0Range(rate, this.getF0Mean());
		}
	}

	/**
	 * Retrieve the first f0 value that is higher than 0.
	 * 
	 * @return The first f0 value that is higher than 0 or 0 if none is found.
	 */
	public int getFirstNonZeroF0Val() {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description()) {
				return ((F0Val) element.getF0vals().firstElement()).getVal();
			}
		}
		return 0;
	}

	/**
	 * Change the f0 mean, i.e. the absolute frequency location, of all
	 * phonemes.
	 * 
	 * @param rate
	 *            The rate of change in percent (100 is no change).
	 */
	public void changeMeanF0(int rate) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			((Phoneme) iter.next()).changeMeanF0(rate);
		}
	}

	/**
	 * Retrieve the mean value of all phoneme mean f0 values.
	 * 
	 * @return The mean f0 value.
	 */
	public double getF0Mean() {
		int sum = 0;
		int num = 0;
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description()) {
				sum += element.getMeanF0();
				num++;
			}
		}
		if (num > 0)
			return sum / num;
		else
			return 0;
	}

	/**
	 * Retrieve the total number of all existent f0 values in the phonemes. Note
	 * that this does not correspond to the frame number.
	 * 
	 * @return The total number of present f0 values.
	 */
	public int getF0ValNum() {
		int sum = 0;
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.hasF0Description()) {
				sum += element.getF0vals().size();
			}
		}
		return sum;
	}

	/**
	 * Retrieve the total duration of the syllable.
	 * 
	 * @return The duration in milliseconds.
	 */
	public int getDuration() {
		int sum = 0;
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			sum += element.getDur();
		}
		return sum;
	}

	/**
	 * Retrieve the stressed feature as a string.
	 * 
	 * @return The stressed feature if focus- or word-stressed, unstressed
	 *         otherwise.
	 * @see Constants
	 */
	public String printStress() {
		if (isFocusStressed())
			return Constants.FOCUS_STRESS;
		if (isWordStressed())
			return Constants.WORD_STRESS;
		else
			return Constants.UNSTRESS;
	}

	/**
	 * Change the duration of all phonemes. Drop phonemes if the get shorter
	 * than framesize.
	 * 
	 * @param rate
	 *            The rate of change in percent, 100 means no change.
	 * @see Constants
	 */
	public void changeDuration(int rate) {
		for (int i = 0; i < phonemes.size(); i++) {
			Phoneme pho = (Phoneme) phonemes.elementAt(i);
			pho.changeDuration(rate);
			if (pho.getDur() < Constants.frameSize && !pho.isSilence()) {
				phonemes.remove(pho);
				i--;
			}
		}
	}

	/**
	 * Change the duration of all phonemes belonging to a given manner. Drop
	 * phonemes if the get shorter than framesize.
	 * 
	 * @param rate
	 *            The rate of change in percent, 100 means no change.
	 * @param manner
	 *            The manner of the phonemes.
	 * @see Constants
	 */
	public void changeMannerSpeechRate(int rate, String manner) {
		for (int i = 0; i < phonemes.size(); i++) {
			Phoneme pho = (Phoneme) phonemes.elementAt(i);
			if (pho.getManner().compareTo(manner) == 0) {
				pho.changeDuration(rate);
				if (pho.getDur() < Constants.frameSize && !pho.isSilence()) {
					phonemes.remove(pho);
					i--;
				}
			}
		}
	}

	/**
	 * Retrieve the information whether this syllable has focus stress.
	 * 
	 * @return True if the syllable id focus stressed, false otherwise.
	 */
	public boolean isFocusStressed() {
		return focusStressed;
	}

	/**
	 * Test if syllable contains a voiced phoneme.
	 * 
	 * @return If at lest one of the phonemes has f0-value above zero, false otherwise.
	 */
	public boolean isVoiced() {
		for (int i = 0; i < phonemes.size(); i++) {
			Phoneme pho = (Phoneme) phonemes.elementAt(i);
			if (pho.isVoiced()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set this syllable as focus stressed and set all other stress-tapes to
	 * false.
	 */
	public void setFocusStressed() {
		this.focusStressed = true;
		unStressed = false;
		wordStressed = false;
	}

	/**
	 * Retrieve the vector of phonemes.
	 * 
	 * @return The vector of phonemes.
	 * @see Phoneme
	 */
	public Vector getPhonemes() {
		return phonemes;
	}

	/**
	 * Set the phonemes vector.
	 * 
	 * @param phonemes
	 *            The new phonemes vector.
	 * @see Phoneme
	 */
	public void setPhonemes(Vector phonemes) {
		this.phonemes = phonemes;
	}

	/**
	 * Retrieve the information whether this syllable is unstressed.
	 * 
	 * @return True if the syllable is unstressed, false otherwise.
	 */
	public boolean isUnStressed() {
		return unStressed;
	}

	/**
	 * Set this syllable as unstressed and set all other stress-tapes to false.
	 */
	public void setUnStressed() {
		this.unStressed = true;
		wordStressed = false;
		focusStressed = false;
	}

	/**
	 * Retrieve the information whether this syllable has word stress.
	 * 
	 * @return True if the syllable is word stressed, false otherwise.
	 */
	public boolean isWordStressed() {
		return wordStressed;
	}

	/**
	 * Set this syllable as word stressed and set all other stress-tapes to
	 * false.
	 */
	public void setWordStressed() {
		this.wordStressed = true;
		unStressed = false;
		focusStressed = false;
	}
}