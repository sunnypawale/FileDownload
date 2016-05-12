package com.np.fd.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FtpDownloader extends AbstractDownloader {

	private URLConnection connection;

	public FtpDownloader(String fileUrl) {
		super(fileUrl);
	}

	public FtpDownloader(String fileUrl, int connectionTimeOut, int readTimeOut) {
		super(fileUrl, connectionTimeOut, readTimeOut);
	}

	public void initiateConenction() throws IOException {
		if (fileUrl != null && !fileUrl.isEmpty()) {
			URL url = new URL(fileUrl);
			connection = url.openConnection();
			connection.setConnectTimeout(connectionTimeOut);
			connection.setReadTimeout(readTimeOut);
		}
	}

	private URLConnection getConnection() throws IOException {
		if (connection == null) {
			initiateConenction();
		}
		return connection;
	}

	@Override
	public InputStream readInputStream() throws IOException {
		return getConnection().getInputStream();
	}

	@Override
	protected String getHeaderField(String name) throws IOException {
		return getConnection().getHeaderField(name);
	}

	@Override
	protected void closeConnection() {
	}
}
