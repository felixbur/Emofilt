/*
 * Created on 18.09.2004
 *
 * @author Felix Burkhardt
 */
package emofilt.gui;

import emofilt.util.Util;

import emofilt.*;
import org.apache.log4j.*;
import java.awt.*;
import java.util.Iterator;

import javax.swing.JPanel;

/**
 * Panel to display the pitch contour.
 * 
 * @author Felix Burkhardt
 */
public class PitchContourScreen extends JPanel {

	private Image pitchContour;

	private int pcWidth = GuiConstants.PC_WIDTH;
	private int pcHeight = GuiConstants.PC_HEIGHT;

	private MainFrame mf;

	private Logger debugLogger = null;

	private int scaleWidth = GuiConstants.SCALE_WIDTH;
	private int borderStart = GuiConstants.BORDER_START;
	private int syllableBorderStart = GuiConstants.SYLLABLE_BORDER_START;
	private int labelStart = GuiConstants.LABEL_START;
	private int freqStart = GuiConstants.FREQ_START;

	private boolean drawLabels = true;

	private boolean drawFreqScale = true;

	private boolean drawTimeScale = true;
	private boolean drawBorders = true;
	private int maxF0 = GuiConstants.MAX_F0;
	private boolean drawSyllables = true;

	public void setDrawSyllables(boolean drawSyllables) {
		this.drawSyllables = drawSyllables;
	}

	public void setDrawBorders(boolean drawBorders) {
		this.drawBorders = drawBorders;
	}

	public void setDrawFreqScale(boolean drawFreqScale) {
		this.drawFreqScale = drawFreqScale;
	}

	public void setDrawLabels(boolean drawLabels) {
		this.drawLabels = drawLabels;
	}

	public PitchContourScreen(MainFrame mf) {
		this.mf = mf;
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
		try {
			maxF0 = Integer.parseInt(mf.emofilt._config
					.getString("pitchContour.maxF0"));
			pcWidth = Integer.parseInt(mf.emofilt._config
					.getString("pcsWidth"));
			pcHeight = Integer.parseInt(mf.emofilt._config
					.getString("pcsHeight"));
			scaleWidth = Integer.parseInt(mf.emofilt._config
					.getString("pitchContour.scaleWidth"));
			borderStart = Integer.parseInt(mf.emofilt._config
					.getString("pitchContour.borderStart"));
			syllableBorderStart = Integer.parseInt(mf.emofilt._config
					.getString("syllableBorderStart"));
			labelStart = Integer.parseInt(mf.emofilt._config
					.getString("pitchContour.labelStart"));
			freqStart = Integer.parseInt(mf.emofilt._config
					.getString("pitchContour.freqStart"));
		} catch (Exception e) {
			// use the constants
		}
	}

