/*
 * Created on 18.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import java.awt.event.*;

import com.felix.util.FileUtil;
import emofilt.util.Util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.jdom.Element;
import org.apache.log4j.*;

import emofilt.Constants;
import emofilt.Emofilt;
import emofilt.EmofiltPlayer;
import emofilt.Language;
import emofilt.Languages;
import emofilt.Utterance;
import emofilt.UtteranceWriter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

/**
 * The main frame of the GUI development for emotion files.
 * 
 * structure with respect to sub panels:
 * 
 * <pre>
 * ___________________________________________ 
 * | menu
 * |------------------------------------------ 
 * | pitch contour | pitch contour 
 * | options       | display 
 * | 
 * | pcs 
 * |------------------------------------------
 * | 
 * | pitch modification panel 
 * | 
 * |------------------------------------------ 
 * | 
 * | duration modification panel 
 * | 
 * |------------------------------------------ 
 * | phonation    | articulation modification 
 * | modification | panel 
 * | 
 * |
 * |------------------------------------------ 
 * | buttons 
 * |
 * |------------------------------------------ 
 * | message board 
 * |
 * |___________________________________________
 * </pre>
 * 
 * @author Felix Burkhardt
 */
public class MainFrame extends JFrame {
	protected Emofilt emofilt;
	private int pcsWidth = GuiConstants.PCS_WIDTH;
	private int pcsHeight = GuiConstants.PCS_HEIGHT;
	private int pcavpWidth = GuiConstants.PCAVP_WIDTH;
	private int pcavpHeight = GuiConstants.PCAVP_WIDTH;
	private int pcpWidth = GuiConstants.PCP_WIDTH;
	private int pcpHeight = GuiConstants.PCP_HEIGHT;
	private int dcpWidth = GuiConstants.DCP_WIDTH;
	private int dcpHeight = GuiConstants.DCP_HEIGHT;
	private String fontName = GuiConstants.FONT_NAME;
	private int fontStyle = GuiConstants.FONT_STYLE;
	private int fontSize = GuiConstants.FONT_SIZE;
	private File tmpSaveDir = null;

	private File tmpLoadDir = null;

	private String sentence;

	private Logger _logger;

	// menu
	private JMenuBar jMenuBar = new JMenuBar();

	private EmofiltMenu jMenuFile = new EmofiltMenu(Emofilt._config
			.getString("mainFrame.menu.file"));

	private EmofiltMenu jMenuUtterance = new EmofiltMenu(Emofilt._config
			.getString("mainFrame.menu.utterance"));

	private EmofiltMenuItem menuItemUtteranceLoad = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.utterance.load"));

	private EmofiltMenuItem menuItemUtteranceNew = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.utterance.new"));
	private ButtonGroup emotionButtonGroup;
	private EmofiltRadioButtonMenuitem rbMale = new EmofiltRadioButtonMenuitem(
			Emofilt._config.getString("mainFrame.menu.utterance.male"));

	private EmofiltRadioButtonMenuitem rbFemale = new EmofiltRadioButtonMenuitem(
			Emofilt._config.getString("mainFrame.menu.utterance.female"));

	private boolean utteranceGenderIsMale = false;

	private EmofiltRadioButtonMenuitem feMale = new EmofiltRadioButtonMenuitem(
			Emofilt._config.getString("mainFrame.menu.utterance.male"));

	private EmofiltMenu jMenuHelp = new EmofiltMenu(Emofilt._config
			.getString("mainFrame.menu.help"));

	private EmofiltCheckboxMenuitem jcbmiMenuHelpTooltips = new EmofiltCheckboxMenuitem(
			Emofilt._config.getString("mainFrame.menu.help.showTooltipsLabel"),
			true);
	// a string to store the last text for synthesis, so it can be restored.
	private String _lastText="";
	private EmofiltMenuItem menuItemHelp = new EmofiltMenuItem(Emofilt._config
			.getString("mainFrame.menu.helpItem"));

