package emofilt;

import java.io.File;

import org.apache.log4j.Logger;

import com.felix.util.FileUtil;
import com.felix.util.PlayWave;
import com.felix.util.Util;

import emofilt.generator.MaryPhoGenerator;
import emofilt.generator.PhoGenInterface;
import emofilt.generator.Txt2PhoPhogenerator;

public class EmofiltPlayer {
	private Emofilt _emofilt;
	private Logger _logger;
	private long WAIT_TIME = 200;
	private long MAX_TIME = 5000;
	private boolean _playing = false;

	/**
	 * Constructor setting the emofilt object.
	 * 
	 * @param emofilt
	 *            The central object.
	 */
	public EmofiltPlayer(Emofilt emofilt) {
		super();
		_emofilt = emofilt;
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		WAIT_TIME = Emofilt._config.getInt("waitForFileProgessMin");
		MAX_TIME = Emofilt._config.getInt("waitForFileTime");
	}

	/**
	 * Generate a wav file from the current utterance, using the config path.
	 * 
	 * @return
	 */
	public String genWavFile() {
		String tmpAudioFilePath = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpAudioFile");
		return genWavFile(tmpAudioFilePath);
	}

	/**
	 * Generate a wav file from the current utterance.
	 * 
	 * @param outPath
	 *            Where to.
	 * @return Error logging.
	 */
	public String genWavFile(String outPath) {
		String wavGenCmd = Emofilt._config.getString("wavGenCmd");
		// path to database with mbrola-voices
		String databasePath = Emofilt._config.getString("databasePath");
		if (databasePath == null) {
			databasePath = "";
		}
		// optional prefix for database
		String wavGenDBPrefix = Emofilt._config.getString("wavGenDBPrefix");
		if (wavGenDBPrefix == null) {
			wavGenDBPrefix = "";
		}
		// phogenerator infile prefix
		String wavGenInPrefix = Emofilt._config.getString("wavGenInPrefix");
		if (wavGenInPrefix == null) {
			wavGenInPrefix = "";
		}
		// phogenerator outfile prefix
		String wavGenOutPrefix = Emofilt._config.getString("wavGenOutPrefix");
		if (wavGenOutPrefix == null) {
			wavGenOutPrefix = "";
		}
		// format option
		String formatOption = Emofilt._config.getString("formatOption");
		if (formatOption == null) {
			formatOption = "";
		}
		String tmpPhoFilePath = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpPhoFile");
		String voiceName = _emofilt.getVoice().getName();
		String execString = wavGenCmd + " " + wavGenDBPrefix + databasePath
				+ voiceName + "/" + voiceName + " " + wavGenInPrefix
				+ tmpPhoFilePath + " " + wavGenOutPrefix + outPath + " "
				+ formatOption;
		_logger.debug("exec: " + execString);
		String output = "";
		try {
			output = com.felix.util.Util.execCmd(execString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtil.waitForFile(outPath, WAIT_TIME, MAX_TIME, false);
		return output;
	}

	/**
	 * Generate a wav file from pho-format string.
	 * 
	 * @param phoText
	 *            The PHO text,
	 * @param outPath
	 *            The output path.
	 * @return Some error logging.
	 */
	public String genWavFile(String phoText, String outPath) {
		try {
			String tmpPhoFilePath = Emofilt._config.getString("tmpDir")
					+ Emofilt._config.getString("tmpPhoFile");
			FileUtil.writeFileContent(tmpPhoFilePath, phoText);
			return genWavFile(outPath);

		} catch (Exception e) {
			Util.reportError(e, _logger);
		}
		return null;
	}

	/**
	 * Generate a PHO file at the path given in Config file.
	 * 
	 * @param text
	 *            The text.
	 * @param male
	 *            The sex.
	 * @return Some error logging.
	 */
	public String genPhoFile(String text, boolean male) {
		String tmpPhoFile = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpPhoFile");
		return genPhoFile(text, tmpPhoFile, male);
	}

	/**
	 * Generate a PHO file.
	 * 
	 * @param text
	 *            The text.
	 * @param outFilePath
	 *            The ouput path.
	 * @param male
	 *            The sex.
	 * @return Some error logging.
	 */
	public String genPhoFile(String text, String outFilePath, boolean male) {
		String method = Emofilt._config.getString("phoGenCmd").trim();
		String language = _emofilt.getActLanguage().getLangname();
		PhoGenInterface phoGenerator = null;
		if (language.compareTo("fr") == 0) {
			String className = Emofilt._config.getString(language
					+ Constants.PHO_GEN_SUFFIX);
			try {
				phoGenerator = (PhoGenInterface) (getClass().getClassLoader()
						.loadClass("emofilt.generator." + className)
						.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} else {
			if (method.compareTo("mary") == 0) {
				phoGenerator = (PhoGenInterface) new MaryPhoGenerator();
			} else if (method.indexOf("txt2pho") > 0) {
				phoGenerator = (PhoGenInterface) new Txt2PhoPhogenerator();
			} else {
				_logger.error("no external phonetizer application specified: "
						+ method);
			}
		}
		if (phoGenerator != null) {
			phoGenerator.init(Emofilt._config, _logger);
			return phoGenerator.genPhoFile(text, outFilePath,
					_emofilt.getActLanguage(), male);
		}
		return "no pho generator";
	}

	/**
	 * Generate a PHO text for some input.
	 * 
	 * @param text
	 *            The input text.
	 * @param male
	 *            The sex.
	 * @return The PHO text.
	 */
	public String getPhoString(String text, boolean male) {
		try {
			String tmpPhoFile = Emofilt._config.getString("tmpDir")
					+ Emofilt._config.getString("tmpPhoFile");
			genPhoFile(text, tmpPhoFile, male);
			return FileUtil.getFileText(new File(tmpPhoFile));
		} catch (Exception e) {
			Util.reportError(e, _logger);
			return null;
		}
	}

	/**
	 * Play to speakers using Java SpeechAPI. Audio file path is specified in
	 * config file.
	 */
	public void play() {
		String tmpAudioFilePath = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpAudioFile");
		play(tmpAudioFilePath);
	}

	/**
	 * Play to speakers using Java SpeechAPI.
	 * 
	 * @param filePath
	 *            Path to audio file.
	 */
	public void play(String filePath) {
		PlayWave filePlayer = new PlayWave(filePath);
		_playing = true;
		filePlayer.run();
		while (filePlayer.isAlive()) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (Exception e) {
			}
			if (!_playing)
				filePlayer.stopPlayback();
		}
		filePlayer = null;
		_playing = false;
	}

	public void stopPlay() {
		_playing = false;
	}

	/**
	 * Play to speakers using Java SpeechAPI.
	 * 
	 * @param filePath
	 *            Path to audio file.
	 */
	public void playAndDelete() {
		String tmpAudioFilePath = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpAudioFile");
		play(tmpAudioFilePath);
		try {
			FileUtil.delete(tmpAudioFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print PHO file to configured pho-path.
	 * 
	 */
	public void printPhoFile() {
		String tmpPhoFile = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpPhoFile");
		printPhoFile(tmpPhoFile);
	}

	/**
	 * Print PHO file.
	 * 
	 * @param outPath
	 *            The output path.
	 */
	public void printPhoFile(String outPath) {
		Utterance utt = _emofilt.getActUtt();
		if (utt == null)
			return;
		new UtteranceWriter().printToFile(utt, outPath);
	}

}
