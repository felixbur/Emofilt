/*
 * Created on 18.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.UIManager;

import marytts.client.http.MaryHttpClient;
import marytts.util.http.Address;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jdom.Element;

import psk.cmdline.ApplicationSettings;
import psk.cmdline.BooleanToken;
import psk.cmdline.StringToken;
import psk.cmdline.TokenOptions;

import com.felix.util.FileUtil;
import com.felix.util.KeyValues;
import com.felix.util.StringUtil;

import emofilt.gui.MainFrame;

/**
 * 
 * The main class that implements the main-method, calls the GUI-mainframe if
 * wanted, or transforms the utterance directly.
 * 
 * There are two interfaces, the GUI uses SWING panels and is called with the
 * option "-useGui", the command line interface reads and writes to stdout or
 * given filenames. See the usage for information (printed out with option "-h")
 * 
 * @author Felix Burkhardt
 */
public class Emofilt { 
	// /** _config contains the laguage-specific denotations. */
	// public static UIDefaults _config = UIManager.getDefaults();
	public static KeyValues _config;

	private String _workingDir = "";

	private ModificationPluginManager _modificationPluginManager;

	private Language _actLanguage = null;

	private Element _actEmotion;

	private Utterance _actUtt;

	private double _globalRate = 0;

	private Languages _languages = null;

	private Utterance _origUtt;

	private UtteranceModifier _uttModifier;

	private Emotions _emotions;

	private boolean _utteranceLoaded = false;

	public static final String LOGGER_NAME = "emofiltDebug";

	/** The main GUI component. */
	private MainFrame _mainFrame = null;

	/** window title */
	public final String windowTitle = "Emofilt Developer";

	private Logger _logger = null;

	private boolean _useGui = false;

	private EmofiltPlayer _emoEmofiltPlayer;

