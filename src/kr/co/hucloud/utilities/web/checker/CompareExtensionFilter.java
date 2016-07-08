package kr.co.hucloud.utilities.web.checker;

import java.io.File;

class CompareExtensionFilter extends ExtensionFilter {

	@Override
	protected String getMimeType(File currentFile) {
		return clearPath(currentFile.getAbsolutePath());
	}

	@Override
	protected boolean isEquals(String mimeTypeOfFile, String extension) {
		if ( mimeTypeOfFile.toUpperCase().endsWith( "." + extension.toUpperCase() ) ) {
			return true;
		}
		return false;
	}
	
	private String clearPath(String fileName) {
		fileName = removePathString(fileName, "\\");
		fileName = removePathString(fileName, "/");
		return fileName;
	}

	private String removePathString(String fileName, String separator) {
		if ( fileName.indexOf(separator) != -1 ) {
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		}
		return fileName;
	}
	
}
