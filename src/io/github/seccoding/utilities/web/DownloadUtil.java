package io.github.seccoding.utilities.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {

	private String uploadPathWithFileName;
	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private File downloadFile;
	
	private String displayFileName;
	private String fileName;
	
	/**
	 * @param response
	 * @param request
	 * @param uploadPathWithFileName: 다운로드 할 파일의 전체 경로를 포함한 파일명.
	 */
	public DownloadUtil(HttpServletResponse response, HttpServletRequest request, String uploadPathWithFileName) {
		this.uploadPathWithFileName = uploadPathWithFileName;
		this.response = response;
		this.request = request;
	}
	
	public void download(String displayFileName) {
		
		this.displayFileName = displayFileName;
		downloadFile = new File(uploadPathWithFileName);
		
		makeDownloadFileName(isUserAgentIsInternetExplorer());
		setResponseHeader();
		startDownload();
	}
	
	private void makeDownloadFileName(boolean internetExplorer) {
		// 다운로드할 파일의 이름을 브라우져별로 가져온다.
		fileName = convertToUnicode(displayFileName);
		if ( internetExplorer ) {
			fileName = urlEncode(fileName).replaceAll("\\+", "%20");
		}
		else {
			// File의 이름을 UTF-8 타입에서 ISO-8859-1 타입으로 변경한다.
			fileName = convertToUnicode(fileName, "ISO-8859-1");
		}
	}
	
	private boolean isUserAgentIsInternetExplorer() {
		String userAgent = request.getHeader("User-Agent");
		if( userAgent.indexOf("MSIE") > -1 ) {
			return true;
		}
		else {
			// "Gecko" (Mozila)
			return false;
		}
	}
	
	private String convertToUnicode(String str) {
		try {
			return new String(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private String convertToUnicode(String str, String toEncoding) {
		try {
			return new String(str.getBytes("UTF-8"), toEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private void setResponseHeader() {
		response.setContentType("application/download; charset=utf-8");
		response.setContentLength( (int) downloadFile.length());
		
		// 브라우져가 받을 파일의 이름을 response에 등록한다.
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + fileName + "\";");
		// 브라우져가 다운로드 받은 후 Binary 파일로 생성하라고 보낸다.
		response.setHeader("Content-Transfer-Encoding", "binary");
	}
	
	private void startDownload() {
		FileInputStream fin = null;
		FileChannel inputChannel = null;
		WritableByteChannel outputChannel = null;
		
		try {
			fin = new FileInputStream(downloadFile);
			inputChannel = fin.getChannel();

			outputChannel = Channels.newChannel(response.getOutputStream());
			inputChannel.transferTo(0, fin.available(), outputChannel);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				if (outputChannel.isOpen())
					outputChannel.close();
			} catch (Exception e) {}
			try {
				if (inputChannel.isOpen())
					inputChannel.close();
			} catch (Exception e) {}
			try {
				if (fin != null)
					fin.close();
			} catch (Exception e) {}
		}
	}
	
}
