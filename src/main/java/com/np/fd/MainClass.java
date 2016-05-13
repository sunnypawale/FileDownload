package com.np.fd;

import java.io.IOException;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.np.fd.downloader.DownlaoderUtilityBean;
import com.np.fd.downloader.impl.DownlaoderUtilityBeanImpl;
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DataValidationException;
import com.np.fd.exception.DownloadException;
import com.np.fd.utils.FileLoaderUtil;
import com.np.fd.utils.JsonHelper;

public class MainClass {
	private static final String INPUT_JSON_FILE_NAME = "input.json";

	private static ApplicationContext applicationContext;

	public static void main(String[] args) throws IOException {
		MainClass mainClass = new MainClass();
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		mainClass.startDownlaod();

	}

	public void startDownlaod() throws JsonParseException, JsonMappingException, IOException {

		try {
			JSONArray inputJsonArr = FileLoaderUtil.getJSONArrayFromFile(INPUT_JSON_FILE_NAME);

			List<SourceDto> sourceDto = JsonHelper.jsonToPojoAsList(inputJsonArr, SourceDto.class);
			DownlaoderUtilityBean downlaoderUtilityBean = applicationContext.getBean("downlaoderUtilityBean",
					DownlaoderUtilityBeanImpl.class);
			downlaoderUtilityBean.initiateDownload(sourceDto);

		} catch (DataValidationException e) {
			System.err.println(e.getMessage());
		}
	}

}
