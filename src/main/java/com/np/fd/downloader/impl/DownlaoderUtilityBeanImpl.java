package com.np.fd.downloader.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.np.fd.constant.Constants;
import com.np.fd.constant.Protocols;
import com.np.fd.downloader.DownlaoderUtilityBean;
import com.np.fd.downloader.FtpDownloader;
import com.np.fd.downloader.HttpDownloader;

public class DownlaoderUtilityBeanImpl implements DownlaoderUtilityBean {

	private static final ExecutorService downloadExecutorThreadPool = Executors.newCachedThreadPool();

	public void initiateDownload(List<String> sourceLocations) {

		for (int i = 0; i < sourceLocations.size(); i++) {
			String source = sourceLocations.get(i);
			Protocols protocol = getProtocol(source);
			if (protocol != null) {
				switch (protocol) {
				case HTTP:
					downloadExecutorThreadPool.submit(new HttpDownloader(source));
					break;
				case HTTPS:
					downloadExecutorThreadPool.submit(new HttpDownloader(source));
					break;
				case FTP:
					downloadExecutorThreadPool.submit(new FtpDownloader(source));
					break;
				case FTPS:
					downloadExecutorThreadPool.submit(new FtpDownloader(source));
					break;
				}

			}
		}
		downloadExecutorThreadPool.shutdown(); // Disable new tasks from being
												// submitted
	}

	private Protocols getProtocol(String sourceLocation) {
		String protocol = null;
		if (sourceLocation != null) {
			int index = sourceLocation.indexOf(Constants.COLON);
			if (index == -1) {
				return null;
			}

			protocol = sourceLocation.substring(0, index);
			return Protocols.valueOf(protocol.toUpperCase());
		}
		return null;
	}

	/*
	 * void shutdownAndAwaitTermination(ExecutorService pool) { pool.shutdown();
	 * // Disable new tasks from being submitted // Wait a while for existing
	 * tasks to terminate while (true) { try { Thread.sleep(10000); } catch
	 * (InterruptedException e) { } if (pool.isTerminated()) {
	 * pool.shutdownNow(); break; } } }
	 */
}
