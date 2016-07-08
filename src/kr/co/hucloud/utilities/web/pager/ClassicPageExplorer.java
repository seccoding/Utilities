package kr.co.hucloud.utilities.web.pager;

public class ClassicPageExplorer implements PageExplorer {

	private Pager pager;
	
	public ClassicPageExplorer(Pager pager) {
		this.pager = pager;
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
		
		if (pager.nowGroupNumber > 0) {
			buffer.append("<a href=\"javascript:movePage('"+pager.prevGroupPageNumber+"')\">" + prev + "</a>");
		}

		int nextPrintPage = pager.groupStartPage + pager.printPage;
		if (nextPrintPage > pager.totalPage) {
			nextPrintPage = pager.totalPage + 1;
		}

		String pageNumber = "";

		for (int i = pager.groupStartPage; i < nextPrintPage; i++) {
			pageNumber = pageFormat.replaceAll("@", i + "");
			if ((i - 1) == pager.pageNo) {
				pageNumber = "<b>" + pageNumber + "</b>";
			}
			buffer.append("<a href=\"javascript:movePage('"+(i - 1)+"')\">" + pageNumber + "</a>");
		}

		if (pager.nowGroupNumber < (pager.totalGroup - 1)) {
			buffer.append("<a href=\"javascript:movePage('"+pager.nextGroupPageNumber+"')\">" + next + "</a>");
		}

		return buffer.toString();
	}
	
}
