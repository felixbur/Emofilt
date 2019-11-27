package emofilt.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;

import marytts.client.http.MaryHttpClient;
import marytts.util.http.Address;

import org.apache.log4j.Logger;

import com.felix.util.FileUtil;
import com.felix.util.KeyValues;

import emofilt.Emofilt;
import emofilt.Language;

public class MaryPhoGenerator implements PhoGenInterface {
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
		String errString = "";
		try {
			String maryHost = Emofilt._config.getString("maryServerHost");
			int maryPort = Integer.parseInt(Emofilt._config
					.getString("maryServerPort"));
			Address address = new Address(maryHost, maryPort);
			MaryHttpClient mary = new MaryHttpClient(address);
			_logger.debug("calling Mary server with phrase " + text
					+ " and language: " + lang.getName());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mary.process(text, "TEXT", "MBROLA", lang.getLocale(), null,
					lang.getName(), baos);
			File phoF = new File(outFilePath);
			System.err.println(baos.toString());
			FileUtil.writeFileContent(phoF, baos.toString());
			baos = null;
			// write out phonetic description
			ByteArrayOutputStream baosa = new ByteArrayOutputStream();
			mary.process(text, "TEXT", "ACOUSTPARAMS", lang.getLocale(), null,
					lang.getName(), baosa);
			File acoustF = new File(paramsFilePath);
			System.err.println(baosa.toString());
			FileUtil.writeFileContent(acoustF, baosa.toString());
			baosa = null;
		} catch (Exception e) {
			e.printStackTrace();
			errString = e.getMessage();
		}

		return errString;
	}
}
