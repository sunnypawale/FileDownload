package com.np.fd.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Callable;

import com.np.fd.Utils.FileUtils;
import com.np.fd.constant.Constants;

public abstract class AbstractDownloader implements Callable<Void> {
	private static final int DEFAULT_CONNECTION_TIME_OUT = 60000;
	private static final int DEFAULT_READ_TIME_OUT = 60000;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	protected static String saveDir;
	protected String fileUrl;
	protected int connectionTimeOut;
	protected int readTimeOut;

	static {
		saveDir = "D:/Download";
	}

	public AbstractDownloader(String fileUrl) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = DEFAULT_CONNECTION_TIME_OUT;
		this.readTimeOut = DEFAULT_READ_TIME_OUT;
	}

	public AbstractDownloader(String fileUrl, int connectionTimeOut,
			int readTimeOut) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = connectionTimeOut;
		this.readTimeOut = readTimeOut;
	}

	public abstract InputStream readInputStream() throws IOException;

	protected abstract String getHeaderField(String name) throws IOException;

	protected abstract void initiateConenction() throws IOException;

	protected abstract void closeConnection() throws IOException;

	/**
	 * Read the file name from header or Url.
	 * 
	 * @return String File Name
	 * @throws IOException
	 */
	protected String getFileName() throws IOException {
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
			fileName = fileUrl.substring(
					fileUrl.lastIndexOf(Constants.FORWORD_SLASH) + 1,
					fileUrl.length());
		}
		return fileName;
	}

	public Void call() throws Exception {

		try {
			initiateConenction();
			String fileName = getFileName();
			InputStream inputStream = readInputStream();

			long contentLength = Long
					.valueOf(getHeaderField(Constants.HeaderConstant.CONTENT_LENGTH));

			File destination = getFile(fileName);
			// opens an output stream to save into file
			FileOutputStream outputStream = FileUtils
					.openOutputStream(destination);
			long dounloadSize = 0;
			try {

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				dounloadSize = FileUtils
						.copy(inputStream, outputStream, buffer);
				outputStream.close();
			} catch (IOException ex) {

			} finally {
				FileUtils.closeOutputStream(outputStream);
				if (dounloadSize < contentLength) {
					FileUtils.forceDelete(destination);
				}
			}
			FileUtils.closeInputStream(inputStream);
			System.out.println("File downloaded");
		} catch (IOException ex) {
			System.out.println("Connection Failed");
		} finally {
			closeConnection();
		}
		return null;
	}

	private File getFile(String fileName) {
		String saveFilePath = saveDir + File.separator;
		File destination = new File(saveFilePath + fileName);

		if (destination.exists()) {
			long time = new Date().getTime();
			fileName.concat(String.valueOf(time));
			destination = new File(saveFilePath + fileName);
		}
		return destination;
	}
}
