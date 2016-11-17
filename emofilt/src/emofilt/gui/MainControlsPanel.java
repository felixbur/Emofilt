/*
 * Created on 05.10.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import emofilt.*;


import org.apache.log4j.*;

import com.felix.util.PlayWave;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import emofilt.util.Util;

/**
 * The panel for the main control buttons.
 * 
 * @author Felix Burkhardt
 */
public class MainControlsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MainFrame mf;

	private JButton playButton;

	private JButton saveButton;
	private JButton savePhoButton;

	private JButton updateButton;

	private Logger _logger;

	private EmofiltSlider intensitySlider;

	private Runtime runtime;

	private String tmpAudioFilePath = "";

	/**
	 * Constructor given the GUI object.
	 * 
	 * @param mf
	 *            The main frame.
	 */
	public MainControlsPanel(MainFrame mf) {
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		runtime = Runtime.getRuntime();
		this.mf = mf;
		saveButton = new JButton(new ImageIcon(getClass().getResource("/res/save.gif")));
		saveButton.setMnemonic(Emofilt._config.getString(
				"mainframe.buttons.mnemonic.save").charAt(0));
		saveButton.setToolTipText(Emofilt._config
				.getString("mainframe.buttons.save.tooltip"));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		savePhoButton = new JButton(new ImageIcon(getClass().getResource("res/savePho.gif")));
		savePhoButton.setMnemonic(Emofilt._config.getString(
				"mainframe.buttons.mnemonic.savePho").charAt(0));
		savePhoButton.setToolTipText(Emofilt._config
				.getString("mainframe.buttons.savePho.tooltip"));
		savePhoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePhoFile();
			}
		});

		updateButton = new JButton(new ImageIcon(getClass().getResource("res/refresh.gif")));
		updateButton.setMnemonic(Emofilt._config.getString(
				"mainframe.buttons.mnemonic.update").charAt(0));
		updateButton.setToolTipText(Emofilt._config
				.getString("mainframe.buttons.update.tooltip"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		playButton = new JButton(new ImageIcon(getClass().getResource("res/play.gif")));
		playButton.setMnemonic(Emofilt._config.getString(
				"mainframe.buttons.mnemonic.play").charAt(0));
		playButton.setToolTipText(Emofilt._config
				.getString("mainframe.buttons.play.tooltip"));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});
		intensitySlider = new EmofiltSlider(JSlider.HORIZONTAL, Integer
				.parseInt(Emofilt._config.getString("mainFrame.globalRate.min")),
				Integer.parseInt(Emofilt._config
						.getString("mainFrame.globalRate.max")), Integer
						.parseInt(Emofilt._config
								.getString("mainFrame.globalRate.val")));
		intensitySlider.setMajorTickSpacing(20);
		intensitySlider.setPaintTicks(true);
		intensitySlider.setToolTipText(Emofilt._config
				.getString("mainFrame.globalRate.tooltip"));
		intensitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int intensity = (int) source.getValue();
					changeUtt(intensity);
				}
			}
		});
		EmofiltButton intensitylabel = new EmofiltButton(Emofilt._config
				.getString("mainFrame.globalRate.label"));
		intensitylabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRateDefault();
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		this.add(updateButton, c);
		this.add(saveButton, c);
		this.add(savePhoButton, c);
		this.add(playButton, c);
		this.add(intensitylabel, c);
		this.add(intensitySlider, c);
		setBackground(Util.getColorFromUidf("lowerpanel.BG"));

		tmpAudioFilePath = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpAudioFile");

	}

	private void changeUtt(int i) {
		mf.emofilt.setGlobalRate(i / 100.0);
		mf.emofilt.modifyUtterance();

	}
	private void setRateDefault() {
		int defaultRate = Integer.parseInt(Emofilt._config
		.getString("mainFrame.globalRate.val"));
		intensitySlider.setValue(defaultRate);
		mf.emofilt.setGlobalRate(defaultRate / 100.0);
		mf.emofilt.modifyUtterance();
		
	}
	
	
	private void update() {
		// regenerate utterance as the voice might have changed the conditions
		mf.emofilt.modifyUtterance();
	}

	private void save() {
		mf.save();
	}

	private void savePhoFile() {
		mf.savePhoFile();
	}

	private void play() {
		Runnable runner = new Runnable() {
			public void run() {
				mf._emofiltPlayer.printPhoFile();
				mf._emofiltPlayer.genWavFile();
				mf._emofiltPlayer.playAndDelete();
			}
		};
		Thread thread = new Thread(runner);
		thread.start();
	}

}