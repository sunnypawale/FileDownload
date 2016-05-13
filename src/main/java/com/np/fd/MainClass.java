package com.np.fd;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.np.fd.Utils.DataFileLoaderUtil;
import com.np.fd.Utils.JsonHelper;
import com.np.fd.downloader.DownlaoderUtilityBean;
import com.np.fd.downloader.impl.DownlaoderUtilityBeanImpl;
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DataValidationException;

public class MainClass {
	private static final String INPUT_JSON_FILE_NAME = "input.json";
	FileInputStream input = null;
	/*
	 * "ftp://test.rebex.net",
	 * "https://www.tv-subs.com/subtitle/silicon-valley-season-2-episode-10-english-7752.zip"
	 * ,
	 * "http://mirror.fibergrid.in/apache//commons/io/source/commons-io-2.5-src.zip"
	 * , "http://www.tutorialspoint.com/struts_2/pdf/index.pdf"
	 */
	private static ApplicationContext applicationContext;

	public static void main(String[] args) throws IOException {
		MainClass mainClass = new MainClass();
		System.setProperty("https.proxyHost","trproxy.rwe.com") ; 
		System.setProperty("https.proxyPort", "443") ; 
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		mainClass.startDownlaod();
		// System.out.println(mainClass.getHostWithoutProtocol("sftp://test.rebex.net"));
		/*
		 * DownlaoderUtilityBean downlaoderUtilityBean = applicationContext
		 * .getBean("downlaoderUtilityBean", DownlaoderUtilityBeanImpl.class);
		 * downlaoderUtilityBean.initiateDownload(Arrays
		 * .asList("ftps://demo:password@test.rebex.net:21/readme.txt"));
		 */

	}

	public String getHostWithoutProtocol(String host) {
		String hostWithOutProtocol = null;
		if (host != null) {
			int index = host.indexOf("//");
			if (index == -1) {
				return null;
			}

			hostWithOutProtocol = host.substring(index + 2, host.length());
		}
		return hostWithOutProtocol;
	}

	public void startDownlaod() throws JsonParseException,
			JsonMappingException, IOException {

		try {
			JSONArray inputJsonArr = DataFileLoaderUtil
					.getJSONOArrayFromFileName(INPUT_JSON_FILE_NAME);

			List<SourceDto> sourceDto = JsonHelper.jsonToPojoAsList(
					inputJsonArr, SourceDto.class);
			DownlaoderUtilityBean downlaoderUtilityBean = applicationContext
					.getBean("downlaoderUtilityBean",
							DownlaoderUtilityBeanImpl.class);
			downlaoderUtilityBean.initiateDownload(sourceDto);

		} catch (DataValidationException e) {
			System.err.println(e.getMessage());
		}
	}

}
