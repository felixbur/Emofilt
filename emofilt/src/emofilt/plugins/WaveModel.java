/*
 * Created on 21.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt.plugins;

import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Element;

import emofilt.ElemNotFoundException;
import emofilt.Emofilt;
import emofilt.F0Val;
import emofilt.Language;
import emofilt.ModificationPlugin;
import emofilt.Phoneme;
import emofilt.Syllable;
import emofilt.util.Util;
import emofilt.Utterance;
import emofilt.gui.ModificationPanel;
import emofilt.gui.TwoRatePanel;

/**
 * Modification Plugin
 * 
 * @author Felix Burkhardt
 */
public class WaveModel implements ModificationPlugin {
	private final String rateDesignator = "rate1";
	private final String rateDesignator2 = "rate2";

	private String name = "";

	private String type = "";

	private final String initFileName = "init/waveModel_init.txt";

	private int defaultRate = 0;
	private int defaultRate2 = 0;

	private HashMap initValues = null;

	private Logger debugLogger = null;

	private TwoRatePanel mp = null;

	private boolean useGui = false;

	PropertyChangeListener pcl = null;

	public String getModificationType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ModificationPanel getPanel() {
		return mp;
	}

	public void init(Logger logger, boolean useGui) {
		this.useGui = useGui;
		debugLogger = logger;
		//debugLogger.debug(name + " initialisation, use GUI=" + useGui);
		try {
			initValues = Util.getValuesFromBufferedReader(
					new BufferedReader(new InputStreamReader(Emofilt.class.getResourceAsStream(initFileName))));
			name = (String) initValues.get("name");
			type = (String) initValues.get("type");
			defaultRate = Integer.parseInt((String) initValues.get("defaultRate"));
			defaultRate2 = Integer.parseInt((String) initValues.get("defaultRate2"));
			if (useGui) {
				mp = new TwoRatePanel(this, initValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger.error(e.getMessage());
		}
	}

	public Utterance modify(Utterance utt, Element emotion, double globalRate, Language lang) {
		int rate = 0;
		int rate2 = 0;
		try {
			rate = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, rateDesignator));
			rate2 = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, rateDesignator2));
		} catch (ElemNotFoundException e) {
			rate = defaultRate;
		}
		if (rate == defaultRate) {
			return utt;
		}
		int change = (int) ((rate - defaultRate));
		int GlobalRateFactor = (int) (change * globalRate);
		rate = defaultRate + change + GlobalRateFactor;
		int change2 = (int) ((rate2 - defaultRate));
		int GlobalRateFactor2 = (int) (change2 * globalRate);
		rate2 = defaultRate + change2 + GlobalRateFactor2;

		Utterance u = (Utterance) utt.clone();
		// first set every frame one f0 value.
		u.modelF0Contour();
		Vector<Integer> tmpF0Vec = new Vector();
		Vector indexList = new Vector(), targetList = new Vector();
		int frameCounter = 0;
		indexList.add(new Integer(0));
		targetList.add(new Integer((int) u.getFirstNonZeroSylMean()));
		// read in target vals and indices
		for (Iterator<Syllable> iter = u.getSyllables().iterator(); iter.hasNext();) {
			Syllable syl = (Syllable) iter.next();
			for (Iterator iter2 = syl.getPhonemes().iterator(); iter2.hasNext();) {
				Phoneme pho = (Phoneme) iter2.next();
				if (syl.isFocusStressed() && pho.isVowel()) {
					if (indexList.size() > 1) {
						int actIndex = frameCounter + pho.getFrameNum() / 2;
						int lastIndex = ((Integer) indexList.lastElement()).intValue();
						indexList.add(new Integer(lastIndex + ((actIndex - lastIndex) / 2)));
						targetList.add(new Integer((int) (u.getF0Mean() * rate2 / 100)));
					}
					indexList.add(new Integer(frameCounter + pho.getFrameNum() / 2));
					targetList.add(new Integer((int) (syl.getF0Mean() * rate / 100)));
				}
				if (pho.isVoiced()) {
					for (int i = 0; i < pho.getF0vals().size(); i++) {
						tmpF0Vec.add(new Integer(((F0Val) pho.getF0vals().elementAt(i)).getVal()));
					}
				} else {
					for (int i = 0; i < pho.getFrameNum(); i++) {
						tmpF0Vec.add(new Integer(0));
					}
				}
				frameCounter += pho.getFrameNum();
			}
		}
		indexList.add(new Integer(frameCounter));
		targetList.add(new Integer((int) ((Syllable) u.getSyllables().lastElement()).getF0Mean()));
		// modify F0Vals
		for (int i = 0; i < indexList.size() - 1; i++) {
			int startIndex = ((Integer) indexList.elementAt(i)).intValue();
			int endIndex = ((Integer) indexList.elementAt(i + 1)).intValue();
			int startVal = ((Integer) targetList.elementAt(i)).intValue();
			int endVal = ((Integer) targetList.elementAt(i + 1)).intValue();
			// count voiced Frames
			int vocCounter = 0;
			for (int k = startIndex; k < endIndex; k++) {
				if (((Integer) tmpF0Vec.elementAt(k)).intValue() != 0) {
					vocCounter++;
				}
			}
			if (startVal < endVal) {
				double step = (int) (endVal - startVal) / (double) vocCounter;
				vocCounter = 0;
				for (int k = startIndex; k < endIndex; k++) {
					if (((Integer) tmpF0Vec.elementAt(k)).intValue() != 0) {
						tmpF0Vec.add(k, new Integer(startVal + (int) (vocCounter * step)));
						vocCounter++;
					}
				}
			} else if (startVal > endVal) {
				double step = (int) (startVal - endVal) / (double) vocCounter;
				vocCounter = 0;
				for (int k = startIndex; k < endIndex; k++) {
					if (((Integer) tmpF0Vec.elementAt(k)).intValue() != 0) {
						tmpF0Vec.add(k, new Integer(startVal - (int) (vocCounter * step)));
						vocCounter++;
					}
				}
			}
		}
		// assign new f0 vals to phones
		frameCounter = 0;
		for (Iterator iter = u.getSyllables().iterator(); iter.hasNext();) {
			Syllable syl = (Syllable) iter.next();
			for (Iterator iter2 = syl.getPhonemes().iterator(); iter2.hasNext();) {
				Phoneme pho = (Phoneme) iter2.next();
				Vector tmpValVec = new Vector();
				for (int i = 0; i < pho.getFrameNum(); i++) {
					tmpValVec.add(Util.checkMinPitch((Integer) tmpF0Vec.elementAt(frameCounter)));
					frameCounter++;
				}
				if (pho.isVoiced()) {
					pho.setF0ValueVector(tmpValVec);
				}
			}
		}
		return u;
	}

	public String toString() {
		return "name: " + name + ", type: " + type;
	}

	public Element setEmotion(Element emotion) {
		String newRate = mp.getRateValue();
		Element returnElem = Util.setValueInEmotion(emotion, name, type, rateDesignator, newRate,
				String.valueOf(defaultRate));
		if (newRate.compareTo(String.valueOf(defaultRate)) != 0) {
			String newType = mp.getRateValue2();
			returnElem = Util.setValueInEmotion(returnElem, name, type, rateDesignator2, newType, "dummy");
		}
		return returnElem;
	}

	public void setGui(Element emotion) {
		int rate = defaultRate;
		int rate2 = defaultRate;
		try {
			rate = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, rateDesignator));
			rate2 = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, rateDesignator2));
		} catch (ElemNotFoundException e) {
			debugLogger.debug(e.getMessage());
		}
		mp.setRateValue(new Integer(rate));
		mp.setRateValue2(new Integer(rate2));
	}

	public void setGuiDefault() {
		mp.setDefault();
	}

	public void setPropertyChangeListener(PropertyChangeListener pcl) {
		this.pcl = pcl;
		mp.setPropertyChangeListener(pcl);
	}
}