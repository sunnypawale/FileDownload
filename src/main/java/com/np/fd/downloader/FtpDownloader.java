package com.np.fd.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.np.fd.constant.Constants;
import com.np.fd.constant.Protocols;
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DownloadException;

public class FtpDownloader extends AbstractDownloader {

	private URLConnection connection;

	public FtpDownloader(SourceDto source) {
		super(source);
	}

	public FtpDownloader(SourceDto source, int connectionTimeOut,
			int readTimeOut) {
		super(source, connectionTimeOut, readTimeOut);
	}

	public void initiateConenction() throws DownloadException {
		try {
			if (source.getHost() != null && !source.getHost().isEmpty()) {
				URL url = new URL(prepareURL());
				connection = url.openConnection();
				connection.setConnectTimeout(connectionTimeOut);
				connection.setReadTimeout(readTimeOut);
			}
		} catch (IOException ex) {
			throw new DownloadException(ex);
		}
	}

	private URLConnection getConnection() throws DownloadException {
		if (connection == null) {
			initiateConenction();
		}
		return connection;
	}

	@Override
	public InputStream readInputStream() throws DownloadException {
		try {
			return getConnection().getInputStream();
		} catch (IOException ex) {
			throw new DownloadException(ex);
		}
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
	}

	@Override
	protected boolean validate() {
		return validate(source);
	}

	public String prepareURL() {
		String host = source.getHost();
		StringBuilder url = new StringBuilder();
		if (source.getUserName() != null) {
			int index = host.indexOf("//");
			if (index == Constants.NOT_FOUND) {
				return null;
			}
			url.append(host.substring(0, index + 2))
					.append(source.getUserName()).append(":")
					.append(source.getPassword()).append("@")
					.append(host.substring(index + 2, host.length()));
		} else {
			url.append(host);
		}
		if ('/' == source.getPath().charAt(0)) {
			url.append(source.getPath());
		} else {
			url.append(Constants.FORWORD_SLASH);
			url.append(source.getPath());
		}
		System.out.println(url.toString());
		return url.toString();
	}
}
