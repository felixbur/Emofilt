/*
 * Created on 18.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.io.FileReader;
import java.util.*;

import org.apache.log4j.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A manager class for the language-objects. Reads and writes a file containing
 * language descriptions in XML format.
 * 
 * @see emofilt.Language
 * 
 * @author Felix Burkhardt
 */
public class Languages {
	private Vector langNames = null;

	private Logger debugLogger = null;

	private Vector languages = null;

	public Vector getLangNames() {
		return langNames;
	}

	/**
	 * Initialize the object with a filename denoting the path to the language
	 * description file (xml-syntax).
	 * 
	 * @param languageFilePath
	 */
	public Languages(String languageFilePath) {
		try {
			debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
			XMLReader parser = XMLReaderFactory
					.createXMLReader("org.apache.xerces.parsers.SAXParser");
			LanguageContentHandler myHandler = new LanguageContentHandler();
			parser.setContentHandler(myHandler);
			parser.setFeature("http://xml.org/sax/features/validation", false);
			debugLogger.debug("lfp: " + languageFilePath);
			parser.parse(new InputSource(new FileReader(languageFilePath)));
			languages = myHandler.getLanguages();
			debugLogger.debug("loaded " + languages.size() + " languages");

			langNames = new Vector();
			for (Iterator liter = languages.iterator(); liter.hasNext();) {
				Language lang = (Language) liter.next();
				String langName = lang.getLangname();
				boolean found = false;
				for (Iterator iter = langNames.iterator(); iter.hasNext();) {
					String ln = (String) iter.next();
					if (ln.compareTo(langName) == 0) {
						found = true;
					}
				}
				if (!found) {
					langNames.add(langName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			debugLogger.error(e.toString());
		}
	}

	/**
	 * Return a vector with the language objects.
	 * 
	 * @return Vector with language objects or empty vector.
	 */
	public Vector getLanguages() {
		return languages;
	}

	/**
	 * Return all language-objects for one language ;-) eg de1, de3, de5 for
	 * language=de.
	 * 
	 * @param langName
	 *            The name of the language (2 characters).
	 * @return Vector with languages or empty language.
	 */
	public Vector getLanguages(String langName) {
		Vector ret = new Vector();
		for (Iterator iter = languages.iterator(); iter.hasNext();) {
			Language element = (Language) iter.next();
			if (element.getLangname().compareTo(langName) == 0) {
				ret.add(element);
			}
		}
		return ret;
	}
	/**
	 * Get all language/voice names as String array.
	 * @return The array.
	 */
	public String [] getLanguageNames() {
		String [] retA = new String[languages.size()];
		int i=0;
		for (Iterator iter = languages.iterator(); iter.hasNext();) {
			Language element = (Language) iter.next();
			retA[i++]=element.getName();
		}
		return retA;
	}

	/**
	 * Return a language (voice) identified by the name.
	 * 
	 * @param langName
	 *            The name of the language, eg. en1
	 * @return The language-object or null if not found
	 */
	public Language findLanguage(String langName) {
		Language ret = null;
		for (Iterator iter = languages.iterator(); iter.hasNext();) {
			Language element = (Language) iter.next();
			if (element.getName().compareTo(langName) == 0) {
				ret = element;
				break;
			}
		}
		return ret;
	}

}