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
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DownloadException;

public abstract class AbstractDownloader implements Callable<Void> {
	private static final int DEFAULT_CONNECTION_TIME_OUT = 60000;
	private static final int DEFAULT_READ_TIME_OUT = 60000;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	protected static String saveDir;
	protected SourceDto source;
	protected int connectionTimeOut;
	protected int readTimeOut;

	static {
		saveDir = ApplicationPropertyConfig.getSaveLocation();
	}

	/*------------------------------------------*/
	/*---------------  Constructor -------------*/
	/*------------------------------------------*/

	public AbstractDownloader(SourceDto source) {
		this.source = source;
		this.connectionTimeOut = DEFAULT_CONNECTION_TIME_OUT;
		this.readTimeOut = DEFAULT_READ_TIME_OUT;
	}

	public AbstractDownloader(SourceDto source, int connectionTimeOut,
			int readTimeOut) {
		this.source = source;
		this.connectionTimeOut = connectionTimeOut;
		this.readTimeOut = readTimeOut;
	}

	public abstract InputStream readInputStream() throws DownloadException;

	protected abstract long getContentLength(String name)
			throws DownloadException;

	protected abstract String getHeaderField(String name)
			throws DownloadException;

	protected abstract void initiateConenction() throws DownloadException;

	protected abstract void closeConnection();

	protected abstract boolean validate();

	protected boolean validate(SourceDto source) {
		if (source.getHost() == null) {
			return false;
		}
		return true;
	}

	/**
	 * Read the file name from header or Url.
	 * 
	 * @return String File Name
	 * @throws IOException
	 */
	protected String getFileName() throws DownloadException {
		String fileName = null;
		String disposition = getHeaderField(Constants.HeaderConstant.CONTENT_DIPOSITION);
		if (disposition != null
				&& disposition.contains(Constants.HeaderConstant.FILE_NAME)) {
			// extracts file name from header field
			int index = disposition.indexOf(Constants.HeaderConstant.FILE_NAME);
			if (index > 0) {
				fileName = disposition.substring(index
						+ Constants.HeaderConstant.FILE_NAME.length(),
						disposition.length());
			}
		} else {
			// extracts file name from URL
			fileName = source.getFileNameFromPath();
		}
		return fileName;
	}

	/* 
	 */
	public Void call() {

		try {

			if (validate()) {

				initiateConenction();
				String fileName = getFileName();
				InputStream inputStream = readInputStream();

				long contentLength = getContentLength(fileName);
				System.out.println("contentLength==========" + contentLength);
				File destination = getFile(fileName);
				// opens an output stream to save into file
				FileOutputStream outputStream = null;
				long dounloadSize = 0;
				try {
					outputStream = FileUtils.openOutputStream(destination);

					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					dounloadSize = FileUtils.copy(inputStream, outputStream,
							buffer);
					System.out
							.println("File downloaded Successfully! location :"
									+ destination);
				} catch (IOException ex) {
					System.err
							.println("File downloaded Failed for fileName: "
									+ fileName + destination + " :: "
									+ ex.getMessage());
				} finally {
					FileUtils.closeOutputStream(outputStream);
					FileUtils.closeInputStream(inputStream);
					if (dounloadSize < contentLength) {
						FileUtils.forceDelete(destination);
					}
				}
			} else {
				System.out.println("Invalid  Details");
			}
		} catch (DownloadException ex) {
			System.err.println("Connection Failed :: " + ex.getMessage());
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
			fileName = fileNameBuffer.append(fileName)
					.append(String.valueOf(time)).toString();
		} else {
			fileName = fileNameBuffer.append(fileName.substring(0, index))
					.append(String.valueOf(time))
					.append(Constants.EXTENSION_SEPARATOR)
					.append(fileName.substring(index + 1, fileName.length()))
					.toString();
		}
		destination = new File(fileLocation + fileName);
		return destination;
	}

}
