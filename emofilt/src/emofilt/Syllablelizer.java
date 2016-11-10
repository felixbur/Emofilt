/*
 * Created on 08.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;

import org.apache.log4j.Logger;

import emofilt.util.Util;

/**
 * This analyzes the phonemes-vector of an utterance and fills the syllable
 * vector.
 * 
 * @author Felix Burkhardt
 */
public class Syllablelizer {
	private Utterance utt;

	private Logger debugLogger;

	/**
	 * Constructor given an utterance.
	 * 
	 * @param utt
	 *            The utterance to be analyzed.
	 */
	public Syllablelizer(Utterance utt) {
		this.utt = utt;
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
	}

	/**
	 * Analyze the syllable structure of a phoneme-chain.
	 * 
	 */
	public void syllablelize() {
		if (utt.isSyllableAnnotated()) {
			fillSyllables();
			return;
		}

		Vector phonemes = utt.getPhonemes();
		if (phonemes.size() < 4) {
			((Phoneme) phonemes.firstElement()).setSyllableStart(true);
			fillSyllables();
			return;
		}

		int actSon, nextSon, overNextSon, prevSon = 0;
		Phoneme phon = null, nextPhon, overNextPhon, prevPhon = null;
		// ((Phoneme) phonemes.elementAt(0)).setSyllableStart(true);
		int i = 0;
		while (i < phonemes.size() - 2) {
			phon = (Phoneme) phonemes.elementAt(i);
			nextPhon = (Phoneme) phonemes.elementAt(i + 1);
			overNextPhon = (Phoneme) phonemes.elementAt(i + 2);
			if (i != 0) {
				prevPhon = (Phoneme) phonemes.elementAt(i - 1);
				prevSon = sonority(prevPhon);
			}
			actSon = sonority(phon);
			nextSon = sonority(nextPhon);
			overNextSon = sonority(overNextPhon);
			if (phon.getManner().compareTo(Phoneme.silence) == 0) {
				phon.setSyllableStart(true);
				nextPhon.setSyllableStart(true);
				i++;
			} else if (Util.isAffricate(phon, nextPhon)) {
				if (overNextPhon.getManner().compareTo(Phoneme.nasal) == 0) {
					overNextPhon.setSyllableStart(true);
					i += 2;
				} else if (actSon < overNextSon) {
					phon.setSyllableStart(true);
					i += 2;
				} else if (phon.isStop() && nextPhon.isFricative()
						&& overNextPhon.isStop()) {
					// jEts-tna:x
					overNextPhon.setSyllableStart(true);
					i += 2;
				}
			} else {
				if (actSon < nextSon) {
					phon.setSyllableStart(true);
					i++;
					// blaI-bn-I-m6 vs a-gnes-b@
					// if (phon.isStop()
					// && nextPhon.isNasal()
					// && overNextSon > 1) {
					// overNextPhon.setSyllableStart(true);
					// i++;
					// }
				} else if (i != 0) {
					// a-na-Ist, en-n-_-bIn
					if (prevSon == actSon && actSon > 1) {
						phon.setSyllableStart(true);
					}
				}
			}
			// control if last phonem was unvoiced and it's own syllable.
			if (prevPhon != null) {
				if (prevPhon.isSyllableStart() && prevSon < 2
						&& phon.isSyllableStart()) {
					prevPhon.setSyllableStart(false);
				}
			}
			i++;
		}
		// check last 2 phonemes
		phon = (Phoneme) phonemes.elementAt(phonemes.size() - 3);
		Phoneme beforeLastPhon = (Phoneme) phonemes
				.elementAt(phonemes.size() - 2);
		Phoneme lastPhon = (Phoneme) phonemes.lastElement();
		// dont allow nonvoiced final sylables
		if (sonority(lastPhon) < 2 && sonority(beforeLastPhon) < 2
				&& sonority(phon) == 1) {
			phon.setSyllableStart(false);
			beforeLastPhon.setSyllableStart(false);
			lastPhon.setSyllableStart(false);
		}
		if (lastPhon.getManner().compareTo(Phoneme.silence) == 0) {
			lastPhon.setSyllableStart(true);
		}
		if (phon.isVowel() && beforeLastPhon.isVowel())
			beforeLastPhon.setSyllableStart(true);
		if (beforeLastPhon.isVowel() && lastPhon.isVowel())
			beforeLastPhon.setSyllableStart(true);
		// first phoneme is always a start
		((Phoneme) phonemes.firstElement()).setSyllableStart(true);

		fillSyllables();
	}

	/**
	 * Fill syllable vector from phoneme-chain already annotated by syllable
	 * markers.
	 */
	public void fillSyllables() {
		Vector phonemes = utt.getPhonemes();
		Syllable syl = null;
		for (int i = 0; i < phonemes.size(); i++) {
			Phoneme phon = (Phoneme) phonemes.elementAt(i);
			if (phon.isSyllableStart()) {
				if (syl != null) {
					utt.getSyllables().add(syl);
					syl = null;
				}
				syl = new Syllable();
				if (phon.isSyllableStartFocusstressed()) {
					syl.setFocusStressed();
				} else if (phon.isSyllableStartWordStressed()) {
					syl.setWordStressed();
				}
				syl.getPhonemes().add(phon);
			} else {
				syl.getPhonemes().add(phon);
			}
		}
		utt.getSyllables().add(syl);
	}

	/**
	 * Get the utterance.
	 * 
	 * @return The utterance.
	 */
	public Utterance getUtterance() {
		return utt;
	}

	/**
	 * Set stress types for all syllables.
	 * Algorithm is global maximum of mean F0.
	 */
	public void setAccents() {
		if (utt.isSyllableAnnotated()) {
			return;
		}
		boolean setFocus = false;
		for (int i = 1; i < utt.getSyllables().size() - 1; i++) {
			Syllable syl = (Syllable) utt.getSyllables().elementAt(i);
			Syllable prevSyl = (Syllable) utt.getSyllables().elementAt(i - 1);
			Syllable nextSyl = (Syllable) utt.getSyllables().elementAt(i + 1);
			if (syl.getF0Mean() > prevSyl.getF0Mean()
					&& syl.getF0Mean() >= nextSyl.getF0Mean()) {
				syl.setFocusStressed();
				setFocus = true;
			}
		}
		// if there was no focus set (descending f0 contour)
		// set the first syllable as focus
		if (!setFocus) {
			((Syllable) utt.getSyllables().firstElement()).setFocusStressed();
		}

	}

	private int sonority(Phoneme p) {
		String manner = p.getManner();
		if (manner.compareTo(Phoneme.long_vowel) == 0) {
			return 3;
		} else if (manner.compareTo(Phoneme.short_vowel) == 0) {
			return 3;
		} else if (manner.compareTo(Phoneme.approximant) == 0) {
			return 2;
		} else if (manner.compareTo(Phoneme.nasal) == 0) {
			return 2;
		} else if (manner.compareTo(Phoneme.fricative_voiced) == 0) {
			return 1;
		} else if (manner.compareTo(Phoneme.fricative_voiceless) == 0) {
			return 1;
		} else if (manner.compareTo(Phoneme.stop_voiced) == 0) {
			return 1;
		} else if (manner.compareTo(Phoneme.stop_voiceless) == 0) {
			return 1;
		} else if (manner.compareTo(Phoneme.silence) == 0) {
			return 1;
		}
		debugLogger.error("Phoneme :" + p.getName() + " has no sonority");
		return -1;
	}

}