	/**
	 * Constructor. Loads the initialization values, analyzes the command line
	 * arguments and triggers the processing of a given PHO file or starts the
	 * graphical user interface.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public Emofilt(String[] args) {
		try {
			ApplicationSettings aps = new ApplicationSettings();
			BooleanToken showUsage = new BooleanToken("h", "print usage", "",
					TokenOptions.optSwitch, false);
			BooleanToken useGUI = new BooleanToken("useGui",
					"display the graphical developer interface", "",
					TokenOptions.optSwitch, false);
			BooleanToken showVersion = new BooleanToken("version",
					"print version", "", TokenOptions.optSwitch, false);
			BooleanToken useMary = new BooleanToken("mary",
					"send input to mary server for text-to-speech", "",
					TokenOptions.optSwitch, false);
			StringToken infile = new StringToken("if",
					"specify input pho-file", "", TokenOptions.optDefault,
					"System.in");
			StringToken loglevel = new StringToken("lv",
					"specify log-level [debug warn error fatal off]", "",
					TokenOptions.optDefault, "debug");
			StringToken outfile = new StringToken("of",
					"specify output pho-file", "", TokenOptions.optDefault,
					"System.out");
			StringToken wavoutfile = new StringToken("ow",
					"specify output wav-file", "", TokenOptions.optDefault,
					"");
			StringToken emofile = new StringToken("ef",
					"specify emotion-description xml-file", "",
					TokenOptions.optDefault, Constants.emotionsDefaultFile);
			StringToken emotion = new StringToken("e",
					"specify emotion (must appear in emotions-description)",
					"", TokenOptions.optDefault, Constants.emotionsDefaultName);
			StringToken configfile = new StringToken("cf",
					"specify configuration-file", "", TokenOptions.optDefault,
					Constants.configDefaultFile);
			StringToken langfile = new StringToken("lf",
					"specify voice-description xml-file", "",
					TokenOptions.optDefault, Constants.languagesDefaultFile);
			StringToken voice = new StringToken("voc", "specify voice", "",
					TokenOptions.optDefault, "");
			StringToken loggerconfigfile = new StringToken("lcf",
					"specify logger configuration log4j-file", "",
					TokenOptions.optDefault, Constants.loggerDefaultConfigFile);
			StringToken globalRateToken = new StringToken(
					"gr",
					"global rate: damp or amplify the modifications for graded emotions between -1 and 1",
					"", TokenOptions.optDefault, Constants.globalRate);
			aps.addToken(showUsage);
			aps.addToken(showVersion);
			aps.addToken(useGUI);
			aps.addToken(useMary);
			aps.addToken(infile);
			aps.addToken(outfile);
			aps.addToken(wavoutfile);
			aps.addToken(emotion);
			aps.addToken(voice);
			aps.addToken(configfile);
			aps.addToken(emofile);
			aps.addToken(langfile);
			aps.addToken(loggerconfigfile);
			aps.addToken(loglevel);
			aps.addToken(globalRateToken);
			aps.parseArgs(args);
			if (showUsage.getValue()) {
				System.out.println("emofilt version " + Constants.VERSION);
				aps.printUsage();
				System.exit(0);
			}
			if (showVersion.getValue()) {
				System.out.println("emofilt version " + Constants.VERSION);
				System.exit(0);
			}
			if (!FileUtil.existFile(configfile.getValue())) {
				System.out.println(configfile.getValue()
						+ " could not be opened");
				System.exit(0);
			}
			_config = new KeyValues(configfile.getValue(), "|");
			_workingDir = _config.getString("workingDir");
			if (!_workingDir.endsWith(System.getProperty("file.separator"))) {
				_workingDir += System.getProperty("file.separator");
			}
			if (_config.getString("loggerConfig") != null
					&& loggerconfigfile.isDefault()) {
				loggerconfigfile.setValue(_workingDir
						+ _config.getString("loggerConfig"));
			}
			if (_config.getString("languagesFile") != null
					&& langfile.isDefault()) {
				langfile.setValue(_workingDir
						+ _config.getString("languagesFile"));
			}
			if (_config.getString("emotionsFile") != null
					&& emofile.isDefault()) {
				emofile.setValue(_workingDir
						+ _config.getString("emotionsFile"));
			}
			if (_config.getString("defaultVoice") != null && voice.isDefault()) {
				voice.setValue((String) _config.getString("defaultVoice"));
			}
			_globalRate = Double.parseDouble(globalRateToken.getValue());
			_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
			_logger.setLevel(string2Loglevel(loglevel.getValue()));
			DOMConfigurator.configure(loggerconfigfile.getValue());
			_useGui = useGUI.getValue();
			if (outfile.getValue().compareTo("System.out") == 0 && !_useGui) {
				// prevent debug messages when printing to stdout
				_logger.setLevel(Level.ERROR);
			}
			_modificationPluginManager = new ModificationPluginManager(this);
			_languages = new Languages(langfile.getValue());
			_actLanguage = _languages.findLanguage(voice.getValue());
			if (_actLanguage == null) {
				_logger.error("lang == null (unknown voice?)");
			}

			_uttModifier = new UtteranceModifier(_modificationPluginManager);
			_emotions = new Emotions(emofile.getValue());
			_actEmotion = _emotions.getEmotion(emotion.getValue());
			if (_actEmotion == null) {
				_actEmotion = new Element("emotion");
				_actEmotion.setAttribute("name", Constants.emotionsDefaultName);
			}
			if (_useGui) {
				_actEmotion = null;
				UIManager.setLookAndFeel(_config.getString("lookAndFeel"));
				// UIManager.setLookAndFeel("emofilt.gui.EmofiltLookAndFeel");
				if (infile.getValue().compareTo("System.in") != 0) {
					loadUtterance(infile.getValue());
				}
				showMainframe();
				_modificationPluginManager.setPropertyChangeListener(_mainFrame
						.getGcl());
				_actEmotion = _emotions.getEmotion(emotion.getValue());
				if (_actEmotion == null) {
					_actEmotion = new Element("emotion");
					_actEmotion.setAttribute("name",
							Constants.emotionsDefaultName);
				}
				_mainFrame.setActEmotion();
			} else {
				if (useMary.getValue()) {
					String tmpPhoFile = Emofilt._config.getString("tmpDir")
							+ Emofilt._config.getString("tmpPhoFile");
					String sentence = "";
					if (infile.getValue().compareTo("System.in") == 0) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(System.in));
						String line;
						while ((line = br.readLine()) != null) {
							sentence += line;
						}
						sentence += "\n";
					} else {
						sentence = FileUtil.getFileText(infile.getValue()); 
					}
					String maryHost = Emofilt._config
							.getString("maryServerHost");
					int maryPort = Integer.parseInt(Emofilt._config
							.getString("maryServerPort"));
					MaryHttpClient mary = new MaryHttpClient( new Address(
							maryHost, maryPort));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					_logger.debug("calling Mary server with phrase " + sentence
							+ " and language: " + _actLanguage.getName());
					mary.process(sentence, "TEXT", "MBROLA",
							_actLanguage.getLocale(), null,
							_actLanguage.getName(), baos);
					File phoF = new File(tmpPhoFile);
					FileUtil.writeFileContent(phoF, baos.toString());
					baos = null;
					loadUtterance(phoF.getPath());
				} else {
					if (infile.getValue().compareTo("System.in") == 0) {
						PhonemesReader ur = new PhonemesReader(_actLanguage);
						_origUtt = ur.readUtterance();
						Syllablelizer sz = new Syllablelizer(_origUtt);
						sz.syllablelize();
						sz.setAccents();
						_origUtt = sz.getUtterance();
					} else {
						loadUtterance(infile.getValue());
					}
				}
				Utterance uttCopy = (Utterance) _origUtt.clone();
				_actUtt = _uttModifier.modify(uttCopy, _actEmotion,
						_globalRate, _actLanguage);
				uttCopy = null;
				UtteranceWriter uw = new UtteranceWriter();
				if (outfile.getValue().compareTo("System.out") != 0) {
					uw.setFilename(outfile.getValue());
					uw.setPrintFile(true);
				}
				uw.printUtterance(_actUtt);
				String waveOutPath = wavoutfile.getValue();
				if(StringUtil.isFilled(waveOutPath)){
					_emoEmofiltPlayer = new EmofiltPlayer(this);
					_emoEmofiltPlayer.printPhoFile();
					_emoEmofiltPlayer.genWavFile(waveOutPath);
				}
				
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
	}

	public static void configureLogger() {
		String workingDir = _config.getString("workingDir");
		if (!workingDir.endsWith(System.getProperty("file.separator"))) {
			workingDir += System.getProperty("file.separator");
		}
		DOMConfigurator.configure(workingDir
				+ _config.getString("loggerConfig"));
	}

	public Emofilt(String configFile) {
		_config = new KeyValues(configFile, "|");
		_workingDir = _config.getString("workingDir");
		if (!_workingDir.endsWith(System.getProperty("file.separator"))) {
			_workingDir += System.getProperty("file.separator");
		}
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		configureLogger();
		_modificationPluginManager = new ModificationPluginManager(this);
		_languages = new Languages(_workingDir
				+ _config.getString("languagesFile"));
		_actLanguage = _languages.findLanguage(_config
				.getString("defaultVoice"));
		if (_actLanguage == null) {
			_logger.error("lang == null (unknown voice?)");
		}

		_uttModifier = new UtteranceModifier(_modificationPluginManager);
		_emotions = new Emotions(_workingDir
				+ _config.getString("emotionsFile"));
		if (_actEmotion == null) {
			_actEmotion = new Element("emotion");
			_actEmotion.setAttribute("name", Constants.emotionsDefaultName);
		}
		_emoEmofiltPlayer = new EmofiltPlayer(this);
	}

	/**
	 * Reset actual emotion to neutral.
	 */
	public void setNeutralEmotion() {
		setEmotion(_config.getString("neutralEmotion"));
	}

