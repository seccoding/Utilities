package kr.co.hucloud.utilities.web.pager;

public class OraclePager extends Pager {

	public OraclePager(int printArticle, int printPage) {
		super(printArticle, printPage);
	}

	public OraclePager() {
		super();
	}

	@Override
	protected void computeArticleNumbers() {
		this.startArticleNumber = (this.pageNo * this.printArticle) + 1;
		this.endArticleNumber = this.startArticleNumber + this.printArticle - 1;
	}

	@Override
	public void setEndArticleNumber(int endArticleNumber) {
		this.endArticleNumber = endArticleNumber;
	}

	@Override
	public int getEndArticleNumber() {
		return this.endArticleNumber;
	}

	
	
}
