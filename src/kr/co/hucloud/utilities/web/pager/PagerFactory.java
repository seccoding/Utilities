package kr.co.hucloud.utilities.web.pager;

public class PagerFactory {

	public static Pager getPager(boolean isDbOracle) {
		if ( isDbOracle ) {
			return new OraclePager();
		}
		else {
			return new OtherPager();
		}
	}
	
	public static Pager getPager(boolean isDbOracle, int printArticle, int printPage) {
		if ( isDbOracle ) {
			return new OraclePager(printArticle, printPage);
		}
		else {
			return new OtherPager(printArticle, printPage);
		}
	}
	
}
