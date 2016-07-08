package kr.co.hucloud.utilities.web.pager;

public abstract class Pager {
	
	public static final boolean ORACLE = true;
	public static final boolean OTHER = false;
	
	private int totalArticleCount;

	protected int printArticle;
	int printPage;

	protected int startArticleNumber;
	protected int endArticleNumber;

	int totalPage;
	int totalGroup;

	int nowGroupNumber;

	int groupStartPage;

	int nextGroupPageNumber;
	int prevGroupPageNumber;

	protected int pageNo;
	
	/**
	 * Paging 객체를 얻어온다.
	 * 한 페이지당 보여지는 게시글 수 10개
	 * 한 페이지당 보여지는 페이지 수 10개
	 * 로 기본 설정됨.
	 */
	public Pager() {
		this.printArticle = 10;
		this.printPage = 10;
	}
	
	public Pager(int printArticle, int printPage) {
		this.printArticle = printArticle;
		this.printPage = printPage;
	}
	
	public void setPageNumber(int pageNumber) {
		setPageNumber(pageNumber + "");
	}
	
	/**
	 * 요청된 페이지의 번호를 얻어온다.
	 * 1 페이지의 경우 0이 입력된다.
	 * 아무것도 입력하지 않았다면 0으로 기본 설정된다.
	 * @param pageNumber
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNo = 0;
		try {
			this.pageNo = Integer.parseInt(pageNumber);
		} catch (NumberFormatException nfe) {
			this.pageNo = 0;
		}

		computeArticleNumbers();
		
		this.nowGroupNumber = this.pageNo / this.printPage;
		this.groupStartPage = (this.nowGroupNumber * this.printPage) + 1;

		this.nextGroupPageNumber = this.groupStartPage + this.printPage - 1;
		this.prevGroupPageNumber = this.groupStartPage - this.printPage - 1;
	}
	
	protected abstract void computeArticleNumbers();
	
	/**
	 * 조회하려는 조건(Query)의 총 게시물 수를 정의한다.
	 * @param count
	 */
	public void setTotalArticleCount(int count) {
		this.totalArticleCount = count;

		this.totalPage = (int) Math.ceil((double) this.totalArticleCount
				/ this.printArticle);
		this.totalGroup = (int) Math.ceil((double) this.totalPage
				/ this.printPage);
	}
	
	/**
	 * 조회하려는 조건(Query)의 총 게시물 수를 가져온다.
	 * @return
	 */
	public int getTotalArticleCount() {
		return this.totalArticleCount;
	}

	/**
	 * Query에서 사용될 탐색 시작 번호 
	 * Oracle의 경우 rownum의 시작 번호
	 * @return
	 */
	public int getStartArticleNumber() {
		return this.startArticleNumber;
	}
	
	/**
	 * Query에서 사용될 탐색 시작 번호를 정의한다.
	 * @param startArticleNumber
	 */
	public void setStartArticleNumber(int startArticleNumber) {
		this.startArticleNumber = startArticleNumber;
	}
	
	/**
	 * Query에서 사용될 탐색 끝 번호를 정의한다.
	 * @param endArticleNumber
	 */
	public abstract void setEndArticleNumber(int endArticleNumber);

	/**
	 * Query에서 사용될 탐색 마지막 번호
	 * Oracle의 경우 rownum의 마지막 번호
	 * @return
	 */
	public abstract int getEndArticleNumber();
	
}