	private EmofiltMenuItem menuItemAbout = new EmofiltMenuItem(Emofilt._config
			.getString("mainFrame.menu.aboutItem"));

	private EmofiltMenuItem menuItemQuit = new EmofiltMenuItem(Emofilt._config
			.getString("mainFrame.menu.quit"));
	private EmofiltMenuItem menuItemSave = new EmofiltMenuItem(Emofilt._config
			.getString("mainFrame.menu.save"));
	private EmofiltMenuItem menuItemSavePho = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.savePho"));

	private EmofiltMenu jMenuEmotion = new EmofiltMenu(Emofilt._config
			.getString("mainFrame.menu.emotion"));

	private EmofiltMenuItem jMenuEmotionSave = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.emotion.save"));

	private EmofiltMenuItem jMenuEmotionNew = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.emotion.new"));

	private EmofiltMenuItem jMenuEmotionColor = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.emotion.color"));

	private EmofiltMenuItem jMenuEmotionRemove = new EmofiltMenuItem(
			Emofilt._config.getString("mainFrame.menu.emotion.remove"));

	private Vector emotionJrbmis = new Vector();

	private EmofiltMenu jMenuLanguage = new EmofiltMenu(Emofilt._config
			.getString("mainFrame.menu.language"));

	private Vector languageJrbmis = new Vector();

	private GuiChangeListener gcl;

	private JPanel jPanelContour = new JPanel();

	private PitchContourScreen pcs;

	private PitchContourAdjustViewPanel pcavp;

	private ModificationControlsPanel pcp, dcp, phonCp, acp;

	private MainControlsPanel mcp;

	private EmofiltLabel msgs;
	protected EmofiltPlayer _emofiltPlayer;
	private File _saveFile;

	/**
	 * maximum length of strings to show on message display
	 */
	private final int maxMsgLength = 80;

	/**
	 * @param title
	 */
	public MainFrame(String title, Emofilt ed) {
		super(title);
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		this.emofilt = ed;
		_emofiltPlayer = new EmofiltPlayer(emofilt);
		try {
			pcsWidth = Integer.parseInt(ed._config.getString("pcsWidth"));
			pcsHeight = Integer.parseInt(ed._config.getString("pcsHeight"));
			pcavpWidth = Integer.parseInt(ed._config.getString("pcavpWidth"));
			pcavpHeight = Integer.parseInt(ed._config.getString("pcavpHeight"));
			pcpWidth = Integer.parseInt(ed._config.getString("pcpWidth"));
			pcpHeight = Integer.parseInt(ed._config.getString("pcpHeight"));
			dcpWidth = Integer.parseInt(ed._config.getString("dcpWidth"));
			dcpHeight = Integer.parseInt(ed._config.getString("dcpHeight"));
			fontName = ed._config.getString("mainFrame.font.name");
			fontSize = Integer.parseInt(ed._config
					.getString("mainFrame.font.size"));
			fontStyle = Integer.parseInt(ed._config
					.getString("mainFrame.font.style"));
		} catch (Exception e) {
			// use the constants
		}
		jbInit();
	}

