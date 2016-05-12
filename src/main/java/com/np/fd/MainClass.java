package com.np.fd;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.np.fd.downloader.DownlaoderUtilityBean;
import com.np.fd.downloader.impl.DownlaoderUtilityBeanImpl;

public class MainClass {

	/*"ftp://test.rebex.net",
	"https://www.tv-subs.com/subtitle/silicon-valley-season-2-episode-10-english-7752.zip",
	"http://mirror.fibergrid.in/apache//commons/io/source/commons-io-2.5-src.zip",
	"http://www.tutorialspoint.com/struts_2/pdf/index.pdf"*/
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		DownlaoderUtilityBean downlaoderUtilityBean = applicationContext.getBean("downlaoderUtilityBean",
				DownlaoderUtilityBeanImpl.class);
		downlaoderUtilityBean.initiateDownload(Arrays.asList(
				"ftps://demo:password@test.rebex.net:21/readme.txt"));
	}

}
