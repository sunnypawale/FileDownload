package com.np.fd.exception;

public class DownloadException extends Exception {

	private static final long serialVersionUID = 1L;

	public DownloadException() {
		super();
	}

	public DownloadException(String message) {
		super(message);
	}

	public DownloadException(Throwable cause) {
		super(cause);
	}

	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public DownloadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
