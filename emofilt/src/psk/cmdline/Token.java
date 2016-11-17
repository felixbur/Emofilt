// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

import java.util.*;

/*
 * Each Token object encapsulates one command
 * line argumet or switch. 
 *
 * @version 	1.0, 11/02/1998
 * @author Panos Kougiouris
 */

public abstract class Token
{
	public String name() {return m_name;};
		
	public int NumberOfValues() {
		return m_values.size();
	}
    
    public String extendedName() {
        if (isSwitch()) {
            return "-" + name();
        } else {
            return name();
        }
    }
	
	//------------------------------------------------------------
	// Subclasses should implement these
	//------------------------------------------------------------
	
	public abstract Object toObject(String lexeme);
	
	// All but bool (where merely the appearence of the flag
	// signifies the existence) return true;
	public boolean hasOneOrMoreArgs() {return true;};
	
	public abstract String type();
	
	// Also implement these two methods
	// <Type> getValue(int i);
	// <Type> getValue();
	
	protected Token(
				 String a_name,  
				 String a_message, 
				 String a_environment_variable,
				 int aTokenOptions  // of type TokenOptions
				 ) {
		m_name = a_name;
		m_message = a_message;
		m_env_variable = a_environment_variable;
		m_flags = aTokenOptions;
		m_firstTime = true;
		m_values = new Vector(1);
	};
	
	//--------------------------------------------------------------
	// These methods are used by the ApplicationSettings class
	// Thast' why they are protected
	//--------------------------------------------------------------
	
	// If we match the switch
	// we parse as many command line arguments as they apply to this
	// switch and then return just before the next one we do not
	// recognize
	protected boolean ParseSwitch(StringArrayIterator cmdLineArgs) throws Exception {
		if (this.isArgument()) return false;
		if ((this.isUsed()) && (!this.allowsMultipleValues())) return false;
		if (cmdLineArgs.get().substring(1).indexOf(name()) != 0) return false;

		// after the match what remains e.g. if we are -t and 
		// argument is -tom then rest == 'om'
		String lexeme = cmdLineArgs.get().substring(1 + name().length());
		if (lexeme.length() == 0) {
			// the "-t foo" or "-t" case
			if (this.hasOneOrMoreArgs()) {
				// move to the "foo"
				cmdLineArgs.moveNext();
				if (!cmdLineArgs.EOF()) 
					lexeme = cmdLineArgs.get();
				else {
					String str = new String("Argument expected for option ");
					str += this.extendedName();
					throw new Exception(str);
				}
			} 
		} else {
			// "-tfoo" case
			if (!this.hasOneOrMoreArgs()) {
				String str = new String("No Argument expected for option ");
				str += this.name();
				throw new Exception(str);	
			}
		}	
		
		this.AddValueFromLexeme(lexeme);
		this.setUsed();

        /*
         * If you comment out these lines then 
         * "-l 1 2 3" will be permitted. Now this should be
         * "-l 1 -l 2 -l 3"
         *
        if (allowsMultipleValues()) {
		    cmdLineArgs.moveNext();
		    // if it supports multiple parse more arguments
		    while ((!cmdLineArgs.EOF()) && 
			       (!isASwitch(cmdLineArgs.get()))) {
			    this.AddValueFromLexeme(cmdLineArgs.get());	
			    cmdLineArgs.moveNext();
		    }
		    cmdLineArgs.movePrevious();
        }
        */
		
		return true;	
	}
	
	protected boolean parseArgument(StringArrayIterator cmdLineArgs) {
		if (isSwitch()) return false;
		if ((isUsed()) && 
			(!allowsMultipleValues())) return false;  
		
		// if it supports multiple parse more arguments
		while ((!cmdLineArgs.EOF()) && 
			   (!isASwitch(cmdLineArgs.get()))) {
			this.AddValueFromLexeme(cmdLineArgs.get());
			this.setUsed();
			cmdLineArgs.moveNext();
			if (!allowsMultipleValues()) break;
		}
		
		return true;
	}
	
	protected void printUsage(java.io.PrintStream str) {
		if (!this.isRequired()) str.print( "[");
		str.print(this.extendedName() + " ");
		str.print(this.type());
		if (this.allowsMultipleValues()) str.print(" ...");
		if (!this.isRequired()) str.print( "]");
		str.print(" ");
	}
	
	protected void printUsageExtended(java.io.PrintStream str) {
		str.print("\t");
		str.print(this.extendedName() + " ");
		str.print("'" + this.m_message + "' ");
		if (hasEnvironmentVariable()) {
			str.print(" Environment: $" +	this.m_env_variable);
		}
		if (!this.isRequired()) {
			str.print(" Default: ");
			str.print(this.getDefaultValue());
		}
		str.println();
	}
	
	protected boolean hasEnvironmentVariable()
	{
		return this.m_env_variable.compareTo("") != 0;
	}
	
	protected String getEnvironmentVariable() {
		return this.m_env_variable;
	}
	
	protected boolean isRequired() {
		return (m_flags & TokenOptions.optRequired) ==  TokenOptions.optRequired; 
	};
	
	protected boolean isSwitch() {
		return !isArgument();
	};
	
	protected boolean isArgument() {
		return (m_flags & TokenOptions.optArgument) == TokenOptions.optArgument;
	};
	
	protected boolean allowsMultipleValues() {
		return (m_flags & TokenOptions.optMultiple) == TokenOptions.optMultiple;
	};
	
	protected boolean isUsed() {
		return (m_flags & TokenOptions.optAlreadyUsed) == TokenOptions.optAlreadyUsed;
	};
	
	protected void setUsed() {m_flags |= TokenOptions.optAlreadyUsed;};
	
	protected void AddValueFromLexeme(String lexeme) {
		if (m_firstTime) {
			SetValueFromLexeme(lexeme, 0);
		} else {
            Util.utilAssert(this.allowsMultipleValues(), "");
			m_values.addElement(toObject(lexeme));
		}
		m_firstTime = false;
	}
	
	protected void SetValueFromLexeme(String lexeme, int i) {
		m_values.setSize(java.lang.Math.max(m_values.size(), i + 1));
		m_values.setElementAt(toObject(lexeme), i);
	}
	
	protected String getDefaultValue() {
		return m_defaultValue.toString();
	}
	
	protected void setDefaultValue(Object obj) {
		m_defaultValue = obj;
		m_values.setSize(java.lang.Math.max(m_values.size(),1));
		m_values.setElementAt(obj, 0);
	}
	
	protected static boolean isASwitch(String arg) {
		return (arg.charAt(0) == '-');
	}

	protected String                   m_name;
	protected String                   m_message;
	protected int                      m_flags;
	protected String                   m_env_variable;
	protected Vector                   m_values;
	protected Object                   m_defaultValue;
	protected boolean				   m_firstTime;
}
