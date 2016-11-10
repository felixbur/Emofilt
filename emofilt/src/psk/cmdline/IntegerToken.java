// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

public class IntegerToken extends Token
{
	public IntegerToken(
		    String a_name,  
            String a_message, 
            String a_environment_variable,
            int aTokenOptions,
			int a_def_value
			) {
		super(a_name, a_message, a_environment_variable, aTokenOptions);
		setDefaultValue(new Integer(a_def_value));
	}
	
    public String type() {
		return "<Integer>";
	}	
	
	public int getValue() {
		return getValue(0);
	}
	
	public int getValue(int i) {
		Integer in = (Integer)m_values.elementAt(i);
		return in.intValue();
	}
	
	public Object toObject(String lexeme) {
		return new Integer(lexeme);
	}
}