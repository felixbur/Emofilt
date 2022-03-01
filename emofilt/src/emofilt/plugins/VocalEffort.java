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
public class VocalEffort implements ModificationPlugin {
    private final String effortDesignator = "effort";

    private String name = "";

    private String type = "";

    private final String initFileName = "init/vocalEffort_init.txt";

    private String defaultEffort = "normal";

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
            defaultEffort = (String) initValues
                    .get("defaultType");
            if (useGui) {
                Vector types = new Vector();
                types.add(Phoneme.EFFORT_NORMAL);
                types.add(Phoneme.EFFORT_LOUD);
                types.add(Phoneme.EFFORT_SOFT);
                mp = new OneTypePanel(this, initValues, types);
            }
        } catch (Exception e) {
            e.printStackTrace();
            debugLogger.error(e.getMessage());
        }
    }

    public Utterance modify(Utterance utt, Element emotion, double globalRate, Language lang) {
        String effort = "undefined";
        try {
            effort = Util.getValueFromEmotion(emotion, name, type,
                    effortDesignator);
        } catch (ElemNotFoundException e) {
            effort = defaultEffort;
        }
        if (globalRate<0) {
            effort = defaultEffort;        	
        }
        if (effort.compareTo(defaultEffort)==0) {
            return utt;
        }
        if (lang.getLangname().compareTo("de") != 0 ) {
            debugLogger.debug("vocal effort  only works with Voice de");
            return utt;
        }
        if (lang.getVoicename().compareTo("6") != 0 && lang.getVoicename().compareTo("7") != 0) {
            debugLogger.debug("vocal effort  only works with Voice de6 or de7");            
            return utt;
        }
        Utterance u = (Utterance) utt.clone();
            for (Iterator iter = u.getSyllables().iterator(); iter.hasNext();) {
                ((Syllable) iter.next()).changeEffort(effort);
           }
        return u;
    }

    public String toString() {
        return "name: " + name + ", type: " + type;
    }

    public Element setEmotion(Element emotion) {
        String newEffort = mp.getTypeValue();
        Element returnElem = Util.setValueInEmotion(emotion, name, type,
                effortDesignator, newEffort, String.valueOf(defaultEffort));
        return returnElem;
    }

    public void setGui(Element emotion) {
        String effortType = defaultEffort;
        try {
            effortType = Util.getValueFromEmotion(emotion, name, type,
                    effortDesignator);
        } catch (ElemNotFoundException e) {
            debugLogger.debug(e.getMessage());
        }
        mp.setTypeValue(effortType);
    }

    public void setGuiDefault() {
        mp.setDefault();
    }

    public void setPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcl = pcl;
        mp.setPropertyChangeListener(pcl);
    }
}