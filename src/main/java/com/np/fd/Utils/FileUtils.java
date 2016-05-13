package com.np.fd.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import com.np.fd.constant.Constants;

public class FileUtils {
	public static final int EOF = -1;

	public static void forceDelete(final File file){
		try {
			Files.delete(file.toPath());
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public static FileOutputStream openOutputStream(final File file)
			throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				String msg = "Unexpected File '" + file
						+ "' exists but is a directory";
				throw new IOException(msg);
			}
			if (file.canWrite() == false) {
				String msg = "Unexpected File '" + file
						+ "' cannot be written to";
				throw new IOException(msg);
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					String msg = "Unexpected Directory '" + parent
							+ "' could not be created";
					throw new IOException(msg);
				}
			}
		}
		return new FileOutputStream(file);
	}

	public static long copy(final InputStream input, final OutputStream output,
			final byte[] buffer) throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void closeOutputStream(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static void closeInputStream(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static int indexOfExtension(final String filename) {
		if (filename == null) {
			return Constants.NOT_FOUND;
		}
		final int extensionPos = filename
				.lastIndexOf(Constants.EXTENSION_SEPARATOR);
		return extensionPos;
	}
}
