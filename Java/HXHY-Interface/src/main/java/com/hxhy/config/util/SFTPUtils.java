package com.hxhy.config.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP 工具类
 *
 * @author Abner
 * @since 2017/09/12 17:11
 */
@Component
public class SFTPUtils {

	private final static String DIR_SPLIT = "/";
	private static final Logger LOGGER = LoggerFactory.getLogger(SFTPUtils.class);
	
	@Value("${ftp.base}") 
	private String base;
	@Value("${ftp.host}") 
	private String host;
	@Value("${ftp.port}") 
	private int port;
	@Value("${ftp.user}") 
	private String username;
	@Value("${ftp.password}") 
	private String password;


	private static Channel channel = null;
	private static Session session = null;

	/**
	 * 文件上传
	 *
	 * @param file
	 * @return
	 */
	public String fileUpload(MultipartFile file) {
		return fileUpload(file,base);
	}

	/**
	 * 文件上传
	 *
	 * @param file
	 * @param parentPath
	 * @return
	 */
	public String fileUpload(MultipartFile file, String subDir) {
		try {
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			return fileUpload(file.getInputStream(), base + subDir, suffix);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("file upload error: ", e);
			return null;
		}
	}

	/**
	 * 文件上传
	 *
	 * @param src
	 * @param dst
	 * @param suffix
	 * @return
	 */
	public String fileUpload(InputStream src, String dst, String suffix) {
		try {
			ChannelSftp channel = getChannel(6000);
			 //跳转到目标目录， 若不存在， 则创建
			 try {
				 channel.cd(dst);
			 } catch (SftpException e) {
				 channel.mkdir(dst);
				 channel.cd(dst);
			 }

			// 判断是否需要从根目录进行检索， 以 "/" 开头的需要先调整到系统根目录
			if (StringUtils.startsWith(dst, DIR_SPLIT)) {
				channel.cd(DIR_SPLIT);
			}

			String[] folders = dst.split(DIR_SPLIT);
			for (String folder : folders) {
				if (folder.length() > 0) {
					try {
						channel.cd(folder);
					} catch (SftpException e) {
						channel.mkdir(folder);
						channel.cd(folder);
					}
				}
			}

			String url = dst + "/" + IDUtils.genID() + "." + suffix;

			channel.put(src, url);
			closeChannel(session, channel);
			return url.substring(base.length());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("file upload error, ", e);
			return null;
		}
	}

	/**
	 * 文件上传
	 *
	 * @param src
	 * @param dst
	 * @return
	 */
	public boolean fileUpload(String src, String dst) {

		try {
			ChannelSftp channel = getChannel(6000);
			channel.put(src, base + dst, ChannelSftp.OVERWRITE);
			closeChannel(session, channel);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("file upload error, ", e);
			return false;
		}
	}

	/**
	 * 获取通道， 并制定超时时间
	 *
	 * @param timeout
	 * @return
	 * @throws JSchException
	 */
	private ChannelSftp getChannel(int timeout) throws JSchException {
		
		JSch jsch = new JSch();
		session = jsch.getSession(username, host, port);
		session.setPassword(password);

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setTimeout(timeout);
		session.connect();

		channel = session.openChannel("sftp");
		channel.connect();

		return (ChannelSftp) channel;
	}

	/**
	 * 关闭通道
	 *
	 * @param session
	 * @param channel
	 * @throws Exception
	 */
	private void closeChannel(Session session, Channel channel) throws Exception {
		if (session != null) {
			session.disconnect();
		}

		if (channel != null) {
			channel.disconnect();
		}
	}
}
