package com.np.fd.Utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.fd.exception.DataValidationException;

/**
 * Provide a helper class used to largely to transform between POJOs and JSON
 * objects.
 */
public class JsonHelper {

	private static ObjectMapper mapper = new ObjectMapper();


	/*-------------------------*/
	/*---  Helper Methods!  ---*/
	/*-------------------------*/

	/**
	 * Convert a JSON request object to POJO.
	 */
	public static <T> T jsonToPojo(JSONObject json, Class<T> type)
			throws DataValidationException {

		if (json == null) {
			String msg = "Can't convert NULL to type '" + type + "'";
			throw new DataValidationException(msg);
		}
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			T result = mapper.readValue(json.toString(), type);
			return result;
		} catch (IOException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName()
					+ " converting JSON object (" + json + ") to type " + type
					+ ": " + e.getMessage();
			throw new DataValidationException(msg);
		}
	}
	
	public static <T> List<T> jsonToPojoAsList(JSONArray jsonArr, Class<T> type)
			throws DataValidationException {

		if (jsonArr == null) {
			String msg = "Can't convert NULL to type '" + type + "'";
			throw new DataValidationException(msg);
		}
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			List<T> result = mapper.readValue(jsonArr.toString(), mapper.getTypeFactory().constructCollectionType(List.class, type));
			return result;
		} catch (IOException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName()
					+ " converting JSON object (" + jsonArr + ") to type " + type
					+ ": " + e.getMessage();
			throw new DataValidationException(msg);
		}
	}

	/**
	 * convert to JSONObject
	 */
	public static JSONObject asJson(Object obj) throws DataValidationException {
		if (obj == null) {
			return new JSONObject();
		}
		try {
			Writer stringWriter = new StringWriter();
			mapper.writeValue(stringWriter, obj);
			return new JSONObject(stringWriter.toString());
		} catch (IOException | JSONException e) {
			String msg = "Unexpected " + e.getClass().getSimpleName()
					+ " converting object (" + obj + ") to type JSONObject: "
					+ e.getMessage();
			throw new DataValidationException(msg);
		}
	}

}
