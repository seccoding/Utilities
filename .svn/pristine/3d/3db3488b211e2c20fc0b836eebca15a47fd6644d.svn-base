package kr.co.hucloud.utilities.web.pager;

public interface PageExplorer {

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
	public String getPagingList(String link, String pageFormat, String prev, String next, String formId);
	
}
