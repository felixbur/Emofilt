package emofilt.storyreading;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.felix.util.FileUtil;
import com.felix.util.StringUtil;

import psk.cmdline.ApplicationSettings;
import psk.cmdline.BooleanToken;
import psk.cmdline.StringToken;
import psk.cmdline.TokenOptions;

import emofilt.Emofilt;
import emofilt.EmofiltPlayer;
import emofilt.Utterance;
import emofilt.UtteranceWriter;
import emofilt.storytagger.TaggedPiece;

public class Story {
	private Vector<Phrase> phrases;

	private Document _storyTree;

	private Element _storyRoot;

	private Logger _logger = null;

	private String _fileName;
	private ApplicationSettings aps;
	private Emofilt _emofilt;
	private EmofiltPlayer _emoPlayer;
	private boolean playing = false;

	/**
	 * Initializes the emotions-object with a filename pointing to the xml-file
	 * containing emotion-descriptions.
	 * 
	 * @param emotionsFilename
	 *            The filename.
	 */
	public Story(String[] args) {
		try {
			DOMConfigurator.configure("emofilt/logConfig.xml");
			_logger = Logger.getLogger("emofilt.storyreading.Story");

			aps = new ApplicationSettings();
			BooleanToken showUsage = new BooleanToken("h", "print usage", "",
					TokenOptions.optSwitch, false);
			StringToken infile = new StringToken("if",
					"specify input story-file", "", TokenOptions.optDefault,
					"System.in");

			aps.addToken(showUsage);
			aps.addToken(infile);
			aps.parseArgs(args);
			if (infile.getValue() != null) {
				readIn(infile.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Story(Emofilt emofilt) {
		_emofilt = emofilt;
		_emoPlayer = new EmofiltPlayer(_emofilt);
		_logger = Logger.getLogger("emofilt.storyreading.Story");
	}

	public void saveAudio(String filename, String markup) {
		String phoString = "";
		Vector<TaggedPiece> phrases = parseMarkup(markup);
		for (Iterator<TaggedPiece> iterator = phrases.iterator(); iterator
				.hasNext();) {
			TaggedPiece shortPhrase = (TaggedPiece) iterator.next();
			System.out.println(shortPhrase.toString());
			_emofilt.loadText(shortPhrase.getText(), true);
			_emofilt.setEmotion(shortPhrase.getEmotion());
			_emofilt.modifyUtterance();
			Utterance actU = _emofilt.getActUtt();
			phoString += new UtteranceWriter().getPhoString(actU);
		}
		String tmpPhoFile = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpPhoFile");
		try {
			FileUtil.writeFileContent(tmpPhoFile, phoString);
		} catch (Exception e) {
			reportError(e);
		}
		_emoPlayer.genWavFile(filename);
	}

	public void playAudioInOneFile(String markup) {
		String tmpWavFile = Emofilt._config.getString("tmpDir")
				+ Emofilt._config.getString("tmpAudioFile");
		saveAudio(tmpWavFile, markup);
		_emoPlayer.playAndDelete();
	}

	public void stopPlay() {
		playing = false;
		_emoPlayer.stopPlay();
	}

	private void reportError(Exception e) {
		e.printStackTrace();
		_logger.error(e.getMessage());
	}

	public void playText(String markup) {
		playAudioInOneFile(markup);
	}

	public void playTextSingle(String markup) {
		Vector<TaggedPiece> phrases = parseMarkup(markup);
		playing = true;
		for (Iterator<TaggedPiece> iterator = phrases.iterator(); iterator
				.hasNext();) {
			TaggedPiece shortPhrase = (TaggedPiece) iterator.next();
			System.out.println(shortPhrase.toString());
			_emofilt.loadText(shortPhrase.getText(), true);
			_emofilt.setEmotion(shortPhrase.getEmotion());
			_emofilt.modifyUtterance();
			_emoPlayer.printPhoFile();
			_emoPlayer.genWavFile();
			_emoPlayer.playAndDelete();
				break;
		}
		playing = false;
	}

	private Vector<TaggedPiece> parseMarkup(String markup) {
		Vector<TaggedPiece> phrases = new Vector<TaggedPiece>();
		StringTokenizer st = new StringTokenizer(markup, "<");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (StringUtil.checkIfWordsContained(token)) {
				int braceIndex = token.indexOf(">");
				if (braceIndex >= 0) {
					if (token.startsWith("/")) {
						// end tag
					} else {
						// begin tag
						String emoS = token.substring(0, braceIndex);
						StringTokenizer st2 = new StringTokenizer(emoS, "\"");
						st2.nextToken();
						String emotionName = st2.nextToken();
						String phrase = StringUtil.stripNewlines(token
								.substring(braceIndex + 1, token.length())
								.trim());
						if (phrase.length() > 0) {
							_logger.debug("token: " + token + ", emotion: "
									+ emotionName + " , text: " + phrase);
							phrases.add(new TaggedPiece(emotionName, phrase));
						}
					}
				}
			}
		}
		return phrases;
	}

	public void testEmofilt(String test) {
		_emofilt.loadText(test, true);
		_emofilt.setEmotion("hotAnger");
		_emofilt.modifyUtterance();
		_emoPlayer.printPhoFile();
		_emoPlayer.genWavFile();
		_emoPlayer.playAndDelete();
	}

	private void readIn(String filename) {
		_fileName = filename;
		try {
			SAXBuilder builder = new SAXBuilder(false);
			_storyTree = builder.build(new File(_fileName));
			_storyRoot = _storyTree.getRootElement();
			List<Element> phrases = _storyRoot.getChildren();
			for (Iterator<Element> iter = phrases.iterator(); iter.hasNext();) {
				Element phrase = (Element) iter.next();
				Element emotion = phrase.getChild("emotion");
				Element category = emotion.getChild("category");
				String emotionName = category.getAttribute("value").getValue();
				String intensity = category.getAttribute("intensity")
						.getValue();
				String text = phrase.getText().trim();
				_logger.debug(text + " " + emotionName + " " + intensity);
			}
		} catch (Exception e) {
			reportError(e);
		}

	}

	public static void main(String[] args) {
		new Story(args);
	}

}
