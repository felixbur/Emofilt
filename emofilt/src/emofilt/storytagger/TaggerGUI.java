package emofilt.storytagger;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.felix.util.ColorPane;
import com.felix.util.ColorUtil;
import com.felix.util.FileUtil;
import com.felix.util.KeyValues;
import com.felix.util.StringUtil;
import com.felix.util.Util;

import emofilt.Constants;
import emofilt.Emofilt;
import emofilt.Emotions;
import emofilt.Language;
import emofilt.gui.MainFrame;
import emofilt.storyreading.Story;

/**
 * A GUI to tag a story with emotions.
 * 
 * @author felix
 * 
 */
public class TaggerGUI extends JFrame implements ActionListener {
	private int dim_width = 700;
	private int dim_height = 300;
	private KeyValues _config;
	private Logger _logger;
	private ColorPane _colorTextField;
	private JTextArea _textField;
	private String _lastText;
	private Story _story;
	private String _actStoryFileName = "";
	private File _tmpLoadDir;
	private Vector<TaggedPiece> _taggedPieces;
	private String _text;
	private JLabel _fileNameLabel = null;
	private Document _undoDocument;
	private TaggerToolbox _toolbox;
	public static String NEUTRAL_EMOTION = "neutral";
	private Emotions _emotions;
	private Emofilt _emofilt;
	private PlayThread _playThread = null;
	private JComboBox _langBox;

