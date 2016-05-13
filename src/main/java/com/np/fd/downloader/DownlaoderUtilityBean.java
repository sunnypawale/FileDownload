package com.np.fd.downloader;

import java.util.List;

import com.np.fd.dto.SourceDto;

public interface DownlaoderUtilityBean {

	/**
	 * initiate download for provided source.
	 * 
	 * @param sourceLocation
	 */
	void initiateDownload(List<SourceDto> sourceLocation);
}
