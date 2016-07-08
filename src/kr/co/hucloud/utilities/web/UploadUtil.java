package kr.co.hucloud.utilities.web;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드를 위한 Utility.
 * @author Minchang
 *
 */
public class UploadUtil {

	private String uploadPath;
	
	/**
	 * 파일이 업로드 될 경로를 가져온다.
	 * @return
	 */
	public String getUploadPath() {
		return uploadPath;
	}
	/**
	 * 파일이 업로드될 경로를 지정한다.
	 * @param uploadPath
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
	private static UploadUtil uploadUtil;
	
	private UploadUtil() {}
	
	/**
	 * UploadUtil 인스턴스를 가져온다.
	 * @param uploadPath 파일이 업로드될 경로
	 * @return
	 */
	public static UploadUtil getInstance(String uploadPath) {
		if ( uploadUtil == null ) {
			uploadUtil = new UploadUtil();
		}
		
		uploadUtil.setUploadPath(uploadPath);
		
		File path = new File(uploadUtil.getUploadPath());
		if ( !path.exists() ) {
			path.mkdirs();
		}
		
		return uploadUtil;
	}
	
	/**
	 * 파일을 업로드 한다.
	 * @param file MultipartFile로, spring-webmvc 전용
	 * @param originalFileName 실제 파일 이름
	 * @param displayFileName 사용자에게 보여질 파일 이름
	 */
	public void uploadFile(MultipartFile file,
			String originalFileName,
			String displayFileName) {
		
		// 사용자가 파일을 업로드 했다면...
		if ( !file.isEmpty() ) {
			
			File uploadedFile = 
					new File(this.getUploadPath() + File.separator + originalFileName);
			try {
				file.transferTo(uploadedFile);
			}
			catch(IOException ioe) {
				throw new RuntimeException(ioe.getMessage(), ioe);
			}
			
		}
		
	}
	
	/**
	 * 파일을 업로드 한다. 
	 * 파일 명이 UUID로 변환되어 저장된다.
	 * @param file MultipartFile로, spring-webmvc 전용
	 * @param displayFileName 사용자에게 보여질 파일 이름
	 * @return
	 */
	public String uploadFileUseUUID(MultipartFile file,
			String displayFileName) {
		
		String originalFileName = "";
		
		// 사용자가 파일을 업로드 했다면...
		if ( !file.isEmpty() ) {
			
			// 서버에 저장될 파일의 명
			originalFileName = 
					UUID.randomUUID().toString();
			
			File uploadedFile = 
					new File(this.getUploadPath() + File.separator + originalFileName);
			try {
				file.transferTo(uploadedFile);
			}
			catch(IOException ioe) {
				throw new RuntimeException(ioe.getMessage(), ioe);
			}
			
		}
		
		return originalFileName;
	}
	
}
