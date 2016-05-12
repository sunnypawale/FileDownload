package com.np.fd;

import java.io.File;

public class MainClass {

	 static String saveDir = "D:/Download";
	 static String saveFilePath = saveDir + File.separator + "asdv";
	public static void main(String[] args) {
		File destination = new File(saveFilePath);
		System.out.println(destination);
	}
}
