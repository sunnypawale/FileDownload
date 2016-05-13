package com.np.fd.constant;

public enum Protocols {

	HTTP("http"), HTTPS("https"), FTP("ftp"), FTPS("ftps"), SFTP("sftp");
	private String name;

	private Protocols(String name) {
		this.name = name;
	}

}