	private void jbInit() {
		initMenu();
		gcl = new GuiChangeListener(emofilt);
		this.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		pcs = new PitchContourScreen(this);
		pcavp = new PitchContourAdjustViewPanel(pcs);
		pcp = new ModificationControlsPanel(this, "pitch");
		dcp = new ModificationControlsPanel(this, "duration");
		phonCp = new ModificationControlsPanel(this, "phonation");
		acp = new ModificationControlsPanel(this, "articulation");
		mcp = new MainControlsPanel(this);
		msgs = new EmofiltLabel("");
		if (emofilt.getVoice().supportsMultipleVoiceQualities()) {
			setVocalEffortPanelsVisible();
		} else {
			setVocalEffortPanelsInvisible();
		}
		JPanel upperpanel = new JPanel();
		upperpanel.setBackground(Util.getColorFromUidf("upperpanel.BG"));
		pcs.setPreferredSize(new Dimension(pcsWidth, pcsHeight));
		pcavp.setPreferredSize(new Dimension(pcavpWidth, pcavpHeight));
		pcp.setPreferredSize(new Dimension(pcpWidth, pcpHeight));
		dcp.setPreferredSize(new Dimension(dcpWidth, dcpHeight));
		upperpanel.add(pcavp);
		upperpanel.add(pcs);
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(acp, c);
		c.gridy = 1;
		getContentPane().add(upperpanel, c);
		c.gridy = 2;
		getContentPane().add(pcp, c);
		c.gridy = 3;
		getContentPane().add(dcp, c);
		JPanel artPhonPanel = new JPanel();
		artPhonPanel.add(phonCp);
		artPhonPanel.add(acp);
		artPhonPanel.setBackground(Util.getColorFromUidf("artPhonPanel.BG"));
		c.gridy = 4;
		getContentPane().add(artPhonPanel, c);
		c.gridy = 5;
		getContentPane().add(mcp, c);
		c.gridy = 6;
		c.anchor = GridBagConstraints.CENTER;
		JPanel msgpanel = new JPanel();
		msgpanel.setBackground(Color.WHITE);
		msgpanel.add(msgs);
		getContentPane().add(msgpanel, c);
		setDefaultMessage();
		getContentPane().setBackground(Util.getColorFromUidf("mainPanel.BG"));
		this.setIconImage((new ImageIcon("res/logo.gif")).getImage());

	}

	private void setVocalEffortPanelsVisible() {
		if (emofilt.getModificationPluginManager().getModificationByName(
				"vocalEffort") != null) {
			emofilt.getModificationPluginManager().getModificationByName(
					"vocalEffort").getPanel().setVisible(true);
		}
	}

	private void setVocalEffortPanelsInvisible() {
		if (emofilt.getModificationPluginManager().getModificationByName(
				"vocalEffort") != null) {
			emofilt.getModificationPluginManager().getModificationByName(
					"vocalEffort").getPanel().setVisible(false);
		}
	}

