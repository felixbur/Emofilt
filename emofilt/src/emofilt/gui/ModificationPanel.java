/*
 * Created on 27.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import emofilt.ModificationPlugin;

/**
 * Generic panel for a single modificator. It is subclassed by more specialized
 * panels.
 * 
 * @author Felix Burkhardt
 */
public class ModificationPanel extends JPanel implements ChangeListener,
		ActionListener {
	private ModificationPlugin mp;
	private PropertyChangeListener pcl;

	/**
	 * Constructor given the modificator.
	 * 
	 * @param mp
	 *            The modificator.
	 */
	public ModificationPanel(ModificationPlugin mp) {
		super();
		this.mp = mp;
	}

	/**
	 * Retrieve the plugin.
	 * 
	 * @return The modificator plugin.
	 */
	public ModificationPlugin getModificationPlugin() {
		return mp;
	}

	public void setPropertyChangeListener(PropertyChangeListener pcl) {
		this.addPropertyChangeListener(pcl);
	}

	public void stateChanged(ChangeEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
	}
}