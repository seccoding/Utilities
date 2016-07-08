package kr.co.hucloud.utilities.web.pager;

public class ListPageExplorer implements PageExplorer {

	private Pager pager;
	
	public ListPageExplorer(Pager pager) {
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
		
		int centerPage = pager.printPage / 2;
		int startPageNumber = pager.pageNo - centerPage;
		if ( startPageNumber < 0 ) {
			startPageNumber = 0;
		}
		
		int endPageNumber = startPageNumber + pager.printPage;
		if ( endPageNumber > pager.totalPage ) {
			endPageNumber = pager.totalPage;
		}
		
		if ( endPageNumber - startPageNumber < pager.printPage ) {
			startPageNumber = startPageNumber - (pager.printPage - (endPageNumber - startPageNumber));
		}
		
		String pageNumber = "";
		
		if ( startPageNumber > 0 ) {
			buffer.append("<a href=\"javascript:movePage('" + (pager.pageNo - 1) + "')\">" + prev + "</a>");
		}
		
		for (int i = startPageNumber; i < endPageNumber; i++) {
			pageNumber = pageFormat.replaceAll("@", (i+1) + "");
			if (i == pager.pageNo) {
				pageNumber = "<b>" + pageNumber + "</b>";
			}
			
			buffer.append("<a href=\"javascript:movePage('" + i + "')\">" + pageNumber + "</a>");
		}
		
		if ( pager.pageNo < endPageNumber ) {
			buffer.append("<a href=\"javascript:movePage('" + (pager.pageNo + 1) + "')\">" + next + "</a>");
		}

		return buffer.toString();
	}
	
}
