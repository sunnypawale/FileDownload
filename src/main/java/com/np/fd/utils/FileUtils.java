package com.np.fd.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

import com.np.fd.constant.Constants;

/**
 * Provide a utility class to do file related operation
 */
public class FileUtils {
	public static final int EOF = -1;

	/**
	 * Delete file.
	 */
	public static void forceDelete(final File file) {
		try {
			Files.delete(file.toPath());
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * Open a OutputStream for give file with validation
	 * 
	 * @param file
	 * @return FileOutputStream
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(final File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				String msg = "Unexpected File '" + file + "' exists but is a directory";
				throw new IOException(msg);
			}
			if (file.canWrite() == false) {
				String msg = "Unexpected File '" + file + "' cannot be written to";
				throw new IOException(msg);
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					String msg = "Unexpected Directory '" + parent + "' could not be created";
					throw new IOException(msg);
				}
			}
		}
		return new FileOutputStream(file);
	}

	/**
	 * Copy content form inputSteam to OutputStream
	 * 
	 * @param input
	 * @param output
	 * @param buffer
	 * @return Copied size
	 * @throws IOException
	 */
	public static long copy(final InputStream input, final OutputStream output, final byte[] buffer)
			throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Close OutputStream if not null, ignore exception
	 * 
	 * @param output
	 */
	public static void closeOutputStream(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Close InputStream if not null, ignore exception
	 * 
	 * @param input
	 */
	public static void closeInputStream(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Get the index of file extension.
	 * 
	 * @param filename
	 * @return
	 */
	public static int indexOfExtension(final String filename) {
		if (filename == null) {
			return Constants.NOT_FOUND;
		}
		final int extensionPos = filename.lastIndexOf(Constants.EXTENSION_SEPARATOR);
		return extensionPos;
	}

	/**
	 * Get the File Object from given filename.
	 * 
	 * @param saveDir
	 * @param fileName
	 * @return
	 */
	public static File getFile(String saveDir, String fileName) {
		String fileLocation = saveDir + File.separator;
		File destination = new File(fileLocation + fileName);

		if (destination.exists()) {
			destination = createFileWithNewName(fileName, fileLocation);
		}
		return destination;
	}

	/**
	 * Create file with new Name,as filename already present in directory.
	 * 
	 * @param fileName
	 * @param fileLocation
	 * @return
	 */
	private static File createFileWithNewName(String fileName, String fileLocation) {
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
