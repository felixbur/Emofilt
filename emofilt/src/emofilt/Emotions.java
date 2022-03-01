/*
 * Created on 06.10.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.awt.Color;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.felix.util.ColorUtil;

/**
 * 
 * This class is used as a manager for the Emotion-objects. Emotion objects are
 * simply JDom-element objects. The class reads and writes an XML file
 * containing the emotion description.
 * 
 * @see org.jdom.Element
 * @author Felix Burkhardt
 */
public class Emotions {
	private Document emotionsTree;

	private Element emotionsRoot;

	private Logger debugLogger = null;

	private String emoFileName;

	/**
	 * Initializes the emotions-object with a filename pointing to the xml-file
	 * containing emotion-descriptions.
	 * 
	 * @param emotionsFilename
	 *            The filename.
	 */
	public Emotions(String emotionsFilename) {
		emoFileName = emotionsFilename;
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
		try {
			SAXBuilder builder = new SAXBuilder(false);
			emotionsTree = builder.build(new File(emotionsFilename));
			emotionsRoot = emotionsTree.getRootElement();
			List emotions = emotionsRoot.getChildren();
			for (Iterator iter = emotions.iterator(); iter.hasNext();) {
				Element emotion = (Element) iter.next();
				String emotionName = emotion.getAttribute("name").getValue();
				//debugLogger.debug(emotionName);
			}
			debugLogger.debug("loaded: "+getEmotionNames());
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger.error(e.getMessage());
		}
	}

	/**
	 * Get a list of emotion dom elemens.
	 * 
	 * @return The list of emotions.
	 */
	public List getEmotions() {
		if (emotionsRoot != null)
			return emotionsRoot.getChildren();
		return null;
	}

	/**
	 * Returns a vector with Strings denoting the names of the stored emotions.
	 * 
	 * @return The vector of name-Strings.
	 */
	public Vector getEmotionNames() {
		Vector ret = new Vector();
		List emotions = emotionsRoot.getChildren();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			ret.add(emotion.getAttribute("name").getValue());
		}
		return ret;
	}

	/**
	 * Return the emotion with the specified name.
	 * 
	 * @param name
	 *            The emotion-name.
	 * @return The emotion (jdom-element) or null if not found.
	 */
	public Element getEmotion(String name) {
		List emotions = emotionsRoot.getChildren();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emotionName = emotion.getAttribute("name").getValue();
			if (emotionName.compareTo(name) == 0) {
				debugLogger.debug("returning emotion: " + name);
				return emotion;
			}
		}
		debugLogger.warn("emotion \"" + name + "\" not found");
		return null;
	}

	/**
	 * Return the emotion with the specified name.
	 * 
	 * @param name
	 *            The emotion-name.
	 * @return The emotion (jdom-element) or null if not found.
	 */
	public void setColor(Element emotion, Color color) {
		emotion.setAttribute("color", ColorUtil.colorToHex(color));
	}

	public Element getDefautEmotion() {
		List emotions = emotionsRoot.getChildren();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emotionName = emotion.getAttribute("name").getValue();
			if (emotionName.compareTo(Constants.NEUTRAL_EMOTION) == 0) {
				debugLogger.debug("returning emotion: "
						+ Constants.NEUTRAL_EMOTION);
				return emotion;
			}
		}
		debugLogger.warn("emotion " + Constants.NEUTRAL_EMOTION + " not found");
		return null;

	}

	public String getDescription(String emoname) {
		List emotions = emotionsRoot.getChildren();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emotionName = emotion.getAttribute("name").getValue();
			if (emotionName.compareTo(emoname) == 0) {
				Element descriptionElem = emotion.getChild("description");
				if (descriptionElem != null) {
					return descriptionElem.getText();
				} else {
					return Emofilt._config
							.getString("noEmotionDescriptionMessage");
				}
			}
		}
		debugLogger.warn("emotion \"" + emoname + "\" not found");
		return "";
	}

	/**
	 * Add an emotion-element to the document tree. If the emotion (identified
	 * by name) is already contained, it will be removed before.
	 * 
	 * @param emo
	 *            The emotion to be added.
	 */
	public void addEmotion(Element emo) {
		List emotions = emotionsRoot.getChildren();
		String emoName = emo.getAttribute("name").getValue();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emotionName = emotion.getAttribute("name").getValue();
			debugLogger.debug(emotionName);
			if (emotionName.compareTo(emoName) == 0) {
				emotionsRoot.removeContent(emotion);
				break;
			}
		}
		emotionsRoot.addContent(emo);
	}

	/**
	 * Remove an emotion description from content root.
	 * 
	 * @param emoName
	 *            The name.
	 */
	public void deleteEmotion(String emoName) {
		List emotions = emotionsRoot.getChildren();
		for (Iterator iter = emotions.iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emotionName = emotion.getAttribute("name").getValue();
			debugLogger.debug(emotionName);
			if (emotionName.compareTo(emoName) == 0) {
				emotionsRoot.removeContent(emotion);
				break;
			}
		}
	}

	/**
	 * Add an empty emotion to content root.
	 * 
	 * @param name
	 *            The emotion's name.
	 * @return The jdom element.
	 */
	public Element addEmotion(String name) {
		Element emoElem = new Element("emotion");
		emoElem.setAttribute("name", name);
		addEmotion(emoElem);
		return emoElem;
	}

	/**
	 * Save the current emotions back to the file. Empty modification-types get
	 * deleted.
	 * 
	 */
	public void save() {
		try {
			// first remove empty modification categories
			List emotions = emotionsRoot.getChildren();
			for (Iterator iter = emotions.iterator(); iter.hasNext();) {
				Element emotion = (Element) iter.next();
				List changes = emotion.getChildren();
				for (Iterator iterator = changes.iterator(); iterator.hasNext();) {
					Element element = (Element) iterator.next();
					if (element.getChildren().size() == 0) {
						emotion.removeContent(element);
					}
				}
			}
			XMLOutputter outputter = new XMLOutputter();
			FileWriter writer = new FileWriter(emoFileName);
			outputter.output(emotionsTree, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger.error(e);
		}
	}

}