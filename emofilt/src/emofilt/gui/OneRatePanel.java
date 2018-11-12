/*
 * Created on 24.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import emofilt.util.Util;
import javax.swing.BorderFactory;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import emofilt.ModificationPlugin;

/**
 * Special panel for a modificator that needs a single rate.
 * 
 * @author Felix Burkhardt
 */
public class OneRatePanel extends ModificationPanel {
	private EmofiltButton mainButton;

	private EmofiltSpinner spinner;

	private EmofiltLabel rateLabel;

	private HashMap initValues;

	/**
	 * 
	 */
	public OneRatePanel(ModificationPlugin mp, HashMap initValues) {
		super(mp);
		this.initValues = initValues;
		setToolTipText((String) initValues.get("tooltip"));
		this.setLayout(new GridBagLayout());
		this.setBackground(Util.getColorFromValues(initValues, "controlBG"));
		GridBagConstraints c = new GridBagConstraints();
		mainButton = new EmofiltButton((String) initValues.get("label"));
		mainButton.setToolTipText((String) initValues.get("button.tooltip"));
		mainButton.setBackground(Util.getColorFromValues(initValues,
				"controlBG"));
		mainButton.setForeground(Util.getColorFromValues(initValues,
				"controlFG"));
		mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefault();
			}
		});
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		this.add(mainButton, c);
		rateLabel = new EmofiltLabel((String) initValues.get("rateLabel"));
		rateLabel.setForeground(Util
				.getColorFromValues(initValues, "controlFG"));
		spinner = new EmofiltSpinner(new SpinnerNumberModel(Integer
				.parseInt((String) initValues.get("defaultRate")), Integer
				.parseInt((String) initValues.get("minRate")), Integer
				.parseInt((String) initValues.get("maxRate")), Integer
				.parseInt((String) initValues.get("step"))));
		spinner.addChangeListener(this);
		c.gridwidth = 1;
		c.gridy = 2;
		this.add(rateLabel, c);
		c.gridx = 1;
		this.add(spinner, c);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.setBorder(border);
	}

	public void setValue(Integer newVal) {
		spinner.setValue(newVal);
	}

	public void setDefault() {
		spinner
				.setValue(Integer
						.decode((String) initValues.get("defaultRate")));
	}

	private boolean isDefault(Number n) {
		if (String.valueOf(n.intValue()).compareTo(
				(String) initValues.get("defaultRate")) == 0) {
			return true;
		}
		return false;
	}

	public void stateChanged(ChangeEvent e) {
		SpinnerNumberModel snm = (SpinnerNumberModel) spinner.getModel();
		if (isDefault(snm.getNumber())) {
			this
					.setBackground(Util.getColorFromValues(initValues,
							"controlBG"));
		} else {
			this.setBackground(Color.red);
		}
		firePropertyChange(null, null, null);
	}

	public void actionPerformed(ActionEvent e) {
		firePropertyChange(null, null, null);
	}

	public String getValue() {
		SpinnerNumberModel snm = (SpinnerNumberModel) spinner.getModel();
		return snm.getNumber().toString();
	}

	public String getName() {
		return getModificationPlugin().getName();
	}
}