// Copyright (c) 1998 Panos Kougiouris All Rights Reserved
package psk.cmdline;

/*
 * The class that contains all the tokens and 
 * initiates the parsing
 *
 * @version 	1.0, 11/04/1998
 * @author Panos Kougiouris
 */


public class ApplicationSettings
{	
	public ApplicationSettings() {
		this(ApplicationSettingsOptions.optDoNotIgnoreUnknown);
	}
	
	public ApplicationSettings(int aApplicationSettingsOptions) {
		m_argDescriptions = new java.util.Vector();
		m_flags = aApplicationSettingsOptions;
	}
	
	public void addToken(Token argum) {
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token argDesc = ((Token)m_argDescriptions.elementAt(i));
			
			//Make sure we have no argument clash
			if (argDesc.name().compareTo(argum.name()) == 0) {
				System.err.print("ApplicationSettings ERROR: option \"");
				System.err.print(argum.name());
				System.err.println("\"is used more than once");
				System.exit(-1);
			}
			
			// make sure there is only one that is non switch
			if (!argDesc.isSwitch() && !argum.isSwitch()) {
				System.err.print("ApplicationSettings ERROR: arguments defined in both '");
				System.err.print(argum.name());
				System.err.print("' and '");
				System.err.println(argDesc.name() + "'");
				System.err.println("Arguments should be defined only once.");
				System.exit(-2);
			}
		}
		m_argDescriptions.addElement(argum);
	}
	
	public void parseArgs(String[] args) throws Exception {
		m_cmdLineArgs = new StringArrayIterator(args);
		
		setEnvironmentValues();
		parseInternal();
	}
	
	public void printUsage(String reason) throws Exception {
		// Print the first line
		System.err.print("Error: ");
		System.err.println(reason + "\n");
		// Print the usage line
		System.err.print("Usage: ");
		System.err.print(m_programName + " ");
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token arg = (Token)m_argDescriptions.elementAt(i);
			arg.printUsage(System.err);
		}
		System.err.println("\n");
		
		// Print the explanations
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token arg = (Token)m_argDescriptions.elementAt(i);
			arg.printUsageExtended(System.err);
		}
		throw new Exception(reason);
	}
	public void printUsage() throws Exception {
		// Print the usage line
		System.out.print("Usage: ");
		System.out.print(m_programName + " ");
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token arg = (Token)m_argDescriptions.elementAt(i);
			arg.printUsage(System.out);
		}
		System.out.println("\n");
		
		// Print the explanations
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token arg = (Token)m_argDescriptions.elementAt(i);
			arg.printUsageExtended(System.out);
		}
	}
	
	//---------------------------------------------------------------------
	
	// This functions should be called only once
	protected void parseNonSwitch() throws Exception {
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token argDesc = ((Token)m_argDescriptions.elementAt(i));
			
			if (!argDesc.parseArgument(m_cmdLineArgs)) continue;
			
			// here should be the end...
			if (!m_cmdLineArgs.EOF()) {
				this.printUsage("too many commeand line arguments.");						
			}
			return;
		}
		
		String str = "Unexepected argument ";
		str += m_cmdLineArgs.get();
		if (!ignoreUnknownSwitches())
			this.printUsage(str);
	}
	
	protected void parseInternal() throws Exception {
		// skip the name of the program
		m_programName = "program";
		
		while (!m_cmdLineArgs.EOF()) {
            try {
			    if (Token.isASwitch(m_cmdLineArgs.get())) {
				    this.parseSwitch();
			    } else {
				    this.parseNonSwitch();
				    break;
			    }
            } catch (Exception ex) {
                String str = ex.getMessage();
                if (ex.getClass() == NumberFormatException.class) {
                    str = str + " ";
                    str = str + " wrong argument type";
                }
				// most likely 'argument expected'
				this.printUsage(str);
			}
			m_cmdLineArgs.moveNext();
		}
		
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token argDesc = ((Token)m_argDescriptions.elementAt(i));
			if (!argDesc.isUsed() && argDesc.isRequired()) {
				String str;
				str = "missing required argument. Name: ";
				str += argDesc.extendedName();
				this.printUsage(str);					 
			}
		}
	}
	
	protected void parseSwitch() throws Exception {
		int i = 0;
		for (i = 0; i < m_argDescriptions.size(); i++) {
			Token argDesc = ((Token)m_argDescriptions.elementAt(i));
			if (argDesc.ParseSwitch(m_cmdLineArgs)) return;
		}
		
		// We tried all the tokens and no one recognized
		if (i >= m_argDescriptions.size()) {
			String str= new String("Unknown option ");
			str += m_cmdLineArgs.get();
			if (!ignoreUnknownSwitches())
				this.printUsage(str);
		}
	}
	
	protected void setEnvironmentValues() {
		for (int i = 0; i < m_argDescriptions.size(); i++) {
			Token argDesc = ((Token)m_argDescriptions.elementAt(i));
			if (argDesc.hasEnvironmentVariable()) {
				String str = Util.GetEnvVariable(argDesc.getEnvironmentVariable());
				if (str.length() != 0) {
					argDesc.SetValueFromLexeme(str, 0);
				}
			}
		}
	}
	
	protected boolean ignoreUnknownSwitches() {
		return (m_flags & ApplicationSettingsOptions.optIgnoreUnknown) != 0;
	}
	
	// Data members
	protected StringArrayIterator m_cmdLineArgs;
	protected String              m_programName;
	protected java.util.Vector    m_argDescriptions; // Vector of Argv objects 
	protected int                 m_flags;
}
