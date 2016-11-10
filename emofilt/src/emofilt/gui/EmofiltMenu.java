/*
 * Created on 12.01.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.Font;

import javax.swing.JMenu;

import emofilt.Emofilt;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltMenu extends JMenu {
	/**
	 * Constructor given a text for the menu label.
	 * 
	 * @param s
	 *            The menu label.
	 */
	public EmofiltMenu(String s) {
		super(s);
		setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
				.parseInt(Emofilt._config.getString("mainFrame.font.style")),
				Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
	}

}