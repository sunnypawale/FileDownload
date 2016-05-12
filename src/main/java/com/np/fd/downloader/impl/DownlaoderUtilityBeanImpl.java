package com.np.fd.downloader.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.np.fd.constant.Constants;
import com.np.fd.downloader.DownlaoderUtilityBean;

public class DownlaoderUtilityBeanImpl implements DownlaoderUtilityBean {
	private static ExecutorService downloadExecutorThreadPool = Executors
			.newCachedThreadPool();

	public void initiateDownload(List<String> sourceLocations, String destination) {

		for (int i = 0; i < sourceLocations.size(); i++) {
			String protocol = getProtocol(sourceLocations.get(i));
		}
	}

	private String getProtocol(String sourceLocation) {
		if(sourceLocation!=null){
			int index = sourceLocation.indexOf(Constants.COLON);
		}
		return null;
	}

}
