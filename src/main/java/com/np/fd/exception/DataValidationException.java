package com.np.fd.exception;

public class DataValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataValidationException() {
		super();
	}

	public DataValidationException(String message) {
		super(message);
	}

	public DataValidationException(Throwable cause) {
		super(cause);
	}

	public DataValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
