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

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * Emofilt specific GUI element.
 * 
 * @author Felix Burkhardt
 */
public class EmofiltSpinner extends JSpinner {

    /**
     * Constructor given a model.
     */
    public EmofiltSpinner(SpinnerModel s) {
        super(s);
        setPreferredSize(new Dimension(40, 20));
        ((JSpinner.DefaultEditor) this.getEditor()).getTextField().setFont(new Font(Emofilt._config.getString("mainFrame.font.name"), Integer
                .parseInt(Emofilt._config.getString("mainFrame.font.style")),
                Integer.parseInt(Emofilt._config.getString("mainFrame.font.size"))));
        ((JSpinner.DefaultEditor) this.getEditor()).getTextField().setBackground(Util.getColorFromUidf("spinnerBG"));
        ((JSpinner.DefaultEditor) this.getEditor()).getTextField().setForeground(Util.getColorFromUidf("spinnerFG"));
    }

}
