// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

public class BooleanToken extends Token
{
	public BooleanToken(
		    String a_name,  
            String a_message, 
            String a_environment_variable,
            int aTokenOptions,
			boolean a_def_value
			) {
		super(a_name, a_message, a_environment_variable, aTokenOptions);
		setDefaultValue(new Boolean(a_def_value));
	}
	
    public String type() {
		return "";
	}	
	
	public boolean getValue() {
		return getValue(0);
	}
	
	public boolean getValue(int i) {
		Boolean in = (Boolean)m_values.elementAt(i);
		return in.booleanValue();
	}
	
	public Object toObject(String lexeme) {
		if (lexeme.length() == 0) {
			Boolean bl = (Boolean)m_defaultValue;
			boolean val = bl.booleanValue();
			val = !val;
			lexeme = (val ? "true" : "false");
		}
		return new Boolean(lexeme);
	}
	
	public boolean hasOneOrMoreArgs() {return false;};
}