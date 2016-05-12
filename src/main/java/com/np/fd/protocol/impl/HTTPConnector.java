package com.np.fd.protocol.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.np.fd.protocol.Connector;

public class HTTPConnector extends Connector {

	private HttpURLConnection connection;
	private int responseCode;

	public HTTPConnector(String fileUrl) {
		super(fileUrl);
	}

	public HTTPConnector(String fileUrl, int connectionTimeOut, int readTimeOut) {
		super(fileUrl, connectionTimeOut, readTimeOut);
	}

	public void initiateConenction() throws IOException {
		URL url = new URL(fileUrl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(connectionTimeOut);
		connection.setReadTimeout(readTimeOut);
		responseCode = connection.getResponseCode();
	}

	@Override
	public InputStream readInputStream() throws IOException {
		initiateConenction();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return connection.getInputStream();
		}
		return null;
	}

	@Override
	protected String getHeaderField(String name) {
		return connection.getHeaderField(name);
	}
}
