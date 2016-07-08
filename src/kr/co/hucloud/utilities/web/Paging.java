package kr.co.hucloud.utilities.web;

/**
 * Paging Util
 * <br/>
 * Pager를 사용할 것을 권장함.
 * 
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 *
 */
@Deprecated
public class Paging {

	private boolean isOracle;
	
	private int totalArticleCount;

	private int printArticle;
	private int printPage;

	private int startArticleNumber;
	private int endArticleNumber;

	private int totalPage;
	private int totalGroup;

	int nowGroupNumber;

	private int groupStartPage;

	private int nextGroupPageNumber;
	private int prevGroupPageNumber;

	private int pageNo;

	/**
	 * Paging 객체를 얻어온다.
	 * 한 페이지당 보여지는 게시글 수 10개
	 * 한 페이지당 보여지는 페이지 수 10개
	 * 로 기본 설정됨.
	 */
	public Paging() {
		this.isOracle = true;
		this.printArticle = 10;
		this.printPage = 10;
	}
	
	/**
	 * Paging 객체를 얻어온다.
	 * 한 페이지당 보여지는 게시글 수 10개
	 * 한 페이지당 보여지는 페이지 수 10개
	 * 로 기본 설정됨.
	 */
	public Paging(boolean isOracle) {
		this.isOracle = isOracle;
		this.printArticle = 10;
		this.printPage = 10;
	}
	
	/**
	 * 한 페이지당 보여지는 게시글 수와 한 페이지당 보여지는 페이지의 수를 지정할 수 있음.
	 * @param printArticle 한 페이지당 보여지는 게시글 수
	 * @param printPage 한 페이지당 보여지는 페이지 수
	 */
	public Paging(int printArticle, int printPage) {
		this.isOracle = true;
		this.printArticle = printArticle;
		this.printPage = printPage;
	}
	
	/**
	 * 한 페이지당 보여지는 게시글 수와 한 페이지당 보여지는 페이지의 수를 지정할 수 있음.
	 * @param printArticle 한 페이지당 보여지는 게시글 수
	 * @param printPage 한 페이지당 보여지는 페이지 수
	 */
	public Paging(int printArticle, int printPage, boolean isOracle) {
		this.isOracle = isOracle;
		this.printArticle = printArticle;
		this.printPage = printPage;
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

		if(this.isOracle) {
			this.startArticleNumber = (this.pageNo * this.printArticle) + 1;
			this.endArticleNumber = this.startArticleNumber + this.printArticle - 1;
		}
		else {
			this.startArticleNumber = (this.pageNo * this.printArticle);
			this.endArticleNumber = this.printArticle;
		}
		
		this.nowGroupNumber = this.pageNo / this.printPage;
		this.groupStartPage = (this.nowGroupNumber * this.printPage) + 1;

		this.nextGroupPageNumber = this.groupStartPage + this.printPage - 1;
		this.prevGroupPageNumber = this.groupStartPage - this.printPage - 1;
	}

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
	public void setEndArticleNumber(int endArticleNumber) {
		if(this.isOracle)
			this.endArticleNumber = endArticleNumber;
		else
			this.endArticleNumber = printArticle;
	}

	/**
	 * Query에서 사용될 탐색 마지막 번호
	 * Oracle의 경우 rownum의 마지막 번호
	 * @return
	 */
	public int getEndArticleNumber() {
		if(this.isOracle)
			return this.endArticleNumber;
		else
			return this.printArticle;
	}
	
	/**
	 * JSP에서 Paging 결과를 보여준다.
	 * @param link Page 번호를 전달할 Parameter Name
	 * @param pageFormat Page 번호를 보여줄 패턴 @(at)가 페이지 번호로 치환된다. [@]로 작성할 경우 [1] [2] [3] 처럼 보여진다.
	 * @param prev 이전 페이지 그룹으로 가는 버튼의 이름을 작성한다.
	 * @param next 다음 페이지 그룹으로 가는 버튼의 이름을 작성한다.
	 * @return
	 */
	@Deprecated
	public String getPagingList(String link, String pageFormat, String prev, String next) {
		return getPagingList(link, pageFormat, prev, next, "");
	}

	/**
	 * JSP에서 Paging 결과를 보여준다.
	 * getPagingList는 &lt;form> 태그 안에 작성되어야 한다.
	 * @param link Page 번호를 전달할 Parameter Name
	 * @param pageFormat Page 번호를 보여줄 패턴 @(at)가 페이지 번호로 치환된다. [@]로 작성할 경우 [1] [2] [3] 처럼 보여진다.
	 * @param prev 이전 페이지 그룹으로 가는 버튼의 이름을 작성한다.
	 * @param next 다음 페이지 그룹으로 가는 버튼의 이름을 작성한다.
	 * @param formId 서버에게 전달될 Form 의 아이디를 작성한다.
	 * @return
	 */
	public String getPagingList(String link, String pageFormat, String prev, String next, String formId) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<script>");
		buffer.append("function movePage(pageNo) {");
		buffer.append("$(\"#"+link+"\").val(pageNo);");
		buffer.append("$(\"#"+formId+"\").attr('action', '');");
		buffer.append("$(\"#"+formId+"\").attr('method', 'post');");
		buffer.append("$(\"#"+formId+"\").submit();");
		buffer.append("}");
		buffer.append("</script>");
		
		buffer.append("<input type=\"hidden\" id=\""+link+"\" name=\""+link+"\" />");
		
		if (this.nowGroupNumber > 0) {
			buffer.append("<a href=\"javascript:movePage('"+this.prevGroupPageNumber+"')\">" + prev + "</a>");
		}

		int nextPrintPage = this.groupStartPage + this.printPage;
		if (nextPrintPage > this.totalPage) {
			nextPrintPage = this.totalPage + 1;
		}

		String pageNumber = "";

		for (int i = this.groupStartPage; i < nextPrintPage; i++) {
			pageNumber = pageFormat.replaceAll("@", i + "");
			if ((i - 1) == this.pageNo) {
				pageNumber = "<b>" + pageNumber + "</b>";
			}
			buffer.append("<a href=\"javascript:movePage('"+(i - 1)+"')\">" + pageNumber + "</a>");
		}

		if (this.nowGroupNumber < (this.totalGroup - 1)) {
			buffer.append("<a href=\"javascript:movePage('"+this.nextGroupPageNumber+"')\">" + next + "</a>");
		}

		return buffer.toString();
	}

}
