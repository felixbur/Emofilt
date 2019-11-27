package emofilt.generator;

import java.io.File;

import org.apache.log4j.Logger;

import com.felix.util.FileUtil;
import com.felix.util.KeyValues;
import com.felix.util.Util;

import emofilt.Emofilt;
import emofilt.Language;

public class LiaPhonPhoGenerator implements PhoGenInterface {
	private KeyValues _config;
	private Logger _logger;
	private String[] _phonems = {"uy", "ee", "ii", "ei", "ai", "aa", "oo", "au", "ou",
			"uu", "EU", "oe", "eu", "in", "an", "on", "un", "yy", "ww", "pp",
			"tt", "kk", "bb", "dd", "gg", "ff", "ss", "ch", "vv", "zz", "jj",
			"ll", "rr", "mm", "nn", "##" };
	private String[] _replacements = {"H", "@", "i", "e", "E", "a", "O", "o", "u", "y",
			"2", "9", "@", "e~", "a~", "o~", "9~", "j", "w", "p", "t", "k",
			"b", "d", "g", "f", "s", "S", "v", "z", "Z", "l", "R", "m", "n",
			"_" };

	@Override
	public void init(KeyValues config, Logger logger) {
		_config = config;
		_logger = logger;
	}

	@Override
	public String genPhoFile(String text, String outFilePath, String tmpParamsFile, Language lang,
			boolean male) {
		String phoGenCmd = _config.getString("workingDir")+System.getProperty("file.separator")+_config.getString("liaphonCommand");
		int waitTime = _config.getInt("liaPhonWaitTime");
		int maxTime = _config.getInt("liaPhonMaxTime");
		String cmdOutput = "";
		// tmporary txtfile for pho-file generation
		String tmpTxtFile = _config.getString("tmpDir")
				+ _config.getString("tmpTxtFile");
		File txtF = new File(tmpTxtFile);
		try {
			FileUtil.writeFileContent(txtF, text);

			_logger.debug("executing: " + phoGenCmd);
			cmdOutput = Util.execCmd(phoGenCmd);
			FileUtil.waitForFile(outFilePath, waitTime, maxTime, false);
			String phoFile = FileUtil.getFileText(outFilePath);
			for (int i = 0; i < _phonems.length; i++) {
				phoFile = phoFile.replace(_phonems[i], _replacements[i]);
			}
			FileUtil.writeFileContent(outFilePath, phoFile);

		} catch (Exception e) {
			e.printStackTrace();
			cmdOutput = e.getMessage();
		}
		return cmdOutput;
	}
}
