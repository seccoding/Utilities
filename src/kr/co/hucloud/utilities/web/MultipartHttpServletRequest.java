package kr.co.hucloud.utilities.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * JSP/Servlet 기반의 Project에서 파일을 Upload 할 수 있는 Utility
 * commons-fileupload를 사용함.
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 *
 */
public class MultipartHttpServletRequest {

	private HttpServletRequest request;
	private List<FileItem> items;
	
	/**
	 * 일반적인 HttpServletReqeust를 MultipartHttpServletRequest로 변환한다.
	 * @param request
	 */
	public MultipartHttpServletRequest(HttpServletRequest request) {
		this.request = request;
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = request.getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8"); 
		
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * request에서 Parameter를 가져온다.
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		for ( FileItem fileItem : items ) {
			if ( fileItem.getFieldName().equals(name) )  {
				try {
					return fileItem.getString("UTF-8");
				} catch (UnsupportedEncodingException e) {
					return fileItem.getString();
				}
			}
		}
		return null;
	}
	
	/**
	 * request에서 Parameter를 가져온다.
	 * @param name
	 * @return
	 */
	public List<String> getParameterValue(String name) {
		List<String> values = new ArrayList<String>();
		
		for ( FileItem fileItem : items ) {
			if ( fileItem.getFieldName().equals(name) )  {
				try {
					values.add(fileItem.getString("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					values.add(fileItem.getString());
				}
			}
		}
		return values;
	}
	
	/**
	 * Session을 얻어온다.
	 * @return
	 */
	public HttpSession getSession() {
		return request.getSession();
	}
	
	/**
	 * RequestDispatcher를 얻어온다.
	 * @param jspPage
	 * @return
	 */
	public RequestDispatcher getRequestDispatcher(String jspPage) {
		return request.getRequestDispatcher(jspPage);
	}
	
	/**
	 * 업로드한 File Parameter를 얻어온다.
	 * @param name
	 * @return
	 */
	public MultipartFile getFile(String name) {
		MultipartFile file = new MultipartFile();
		
		for ( FileItem fileItem : items ) {
			if ( fileItem.getFieldName().equals(name) )  {
				file.setFileName(fileItem.getName());
				file.setFileSize(fileItem.getSize());
				file.setContentType(fileItem.getContentType());
				file.setFileItem(fileItem);
				
				return file;
			}
		}
		
		return null;
	}
	
	/**
	 * Upload 한 파일이 임시적으로 저장될 클래스
	 * @author Minchang Jang (mc.jang@hucloud.co.kr)
	 *
	 */
	public class MultipartFile {
		private String fileName;
		private long fileSize;
		private String contentType;
		private FileItem fileItem;
		
		/**
		 * 파일의 이름을 가져온다.
		 * @return
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * 파일의 이름을 설정한다.
		 * @param fileName
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * 파일의 크기를 가져온다.
		 * @return
		 */
		public long getFileSize() {
			return fileSize;
		}
		/**
		 * 파일의 크기를 설정한다.
		 * @param fileSize
		 */
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		/**
		 * 파일의 Type을 가져온다.
		 * @return
		 */
		public String getContentType() {
			return contentType;
		}
		/**
		 * 파일의 Type을 설정한다.
		 * @param contentType
		 */
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		
		/**
		 * Upload 한 File이 임시적으로 저장되는 FileItem(commons-fileupload 전용)
		 * @param fileItem
		 */
		public void setFileItem(FileItem fileItem) {
			this.fileItem = fileItem;
		}
		
		/**
		 * Upload 한 파일을 지정한 장소에 저장한다.
		 * @param dest
		 * @return
		 */
		public File write(String dest) {
			File file = new File(dest);
			try {
				fileItem.write(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return file;
		}
		
	}
	
}


