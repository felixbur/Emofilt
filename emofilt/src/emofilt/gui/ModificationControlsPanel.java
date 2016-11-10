/*
 * Created on 05.10.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import emofilt.*;

import org.apache.log4j.*;
import org.jdom.Element;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import emofilt.util.Util;

/**
 * Generic panel for the your modification types (pitch, duration, articulation,
 * phonation).
 * 
 * @author Felix Burkhardt
 */
public class ModificationControlsPanel extends JPanel {
	private MainFrame mf;

	private Vector modificationPlugins;

	private Logger debugLogger;

	private String type = "";

	/**
	 * Constructor, given the main GUI object and the type.
	 * 
	 * @param mf
	 *            The main GUI object.
	 * @param type
	 *            The type (pitch, duration, articulation, phonation)
	 */
	public ModificationControlsPanel(MainFrame mf, String type) {
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
		this.type = type;
		this.mf = mf;
		this.setBackground(Util.getColorFromUidf(type + ".controlBG"));
		JLabel topLabel = new JLabel(Emofilt._config.getString(type
				+ ".label.label"));
		topLabel.setForeground(Util.getColorFromUidf(type + ".controlFG"));
		modificationPlugins = mf.emofilt.getModificationPluginManager()
				.getModificationsByType(type);
		this.add(topLabel);
		for (Iterator iter = modificationPlugins.iterator(); iter.hasNext();) {
			ModificationPlugin mod = (ModificationPlugin) iter.next();
			this.add(mod.getPanel());
		}
	}

	/**
	 * Set new values for all modificators.
	 * 
	 * @param emotion
	 *            The emotiuon object containing the values.
	 */
	public void setEmotion(Element emotion) {
		Element modChanges = emotion.getChild(type);
		if (modChanges != null) {
			for (Iterator iter = modificationPlugins.iterator(); iter.hasNext();) {
				ModificationPlugin mod = (ModificationPlugin) iter.next();
				mod.setGui(emotion);
			}
		} else {
			for (Iterator iter = modificationPlugins.iterator(); iter.hasNext();) {
				ModificationPlugin element = (ModificationPlugin) iter.next();
				element.setGuiDefault();
			}
		}

	}
}