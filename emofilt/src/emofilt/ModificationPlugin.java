/*
 * Created on 21.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;
import org.jdom.Element;

import emofilt.gui.ModificationPanel;

/**
 * <p>
 * Interface for the plugin system of emofilt.
 * <p>
 * Algorithms to modify utterances should implement this.
 * <p>
 * In order to implement a new modification, authors should
 * <ul>
 * <li>implement this interface as a class in the plug-in package.
 * <li>add its name to the emofilt-config file
 * <li>add an initialisation file in the int directory.
 * </ul>
 * <p>
 * this is an example of the initialisation file:
 * 
 * <pre>
 * name|f0Mean
 * label|level
 * type|pitch
 * defaultRate|100
 * minRate|0
 * maxRate|400
 * step|10
 * rateLabel|rate
 * tooltip|<html>Change the overall level of pitchcontour in percent.
 * Values higher than 100 increase, values lower than that decrease the level.
 * button.tooltip|Click the label to reset the values to default.
 * # Foreground color 
 * controlFG|white 
 * # Background color
 * controlBG|lightgrey
 * </pre>
 * </p>
 * 
 * 
 * @author Felix Burkhardt
 */
public interface ModificationPlugin {
	/**
	 * Initialize Modification and give a logger object.
	 * 
	 * @param logger
	 *            A log4j logger.
	 * @param useGui
	 *            Indicate whether the GUI will be displayed.
	 */
	public void init(Logger logger, boolean useGui);

	/**
	 * Change an utterance according to an emotion-description, perhaps
	 * depending on the chosen language.
	 * 
	 * @param utt
	 *            The original utterance.
	 * @param emotion
	 *            The emotion as a jdom element.
	 * @param rate
	 *            The rate of change as a value between 0 and 1. A value of 1
	 *            means full effect, 0 no change.
	 * @param lang
	 *            The language of the utterance.
	 * @return The modified utterance.
	 */
	public Utterance modify(Utterance utt, Element emotion, double rate,
			Language lang);

	/**
	 * Return the type of modification.
	 * 
	 * @return Either pitch, duration, phonation or articulation.
	 */
	public String getModificationType();

	/**
	 * Change the Gui because the Emotion might have changed.
	 * 
	 * @param emotion
	 *            The emotion as a jdom element.
	 */
	public void setGui(Element emotion);

	/**
	 * Change the emotion because the user made some change.
	 * 
	 * @param emotion
	 *            The emotion as a jdom element.
	 * @return The modified emotion as a jdom element.
	 */
	public Element setEmotion(Element emotion);

	/**
	 * Set a PropertyChangeListener so emofilt knows about gui-changes.
	 * 
	 */
	public void setPropertyChangeListener(PropertyChangeListener pcl);

	/**
	 * return a unique identifier.
	 * 
	 * @return The name.
	 */
	public String getName();

	/**
	 * Return the variables for debugging.
	 * 
	 * @return The values of name, type etc.
	 */
	public String toString();

	/**
	 * Set GUI to default value;
	 * 
	 */
	public void setGuiDefault();

	/**
	 * Return the panel for the GUI to set values.
	 * 
	 * @return The panel for the GUI.
	 */
	public ModificationPanel getPanel();

}
