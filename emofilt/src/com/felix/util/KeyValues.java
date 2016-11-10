package com.felix.util;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class for key values, might be read from string, e.g.
 * "key1:value1, key2:value2, etc"
 * 
 * 
 * 
 * @author felix
 * 
 */
public class KeyValues {
	private KeyValue[] _keyValues;
	private HashMap<String, String> _hm;
	/**
	 * a vector to store file lines of config file in order to keep also the
	 * comment lines.
	 **/
	private Vector<String> _fileLines;
	private String _pathBase = "", _filePath = "", _pairSeparator = "",
			_keyValueSeparator = "";

	/**
	 * Constructor from a String, e.g. "key1:value1, key2:value2, etc".
	 * 
	 * @param s
	 * @param pairSeparator
	 *            Separates the pairs.
	 * @param keyValueSeparator
	 *            Separates key from value.
	 */
	public KeyValues(String s, String pairSeparator, String keyValueSeparator) {
		try {
			_pairSeparator = pairSeparator;
			_keyValueSeparator = keyValueSeparator;
			StringTokenizer st = new StringTokenizer(s, pairSeparator);
			Vector<String> tmp = new Vector<String>();
			while (st.hasMoreTokens()) {
				String object = st.nextToken();
				if (!FileUtil.isCommentOrEmpty(object)) {
					tmp.add(object);
				}
			}
			readKeyValues(tmp, keyValueSeparator, false);
			tmp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the path base.
	 * 
	 * @return The path base.
	 */
	public String getPathBase() {
		return _pathBase;
	}

	/**
	 * Set the path base.
	 * 
	 * @param pathBase
	 */
	public void setPathBase(String pathBase) {
		_pathBase = pathBase;
	}

	/**
	 * Constructor from a vector, containing key-value description strings.
	 * 
	 * @param keyValueStrings
	 * @param keyValueSeparator
	 *            Separates key from value.
	 */
	public KeyValues(Vector<String> keyValueStrings, String keyValueSeparator) {
		readKeyValues(keyValueStrings, keyValueSeparator, false);
	}

	/**
	 * Constructor from an input stream, each line containing key-value
	 * description strings.
	 * 
	 * @param inputStream
	 * @param keyValueSeparator
	 *            Separates key from value.
	 */
	public KeyValues(InputStream inputStream, String keyValueSeparator) {
		_keyValueSeparator = keyValueSeparator;
		try {
			Vector<String> filelines = FileUtil.getFileLines(inputStream);
			readKeyValues(filelines, keyValueSeparator, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor from an input stream, each line containing key-value
	 * description strings.
	 * 
	 * @param inputStream
	 *            The Input Stream
	 * @param keyValueSeparator
	 *            The key-value separator
	 * @param charEnc
	 *            The character encoding
	 */
	public KeyValues(InputStream inputStream, String keyValueSeparator,
			String charEnc) {
		_keyValueSeparator = keyValueSeparator;
		try {
			Vector<String> filelines = FileUtil.getFileLines(inputStream,
					charEnc);
			readKeyValues(filelines, keyValueSeparator, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor from a file name, each line containing key-value description
	 * strings.
	 * 
	 * @param filename
	 * @param keyValueSeparator
	 *            Separates key from value.
	 */
	public KeyValues(String filename, String keyValueSeparator) {
		_keyValueSeparator = keyValueSeparator;
		try {
			_filePath = filename;
			_fileLines = FileUtil.getFileLines(filename);
			readKeyValues(_fileLines, keyValueSeparator, false);
		} catch (Exception e) {
			System.err.println("ERROR on filename: " + filename);
			e.printStackTrace();
		}
	}

	/**
	 * Constructor from a file name, each line containing key-value description
	 * 
	 * @param file
	 *            The file.
	 * @param keyValueSeparator
	 *            The Keyvalue Separator.
	 * @param charEnc
	 *            The Character Encoding, e.g. "UTF-8"
	 */
	public KeyValues(File file, String keyValueSeparator, String charEnc) {
		_keyValueSeparator = keyValueSeparator;
		try {
			_fileLines = FileUtil.getFileLines(file, charEnc);
			readKeyValues(_fileLines, keyValueSeparator, false);
		} catch (Exception e) {
			System.err.println("ERROR on filename: " + file.getName());
			e.printStackTrace();
		}
	}

	/**
	 * Add key-values from a file to existing ones.
	 * 
	 * @param filename
	 * @param keyValueSeparator
	 *            Separates key from value.
	 * @return The new key values.
	 */
	public KeyValues addKeyValues(String filename, String keyValueSeparator) {
		_keyValueSeparator = keyValueSeparator;

		try {
			Vector<String> filelines = FileUtil.getFileLines(filename);
			readKeyValues(filelines, keyValueSeparator, true);
		} catch (Exception e) {
			System.err.println("ERROR on filename: " + filename);
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Add key-values from a String to existing ones.
	 * 
	 * @param s
	 * @param pairSeparator
	 *            Separates the pairs.
	 * @param keyValueSeparator
	 *            Separates key from value.
	 * @return The new Key values.
	 */
	public KeyValues addKeyValues(String s, String pairSeparator,
			String keyValueSeparator) {
		try {
			StringTokenizer st = new StringTokenizer(s, pairSeparator);
			Vector<String> tmp = new Vector<String>();
			while (st.hasMoreTokens()) {
				String object = st.nextToken();
				if (!FileUtil.isCommentOrEmpty(object)) {
					tmp.add(object);
				}
			}
			readKeyValues(tmp, keyValueSeparator, true);
			tmp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Return array of KeyValues.
	 * 
	 * @return The array of KeyValues.
	 */
	public KeyValue[] getKeyValues() {
		return _keyValues;
	}

	/**
	 * Return values as blank-separated string or "null".
	 * 
	 * @return The values or "null".
	 */
	public String getValuesAsString() {
		if (_keyValues == null)
			return "null";
		String ret = "";
		for (int i = 0; i < _keyValues.length; i++) {
			ret += _keyValues[i].getValue() + " ";
		}
		return ret.trim();

	}

	/**
	 * Return unique list (ignoring doublettes) of values as blank separated
	 * string.
	 * 
	 * @return The list.
	 */
	public String getUniqValuesAsString() {
		Vector<String> contained = new Vector<String>();
		if (_keyValues == null)
			return "null";
		String ret = "";
		for (int i = 0; i < _keyValues.length; i++) {
			String val = _keyValues[i].getValue();
			if (!StringUtil.isStringInVector(val, contained)) {
				ret += val + " ";
			}
			contained.add(val);
		}
		return ret.trim();
	}

	/**
	 * Return unique list (ignoring doublettes) of values as String array.
	 * 
	 * @return The unique list.
	 */
	public String[] getUniqValuesAsArray() {
		String uv = getUniqValuesAsString();
		StringTokenizer st = new StringTokenizer(uv);
		String[] ret = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			ret[i++] = st.nextToken();
		}
		return ret;
	}

	/**
	 * Return String array of blank separated tokens. string.
	 * 
	 * @return The String array.
	 */
	public String[] getValueAsStringArray(String key) {
		String val = this.getHashMap().get(key);
		StringTokenizer st = new StringTokenizer(val);
		String[] ret = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			ret[i++] = st.nextToken();
		}
		return ret;
	}

	/**
	 * Return keys as blank separated String.
	 * 
	 * @return The keys as blank separated String.
	 */
	public String getKeysAsString() {
		if (_keyValues == null)
			return "null";
		String ret = "";
		for (int i = 0; i < _keyValues.length; i++) {
			ret += _keyValues[i].getKey() + " ";
		}
		return ret.trim();

	}

	/**
	 * Return unique list (ignoring doublettes) of keys as blank separated
	 * string.
	 * 
	 * @return A unique list.
	 */
	public String getUniqKeysAsString() {
		Vector<String> contained = new Vector<String>();
		if (_keyValues == null)
			return "null";
		String ret = "";
		for (int i = 0; i < _keyValues.length; i++) {
			String val = _keyValues[i].getKey();
			if (!StringUtil.isStringInVector(val, contained)) {
				ret += val + " ";
			}
			contained.add(val);
		}
		return ret.trim();

	}

	/**
	 * Return KeyValue descriptions as newline separated list.
	 */
	public String toString() {
		if (_keyValues == null)
			return "null";
		String ret = "";
		for (int i = 0; i < _keyValues.length; i++) {
			ret += _keyValues[i].toString() + "\n";
		}
		return ret;
	}

	/**
	 * Store in file.
	 */
	public void fileStore() {
		try {
			Vector<String> storeVec = new Vector<String>();
			for (int i = 0; i < _keyValues.length; i++) {
				storeVec.add(_keyValues[i].getKey() + _keyValueSeparator
						+ _keyValues[i].getValue());
			}
			FileUtil.writeFileContent(_filePath, storeVec,
					FileUtil.STD_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Store in file including original comments..
	 */
	public void fileStoreWithComments() {
		try {
			Vector<String> storeVec = new Vector<String>();
			for (int j = 0; j < _fileLines.size(); j++) {
				String line = _fileLines.elementAt(j);
				StringTokenizer st = new StringTokenizer(line,
						_keyValueSeparator);
				boolean found = false;
				try {
					String keyC = st.nextToken();
					for (int i = 0; i < _keyValues.length; i++) {
						String key = _keyValues[i].getKey();
						String value = _keyValues[i].getValue();
						if (keyC.compareTo(key) == 0) {
							storeVec.add(key + _keyValueSeparator + value);
							found = true;
							break;
						}
					}
				} catch (Exception e) {
					// was no key value
				}
				if (!found) {
					storeVec.add(line);
				}
			}
			FileUtil.writeFileContent(_filePath, storeVec,
					FileUtil.STD_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Store in file including original comments..
	 */
	public void fileStoreWithComments(String fileName) {
		try {
			Vector<String> storeVec = new Vector<String>();
			for (int j = 0; j < _fileLines.size(); j++) {
				String line = _fileLines.elementAt(j);
				StringTokenizer st = new StringTokenizer(line,
						_keyValueSeparator);
				String keyC = st.nextToken();
				boolean found = false;
				for (int i = 0; i < _keyValues.length; i++) {
					String key = _keyValues[i].getKey();
					String value = _keyValues[i].getValue();
					if (keyC.compareTo(key) == 0) {
						storeVec.add(key + _keyValueSeparator + value);
						found = true;
						break;
					}
				}
				if (!found) {
					storeVec.add(line);
				}
			}
			FileUtil
					.writeFileContent(fileName, storeVec, FileUtil.STD_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Store in file.
	 */
	public void fileStore(String fileName) {
		try {
			Vector<String> storeVec = new Vector<String>();
			for (int i = 0; i < _keyValues.length; i++) {
				storeVec.add(_keyValues[i].getKey() + _keyValueSeparator
						+ _keyValues[i].getValue());
			}
			FileUtil
					.writeFileContent(fileName, storeVec, FileUtil.STD_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve the value for a specific key.
	 * 
	 * @param key
	 * @return The value for a specific key.
	 */
	public String getString(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return val;
	}

	/**
	 * Set a new value for a key.
	 * 
	 * @param key
	 * @param newValue
	 */
	public void setValue(String key, String newValue) {
		boolean found = false;
		for (int i = 0; i < _keyValues.length; i++) {
			if (_keyValues[i].getKey().compareTo(key) == 0) {
				_keyValues[i].setValue(newValue);
				found = true;
				break;
			}
		}
		if (!found) {
			System.err.println("WARNING: no key for " + key);
		}
	}

	/**
	 * Test a String value.
	 * 
	 * @param key
	 *            The key.
	 * @param tval
	 *            The supposed value.
	 * @return True if value for key is test value.
	 */
	public boolean isString(String key, String tval) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		if (val.trim().equalsIgnoreCase(tval.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * Retrieve the value for a specific key for Booleans.
	 * 
	 * @param key
	 *            The key.
	 * @return True if values parses as boolean true.
	 */
	public boolean getBool(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return Boolean.parseBoolean(val);
	}

	/**
	 * Retrieve the value for a specific key for integers.
	 * 
	 * @param key
	 *            The key.
	 * @return The value as integer.
	 */
	public int getInt(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return Integer.parseInt(val);
	}

	/**
	 * Retrieve the value for a specific key for a string array. E.g. point 13
	 * 2341 constructs new Dimension(13, 2341)
	 * 
	 * @param key
	 *            The key.
	 * @param sep
	 *            The separator string, e.g. " " or ",".
	 * @return The value a String array.
	 */
	public String[] getStringArray(String key, String sep) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return StringUtil.stringToArray(val.trim(), sep);
	}

	/**
	 * Retrieve the value for a specific key for doubles.
	 * 
	 * @param key
	 *            The key.
	 * @return The value as double.
	 */
	public double getDouble(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return Double.parseDouble(val);
	}

	/**
	 * Retrieve the value for a specific key as a file path.
	 * 
	 * @param key
	 *            The key.
	 * @return The value fronted by the pathBase.
	 */
	public String getPathValue(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		if (Util.isEmpty(val)) {
			return "";
		}
		if (Util.isEmpty(_pathBase))
			return val;
		return _pathBase + File.separator + val;
	}

	/**
	 * Retrieve the value for a specific key as an absolute file path.
	 * 
	 * @param key
	 *            The key.
	 * @return The value taken as a file.
	 */
	public String getAbsPath(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return new File(val).getAbsolutePath();
	}

	/**
	 * Retrieve the value for a specific key as a file handler based on absolute
	 * path.
	 * 
	 * @param key
	 *            The key.
	 * @return The value taken as a file.
	 */
	public File getFileHandler(String key) {
		String val = this.getHashMap().get(key);
		if (val == null) {
			System.err.println("WARNING: no value for " + key);
		}
		return new File(getPathValue(key));
	}

	/**
	 * Get number of KeyValues.
	 * 
	 * @return The number of key values.
	 */
	public int getSize() {
		return _keyValues.length;
	}

	private void readKeyValues(Vector<String> keyValueStrings,
			String keyValueSeparator, boolean add) {
		try {
			// temporary vector to filter comments and empty lines
			Vector<String> tmp = new Vector<String>();
			for (Iterator<String> iterator = keyValueStrings.iterator(); iterator
					.hasNext();) {
				String object = (String) iterator.next();
				if (!FileUtil.isCommentOrEmpty(object)) {
					tmp.add(object);
				}
			}
			int i = 0;
			if (!add) {
				_keyValues = new KeyValue[tmp.size()];
			} else {
				i = _keyValues.length;
				KeyValue[] tmp2 = new KeyValue[_keyValues.length + tmp.size()];
				System.arraycopy(_keyValues, 0, tmp2, 0, _keyValues.length);
				_keyValues = new KeyValue[_keyValues.length + tmp.size()];
				System.arraycopy(tmp2, 0, _keyValues, 0, tmp2.length);
			}
			for (Iterator<String> iterator = tmp.iterator(); iterator.hasNext();) {
				String object = (String) iterator.next();
				String key = "";
				int sepIndex = object.indexOf(keyValueSeparator);
				key = object.substring(0, sepIndex);
				String value = object.substring(sepIndex + 1, object.length());
				// if (value.length() == 0) {
				// System.err.println("WARNING: empty value at " + key);
				// }
				_keyValues[i++] = new KeyValue(key, value);
			}
			tmp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return keys and values as hash map (internally an array).
	 * 
	 * @return The keys and values as hash map.
	 */
	public HashMap<String, String> getHashMap() {
		if (_hm == null) {
			_hm = new HashMap<String, String>();
			for (int i = 0; i < _keyValues.length; i++) {
				_hm.put(_keyValues[i].getKey(), _keyValues[i].getValue());
			}
		}
		return _hm;
	}

	/**
	 * Main method used for testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KeyValues kv = new KeyValues("t1,t2;t3,\nt4,,;t5, \"t6 \";t6,", ";",
				",");
		System.out.println(kv.toString());
		System.out.println("value of t1: >" + kv.getString("t1") + "<");
		System.out.println("value of t2: >" + kv.getString("t2") + "<");
		System.out.println("value of t3: >" + kv.getString("t3") + "<");
		System.out.println("value of t4: >" + kv.getString("t4") + "<");
		System.out.println("value of t5: >" + kv.getString("t5") + "<");
		System.out.println("value of t6: >" + kv.getString("t6") + "<");

		kv = kv.addKeyValues("t8.t9:t10.t11", ":", ".");
		System.out.println("\nkv extended:\n" + kv.toString());

	}
}
