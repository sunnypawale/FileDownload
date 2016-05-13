package com.np.fd.downloader;

import java.util.List;

import com.np.fd.dto.SourceDto;

public interface DownlaoderUtilityBean {

	
	void initiateDownload(List<SourceDto> sourceLocation);
}
