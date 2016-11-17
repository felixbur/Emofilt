// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

/*
 * This is a utility class. It encapsulates an array
 * and an index that points to the current object
 *
 * @version 	1.0,11/01/1998
 * @author Panos Kougiouris
 */

public class StringArrayIterator
{	
	public StringArrayIterator(String[] aStrings) {
		m_index = 0;
		m_strings = aStrings;
	}
	
	public boolean EOF() {
		return m_index >= m_strings.length;
	}
	
	public void moveNext() {
		m_index++;
	}
	
	public String get() {
		return m_strings[m_index];
	}
	
	private String[] m_strings;
	private int      m_index;
}
