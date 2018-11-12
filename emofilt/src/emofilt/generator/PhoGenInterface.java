package emofilt.generator;

import org.apache.log4j.Logger;

import com.felix.util.KeyValues;

import emofilt.Language;

public interface PhoGenInterface {
	public void init(KeyValues config, Logger logger);
	public String genPhoFile(String text, String outFilePath, Language lang,  boolean male);
}