	public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		if (pitchContour == null) {
			pitchContour = createImage(pcWidth, pcHeight);
		}
		paintContourImage();
		g.drawImage(pitchContour, 0, 0, this);
	}

	private void paintContourImage() {
		Utterance utt = mf.emofilt.getActUtt();
		if (utt == null) {
			return;
		}
		// debugLogger.debug("drawing pitchContour for utterance with mean F0: "
		// + utt.getF0Mean());
		if (pitchContour == null)
			return;
		Graphics g = pitchContour.getGraphics();
		double Y_SCALE_FACTOR = (double) pcHeight / maxF0;

		// draw background
		g.setColor(Util.getColorFromUidf("mainFrame.pitchContourBG"));
		g.fillRect(0, 0, pcWidth, pcHeight);
		// draw frequency scale
		if (drawFreqScale) {
			g.setColor(Util.getColorFromUidf("mainFrame.scaleColor"));
			for (int i = 0; i < maxF0 / 100; i++) {
				int y = (int) (Y_SCALE_FACTOR * (i * 100 + freqStart));
				g.drawLine(0, pcHeight - y, scaleWidth, pcHeight - y);
				g.drawString(String.valueOf(i * 100) + "Hz", 0, pcHeight - y);
			}
		}
		int displayXlen = pcWidth - scaleWidth; 
		// draw time scale
		double X_SCALE_FACTOR = 0;
		if (drawTimeScale) {
			double uds = ((double) utt.getDuration() / 1000.0);
			X_SCALE_FACTOR = displayXlen / uds;
			g.setColor(Util.getColorFromUidf("mainFrame.scaleColor"));
			for (int i = 0; i < uds; i++) {
				int x = (int) (X_SCALE_FACTOR * i) + scaleWidth;
				g.drawLine(x, pcHeight, x, pcHeight - 10);
				g.drawString(String.valueOf(i) + "s", x + 1, pcHeight);
			}
		}
		// draw utterance
		int xold = scaleWidth;
		double xlen = (double) utt.getDuration();
//		debugLogger.debug("duration: " + utt.getDuration());
		X_SCALE_FACTOR = pcWidth / xlen;
		for (int i = 0; i < utt.getSyllables().size(); i++) {
			Syllable syl = (Syllable) utt.getSyllables().elementAt(i);
			//debugLogger.debug(syl.toString());
			if (drawSyllables) {
				// draw syllable borders
				g.setColor(Util
						.getColorFromUidf("mainFrame.syllableBorderColor"));
				g.drawLine(xold, pcHeight - syllableBorderStart, xold, pcHeight
						- pcHeight);
				if (syl.isWordStressed()) {
					g.setColor(Util
							.getColorFromUidf("mainFrame.syllableBorderColor"));
					g.drawString("^", xold, pcHeight - syllableBorderStart);
				}
				if (syl.isFocusStressed()) {
					g.setColor(Util
							.getColorFromUidf("mainFrame.syllableBorderColor"));
					g.drawString("^!", xold, pcHeight - syllableBorderStart);
				}
			}

			for (int k = 0; k < syl.getPhonemes().size(); k++) {
				Phoneme pho = (Phoneme) syl.getPhonemes().elementAt(k);

				// debugLogger.debug(pho.toString()+", xold: "+xold);
				// draw phoneme name and borders
				if (drawBorders & !(drawSyllables & pho.isSyllableStart())) {
					g.setColor(Util
							.getColorFromUidf("mainFrame.phonemeBorderColor"));
					g.drawLine(xold, pcHeight - borderStart, xold, pcHeight
							- pcHeight);
				}
				if (drawLabels) {
					g.setColor(Util
							.getColorFromUidf("mainFrame.phonemeLabelColor"));
					g.drawString(pho.getName(), xold, pcHeight - labelStart);
				}
				if (pho.isVoiced()) {
					for (Iterator iter = pho.getF0vals().iterator(); iter
							.hasNext();) {
						F0Val f0Val = (F0Val) iter.next();
						int posInMs = (int) (((double) f0Val.getPos() * pho
								.getDur()) / 100.0);

						int xval = (int)(xold + (posInMs * X_SCALE_FACTOR));
						int y = (int) ((f0Val.getVal() + freqStart) * Y_SCALE_FACTOR);
						g.setColor(Util
								.getColorFromUidf("mainFrame.pitchContourFG"));
						g.drawLine(xval, pcHeight - y, xval, pcHeight - y);
						g.drawLine(xval, pcHeight - y - 1, xval, pcHeight - y
								- 1);
						g.drawLine(xval, pcHeight - y + 1, xval, pcHeight - y
								+ 1);
						g.drawLine(xval + 1, pcHeight - y, xval + 1, pcHeight
								- y);
						g.drawLine(xval - 1, pcHeight - y - 1, xval - 1,
								pcHeight - y - 1);
					}
				}
				xold += (int)(pho.getDur() * X_SCALE_FACTOR);
			}
		}
	}

	public void setDrawTimeScale(boolean drawTimeScale) {
		this.drawTimeScale = drawTimeScale;
	}
}