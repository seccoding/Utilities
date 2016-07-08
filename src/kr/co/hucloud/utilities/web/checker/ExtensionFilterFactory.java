package kr.co.hucloud.utilities.web.checker;

public class ExtensionFilterFactory {

	public static ExtensionFilter getFilter(int filterType) {
		
		if ( filterType == ExtFilter.COMPARE_EXTENSION ) {
			return new CompareExtensionFilter();
		}
		else if ( filterType == ExtFilter.APACHE_TIKA ) {
			return new TikaMimeTypeFilter();
		}
		else if ( filterType == ExtFilter.JMIME_MAGIC ) {
			return new JMimeMagicMimeTypeFilter();
		}
		else {
			throw new RuntimeException("Filter Type이 올바르지 않습니다.");
		}
		
	}
	
}
