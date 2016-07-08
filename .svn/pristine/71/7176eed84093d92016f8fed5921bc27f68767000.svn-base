package kr.co.hucloud.utilities.web.checker;

import java.io.File;

public abstract class ExtensionFilter {

	public boolean doFilter(String filePath, String ... validExtensions) {
		
		File currentFile = new File(filePath);
		String mimeType = getMimeType(currentFile);
		
		for (String extension : validExtensions) {
			if ( isEquals(mimeType, extension) ) {
				return true;
			}
		}
		
		return false;
	}

	protected abstract String getMimeType(File currentFile);
	
	protected abstract boolean isEquals(String mimeTypeOfFile, String extension);
	
}
