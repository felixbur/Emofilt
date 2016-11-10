/*
 * Created on 06.10.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.jdom.*;

import emofilt.*;

/**
 * Listener class for the modification elements.
 * 
 * @author Felix Burkhardt
 */
public class GuiChangeListener implements PropertyChangeListener {
	private Logger debugLogger;

	private Emofilt emofilt;

	/**
	 * Constructor passes the main object.
	 */
	public GuiChangeListener(Emofilt ed) {
		emofilt = ed;
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
	}

	/**
	 * If a change occured the new value is set in the current emotion and the
	 * utterance gets modified.
	 */
	public void propertyChange(PropertyChangeEvent e) {
		Element emotion = emofilt.getActEmotion();
		if (emotion == null)
			return;
		try {
			ModificationPlugin mpi = ((ModificationPanel) e.getSource())
					.getModificationPlugin();
			emotion = mpi.setEmotion(emotion);
		} catch (Exception ex) {
			ex.printStackTrace();
			debugLogger.error(ex);
		}
		emofilt.modifyUtterance();
	}
}