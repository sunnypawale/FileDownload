package com.np.fd.downloader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.np.fd.Utils.DataFileLoaderUtil;
import com.np.fd.dto.SourceDto;
import com.np.fd.exception.DownloadException;
import com.sun.xml.internal.stream.Entity;

public class SftpDownloader extends AbstractDownloader {

	private static final String KNOWN_HOSTS_FILE_NAME = "known_hosts";
	JSch jsch = null;
	Session session = null;
	Channel channel = null;
	ChannelSftp channelSftp = null;
	String knownHostsFile = null;

	public SftpDownloader(SourceDto source) {
		super(source);
	}

	public SftpDownloader(SourceDto source, int connectionTimeOut,
			int readTimeOut) {
		super(source, connectionTimeOut, readTimeOut);
	}

	public void initiateConenction() throws DownloadException {
		jsch = new JSch();
		try {
			knownHostsFile = DataFileLoaderUtil
					.getTestDataFilePath(KNOWN_HOSTS_FILE_NAME);
			session = jsch.getSession(source.getUserName(),
					source.getHostWithoutProtocol());
			session.setPassword(source.getPassword());
			jsch.setKnownHosts(knownHostsFile);
			session.setTimeout(connectionTimeOut);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
		} catch (JSchException ex) {
			throw new DownloadException(ex);
		}
	}

	private ChannelSftp getConnection() throws DownloadException {
		if (channelSftp == null) {
			initiateConenction();
		}
		return channelSftp;
	}

	@Override
	public InputStream readInputStream() throws DownloadException {
		try {
			return getConnection().get(source.getPath());
		} catch (SftpException ex) {
			throw new DownloadException(ex);
		}
	}

	@Override
	protected String getHeaderField(String name) throws DownloadException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected long getContentLength(String name) {
		try {
			Vector<LsEntry> fileList = channelSftp.ls("/");
			Iterator<LsEntry> itr = fileList.iterator();
			while (itr.hasNext()) {
				LsEntry entry = itr.next();
				if (name.equals(entry.getFilename())) {
					return entry.getAttrs().getSize();
				}

			}
		} catch (SftpException e) {
			return 0;
		}
		return 0;
	}

	@Override
	protected void closeConnection() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	@Override
	protected boolean validate() {
		return validate(source);
	}
}
