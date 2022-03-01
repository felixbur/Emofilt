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
import emofilt.Language;
import emofilt.ModificationPlugin;
import emofilt.Phoneme;
import emofilt.Syllable;
import emofilt.util.Util;
import emofilt.Utterance;
import emofilt.gui.ModificationPanel;
import emofilt.gui.OneTypePanel;

/**
 * Modification Plugin
 * 
 * @author Felix Burkhardt
 */
public class VowelTarget implements ModificationPlugin {
    private final String targetDesignator = "target";

    private String name = "";

    private String type = "";

    private final String initFileName = "init/vowelTarget_init.txt";

    private String defaultTarget = "normal";

    private HashMap initValues = null;

    private Logger debugLogger = null;

    private OneTypePanel mp = null;

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
            initValues = Util.getValuesFromBufferedReader(new BufferedReader(new InputStreamReader(Emofilt.class.getResourceAsStream(initFileName))));
            name = (String) initValues.get("name");
            type = (String) initValues.get("type");
            defaultTarget = (String) initValues
                    .get("defaultType");
            if (useGui) {
                Vector types = new Vector();
                types.add(Phoneme.VOWEL_NORMAL);
                types.add(Phoneme.VOWEL_OVERSHOOT);
                types.add(Phoneme.VOWEL_UNDERSHOOT);
                mp = new OneTypePanel(this, initValues, types);
            }
        } catch (Exception e) {
            e.printStackTrace();
            debugLogger.error(e.getMessage());
        }
    }

    public Utterance modify(Utterance utt, Element emotion, double globalRate, Language lang) {
        String target = "undefined";
        try {
            target = Util.getValueFromEmotion(emotion, name, type,
                    targetDesignator);
        } catch (ElemNotFoundException e) {
            target = defaultTarget;
        }
        if (globalRate<0) {
            target = defaultTarget;        	
        }
        if (target.compareTo(defaultTarget)==0) {
            return utt;
        }
        Utterance u = (Utterance) utt.clone();
        if (target.compareTo(Phoneme.VOWEL_OVERSHOOT) == 0) {
            for (Iterator iter = u.getSyllables().iterator(); iter.hasNext();) {
                ((Syllable) iter.next()).decentralize();
            }
        } else if (target.compareTo(Phoneme.VOWEL_UNDERSHOOT) == 0) {
            for (Iterator iter = u.getSyllables().iterator(); iter.hasNext();) {
                ((Syllable) iter.next()).centralize();
            }
        }
        return u;
    }

    public String toString() {
        return "name: " + name + ", type: " + type;
    }

    public Element setEmotion(Element emotion) {
        String newTarget = mp.getTypeValue();
        Element returnElem = Util.setValueInEmotion(emotion, name, type,
                targetDesignator, newTarget, String.valueOf(defaultTarget));
        return returnElem;
    }

    public void setGui(Element emotion) {
        String targetType = defaultTarget;
        try {
            targetType = Util.getValueFromEmotion(emotion, name, type,
                    targetDesignator);
        } catch (ElemNotFoundException e) {
            debugLogger.debug(e.getMessage());
        }
        mp.setTypeValue(targetType);
    }

    public void setGuiDefault() {
        mp.setDefault();
    }

    public void setPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcl = pcl;
        mp.setPropertyChangeListener(pcl);
    }
}