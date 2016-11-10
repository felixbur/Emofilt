/*
 * Created on 26.08.2004
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.util.*;

import org.apache.log4j.*;
import org.jdom.*;

/**
 * Class to modify an utterance. Main method takes an utterance and an emotion and modifies the
 * utterance according to the emotion's modification-rules.
 * 
 * 
 * @author Felix Burkhardt
 */
public class UtteranceModifier {
	private Document emotionsTree;

	private Logger debugLogger = null;

	private Utterance utt = null;

	private ModificationPluginManager mpim;

	/**
	 * The constructor for the object.
	 * 
	 * @param mpim
	 *            The modification plugin manager manages the different
	 *            modification methods.
	 */
	public UtteranceModifier(ModificationPluginManager mpim) {
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
		this.mpim = mpim;
	}

	/**
	 * 
	 * Take the values of an original utterance and modify them according to the
	 * description given by one emotion file.
	 * 
	 * @param u
	 *            The original utterance.
	 * @param emotion
	 *            The target emotion.
	 * @param rate
	 *            A rate value between 0 and 1 that controls the rate of change
	 *            for all modifications described in the emotion description.
	 * @param lang
	 *            The language id that describes the utterance phoneme set.
	 * @return An utterance that should display the target emotion expression.
	 * @throws Exception
	 *             The exceptions are passed on from deeper methods.
	 */
	public Utterance modify(Utterance u, Element emotion, double rate,
			Language lang) throws Exception {
		utt = u;
		debugLogger.debug("changing utterance with intensity rate: "+rate);
		for (Iterator iter = mpim.getPlugins().iterator(); iter.hasNext();) {
			ModificationPlugin mpi = (ModificationPlugin) iter.next();
			utt = mpi.modify(utt, emotion, rate, lang);
		}
		return utt;
	}
}