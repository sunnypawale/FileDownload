package com.np.fd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.np.fd.constant.Constants;
import com.np.fd.constant.Protocols;

@JsonInclude(Include.NON_NULL)
public class SourceDto {

	@JsonProperty
	private String userName;
	@JsonProperty
	private String host;
	@JsonProperty
	private String path;
	@JsonProperty
	private String password;
	@JsonProperty
	private int port;

	public SourceDto() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHostWithoutProtocol() {
		String hostWithOutProtocol = null;
		if (this.host != null) {
			int index = this.host.indexOf("//");
			if (index == -1) {
				return null;
			}

			hostWithOutProtocol = this.host.substring(index + 2,
					this.host.length());
		}
		return hostWithOutProtocol;
	}

	public Protocols getProtocol() {
		String protocol = null;
		if (this.host != null) {
			int index = this.host.indexOf(Constants.COLON);
			if (index == -1) {
				return null;
			}

			protocol = this.host.substring(0, index);
			return Protocols.valueOf(protocol.toUpperCase());
		}
		return null;
	}

	public String getFileNameFromPath() {
		String fileName = path.substring(
				path.lastIndexOf(Constants.FORWORD_SLASH) + 1, path.length());
		return fileName;
	}

}
