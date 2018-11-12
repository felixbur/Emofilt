// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

/*
 * The Implemetation of Tokens for Strings
 *
 * @version 1.0, 11/02/1998
 * @author  Panos Kougiouris
 */ 

public class StringToken extends Token
{
	public StringToken(
		    String a_name,  
            String a_message, 
            String a_environment_variable,
            int aTokenOptions,
			String a_def_value
			) {
		super(a_name, a_message, a_environment_variable, aTokenOptions);
		setDefaultValue(a_def_value);
	}
	
    public String type() {
		return "<String>";
	}	
	
	public String getValue() {
		return getValue(0);
	}
	public void setValue(String s) {
	    m_values.add(0, s);
	}
	public boolean isDefault() {
	    if (getDefaultValue().compareTo(getValue())==0)
	        return true;
	    return false;
	}
	public String getValue(int i) {
		return (String)m_values.elementAt(i);
	}
	
	public Object toObject(String lexeme) {
		return lexeme;
	}
}
