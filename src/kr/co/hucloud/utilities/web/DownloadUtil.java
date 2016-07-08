package kr.co.hucloud.utilities.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 파일 다운로드를 유용하게 함.
 * Internet Explorer, Mozilia 모두 호환
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 *
 */
public class DownloadUtil {

	private String uploadPath;
	
	/**
	 * 파일이 업로드 되어 있는 경로를 가져옴.
	 * @return
	 */
	public String getUploadPath() {
		return uploadPath;
	}
	/**
	 * 파일이 업로드 되어 있는 경로를 지정.
	 * @param uploadPath
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
	private static DownloadUtil downloadUtil;
	
	private DownloadUtil() {}
	
	/**
	 * DownloadUtil 인스턴스를 가져옴.
	 * @param filePath 다운로드 하려는 파일이 위치한 서버상의 물리적인 절대 경로
	 * @return
	 */
	public static DownloadUtil getInstance(String filePath) {
		
		if ( downloadUtil == null ) {
			downloadUtil = new DownloadUtil();
		}
		
		downloadUtil.setUploadPath(filePath);
		
		return downloadUtil;
	}
	
	/**
	 * 파일을 다운로드 함.
	 * @param request
	 * @param response
	 * @param realFileName 서버에 존재하는 물리적인 파일의 이름
	 * @param displayFileName 다운로드 할 때 사용자에게 보여질 파일의 이름
	 * @throws UnsupportedEncodingException
	 */
	public void download(HttpServletRequest request,
						HttpServletResponse response,
						String realFileName,
						String displayFileName) throws UnsupportedEncodingException {
		
		File downloadFile = new File(this.getUploadPath() + File.separator + realFileName);
		
		response.setContentType("application/download; charset=utf-8");
		response.setContentLength( (int) downloadFile.length());
		
		// 사용자의 브라우져 정보를 가져온다.
		String userAgent = request.getHeader("User-Agent");
		// 사용자의 브라우저가 MicroSoft Internet Explorer 인지 확인한다.
		boolean internetExplorer = userAgent.indexOf("MSIE") > -1;
		if( !internetExplorer ) {
			internetExplorer = userAgent.indexOf("Gecko") > -1;
		}
		
		// 다운로드할 파일의 이름을 브라우져별로 가져온다.
		String fileName = new String(displayFileName.getBytes(), "UTF-8");
		if ( internetExplorer ) {
			fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		}
		else {
			// File의 이름을 UTF-8 타입에서 ISO-8859-1 타입으로 변경한다.
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		// 브라우져가 받을 파일의 이름을 response에 등록한다.
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + fileName + "\";");
		// 브라우져가 다운로드 받은 후 Binary 파일로 생성하라고 보낸다.
		response.setHeader("Content-Transfer-Encoding", "binary");
		
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
