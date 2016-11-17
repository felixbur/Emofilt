/*
 * Created on 22.11.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import emofilt.Emofilt;
import emofilt.util.Util;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltTypeChooser extends JComboBox {
	/**
	 * Constructor with options.
	 * 
	 * @param types
	 *            The types to choose from.
	 * @param initValues
	 *            The initial values.
	 */
	public EmofiltTypeChooser(Vector types, HashMap initValues) {
		super(types);
		setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
				.parseInt(Emofilt._config.getString("mainFrame.font.style")),
				Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
		this.setBackground(Util.getColorFromValues(initValues, "controlBG"));
		this.setForeground(Util.getColorFromValues(initValues, "controlFG"));
	}

	/**
	 * Get the actual value.
	 * 
	 * @return The selected value.
	 */
	public String getValue() {
		return (String) this.getSelectedItem();
	}

	/**
	 * Set the selected value.
	 * 
	 * @param c
	 *            The value to set.
	 */
	public void setValue(String c) {
		setSelectedItem(c);
	}
}