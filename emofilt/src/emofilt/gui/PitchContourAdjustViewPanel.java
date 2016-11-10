/*
 * Created on 04.01.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import emofilt.Emofilt;
import emofilt.util.Util;

/**
 * The panel that contains the checkboxes to influence the pitch display.
 * 
 * @author Felix Burkhardt
 */
public class PitchContourAdjustViewPanel extends JPanel {

	private JCheckBox cbPhonemeBorders = new JCheckBox(Emofilt._config
			.getString("mainFrame.pitchContour.phonemeBorders"), false);

	private JCheckBox cbPhonemeLabels = new JCheckBox(Emofilt._config
			.getString("mainFrame.pitchContour.phonemeLabels"), true);

	private JCheckBox cbFreqscale = new JCheckBox(Emofilt._config
			.getString("mainFrame.pitchContour.freqscale"), true);

	private JCheckBox cbTimescale = new JCheckBox(Emofilt._config
			.getString("mainFrame.pitchContour.timescale"), true);

	private JCheckBox cbSyllableBorders = new JCheckBox(Emofilt._config
			.getString("mainFrame.pitchContour.syllableBorders"), true);

	private PitchContourAdjustViewPanel pcavpthis;
	private PitchContourScreen pcs;

	/**
	 * Constructor given the upper panel.
	 */
	public PitchContourAdjustViewPanel(PitchContourScreen pcs) {
		super();
		setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbPhonemeBorders.setFont(new Font("Dialog", 0, 10));
		cbPhonemeBorders.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbPhonemeBorders.setForeground(Util.getColorFromUidf("upperpanel.FG"));
		cbFreqscale.setFont(new Font("Dialog", 0, 10));
		cbFreqscale.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbFreqscale.setForeground(Util.getColorFromUidf("upperpanel.FG"));
		cbPhonemeLabels.setFont(new Font("Dialog", 0, 10));
		cbPhonemeLabels.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbPhonemeLabels.setForeground(Util.getColorFromUidf("upperpanel.FG"));
		cbSyllableBorders.setFont(new Font("Dialog", 0, 10));
		cbSyllableBorders.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbSyllableBorders.setForeground(Util.getColorFromUidf("upperpanel.FG"));
		cbTimescale.setFont(new Font("Dialog", 0, 10));
		cbTimescale.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		cbTimescale.setForeground(Util.getColorFromUidf("upperpanel.FG"));
		pcavpthis = this;
		this.pcs = pcs;
		pcs.setDrawBorders(false);

		cbPhonemeBorders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbPhonemeBorders.isSelected())
					pcavpthis.pcs.setDrawBorders(true);
				else
					pcavpthis.pcs.setDrawBorders(false);
				pcavpthis.pcs.repaint();
			}
		});
		cbFreqscale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbFreqscale.isSelected())
					pcavpthis.pcs.setDrawFreqScale(true);
				else
					pcavpthis.pcs.setDrawFreqScale(false);
				pcavpthis.pcs.repaint();
			}
		});
		cbPhonemeLabels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbPhonemeLabels.isSelected())
					pcavpthis.pcs.setDrawLabels(true);
				else
					pcavpthis.pcs.setDrawLabels(false);
				pcavpthis.pcs.repaint();
			}
		});
		cbTimescale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbTimescale.isSelected())
					pcavpthis.pcs.setDrawTimeScale(true);
				else
					pcavpthis.pcs.setDrawTimeScale(false);
				pcavpthis.pcs.repaint();
			}
		});
		cbSyllableBorders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbSyllableBorders.isSelected())
					pcavpthis.pcs.setDrawSyllables(true);
				else
					pcavpthis.pcs.setDrawSyllables(false);
				pcavpthis.pcs.repaint();
			}
		});
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		add(cbPhonemeBorders, c);
		c.gridy = 1;
		add(cbSyllableBorders, c);
		c.gridy = 2;
		add(cbPhonemeLabels, c);
		c.gridy = 3;
		add(cbFreqscale, c);
		c.gridy = 4;
		add(cbTimescale, c);
	}

}