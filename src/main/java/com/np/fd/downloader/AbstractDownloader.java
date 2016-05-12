package com.np.fd.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Callable;

import com.np.fd.Utils.FileUtils;
import com.np.fd.config.ApplicationPropertyConfig;
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
		saveDir = ApplicationPropertyConfig.getSaveLocation();
	}

	/*------------------------------------------*/
	/*---------------  Constructor -------------*/
	/*------------------------------------------*/

	public AbstractDownloader(String fileUrl) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = DEFAULT_CONNECTION_TIME_OUT;
		this.readTimeOut = DEFAULT_READ_TIME_OUT;
	}

	public AbstractDownloader(String fileUrl, int connectionTimeOut, int readTimeOut) {
		this.fileUrl = fileUrl;
		this.connectionTimeOut = connectionTimeOut;
		this.readTimeOut = readTimeOut;
	}

	public abstract InputStream readInputStream() throws IOException;

	protected abstract String getHeaderField(String name) throws IOException;

	protected abstract void initiateConenction() throws IOException;

	protected abstract void closeConnection();

	/**
	 * Read the file name from header or Url.
	 * 
	 * @return String File Name
	 * @throws IOException
	 */
	protected String getFileName() throws IOException {
		String fileName = null;
		String disposition = getHeaderField(Constants.HeaderConstant.CONTENT_DIPOSITION);
		if (disposition != null && disposition.contains(Constants.HeaderConstant.FILE_NAME)) {
			// extracts file name from header field
			int index = disposition.indexOf(Constants.HeaderConstant.FILE_NAME);
			if (index > 0) {
				fileName = disposition.substring(index + Constants.HeaderConstant.FILE_NAME.length(),
						disposition.length());
			}
		} else {
			// extracts file name from URL
			fileName = fileUrl.substring(fileUrl.lastIndexOf(Constants.FORWORD_SLASH) + 1, fileUrl.length());
		}
		return fileName;
	}

	/* 
	 */
	public Void call() {

		try {
			initiateConenction();
			String fileName = getFileName();
			InputStream inputStream = readInputStream();

			long contentLength = Long.valueOf(getHeaderField(Constants.HeaderConstant.CONTENT_LENGTH));

			File destination = getFile(fileName);
			// opens an output stream to save into file
			FileOutputStream outputStream = FileUtils.openOutputStream(destination);
			long dounloadSize = 0;
			try {

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				dounloadSize = FileUtils.copy(inputStream, outputStream, buffer);
				System.out.println("File downloaded");
			} catch (IOException ex) {

			} finally {
				FileUtils.closeOutputStream(outputStream);
				if (dounloadSize < contentLength) {
					FileUtils.forceDelete(destination);
				}
			}
			FileUtils.closeInputStream(inputStream);
		} catch (IOException ex) {
			System.out.println("Connection Failed");
		} finally {
			closeConnection();
		}
		return null;
	}

	private File getFile(String fileName) {
		String fileLocation = saveDir + File.separator;
		File destination = new File(fileLocation + fileName);

		if (destination.exists()) {
			destination = createFileWithNewName(fileName, fileLocation);
		}
		return destination;
	}

	private File createFileWithNewName(String fileName, String fileLocation) {
		File destination;
		long time = new Date().getTime();
		StringBuffer fileNameBuffer = new StringBuffer();
		final int index = FileUtils.indexOfExtension(fileName);
		if (index == Constants.NOT_FOUND) {
			fileName = fileNameBuffer.append(fileName).append(String.valueOf(time)).toString();
		} else {
			fileName = fileNameBuffer.append(fileName.substring(0, index)).append(String.valueOf(time))
					.append(Constants.EXTENSION_SEPARATOR).append(fileName.substring(index + 1, fileName.length()))
					.toString();
		}
		destination = new File(fileLocation + fileName);
		return destination;
	}

}
