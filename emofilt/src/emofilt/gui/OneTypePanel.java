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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

import emofilt.ModificationPlugin;
import emofilt.util.Util;

/**
 * Special panel for a modificator that needs a single type.
 * 
 * @author Felix Burkhardt
 */
public class OneTypePanel  extends ModificationPanel {
    private EmofiltButton mainButton;
    private EmofiltLabel typeLabel;
    private EmofiltTypeChooser typeChooser;
    private HashMap initValues;

    /**
     * 
     */
    public OneTypePanel(ModificationPlugin mp, HashMap initValues, Vector types) {
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
        c.gridwidth = 1;
        c.gridy = 2;
        typeLabel = new EmofiltLabel((String) initValues.get("typeLabel"));
        typeLabel.setForeground(Util.getColorFromValues(initValues, "controlFG"));
        this.add(typeLabel, c);
        typeChooser = new EmofiltTypeChooser(types, initValues);
        typeChooser.addActionListener(this);
        c.gridx = 1;
        this.add(typeChooser, c);        
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
    }

    public void setTypeValue(String newVal) {
        typeChooser.setValue(newVal);
    }

    public void setDefault() {
        typeChooser.setValue((String) initValues.get("defaultType"));
    }


    private boolean isDefault(String s) {
        if (s.compareTo(
                (String) initValues.get("defaultType")) == 0) {
            return true;
        }
        return false;
    }

    public void stateChanged(ChangeEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        if (typeChooser.getValue() == null)
            return;
        if (isDefault(typeChooser.getValue())) {
            this
                    .setBackground(Util.getColorFromValues(initValues,
                            "controlBG"));
        } else {
            this.setBackground(Color.red);
        }
        firePropertyChange(null, null, null);
    }
    public String getTypeValue() {
        return typeChooser.getValue();
    }


    public String getName() {
        return getModificationPlugin().getName();
    }

}