	/**
	 * Load from text string (requires external phonetization component)
	 * 
	 * @param text
	 */
	public void loadText(String text, boolean male) {
		try {
			String tmpPhoFile = Emofilt._config.getString("tmpDir")
					+ Emofilt._config.getString("tmpPhoFile");
			_emoEmofiltPlayer.genPhoFile(text, tmpPhoFile, male);
			loadUtterance(tmpPhoFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load Mbrola PHO format.
	 * 
	 * @param pho
	 */
	public void loadPho(String pho) {
		PhonemesReader ur = new PhonemesReader(_actLanguage);
		_origUtt = ur.readFromString(pho);
		Syllablelizer sz = new Syllablelizer(_origUtt);
		sz.syllablelize();
		sz.setAccents();
		_origUtt = sz.getUtterance();

	}

	/**
	 * Get information if graphical user interface is displayed.
	 * 
	 * @return True if GUI is shown.
	 */
	public boolean useGui() {
		return _useGui;
	}

	/**
	 * Retrieve the main frame of the graphical user interface.
	 * 
	 * @return The frame of the graphical user interface or null if not set.
	 */
	public MainFrame getMainFrame() {
		return _mainFrame;
	}

	/**
	 * Ask if an utterance is loaded.
	 * 
	 * @return True if an utterance is loaded, false otherwise.
	 */
	public boolean isUtteranceLoaded() {
		return _utteranceLoaded;
	}

	/**
	 * Given a filename, suppose a PHO file and load the utterance.
	 * 
	 * @param filename
	 *            The path to the PHO-file.
	 * @throws Exception
	 *             E.g. if the file does not exist.
	 */
	public void loadUtterance(String filename) throws Exception {
		PhonemesReader ur = new PhonemesReader(filename, _actLanguage);
		try {
			_origUtt = ur.readUtterance();
		} catch (Exception e) {
			e.printStackTrace();
			throw (e);
		}

		// System.err.println("origUtt.printPhonemes()\n"+origUtt.printPhonemes());
		Syllablelizer sz = new Syllablelizer(_origUtt);
		sz.syllablelize();
		sz.setAccents();
		_origUtt = sz.getUtterance();
		_actUtt = sz.getUtterance();
		// System.err.println("actUtt.printSyllables()\n"
		// + actUtt.printSyllables());

		if (_mainFrame != null)
			_mainFrame.getPcs().repaint();
		_utteranceLoaded = true;
	}

	/**
	 * Get the manager class for the plugins.
	 * 
	 * @return The ModificationPluginManager.
	 */
	public ModificationPluginManager getModificationPluginManager() {
		return _modificationPluginManager;
	}

	/**
	 * Retrieve the current language identifier.
	 * 
	 * @return Returns the actLanguage.
	 */
	public Language getVoice() {
		return _actLanguage;
	}

	/**
	 * Set the current language.
	 * 
	 * @param name
	 *            The actLanguage to set.
	 */
	public void setVoice(String name) {
		this._actLanguage = _languages.findLanguage(name);
	}

	/**
	 * Retrieve all loaded languages.
	 * 
	 * @return Returns the languages.
	 */
	public Languages getLanguages() {
		return _languages;
	}

	public Language getActLanguage() {
		return _actLanguage;
	}

	public void setActLanguage(Language l) {
		_logger.debug("setting language to " + l.getName());
		_actLanguage = l;
	}

	/**
	 * Add the current emotion to the loaded emotions and save to file.
	 * 
	 */
	public void saveEmotions() {
		_emotions.addEmotion(_actEmotion);
		_emotions.save();
	}

	public double getGlobalRate() {
		return _globalRate;
	}

	public void setGlobalRate(double globalRate) {
		this._globalRate = globalRate;
	}

	/**
	 * Modify the current utterance according to the current emotion.
	 */
	public void modifyUtterance() {
		// System.err.println("emofilt: act emotion: "+getActEmotion().getAttributeValue("name"));

		if (_origUtt == null || _actEmotion == null)
			return;
		try {
			Utterance uttCopy = (Utterance) _origUtt.clone();
			_actUtt = _uttModifier.modify(uttCopy, _actEmotion, _globalRate,
					_actLanguage);
			uttCopy = null;
			if (_mainFrame != null)
				_mainFrame.getPcs().repaint();
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e);
		}
	}

	/**
	 * Retrieve the current utterance.
	 * 
	 * @return The current utterance.
	 */
	public Utterance getActUtt() {
		return _actUtt;
	}

	/**
	 * Retrieve the original (unmodified) utterance.
	 * 
	 * @return The original utterance.
	 */
	public Utterance getOrigUtt() {
		return _origUtt;
	}

	/**
	 * The main method starts the constructor.
	 * 
	 * @param args
	 *            The commandline arguments.
	 */
	public static void main(String[] args) {
		new Emofilt(args);
	}

	/**
	 * Retrieve all loaded emotions.
	 * 
	 * @return The loaded emotions.
	 */
	public Emotions getEmotions() {
		return _emotions;
	}

	/**
	 * Retrieve the current emotion as a jdom Element.
	 * 
	 * @return The current emotion as a jdom Element.
	 */
	public Element getActEmotion() {
		return _actEmotion;
	}

	/**
	 * Add a new emotion to internal list.
	 * 
	 * @param name
	 *            The nane.
	 */
	public void addEmotion(String name) {
		Element emoElem = _emotions.addEmotion(name);
		setActEmotion(emoElem);

	}

	/**
	 * Set the current emotion as a jdom Element.
	 * 
	 * @param actEmotion
	 *            The emotion to be set as current.
	 */
	public void setActEmotion(Element actEmotion) {
		this._actEmotion = actEmotion;
	}

	/**
	 * Set a new emotion.
	 * 
	 * @param emotionName
	 */
	public void setEmotion(String emotionName) {
		this._actEmotion = _emotions.getEmotion(emotionName);
	}

	private Level string2Loglevel(String s) throws Exception {
		if (s.compareTo("debug") == 0)
			return Level.DEBUG;
		if (s.compareTo("warn") == 0)
			return Level.WARN;
		if (s.compareTo("error") == 0)
			return Level.ERROR;
		if (s.compareTo("fatal") == 0)
			return Level.FATAL;
		if (s.compareTo("off") == 0)
			return Level.OFF;
		throw new Exception("unspecified loglevel");
	}

	private void showMainframe() {
		_mainFrame = new MainFrame(windowTitle, this);
		_mainFrame.pack();
		_mainFrame.setVisible(true);
	}

}