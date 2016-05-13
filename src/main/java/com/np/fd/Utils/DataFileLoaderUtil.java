package com.np.fd.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.np.fd.exception.DataValidationException;

public class DataFileLoaderUtil {

	public static String getTestDataFilePath(String fileName) {
		// J-
		String result = System.getProperty("user.dir") + File.separator + "src"
				+ File.separator + "main" + File.separator + "resources"
				+ File.separator + fileName;
		// J+

		return result;
	}

	public static String getFileAsString(String fileName)
			throws DataValidationException {
		try {
			String encodedContentNode = readFile(getTestDataFilePath(fileName));
			return encodedContentNode;
		} catch (IOException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName()
					+ " While reading file (" + fileName + ")  "
					+ e.getMessage();
			throw new DataValidationException(msg);
		}

	}

	public static JSONArray getJSONOArrayFromFile(String filePath)
			throws DataValidationException {
		try {
			String encodedContentNode = readFile(filePath);
			return new JSONArray(encodedContentNode);
		} catch (JSONException | IOException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName()
					+ " While reading file (" + filePath + ")  "
					+ e.getMessage();
			throw new DataValidationException(msg);
		}
	}

	public static JSONArray getJSONOArrayFromFileName(String fileName)
			throws DataValidationException {
		JSONArray result = null;
		result = getJSONOArrayFromFile(getTestDataFilePath(fileName));
		return result;
	}

	private static String readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

}
