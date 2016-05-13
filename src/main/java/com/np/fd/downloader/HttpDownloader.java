package com.np.fd.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.np.fd.constant.Constants;
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DownloadException;

/**
 * HTTP/HTTPS file Downloader concrete implementation of
 * {@link AbstractDownloader}
 */
public class HttpDownloader extends AbstractDownloader {

	private HttpURLConnection connection;
	private int responseCode = 0;

	/*------------------------------------------*/
	/*---------------  Constructor -------------*/
	/*------------------------------------------*/

	public HttpDownloader(SourceDto source) {
		super(source);
	}

	public HttpDownloader(SourceDto source, int connectionTimeOut, int readTimeOut) {
		super(source, connectionTimeOut, readTimeOut);
	}

	/*------------------------------------------*/
	/*---------------  private methods ---------*/
	/*------------------------------------------*/

	private HttpURLConnection getConnection() throws DownloadException {
		if (connection == null) {
			initiateConenction();
		}
		return connection;
	}

	private String prepareURL() {
		String host = source.getHost();
		StringBuilder url = new StringBuilder(host);
		if ('/' == source.getPath().charAt(0)) {
			url.append(source.getPath());
		} else {
			url.append(Constants.FORWORD_SLASH);
			url.append(source.getPath());
		}
		return url.toString();
	}

	/*------------------------------------------*/
	/*---------------  implementation ----------*/
	/*------------------------------------------*/
	@Override
	public void initiateConenction() throws DownloadException {
		try {
			if (source.getHost() != null && !source.getHost().isEmpty()) {
				URL url = new URL(prepareURL());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(connectionTimeOut);
				connection.setReadTimeout(readTimeOut);
				responseCode = connection.getResponseCode();
			}
		} catch (IOException ex) {
			throw new DownloadException(ex);
		}
	}

	@Override
	public InputStream readInputStream() throws DownloadException {
		try {
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return getConnection().getInputStream();
			}
		} catch (IOException ex) {
			throw new DownloadException(ex);
		}
		return null;
	}

	@Override
	protected long getContentLength(String name) throws DownloadException {
		return getConnection().getContentLengthLong();
	}

	@Override
	protected String getHeaderField(String name) throws DownloadException {
		return getConnection().getHeaderField(name);
	}

	@Override
	protected void closeConnection() {
		if (connection != null) {
			connection.disconnect();
		}
	}

	@Override
	protected boolean validate() {
		return validateHost(source);
	}

}
