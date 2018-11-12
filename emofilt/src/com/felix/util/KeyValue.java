package com.felix.util;

/**
 * 
 * A class to hold two Strings.
 */
public class KeyValue {
	private String key = null;
	private String value = null;

	public KeyValue() {
	}

	/**
	 * Constructor with key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public KeyValue(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * Return the key.
	 * 
	 * @return The key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key.
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Return the value.
	 * 
	 * @return The value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns blank separated key and value or respective "null".
	 */
	public String toString() {
		if (key == null)
			key = "null";
		if (value == null)
			value = "null";
		return key + " " + value;
	}
}
