/*
 * Created on 23.08.2005
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

import javax.swing.BorderFactory;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

import emofilt.ModificationPlugin;
import emofilt.util.Util;

/** 
 * Special panel for a modificator that needs two rates.
 * 
 * @author Felix Burkhardt
 */
public class TwoRatePanel  extends ModificationPanel {
    private EmofiltButton mainButton;
    private EmofiltSpinner spinner;
    private EmofiltLabel rateLabel;
    private EmofiltSpinner spinner2;
    private EmofiltLabel rateLabel2;
    private HashMap initValues;

    /**
     * 
     */
    public TwoRatePanel(ModificationPlugin mp, HashMap initValues) {
        super(mp);
        this.initValues = initValues;
        setToolTipText((String) initValues.get("tooltip"));
        this.setLayout(new GridBagLayout());
        this.setBackground(Util.getColorFromValues(initValues, "controlBG"));
        GridBagConstraints c = new GridBagConstraints();
        mainButton = new EmofiltButton((String) initValues.get("label"));
        mainButton.setToolTipText((String) initValues.get("button.tooltip"));
        mainButton.setBackground(Util.getColorFromValues(initValues, "controlBG"));
        mainButton.setForeground(Util.getColorFromValues(initValues, "controlFG"));
        mainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDefault();
            }
        });
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        this.add(mainButton, c);
        rateLabel = new EmofiltLabel((String) initValues.get("rateLabel"));
        rateLabel.setForeground(Util.getColorFromValues(initValues, "controlFG"));
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
        rateLabel2 = new EmofiltLabel((String) initValues.get("rateLabel2"));
        rateLabel2.setForeground(Util.getColorFromValues(initValues, "controlFG"));
        spinner2 = new EmofiltSpinner(new SpinnerNumberModel(Integer
                .parseInt((String) initValues.get("defaultRate2")), Integer
                .parseInt((String) initValues.get("minRate2")), Integer
                .parseInt((String) initValues.get("maxRate2")), Integer
                .parseInt((String) initValues.get("step2"))));
        spinner2.addChangeListener(this);
        c.gridx = 2;
        this.add(rateLabel2, c);
        c.gridx = 3;
        this.add(spinner2, c);
        
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
    }

    public void setRateValue(Integer newVal) {
        spinner.setValue(newVal);
    }
    public void setRateValue2(Integer newVal) {
        spinner2.setValue(newVal);
    }

    public void setDefault() {
        spinner.setValue(Integer.decode((String) initValues.get("defaultRate")));
        spinner2.setValue(Integer.decode((String) initValues.get("defaultRate2")));
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

    public String getRateValue() {
        SpinnerNumberModel snm = (SpinnerNumberModel) spinner.getModel();
        return snm.getNumber().toString();
    }

    public String getRateValue2() {
        SpinnerNumberModel snm = (SpinnerNumberModel) spinner2.getModel();
        return snm.getNumber().toString();
    }

    public String getName() {
        return getModificationPlugin().getName();
    }

}
