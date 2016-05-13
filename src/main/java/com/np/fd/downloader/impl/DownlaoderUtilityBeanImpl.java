package com.np.fd.downloader.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.np.fd.constant.Protocols;
import com.np.fd.downloader.DownlaoderUtilityBean;
import com.np.fd.downloader.FtpDownloader;
import com.np.fd.downloader.HttpDownloader;
import com.np.fd.downloader.SftpDownloader;
import com.np.fd.dto.SourceDto;

public class DownlaoderUtilityBeanImpl implements DownlaoderUtilityBean {

	private static final ExecutorService downloadExecutorThreadPool = Executors
			.newCachedThreadPool();

	public void initiateDownload(List<SourceDto> sourceLocations) {

		for (int i = 0; i < sourceLocations.size(); i++) {
			SourceDto source = sourceLocations.get(i);
			Protocols protocol = source.getProtocol();
			if (protocol != null) {
				switch (protocol) {
				case HTTP:
				case HTTPS:
					downloadExecutorThreadPool
							.submit(new HttpDownloader(source));
					break;
				case FTPS:
				case FTP:
					downloadExecutorThreadPool
							.submit(new FtpDownloader(source));
					break;
				case SFTP:
					downloadExecutorThreadPool
							.submit(new SftpDownloader(source));
					break;
				}

			}
		}
		downloadExecutorThreadPool.shutdown(); // Disable new tasks from being
												// submitted
	}

	/*
	 * void shutdownAndAwaitTermination(ExecutorService pool) { pool.shutdown();
	 * // Disable new tasks from being submitted // Wait a while for existing
	 * tasks to terminate while (true) { try { Thread.sleep(10000); } catch
	 * (InterruptedException e) { } if (pool.isTerminated()) {
	 * pool.shutdownNow(); break; } } }
	 */
}
