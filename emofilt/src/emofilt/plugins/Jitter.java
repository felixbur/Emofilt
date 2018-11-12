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

import org.apache.log4j.Logger;
import org.jdom.Element;

import emofilt.ElemNotFoundException;
import emofilt.Emofilt;
import emofilt.Language;
import emofilt.ModificationPlugin;
import emofilt.Syllable;
import emofilt.util.Util;
import emofilt.Utterance;
import emofilt.gui.ModificationPanel;
import emofilt.gui.OneRatePanel;

/**
 * Modification Plugin
 * 
 * @author Felix Burkhardt
 */
public class Jitter implements ModificationPlugin {
    private String name = "";

    private String type = "";

    private final String initFileName = "init/jitter_init.txt";

    private int defaultRate = 0;

    private HashMap initValues = null;

    private Logger debugLogger = null;

    private OneRatePanel mp = null;

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
        debugLogger.debug(name+ " initialisation, use GUI="+useGui);
        try {
            initValues = Util.getValuesFromBufferedReader(new BufferedReader(new InputStreamReader(Emofilt.class.getResourceAsStream(initFileName))));
            name = (String) initValues.get("name");
            type = (String) initValues.get("type");
            defaultRate = Integer.parseInt((String) initValues
                    .get("defaultRate"));
            if (useGui) {
                mp = new OneRatePanel(this, initValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
            debugLogger.error(e.getMessage());
        }
    }

    public Utterance modify(Utterance utt, Element emotion, double globalRate, Language lang) {
        int rate = 0;
        try {
            rate = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, "rate"));
        } catch (ElemNotFoundException e) {
            rate = defaultRate;
        }
        if (rate == defaultRate) {
            return utt;
        }
        Utterance u = (Utterance) utt.clone();
        int change = (int)((rate-defaultRate));
        int GlobalRateFactor  = (int)(change*globalRate);
        rate = defaultRate + change + GlobalRateFactor;
        // first set every frame one f0-value
        u.modelF0Contour();
        for (Iterator iter = u.getSyllables().iterator(); iter.hasNext();) {
            ((Syllable) iter.next()).addJitter(rate);
        }
        return u;
    }

    public String toString() {
        return "name: " + name + ", type: " + type;
    }

    public Element setEmotion(Element emotion) {
        String newRate = mp.getValue();
        Element returnElem = Util.setValueInEmotion(emotion, name, type,
                "rate", newRate, String.valueOf(defaultRate));
        return returnElem;
    }

    public void setGui(Element emotion) {
        int rate = defaultRate;
        try {
            rate = Integer.parseInt(Util.getValueFromEmotion(emotion, name, type, "rate"));
        } catch (ElemNotFoundException e) {
            debugLogger.debug(e.getMessage());
        }
        mp.setValue(new Integer(rate));
    }

    public void setGuiDefault() {
        mp.setDefault();
    }

    public void setPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcl = pcl;
        mp.setPropertyChangeListener(pcl);
    }
}