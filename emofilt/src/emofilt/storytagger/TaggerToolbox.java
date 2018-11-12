package emofilt.storytagger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.felix.util.ColorUtil;
import com.felix.util.FileUtil;
import com.felix.util.KeyValues;
import com.felix.util.StringUtil;

import emofilt.Emofilt;
import emofilt.Emotions;
import emofilt.gui.MainFrame;
import emofilt.storyreading.Story;

public class TaggerToolbox extends JFrame implements ActionListener {
	private KeyValues _config;
	private Logger _logger;
	private TaggerGUI _taggerGUI;
	private Emotions _emotions;
	private ButtonGroup emotionsBG;
	private JPanel _mainPane;

	public TaggerToolbox(TaggerGUI taggerGui, KeyValues config, Emofilt emofilt) {
		super("tagger toolbox");
		_config = config;
		_emotions = emofilt.getEmotions();
		_logger = Logger.getLogger(Emofilt.LOGGER_NAME);
		_taggerGUI = taggerGui;
		_mainPane = new JPanel();
		_mainPane.setLayout(new BoxLayout(_mainPane, BoxLayout.Y_AXIS));
		_mainPane.add(makeEmotionPanel());
		// _mainPane.add(makeButtonPanel());
		this.getContentPane().add(_mainPane);
	}

	public void showFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public JPanel getPane() {
		return _mainPane;
	}

	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton b1 = getNewButton("tbb_removeTags");
		JButton b4 = getNewButton("tbb_undo");
		JButton b7 = getNewButton("closeButton");
		panel.add(b1);
		panel.add(b4);
		panel.add(b7);
		return panel;
	}

	private JPanel makeEmotionPanel() {
		int emotionNumBreak = _config.getInt("emotionNumBreak");
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.white, 10));
		emotionsBG = new ButtonGroup();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		int emotionNum = _emotions.getEmotions().size(), emotionIndex = 0;
		boolean first = true;
		JPanel subPanel=null;
		for (Iterator iter = _emotions.getEmotions().iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emoS = emotion.getAttribute("name").getValue();
			String colorS = emotion.getAttribute("color").getValue();
			Color col = ColorUtil.hexStringToColor(colorS);
			if (emotionIndex % emotionNumBreak == 0) {
				if (first) {
					first = false;
				} else {
					panel.add(subPanel);
				}
				subPanel = new JPanel();
				subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
				subPanel.setBorder(new LineBorder(Color.white, 10));
				JRadioButton cbn = new JRadioButton();
				cbn.addActionListener(this);
				cbn.setActionCommand(emoS);
				JLabel b1lab = new JLabel(emoS);
				b1lab.setBorder(new LineBorder(col, 5));
				subPanel.add(cbn);
				subPanel.add(b1lab);
				emotionsBG.add(cbn);
			} else {
				JRadioButton cbn = new JRadioButton();
				cbn.addActionListener(this);
				cbn.setActionCommand(emoS);
				JLabel b1lab = new JLabel(emoS);
				b1lab.setBorder(new LineBorder(col, 5));
				subPanel.add(cbn);
				subPanel.add(b1lab);
				emotionsBG.add(cbn);
			}
			emotionIndex++;
		}
		panel.add(subPanel);
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if ("closeButton".equals(e.getActionCommand())) {
			// dispose();
			System.exit(0);
		} else if ("tbb_removeTags".equals(e.getActionCommand())) {
			_taggerGUI.removeTags();
		} else if ("tbb_undo".equals(e.getActionCommand())) {
			_taggerGUI.undo();
		}
		for (Iterator iter = _emotions.getEmotions().iterator(); iter.hasNext();) {
			Element emotion = (Element) iter.next();
			String emoS = emotion.getAttribute("name").getValue();
			String colorS = emotion.getAttribute("color").getValue();
			Color col = ColorUtil.hexStringToColor(colorS);
			if (emoS.equals(e.getActionCommand())) {
				_taggerGUI.setEmotion(emoS, col);
				emotionsBG.clearSelection();
			}
		}
	}

	private JButton getNewButton(String configString) {
		JButton returnButton = new JButton(_config.getString(configString));
		returnButton.setActionCommand(configString);
		returnButton.addActionListener(this);
		return returnButton;
	}

}
