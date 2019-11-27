package emofilt.generator;

import java.io.File;

import org.apache.log4j.Logger;

import com.felix.util.FileUtil;
import com.felix.util.KeyValues;
import com.felix.util.Util;

import emofilt.Emofilt;
import emofilt.Language;

public class Txt2PhoPhogenerator implements PhoGenInterface {
	private KeyValues _config;
	private Logger _logger;

	@Override
	public void init(KeyValues config, Logger logger) {
		_config = config;
		_logger = logger;
	}

	@Override
	public String genPhoFile(String text, String outFilePath, String paramsFilePath, Language lang,
			boolean male) {
		int waitTime = _config.getInt("txt2phoWaitTime");
		int maxTime = _config.getInt("txt2phoMaxTime");
		String output="";
		try {
			// tmporary txtfile for pho-file generation
			String tmpTxtFile = Emofilt._config
					.getPathValue("tmpTxtFile");
			File txtF = new File(tmpTxtFile);
			FileUtil.writeFileContent(txtF, text);

			// command to generate a phofile
			String phoGenCmd = Emofilt._config.getString("phoGenCmd");
			// optional database
			String phoGenDB = Emofilt._config.getPathValue("phoGenDB");
			if (phoGenDB.compareTo("null") == 0) {
				phoGenDB = "";
			}
			// optional gender option for male
			String phoGenCmdGenderMaleOption = Emofilt._config
					.getString("phoGenCmdGenderMaleOption");
			if (phoGenCmdGenderMaleOption.compareTo("null") == 0) {
				phoGenCmdGenderMaleOption = "";
			}
			// optional gender option for female
			String phoGenCmdGenderFemaleOption = Emofilt._config
					.getString("phoGenCmdGenderFemaleOption");
			if (phoGenCmdGenderFemaleOption.compareTo("null") == 0) {
				phoGenCmdGenderFemaleOption = "";
			}
			// optional prefix for database
			String phoGenDBPrefix = Emofilt._config
					.getString("phoGenDBPrefix");
			if (phoGenDBPrefix.compareTo("null") == 0) {
				phoGenDBPrefix = "";
			}
			// phogenerator infile prefix
			String phoGenInPrefix = Emofilt._config
					.getString("phoGenInPrefix");
			if (phoGenInPrefix.compareTo("null") == 0) {
				phoGenInPrefix = "";
			}
			// phogenerator outfile prefix
			String phoGenOutPrefix = Emofilt._config
					.getString("phoGenOutPrefix");
			if (phoGenOutPrefix.compareTo("null") == 0) {
				phoGenOutPrefix = "";
			}
			// <phoGenCmd> <phoGenDBPrefix> <phoGenDB> <genderOption>
			// <phoGenInPrefix> <tmpTxtFile> <phoGenOutPrefix>
			// <tmpPhoFile>
			String genderOption = male ? phoGenCmdGenderMaleOption
					: phoGenCmdGenderFemaleOption;
			String execCommand = phoGenCmd + " " + phoGenDBPrefix
					+ phoGenDB + " " + genderOption + " "
					+ phoGenInPrefix + tmpTxtFile + " "
					+ phoGenOutPrefix + outFilePath;
			_logger.debug("executing: " + execCommand);
			output = Util.execCmd(execCommand);
			FileUtil.waitForFile(outFilePath, waitTime, maxTime, false);
		} catch (Exception e) {
			e.printStackTrace();
			output = e.getMessage();
		}
		return output;
	}

}