	/**
	 * This calls the program's exit-function, if the user closes the
	 * program-window.
	 * 
	 * @param e
	 *            WindowEvent
	 */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	private void showHelp() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				MainFrame.class.getResourceAsStream("res/emofiltHelp.txt")));
		String helpText = "";
		String buffer;
		while ((buffer = br.readLine()) != null) {
			helpText += buffer + "\n";
		}

		JOptionPane.showMessageDialog(this, helpText, Emofilt._config
				.getString("mainFrame.help.windowTitle"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAbout() throws Exception {
		String aboutText = "emofilt version: " + Constants.VERSION
				+ "\nhttp:\\\\" + Emofilt._config.getString("emofilt.webpage");
		JOptionPane.showMessageDialog(this, aboutText, Emofilt._config
				.getString("mainFrame.about.windowTitle"),
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(MainFrame.class
						.getResource("res/logo.gif")));
	}
 
	private void initMenu() {
		jMenuBar.setFont(new Font(fontName, fontStyle, fontSize));
		// menu-structure
		// ####### file menu
		menuItemQuit.setIcon(new ImageIcon(getClass().getResource("/res/door.gif")));
		menuItemQuit.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.quit.tooltip"));
		menuItemQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emofilt.saveEmotions();
				System.exit(0);
			}
		});
		menuItemQuit.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.quit").charAt(0));

		menuItemSave.setIcon(new ImageIcon(getClass().getResource("res/save.gif")));
		menuItemSave.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.save.tooltip"));
		menuItemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		menuItemSave.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.quit").charAt(0));

		menuItemSavePho.setIcon(new ImageIcon(getClass().getResource("res/savePho.gif")));
		menuItemSavePho.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.savePho.tooltip"));
		menuItemSavePho.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePhoFile();
			}
		});
		menuItemSavePho.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.savePho").charAt(0));
		jMenuFile.setIcon(new ImageIcon(getClass().getResource("res/home.gif")));
		jMenuFile.add(menuItemSave);
		jMenuFile.add(menuItemSavePho);
		jMenuFile.add(menuItemQuit);
		jMenuFile.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.file").charAt(0));
		jMenuBar.add(jMenuFile);

		// ############ emotion menu
		jMenuEmotionSave.setIcon(new ImageIcon(getClass().getResource("res/save.gif")));
		jMenuEmotionSave.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.emotion.save").charAt(0));
		jMenuEmotionSave.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.emotion.save.tooltip"));
		jMenuEmotionSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emofilt.getEmotions().addEmotion(emofilt.getActEmotion());
				emofilt.getEmotions().save();
			}
		});
		jMenuEmotionNew.setIcon(new ImageIcon(getClass().getResource("res/new.gif")));
		jMenuEmotionNew.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.emotion.new").charAt(0));
		jMenuEmotionNew.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.emotion.new.tooltip"));
		jMenuEmotionNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newEmotion();
			}
		});
		jMenuEmotionColor.setIcon(new ImageIcon(getClass().getResource("res/brush.gif")));
		jMenuEmotionColor.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.emotion.color").charAt(0));
		jMenuEmotionColor.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.emotion.color.tooltip"));
		jMenuEmotionColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorEmotion();
			}
		});
		jMenuEmotionRemove.setIcon(new ImageIcon(getClass().getResource("res/delete.gif")));
		jMenuEmotionRemove.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.emotion.remove").charAt(0));
		jMenuEmotionRemove.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.emotion.remove.tooltip"));
		jMenuEmotionRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeEmotion();
			}
		});
		jMenuEmotion.setIcon(new ImageIcon(getClass().getResource("res/logo-icon.gif")));
		jMenuEmotion.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.emotion").charAt(0));
		setEmotionsInMenu();
		jMenuBar.add(jMenuEmotion);
		// ############ language menu
		jMenuLanguage.setIcon(new ImageIcon(getClass().getResource("res/speechwave.gif")));

		jMenuLanguage.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.language").charAt(0));
		ButtonGroup languageButtonGroup = new ButtonGroup();
		Languages langs = emofilt.getLanguages();
		Vector langNames = langs.getLangNames();
		for (Iterator iter = langNames.iterator(); iter.hasNext();) {
			String langName = (String) iter.next();
			EmofiltMenu jmLang = new EmofiltMenu(langName);
			for (Iterator liter = langs.getLanguages(langName).iterator(); liter
					.hasNext();) {
				Language actLang = (Language) liter.next();
				String langVersion = actLang.getName();
				EmofiltRadioButtonMenuitem jrbmi = new EmofiltRadioButtonMenuitem(
						langVersion);
				LanguageSelectionActionListener lsal = new LanguageSelectionActionListener(
						langVersion);
				jrbmi.addActionListener(lsal);
				jrbmi.setToolTipText(actLang.getDescription());
				if (langVersion.compareTo(emofilt.getVoice().getName()) == 0) {
					jrbmi.setSelected(true);
				}
				languageButtonGroup.add(jrbmi);
				languageJrbmis.add(jrbmi);
				jmLang.add(jrbmi);
			}
			jMenuLanguage.add(jmLang);

		}
		jMenuBar.add(jMenuLanguage);
		// ############# utterance menu
		menuItemUtteranceLoad.setIcon(new ImageIcon("res/open.gif"));
		menuItemUtteranceLoad.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.utterance.load.tooltip"));
		menuItemUtteranceLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadfile();
			}
		});
		menuItemUtteranceLoad.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.utterance.load").charAt(0));
		menuItemUtteranceNew.setIcon(new ImageIcon("res/new.gif"));

		menuItemUtteranceNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateNewUtterance();
			}
		});
		menuItemUtteranceNew.setToolTipText(Emofilt._config
				.getString("mainFrame.menu.utterance.new.tooltip"));
		menuItemUtteranceNew.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.utterance.new").charAt(0));
		jMenuUtterance.setIcon(new ImageIcon("res/text.gif"));
		jMenuUtterance.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.utterance").charAt(0));
		jMenuUtterance.add(menuItemUtteranceLoad);
		jMenuUtterance.add(menuItemUtteranceNew);
		ButtonGroup genderGroup = new ButtonGroup();
		rbMale.setIcon(new ImageIcon("res/male.gif"));
		rbMale.setToolTipText(Emofilt._config
				.getString("phoGenCmdGenderMaleOption.tooltip"));
		rbMale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					utteranceGenderIsMale = rbMale.isSelected();
				} catch (Exception ex) {
					setErrorMsg(ex.getMessage());
				}
			}
		});
		rbFemale.setIcon(new ImageIcon("res/female.gif"));
		rbFemale.setToolTipText(Emofilt._config
				.getString("phoGenCmdGenderFemaleOption.tooltip"));
		rbFemale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					utteranceGenderIsMale = rbMale.isSelected();
				} catch (Exception ex) {
					setErrorMsg(ex.getMessage());
				}
			}
		});
		genderGroup.add(rbFemale);
		genderGroup.add(rbMale);
		rbFemale.setSelected(true);
		jMenuUtterance.add(rbMale);
		jMenuUtterance.add(rbFemale);
		jMenuBar.add(jMenuUtterance);

		// ############# help menu
		jMenuBar.add(Box.createHorizontalGlue());
		jcbmiMenuHelpTooltips.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jcbmiMenuHelpTooltips.isSelected())
					ToolTipManager.sharedInstance().setEnabled(true);
				else
					ToolTipManager.sharedInstance().setEnabled(false);
			}
		});
		menuItemHelp.setIcon(new ImageIcon("res/help2.gif"));
		menuItemHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showHelp();
				} catch (Exception ex) {
					setErrorMsg(ex.getMessage());
				}
			}
		});
		jMenuHelp.add(jcbmiMenuHelpTooltips);
		jMenuHelp.add(menuItemHelp);
		jMenuHelp.setIcon(new ImageIcon("res/help.gif"));
		menuItemAbout.setIcon(new ImageIcon("res/info.gif"));
		menuItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showAbout();
				} catch (Exception ex) {
					setErrorMsg(ex.getMessage());
				}
			}
		});
		jMenuHelp.add(menuItemAbout);
		jMenuHelp.setMnemonic(Emofilt._config.getString(
				"mainFrame.menu.mnemonic.help").charAt(0));
		jMenuBar.add(jMenuHelp);
		setJMenuBar(jMenuBar);

	}

	private void setEmotionsInMenu() {
		jMenuEmotion.removeAll();
		jMenuEmotion.add(jMenuEmotionSave);
		jMenuEmotion.add(jMenuEmotionNew);
		jMenuEmotion.add(jMenuEmotionColor);
		jMenuEmotion.add(jMenuEmotionRemove);
		emotionButtonGroup = new ButtonGroup();
		for (Iterator eiter = emofilt.getEmotions().getEmotionNames()
				.iterator(); eiter.hasNext();) {
			String emotionName = (String) eiter.next();
			EmofiltRadioButtonMenuitem jrbmi = new EmofiltRadioButtonMenuitem(
					emotionName);
			EmotionSelectionActionListener esal = new EmotionSelectionActionListener(
					emotionName);
			jrbmi.addActionListener(esal);
			jrbmi.setToolTipText(emofilt.getEmotions().getDescription(
					emotionName));
			if (emofilt.getActEmotion() != null
					&& emotionName.compareTo(emofilt.getActEmotion()
							.getAttribute("name").getValue()) == 0) {
				jrbmi.setSelected(true);
			}
			emotionButtonGroup.add(jrbmi);
			emotionJrbmis.add(jrbmi);
			jMenuEmotion.add((EmofiltRadioButtonMenuitem) emotionJrbmis
					.lastElement());
		}

	}

	private void newEmotion() {
		String emoName = JOptionPane.showInputDialog(Emofilt._config
				.getString("mainFrame.menu.emotion.new.question"));
		emofilt.addEmotion(emoName);
		setEmotionsInMenu();
		Color newColor = JColorChooser.showDialog(this, Emofilt._config
				.getString("mainFrame.menu.emotion.color.question"),
				Color.white);
		if (newColor != null) {
			emofilt.getEmotions().setColor(emofilt.getActEmotion(), newColor);
		}
		emofilt.getEmotions().save();

	}

	private void colorEmotion() {
		Color newColor = JColorChooser.showDialog(this, Emofilt._config
				.getString("mainFrame.menu.emotion.color.question"),
				Color.white);
		if (newColor != null) {
			emofilt.getEmotions().setColor(emofilt.getActEmotion(), newColor);
			emofilt.getEmotions().save();
		}

	}

	private void removeEmotion() {

		String name = emofilt.getActEmotion().getAttributeValue("name");
		emofilt.getEmotions().deleteEmotion(name);
		emofilt.setActEmotion(emofilt.getEmotions().getDefautEmotion());
		setEmotionsInMenu();
		emofilt.getEmotions().save();
	}

	private void generateNewUtterance() {
		try {
			String tmpPhoFile = Emofilt._config.getString("tmpDir")
					+ Emofilt._config.getString("tmpPhoFile");
			sentence = SimpleInput.getString(Emofilt._config
					.getString("mainFrame.insertStringLabel"), _lastText);
			_lastText = sentence;
			String cmdOutput = _emofiltPlayer.genPhoFile(sentence, tmpPhoFile,
					utteranceGenderIsMale);
			if (cmdOutput.trim().length() > 0) {
				setMsg(cmdOutput);
			}
			emofilt.loadUtterance(tmpPhoFile);
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMsg(e.getMessage());
		}
	}

	private void loadfile() {
		File file = null;
		try {
			// Create a file chooser
			if (tmpLoadDir == null) {
				tmpLoadDir = new File(Emofilt._config.getString("fileDir"));
			}
			final JFileChooser fc = new JFileChooser(tmpLoadDir);

			int returnVal = fc.showOpenDialog(MainFrame.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				tmpLoadDir = fc.getCurrentDirectory();
				file = fc.getSelectedFile();
				emofilt.loadUtterance(file.getAbsolutePath());
			}
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private class EmotionSelectionActionListener implements ActionListener {
		String name = "";

		public EmotionSelectionActionListener(String name) {
			this.name = name;
		}

		public void actionPerformed(ActionEvent e) {
			setEmotion(name);
		}
	};

	private class LanguageSelectionActionListener implements ActionListener {
		String name = "";

		public LanguageSelectionActionListener(String name) {
			this.name = name;
		}

		public void actionPerformed(ActionEvent e) {
			setActLanguage(name);
		}
	};

	private void setActLanguage(String name) {
		emofilt.setVoice(name);
		if (emofilt.getVoice().supportsMultipleVoiceQualities()) {
			setVocalEffortPanelsVisible();
		} else {
			setVocalEffortPanelsInvisible();
		}
		setActEmotion();
		// refresh message string
		setDefaultMessage();
	}

	public void setActEmotion() {
		setEmotion(emofilt.getActEmotion().getAttributeValue("name"));
	}

	public void setEmotion(String emoName) {
		_logger.debug("setting emotions: " + emoName);
		Element emotion = emofilt.getEmotions().getEmotion(emoName);
		emofilt.setActEmotion(emotion);
		pcp.setEmotion(emotion);
		dcp.setEmotion(emotion);
		phonCp.setEmotion(emotion);
		acp.setEmotion(emotion);
		setDefaultMessage();
	}

	private void setDefaultMessage() {
		String actEmoname = "";
		if (emofilt.getActEmotion() != null)
			actEmoname = emofilt.getActEmotion().getAttribute("name")
					.getValue();
		setMsg("emotion: " + actEmoname + ", voice: "
				+ emofilt.getVoice().getName());
	}

	public void setMsg(String msg) {
		if (msg.length() > maxMsgLength) {
			msg = msg.substring(0, maxMsgLength);
		}
		msgs.setFont(new Font("Dialog", 0, 14));
		msgs.setForeground(Color.black);
		msgs.setText(msg);
	}

	public void setErrorMsg(String msg) {
		if (msg == null)
			return;
		// if (msg.length() > maxMsgLength) {
		// msg = msg.substring(0, maxMsgLength);
		// }
		// msgs.setForeground(Color.RED);
		// msgs.setBackground(Color.WHITE);
		// msgs.setFont(new Font("Dialog", 0, 14));
		// msgs.setText("ERROR: " + msg);
		JOptionPane.showMessageDialog(this, new JTextArea(msg));
	}

	public GuiChangeListener getGcl() {
		return gcl;
	}

	public PitchContourScreen getPcs() {
		return pcs;
	}

	protected void save() {
		// first get a name for the file
		try {
			// Create a file chooser
			if (tmpSaveDir == null) {
				tmpSaveDir = new File(Emofilt._config.getString("workingDir"));
			}
			final JFileChooser fc = new JFileChooser(tmpSaveDir);
			FileFilter ff = new FileFilter() {
				public boolean accept(File f) {
					if (f.getName().endsWith(".wav"))
						return true;
					return false;
				}

				public String getDescription() {
					return "wav";
				}
			};
			fc.setFileFilter(ff);
			int returnVal = fc.showSaveDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				tmpSaveDir = fc.getCurrentDirectory();
				_saveFile = fc.getSelectedFile();
				if (!_saveFile.getName().endsWith(".wav")) {
					_saveFile = new File(_saveFile.getAbsolutePath() + ".wav");
				}
			} else {
				return;
			}
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			e.printStackTrace();
			return;
		}
		_logger.debug("saving wav-file to: " + _saveFile.getAbsolutePath());
		Runnable runner = new Runnable() {
			public void run() {
				_emofiltPlayer.genWavFile(_saveFile.getAbsolutePath());
			}
		};
		Thread thread = new Thread(runner);
		thread.start();

	}

	protected void savePhoFile() {
		// first get a name for the file
		try {
			// Create a file chooser
			if (tmpSaveDir == null) {
				tmpSaveDir = new File(Emofilt._config.getString("workingDir"));
			}
			final JFileChooser fc = new JFileChooser(tmpSaveDir);
			FileFilter ff = new FileFilter() {
				public boolean accept(File f) {
					if (f.getName().endsWith(".pho"))
						return true;
					return false;
				}

				public String getDescription() {
					return "pho";
				}
			};
			fc.setFileFilter(ff);
			int returnVal = fc.showSaveDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				tmpSaveDir = fc.getCurrentDirectory();
				_saveFile = fc.getSelectedFile();
				if (!_saveFile.getName().endsWith(".pho")) {
					_saveFile = new File(_saveFile.getAbsolutePath() + ".pho");
				}

			} else {
				return;
			}
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			e.printStackTrace();
			return;
		}
		_logger.debug("saving pho-file to: " + _saveFile.getAbsolutePath());
		Runnable runner = new Runnable() {
			public void run() {
				_emofiltPlayer.printPhoFile(_saveFile.getAbsolutePath());
			}
		};
		Thread thread = new Thread(runner);
		thread.start();
	}

}