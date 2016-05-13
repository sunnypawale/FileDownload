package com.np.fd.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.np.fd.Utils.FileUtils;
import com.np.fd.constant.Constants;

public class ApplicationPropertyConfig {

	private static final String PROPERTY_FILE_NAME = "config.properties";
	private static Properties prop = new Properties();
	InputStream input = null;

	public ApplicationPropertyConfig() {
		loadProprtyFile();
	}

	private void loadProprtyFile() {
		try {

			input = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
			if (input == null) {
				System.err.println("Sorry, unable to find " + PROPERTY_FILE_NAME);
				return;
			}

			// load a properties file from class path,
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			FileUtils.closeInputStream(input);
		}
	}

	public static String getSaveLocation() {
		return prop.getProperty(Constants.Properties.FILE_SAVE_DIR).trim();
	}

}
