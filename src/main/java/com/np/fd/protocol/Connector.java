package com.np.fd.protocol;

import java.io.IOException;
import java.io.InputStream;

import com.np.fd.constant.Constants;

public abstract class Connector {

	private static final int DEFAULT_CONNECTION_TIME_OUT = 60000;
	private static final int DEFAULT_READ_TIME_OUT = 60000;

	protected String fileUrl;
	protected int connectionTimeOut;
	protected int readTimeOut;

	public Connector(String fileUrl) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = DEFAULT_CONNECTION_TIME_OUT;
		this.readTimeOut = DEFAULT_READ_TIME_OUT;
	}

	public Connector(String fileUrl, int connectionTimeOut, int readTimeOut) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = connectionTimeOut;
		this.readTimeOut = readTimeOut;
	}

	public abstract InputStream readInputStream() throws IOException;

	protected abstract String getHeaderField(String name);

	/**
	 * Read the file name from header or Url.
	 * 
	 * @return String File Name
	 */
	protected String getFileName() {
		String fileName = null;
		String disposition = getHeaderField(Constants.HeaderConstant.CONTENT_DIPOSITION);
		if (disposition != null && disposition.contains(Constants.FILE_NAME)) {
			// extracts file name from header field
			int index = disposition.indexOf(Constants.FILE_NAME
					+ Constants.EQUAL);
			if (index > 0) {
				fileName = disposition.substring(index + 10,
						disposition.length() - 1);
			}
		} else {
			// extracts file name from URL
			fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1,
					fileUrl.length());
		}
		return fileName;
	}
}
