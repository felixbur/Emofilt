/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;

/**
 * Main class to model an utterance as a set of syllables.
 * 
 * @author Felix Burkhardt
 */
public class Utterance {
	private Vector phonemes;

	private Vector syllables;

	private boolean syllableAnnotated = false;

	/**
	 * Test is the syllables are filled-
	 * 
	 * @return True if syllable vector is filled, false otherwise.
	 */
	public boolean isSyllableAnnotated() {
		return syllableAnnotated;
	}

	/**
	 * Set the information that the syllable are filled to true;
	 * 
	 * @param syllableAnnotated
	 *            Set to true if syllables are filled.
	 */
	protected void setSyllableAnnotated() {
		this.syllableAnnotated = true;
	}

	/**
	 * Constructor. Initialized phonemes and syllables.
	 */
	public Utterance() {
		syllables = new Vector();
		phonemes = new Vector();
	}

	
	/**
	 * Return a String representation of the utterance.
	 * 
	 * @return A String representation of the utterance.
	 */
	public String toString() {
		String ret = "utterance, duration: " + getDuration() + "\n";
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme p = (Phoneme) iter.next();
			ret += p.getName();
			if (iter.hasNext())
				ret += " ";
		}
		;
		return ret;
	}

	/**
	 * Return the mean F0-value as mean of the syllable means.
	 * 
	 * @return The mean F0-value.
	 */
	public double getF0Mean() {
		int sum = 0;
		double mean = 0;
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable s = (Syllable) iter.next();
			sum++;
			mean += s.getF0Mean();
		}
		return mean / sum;
	}

	/**
	 * Return the mean F0-value of the last syllable.
	 * 
	 * @return The mean F0-value.
	 */
	public double getF0MeanOfLastSyllable() {
		return ((Syllable) syllables.lastElement()).getF0Mean();
	}

	/**
	 * Create a copy of the utterance.
	 * 
	 * @return A coipy of the utterance as a new Object.
	 */
	public Object clone() {
		Utterance ret = new Utterance();
		Vector syls = new Vector();
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable syl = (Syllable) iter.next();
			syls.add(syl.clone());

		}
		ret.setSyllables(new Vector(syls));
		if (syllableAnnotated)
			ret.setSyllableAnnotated();
		Vector ps = new Vector();
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme p = (Phoneme) iter.next();
			ps.add(p.clone());

		}
		ret.setPhonemes(ps);
		return ret;
	}

	/**
	 * Retrieve all phonemes information as a String. Note that the syllables
	 * are also filled with phonemes.
	 * 
	 * @return A String with all phonemes information.
	 */
	public String printPhonemes() {
		String ret = "";
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme p = (Phoneme) iter.next();
			ret += p.toString();
			if (iter.hasNext())
				ret += "\n";
		}
		return ret;
	}

	/**
	 * Retrieve the first f0 value that is larger than 0 from the syllables.
	 * 
	 * @return The first f0 value that is larger than 0 or 0 if none found.
	 */
	public int getFirstNonZeroF0Val() {
		for (int i = 0; i < syllables.size(); i++) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			int test = syl.getFirstNonZeroF0Val();
			if (test > 0) {
				return test;
			}
		}
		return 0;
	}

	/**
	 * Retrieve a String representation of the syllables Vector.
	 * 
	 * @return A String representation of the syllables Vector.
	 */
	public String printSyllables() {
		String ret = "";
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable s = (Syllable) iter.next();
			ret += s.printPhonemes();
			if (iter.hasNext())
				ret += "\n";
		}
		return ret;
	}

	/**
	 * Get the rounded number of frames according to the syllables Vector.
	 * 
	 * @return The rounded number of frames.
	 */
	public int getFrameNum() {
		int ret = 0;
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable s = (Syllable) iter.next();
			ret += s.getFrameNum();
		}
		return ret;
	}

	/**
	 * Return the Number of F0 values.
	 * 
	 * @return The number of F0 values.
	 */
	public int getF0ValNum() {
		int ret = 0;
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable s = (Syllable) iter.next();
			ret += s.getF0ValNum();
		}
		return ret;
	}

	/**
	 * Retrieve the last phoneme that is voiced from the syllables vector.
	 * 
	 * @return The last phoneme that is voiced.
	 */
	public Phoneme getLastVoicedPhoneme() {
		for (int i = syllables.size() - 1; i >= 0; i--) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			Vector ps = syl.getPhonemes();
			for (int j = ps.size() - 1; j >= 0; j--) {
				Phoneme p = (Phoneme) ps.elementAt(j);
				if (p.isVoiced()) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieve the first phoneme that has a voice information (f0 > 0) from the
	 * syllables vector.
	 * 
	 * @return The first phoneme that is voiced or null if none exists.
	 */
	public Phoneme getFirstVoicedPhoneme() {
		for (int i = 0; i < syllables.size(); i++) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			Vector ps = syl.getPhonemes();
			for (int j = 0; j < ps.size(); j++) {
				Phoneme p = (Phoneme) ps.elementAt(j);
				if (p.hasVoicing()) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieve the index of the first voiced syllable from the syllables
	 * vector.
	 * 
	 * @return The index or length of vector if none exists.
	 */
	public int getFirstVoicedSyllableIndex() {

		for (int i = 0; i < syllables.size(); i++) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			if (syl.isVoiced()) {
				return i;
			}
		}
		return syllables.size();
	}

	/**
	 * Retrieve the last syllable that contains a voiced phoneme from the
	 * syllables vector.
	 * 
	 * @return The last syllable that contains a voiced phoneme or null if none
	 *         exists.
	 */
	public Syllable getLastVoicedSyllable() {
		for (int i = syllables.size() - 1; i >= 0; i--) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			if (syl.isVoiced()) {
				return syl;
			}
		}
		return null;
	}

	/**
	 * Retrieve the index of the last syllable that contains a voiced phoneme
	 * from the syllables vector.
	 * 
	 * @return The index of the last syllable that contains a voiced phoneme or
	 *         -1 if none exists.
	 */
	public int getLastVoicedSyllableIndex() {
		for (int i = syllables.size() - 1; i >= 0; i--) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			if (syl.isVoiced()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Retrieve the first f0 mean value that is larger than 0 from the
	 * syllables.
	 * 
	 * @return The first f0 value that is larger than 0 or 0 if none found.
	 */
	public double getFirstNonZeroSylMean() {
		for (int i = 0; i < syllables.size(); i++) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			if (syl.getF0Mean() > 0) {
				return syl.getF0Mean();
			}
		}
		return 0;
	}

	/**
	 * Interpolate between the F0 values so that at every frame is one value.
	 * The interpolation algorithm of the phonemes gets passed the previous and
	 * the next value.
	 */
	public void modelF0Contour() {
		// one has to interpolate all f0-tracks by frame size in order for
		// the pitch algorithms to work
		int prevF0 = getFirstNonZeroF0Val();
		int nextF0 = 0;
		int lastF0 = getLastVoicedPhoneme().getLastF0Val();
		Phoneme prevPhoneme = getFirstVoicedPhoneme();
		int startSyl = getFirstVoicedSyllableIndex();
		int endSyl = getLastVoicedSyllableIndex();
		boolean first = true;
		for (int i = startSyl; i <= endSyl; i++) {
			Syllable syl = (Syllable) syllables.elementAt(i);
			Vector ps = syl.getPhonemes();
			for (int j = 0; j < ps.size(); j++) {
				Phoneme p = (Phoneme) ps.elementAt(j);
				if (p.hasVoicing()) {
					if (!first) {
						nextF0 = p.getFirstF0Val();
						prevPhoneme.modelTrack(prevF0, nextF0);
						prevF0 = prevPhoneme.getLastF0Val();
					} else {
						first = false;
					}
					prevPhoneme = p;
				}
			}
		}
		getLastVoicedPhoneme().modelTrack(prevF0, lastF0);
	}

	/**
	 * Retrieve the total duration of the syllables vector in milliseconds.
	 * 
	 * @return Duration in Milliseconds.
	 */
	public int getDuration() {
		int ret = 0;
		for (Iterator iter = syllables.iterator(); iter.hasNext();) {
			Syllable s = (Syllable) iter.next();
			ret += s.getDuration();
		}
		return ret;
	}

	/**
	 * Retrieve the phonemes vector. Note that there are also phonemes in the
	 * syllables, the phonemes are solely used in the processing step before the
	 * analysis of syllable structure.
	 * 
	 * @return The phonemes vector.
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
	 * Retrieve the syllables vector.
	 * 
	 * @return The syllables Vector.
	 * @see Syllable
	 */
	public Vector getSyllables() {
		return syllables;
	}

	/**
	 * Set the syllables vector.
	 * 
	 * @param syllables
	 *            The new syllables vector.
	 * @see Syllable
	 */
	public void setSyllables(Vector syllables) {
		this.syllables = syllables;
	}
}