/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;

/**
 * 
 *  A content handler extending the DefaultHandler used by the
 * Sax-parser to parse the language-description file.
 *  
 * @author Felix Burkhardt
 */
public class LanguageContentHandler extends DefaultHandler {
    Phoneme p = null;

    Vector ps = null;

    Vector replacements = null;

    Replacement rep = null;

    Logger debugLogger;

    Language l = null;

    boolean inDescription = false;

    Vector ls = null;

    String description = "";

    /**
     *  
     */
    public LanguageContentHandler() {
        debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String s = new String(ch, start, length);
        if (ch[0] == '\n') {
            return;
        }
        if (inDescription) {
            description += s;
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.compareTo("replacement") == 0) {
            replacements.add(rep);
            rep = null;
        } else if (qName.compareTo("phoneme") == 0) {
            ps.add(p);
            p = null;
        } else if (qName.compareTo("description") == 0) {
            inDescription = false;
        } else if (qName.compareTo("language") == 0) {
            l.setPhonemes(ps);
            replacements = null;
            ps = null;
            l.setDescription(description);
            description = "";
            ls.add(l);
            debugLogger.debug("loaded: " + l.toString());
            l = null;
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (qName.compareTo("languages") == 0) {
            ls = new Vector();
        } else if (qName.compareTo("language") == 0) {
            l = new Language();
            ps = new Vector();
            replacements = new Vector();
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("name")) {
                        l.setName(attributes.getValue(i));
                    }
                    if (attributes.getLocalName(i).equals("male")) {
                        l.setMale(Boolean.getBoolean(attributes.getValue(i)));
                    }
                    if (attributes.getLocalName(i).equals("locale")) {
                        l.setLocale(attributes.getValue(i));
                    }
                }
            }
        } else if (qName.compareTo("description") == 0) {
            inDescription = true;
        } else if (qName.compareTo("voiceQuality") == 0) {
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("multiple")) {
                        l.setSupportsMultipleVoiceQualities(Boolean.valueOf(
                                attributes.getValue(i)).booleanValue());
                    }
                }
            }
        } else if (qName.compareTo("phoneme") == 0) {
            p = new Phoneme();
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("name")) {
                        p.setName(attributes.getValue(i));
                    } else if (attributes.getLocalName(i).equals("manner")) {
                        p.setManner(attributes.getValue(i));
                    } else if (attributes.getLocalName(i).equals("voiced")) {
                        p.setVoiced(Boolean.valueOf(attributes.getValue(i))
                                .booleanValue());
                    }
                }
            }
            checkReplacement();
        } else if (qName.compareTo("replacement") == 0) {
            rep = new Replacement();
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("central")) {
                        rep.central = attributes.getValue(i);
                    } else if (attributes.getLocalName(i).equals("decentral")) {
                        rep.decentral = attributes.getValue(i);
                    }
                }
            }
        }
    }

    public Vector getLanguages() {
        return ls;
    }

    private void checkReplacement() {
        if (p == null || replacements == null)
            return;
        for (Iterator iter = replacements.iterator(); iter.hasNext();) {
            Replacement rep = (Replacement) iter.next();
            if (rep.central.compareTo(p.getName()) == 0) {
                p.setDecentralVariant(rep.decentral);
            }
            if (rep.decentral.compareTo(p.getName()) == 0) {
                p.setCentralVariant(rep.central);
            }
        }
    }

    private class Replacement {
        String central;

        String decentral;
    }
}

