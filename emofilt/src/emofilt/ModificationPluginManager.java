/*
 * Created on 21.07.2005
 *
 * @author Felix Burkhardt
 */
package emofilt;

import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * The Modification Plugin Manager is the central instance to handle the
 * management of the modification plug-ins.
 * 
 * @author Felix Burkhardt
 */
public class ModificationPluginManager {
	private Logger debugLogger = null;

	private Vector plugins;

	private Emofilt emofilt;

	/**
	 * The constructor. Loads the available plug-ins as given in the
	 * initialisation file.
	 * 
	 * @param emofilt
	 *            The emofilt calling instance.
	 */
	public ModificationPluginManager(Emofilt emofilt) {
		this.emofilt = emofilt;
		debugLogger = Logger.getLogger(Emofilt.LOGGER_NAME);
		loadPlugins();
	}

	/**
	 * Get the avaiable plug-ins.
	 * 
	 * @return A vector containing the available plug-ins.
	 */
	public Vector getPlugins() {
		return plugins;
	}

	/**
	 * Return a plug-in for a known name.
	 * 
	 * @param name
	 *            The name of the plug-in.
	 * @return The plug-in that is identified by that name.
	 */
	public ModificationPlugin getModificationByName(String name) {
		for (Iterator iter = plugins.iterator(); iter.hasNext();) {
			ModificationPlugin mpi = (ModificationPlugin) iter.next();
			if (mpi.getName().compareTo(name) == 0) {
				return mpi;
			}
		}
		debugLogger.warn("search for unknown name: " + name);
		return null;
	}

	/**
	 * Get all plug-ins for a certain type (e.g. "duration").
	 * 
	 * @param type
	 *            The type, e.g. "pitch"
	 * @return A vector containing all plug-ins that belong to the given type.
	 */
	public Vector getModificationsByType(String type) {
		Vector returnVec = new Vector();
		for (Iterator iter = plugins.iterator(); iter.hasNext();) {
			ModificationPlugin mpi = (ModificationPlugin) iter.next();
			if (mpi.getModificationType().compareTo(type) == 0) {
				returnVec.add(mpi);
			}
		}
		return returnVec;
	}

	/**
	 * For all plug-ins set the listener to be notified if any of the plug-ins changes.
	 * @param pcl
	 */
	public void setPropertyChangeListener(PropertyChangeListener pcl) {
		for (Iterator iter = plugins.iterator(); iter.hasNext();) {
			ModificationPlugin mpi = (ModificationPlugin) iter.next();
			mpi.setPropertyChangeListener(pcl);
		}
	}

	private void loadPlugins() {
		Vector pluginNames = new Vector();
		plugins = new Vector();
		String pluginS = Emofilt._config.getString("plugins");
		if (pluginS == null || pluginS.length() == 0) {
			debugLogger.warn("no plugins given");
			return;
		}
		//debugLogger.debug(pluginS);
		StringTokenizer st = new StringTokenizer(pluginS, ", ");
		while (st.hasMoreElements()) {
			pluginNames.add(st.nextToken());
		}
		ClassLoader cl = getClass().getClassLoader();
		try {
			for (Iterator iter = pluginNames.iterator(); iter.hasNext();) {
				String pluginName = (String) iter.next();
				ModificationPlugin mpi = (ModificationPlugin) (cl
						.loadClass("emofilt.plugins." + pluginName)
						.newInstance());
				mpi.init(debugLogger, emofilt.useGui());
				plugins.add(mpi);
				//debugLogger.debug("loaded plugin: " + mpi.toString());
			}
			debugLogger.debug("loaded " + plugins.size() + " plugins.");
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger.error(e.getMessage());
		}
	}

}