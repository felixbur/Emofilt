/*
 * Created on 14.01.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.Dimension;
import java.awt.Font;

import emofilt.Emofilt;
import emofilt.util.Util;

import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltSlider extends JSlider {

	/**
	 * Constructor with options.
	 * 
	 * @param orientation
	 *            Horizontal or vertical, see JSlider constants.
	 * @param min
	 *            The minimal value.
	 * @param max
	 *            The maximum value.
	 * @param val
	 *            The initial value.
	 */
	public EmofiltSlider(int orientation, int min, int max, int val) {
		super(orientation, min, max, val);
		setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
				.parseInt(Emofilt._config.getString("mainFrame.font.style")),
				Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
	}

}
