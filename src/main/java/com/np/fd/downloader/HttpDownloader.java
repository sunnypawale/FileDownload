package com.np.fd.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader extends AbstractDownloader {

	private HttpURLConnection connection;
	private int responseCode = 0;

	public HttpDownloader(String fileUrl) {
		super(fileUrl);
	}

	public HttpDownloader(String fileUrl, int connectionTimeOut,
			int readTimeOut) {
		super(fileUrl, connectionTimeOut, readTimeOut);
	}

	public void initiateConenction() throws IOException {
		if (fileUrl != null && !fileUrl.isEmpty()) {
			URL url = new URL(fileUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(connectionTimeOut);
			connection.setReadTimeout(readTimeOut);
			responseCode = connection.getResponseCode();
		}
	}

	private HttpURLConnection getConnection() throws IOException {
		if (connection == null) {
			initiateConenction();
		}
		return connection;
	}

	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public InputStream readInputStream() throws IOException {
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return getConnection().getInputStream();
		}
		return null;
	}

	@Override
	protected String getHeaderField(String name) throws IOException {
		return getConnection().getHeaderField(name);
	}

	@Override
	protected void closeConnection() throws IOException {
		if (connection != null) {
			connection.disconnect();
		}
	}
}
