/*
 * Created on 21.01.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.Font;

import javax.swing.JCheckBoxMenuItem;

import emofilt.Emofilt;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltCheckboxMenuitem extends JCheckBoxMenuItem {

	/**
	 * Constructor given a label.
	 * 
	 * @param s
	 *            String for label.
	 */
	public EmofiltCheckboxMenuitem(String s) {
		super(s);
		setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
				.parseInt(Emofilt._config.getString("mainFrame.font.style")),
				Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
	}

	/**
	 * Constructor given a label an a selection flag.
	 * 
	 * @param s
	 *            String for label.
	 * @param b
	 *            If true item will be selected.
	 */
	public EmofiltCheckboxMenuitem(String s, boolean b) {
		super(s, b);
		setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
				.parseInt(Emofilt._config.getString("mainFrame.font.style")),
				Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
	}

}