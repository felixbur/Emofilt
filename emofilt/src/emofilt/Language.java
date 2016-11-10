/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;
import org.apache.log4j.*;

/**
 * 
 * A language is a set of phonemes and replacement-rules. It corresponds with
 * the mbrola voices. They are read from a description file with xml-syntax
 * containing several languages and describing for each Phoneme their name and
 * manner.
 * 
 * @author Felix Burkhardt
 */
public class Language {

	/**
	 * the name of the xml-description.
	 */
	private String name = null;

	private Logger debugLogger = null;

	private String description = "";

	private String locale = "";
	private Vector phonemes = null;

	private String langname = null;

	private String voicename = null;

	private boolean supportsMultipleVoiceQualities = false;

	private boolean isMale = false;

	public Language() {
		this.debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	/**
	 * This sets the set of phonemes from a xml-description. must be called
	 * before calling classify() !
	 */
	public void setPhonemes(Vector ps) {
		phonemes = ps;
	}

	/**
	 * This classifies a phoneme (assignes features like manner or voiced)
	 * according to a language and returns it's features in a phoneme-object.
	 * 
	 * @param phonemeName
	 *            The name to be searched for in the language.
	 * @return Phoneme with features according to the name.
	 */
	public Phoneme classify(String phonemeName) {
		for (Iterator iter = phonemes.iterator(); iter.hasNext();) {
			Phoneme element = (Phoneme) iter.next();
			if (element.getName().compareTo(phonemeName) == 0) {
				return element;
			}
		}
		debugLogger.warn("phoneme name " + phonemeName
				+ " not found in language " + name);
		return null;
	}

	/**
	 * Return the languages name consisting of languagename and voicename, e.g.
	 * "de1" from "de" and "1"..
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	public String toString() {
		String ret = "";
		ret += "language: " + langname + ", voice: " + voicename;
		return ret;
	}

	/**
	 * Set the name. klanguagename and voicename are derived from that.
	 * 
	 * @param name
	 *            The name, eg "en1".
	 */
	public void setName(String name) {
		this.name = name;
		if (name.length() == 3) {
			langname = name.substring(0, 2);
			voicename = name.substring(2, 3);
		} else {
			debugLogger.error("langname (" + name + ") has ionvalid length.");
		}
	}

	/**
	 * Test whether the voice supports multiple voice qualities like de6 and
	 * de7. The can be reached by usgin "phonename"_soft or "phonname"_loud
	 * 
	 * @return Returns true if voice has more than one inventory.
	 */
	public boolean supportsMultipleVoiceQualities() {
		if (supportsMultipleVoiceQualities)
			return true;
		return false;
	}

	/**
	 * Sets the flag to support mutliple voicequality-inventories.
	 * 
	 * @param supportsMultipleVoiceQualities
	 *            The supportsMultipleVoiceQualities to set.
	 */
	public void setSupportsMultipleVoiceQualities(
			boolean supportsMultipleVoiceQualities) {
		this.supportsMultipleVoiceQualities = supportsMultipleVoiceQualities;
	}

	/**
	 * Returns the language name, e.g. if "fr1" it returns "fr".
	 * 
	 * @return Returns the langname.
	 */
	public String getLangname() {
		return langname;
	}

	/**
	 * Returns the voicename, e.g. if "fr1" it returns "1".
	 * 
	 * @return Returns the voicename.
	 */
	public String getVoicename() {
		return voicename;
	}

	/**
	 * Return a description read from the xml-file.
	 * 
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the locale, e.g. "en_US" as specified in the language configuration
	 * file.
	 * 
	 * @return The locale.
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Set the locale,
	 * 
	 * @param locale
	 *            The Locale.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

}