	/**
	 * Start with path to configuration file.
	 * 
	 * @param configPath
	 */
	public TaggerGUI(String configPath) {
		super();
		_config = new KeyValues(configPath, "=");
		setTitle(_config.getString("windowTitle") + " "
				+ Constants.TAGGER_VERSION);
		JPanel labelPane = new JPanel();
		_emofilt = new Emofilt(_config.getString("emofiltConfig"));
		_story = new Story(_emofilt);
		_toolbox = new TaggerToolbox(this, _config, _emofilt);
		Emofilt.configureLogger();
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		_taggedPieces = new Vector<TaggedPiece>();
		labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.Y_AXIS));
		labelPane.add(makeTextPanel());
		labelPane.add(makeButtonPanel());
		_emotions = _emofilt.getEmotions();
		labelPane.add(_toolbox.getPane());
		this.getContentPane().add(labelPane);
		dim_width = _config.getInt("dimWidth");
		dim_height = _config.getInt("dimHeight");
		this.getContentPane().setPreferredSize(
				new Dimension(dim_width, dim_height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Remove existing tags from text.
	 */
	public void removeTags() {
		_colorTextField.setForegroundForSelection(Constants.NEUTRAL_COLOR);
	}

	/**
	 * Get back to last saved version.
	 * 
	 */
	public void undo() {
		_colorTextField.setDocument(_undoDocument);
	}

	/**
	 * Set a new emotion for the selected text.
	 * 
	 * @param emotionName
	 *            The emotion.
	 * @param color
	 *            The color.
	 */
	public void setEmotion(String emotionName, Color color) {
		_undoDocument = _colorTextField.getDocument();
		_colorTextField.setForegroundForSelection(color);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            The configuration file.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("usage TaggerGUI <path to config file>");
		} else {
			new TaggerGUI(args[0]);
		}
	}

	/**
	 * Implement the GUI listener.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("exitButton".equals(e.getActionCommand())) {
			System.exit(0);
		} else if ("playButton".equals(e.getActionCommand())) {
			play();
		} else if ("stopButton".equals(e.getActionCommand())) {
			stop();
		} else if ("playSingleButton".equals(e.getActionCommand())) {
			playSingle();
		} else if ("loadButton".equals(e.getActionCommand())) {
			load();
		} else if ("saveButton".equals(e.getActionCommand())) {
			save();
		} else if ("saveAsButton".equals(e.getActionCommand())) {
			saveAs();
		} else if ("saveAudioButton".equals(e.getActionCommand())) {
			saveAudio();
		} else if (e.getSource() == _langBox) {
			_emofilt.setActLanguage(_emofilt.getLanguages().findLanguage(
					((String) _langBox.getSelectedItem()).trim()));
		}
	}

	private void load() {
		_colorTextField.setText("");
		// Create a file chooser
		if (_tmpLoadDir == null) {
			_tmpLoadDir = new File(Emofilt._config.getString("workingDir"));
		}
		final JFileChooser fc = new JFileChooser(_tmpLoadDir);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			_tmpLoadDir = fc.getCurrentDirectory();
			_actStoryFileName = fc.getSelectedFile().getAbsolutePath();
		}
		try {
			String text = FileUtil.getFileText(_actStoryFileName,
					FileUtil.ENCODING_UTF_8);
			_textField.setText(text);
			Vector<TaggedPiece> pieces = parseMarkup(text);
			for (Iterator iterator = pieces.iterator(); iterator.hasNext();) {
				TaggedPiece tp = (TaggedPiece) iterator.next();
				_colorTextField.append(tp.getColor(), tp.getText());
				_colorTextField.appendNewLine();
			}
			appendTitle(new File(_actStoryFileName).getName());
		} catch (Exception e) {
			e.printStackTrace();
			Util.reportError(e, _logger);
		}
		System.err.println(makeEmotionTagText());
	}

	private void save() {
		// _story.testEmofilt(_textField.getText());
		if (_actStoryFileName.length() == 0) {
			saveAs();
		} else {
			try {
				FileUtil.writeFileContent(_actStoryFileName,
						makeEmotionTagText(), FileUtil.ENCODING_UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
				Util.reportError(e, _logger);
			}
		}
	}

	private void saveAs() {
		try {
			// Create a file chooser
			if (_tmpLoadDir == null) {
				_tmpLoadDir = new File(Emofilt._config.getString("workingDir"));
			}
			final JFileChooser fc = new JFileChooser(_tmpLoadDir);

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				_tmpLoadDir = fc.getCurrentDirectory();
				_actStoryFileName = fc.getSelectedFile().getAbsolutePath();
				appendTitle(new File(_actStoryFileName).getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		save();
	}

	private void saveAudio() {
		try {
			String filename;
			// Create a file chooser
			if (_tmpLoadDir == null) {
				_tmpLoadDir = new File(Emofilt._config.getString("workingDir"));
			}
			final JFileChooser fc = new JFileChooser(_tmpLoadDir);

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				_tmpLoadDir = fc.getCurrentDirectory();
				filename = fc.getSelectedFile().getAbsolutePath();
				String text = makeEmotionTagText();
				_story.saveAudio(filename, text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stop() {
		System.err.println("pressed stop.");
		if (_playThread != null) {
			_playThread.stop();
		}
	}

	private class PlayThread extends Thread {
		public void run() {
			_story.playText(_text);
		}

		public void stopPlay() {
			_story.stopPlay();
		}
	}

	private void play() {
		_text = makeEmotionTagText();
		_textField.setText(_text);
		_playThread = new PlayThread();
		_playThread.start();

	}

	private void playSingle() {
		_text = makeEmotionTagText();
		_textField.setText(_text);
		_playThread = new PlayThread();
		_playThread.start();
	}

	private JButton getNewButton(String configString) {
		JButton returnButton = new JButton(_config.getString(configString));
		returnButton.setActionCommand(configString);
		returnButton.addActionListener(this);
		return returnButton;
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
						// String phrase = StringUtil.stripNewlines(token
						// .substring(braceIndex + 1, token.length())
						// .trim());
						String phrase = token.substring(braceIndex + 1,
								token.length()).trim();
						if (phrase.length() > 0) {
							TaggedPiece tp = new TaggedPiece(emotionName,
									phrase);
							Element emotion = _emotions.getEmotion(emotionName);
							Color c = ColorUtil.hexStringToColor(emotion
									.getAttribute("color").getValue());
							tp.setColor(c);
							phrases.add(tp);
						}
					}
				} else {
					String phrase = token;
					TaggedPiece tp = new TaggedPiece(Constants.NEUTRAL_EMOTION,
							phrase);
					tp.setColor(Constants.NEUTRAL_COLOR);
					phrases.add(tp);
				}
			}
		}
		return phrases;
	}

	private JTabbedPane makeTextPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
		_colorTextField = new ColorPane();
		JScrollPane inputScrollPane = new JScrollPane(_colorTextField);
		inputScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		int width = _config.getInt("dimWidth");
		int height = _config.getInt("dimHeight");
		inputScrollPane.setPreferredSize(new Dimension(width, height));
		colorPanel.add(inputScrollPane);
		_colorTextField.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent mouseEvent) {
				Point clickPoint = mouseEvent.getPoint();
			}

			int dClkRes = 300;
			long timeMouseDown = 0;

			public void mousePressed(MouseEvent mouseEvent) {
				int modifiers = mouseEvent.getModifiers();
				Point clickPoint = mouseEvent.getPoint();
				if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
					// left click
					if (mouseEvent.getClickCount() == 2) {
						_colorTextField.selectLineAroundCursor();
					}
				} else if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
					// right click
				}
			}
		});
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		_textField = new JTextArea();
		JScrollPane inputScrollPane2 = new JScrollPane(_textField);
		inputScrollPane2
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane2.setPreferredSize(new Dimension(width, height));
		inputScrollPane2
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textPanel.add(inputScrollPane2);
		_textField.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent mouseEvent) {
				Point clickPoint = mouseEvent.getPoint();
			}

			public void mousePressed(MouseEvent mouseEvent) {
				int modifiers = mouseEvent.getModifiers();
				Point clickPoint = mouseEvent.getPoint();
				if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
					// left click
				} else if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
					// right click
				}
			}
		});
		colorPanel.setName(_config.getString("colorPanelName"));
		tabbedPane.add(colorPanel);
		textPanel.setName(_config.getString("textPanelName"));
		tabbedPane.add(textPanel);
		return tabbedPane;
	}

	private void showTaggedPieces() {
		for (int i = 0; i < _taggedPieces.size(); i++) {
			TaggedPiece tp = _taggedPieces.elementAt(i);
		}
	}

	private String makeEmotionTagText() {
		String retString = _colorTextField.getColorTaggedText();
		retString = retString.replaceAll("color", "emotion");
		for (Iterator iter = _emotions.getEmotions().iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emoS = emotion.getAttribute("name").getValue();
			String colorS = emotion.getAttribute("color").getValue();
			retString = retString.replaceAll(colorS, emoS);
		}
		return retString;
	}

	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(getNewButton("playButton"));
		panel.add(getNewButton("playSingleButton"));
		panel.add(getNewButton("stopButton"));
		panel.add(getNewButton("loadButton"));
		panel.add(getNewButton("saveButton"));
		panel.add(getNewButton("saveAsButton"));
		panel.add(getNewButton("saveAudioButton"));
		panel.add(getNewButton("exitButton"));

		_langBox = new JComboBox(_emofilt.getLanguages().getLanguageNames());
		_langBox.setSelectedItem(_emofilt.getActLanguage().getName());
		panel.add(_langBox);
		_langBox.addActionListener(this);
		return panel;
	}

	private void appendTitle(String s) {
		setTitle(_config.getString("windowTitle") + " "
				+ Constants.TAGGER_VERSION + " " + s);
	}
}
