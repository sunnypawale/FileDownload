package com.np.fd.constant;

public interface Constants {

	String EQUAL = "=";
	String FORWORD_SLASH = "/";
	char COLON = ':';
	int NOT_FOUND = -1;
	char EXTENSION_SEPARATOR = '.';

	interface HeaderConstant {
		String CONTENT_DIPOSITION = "Content-Disposition";
		String CONTENT_LENGTH = "Content-Length";
		String FILE_NAME = "filename=";
	}

	interface Properties {
		String FILE_SAVE_DIR = "file.save.dir";
	}
}
