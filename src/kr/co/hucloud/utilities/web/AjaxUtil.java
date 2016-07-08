package kr.co.hucloud.utilities.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 요청자에게 Ajax Response를 보냄.
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 */
public class AjaxUtil {

	public static void sendResponse(
			HttpServletResponse response, String message) {
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(writer != null) {
				writer.close();
			}
		}
		
	}
	
}
