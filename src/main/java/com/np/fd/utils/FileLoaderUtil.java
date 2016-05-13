package com.np.fd.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.np.fd.exception.DataValidationException;

/**
 * File Loader
 */
public class FileLoaderUtil {

	/*-------------------------*/
	/*---  private Methods!  --*/
	/*-------------------------*/

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

	/*-------------------------*/
	/*---  public Methods!  ---*/
	/*-------------------------*/
	/**
	 * Get file location
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(String fileName) {
		// J-
		String result = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + fileName;
		// J+

		return result;
	}

	/**
	 * Return file as a string
	 * 
	 * @param fileName
	 * @return
	 * @throws DataValidationException
	 */
	public static String getFileAsString(String fileName) throws DataValidationException {
		try {
			String encodedContentNode = readFile(getFilePath(fileName));
			return encodedContentNode;
		} catch (IOException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName() + " While reading file (" + fileName + ")  "
					+ e.getMessage();
			throw new DataValidationException(msg);
		}

	}

	/**
	 * Return file as a JSONArray
	 * 
	 * @param fileName
	 * @return
	 * @throws DataValidationException
	 */
	public static JSONArray getJSONArrayFromFile(String fileName) throws DataValidationException {
		try {
			String encodedContentNode = getFileAsString(fileName);
			return new JSONArray(encodedContentNode);
		} catch (JSONException | DataValidationException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName() + " While reading file (" + fileName + ")  "
					+ e.getMessage();
			throw new DataValidationException(msg);
		}
	}

}
