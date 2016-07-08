package kr.co.hucloud.utilities.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 사용을 권장하지 않는다.
 * @author mcjan
 *
 */
@Deprecated
public class SessionUtil {

	public static final String LOGIN_MEMBER_KEY = "_MEMBER_";
	
	public static <T> void login(MultipartHttpServletRequest request, String key, T value) {
		login((HttpServletRequest) request, key, value);
	}
	
	public static <T> void login(HttpServletRequest request, String key, T value) {
		login(request.getSession(), key, value);
	}
	
	public static <T> void login(HttpSession session, String key, T value) {
		session.setAttribute(key, value);
	}
	
	public static <T> T getLogin(MultipartHttpServletRequest request, String key) {
		return getLogin((HttpServletRequest) request, key);
	}
	
	public static <T> T getLogin(HttpServletRequest request, String key) {
		return getLogin(request.getSession(), key);
	}
	
	public static <T>T getLogin(HttpSession session, String key) {
		Object obj = session.getAttribute(key);
		
		if ( obj == null ) {
			return null;
		}
		
		return (T) obj;
	}
	
	public static boolean wasLogin(MultipartHttpServletRequest request, String key) {
		return wasLogin((HttpServletRequest) request, key);
	}
	
	public static boolean wasLogin(HttpServletRequest request, String key) {
		return wasLogin(request.getSession(), key);
	}
	
	public static boolean wasLogin(HttpSession session, String key) {
		return session.getAttribute(key) != null;
	}
	
	public static <T> void setValue(MultipartHttpServletRequest request, String key, T value) {
		login((HttpServletRequest) request, key, value);
	}
	
	public static <T> void setValue(HttpServletRequest request, String key, T value) {
		login(request.getSession(), key, value);
	}
	
	public static <T> void setValue(HttpSession session, String key, T value) {
		login(session, key, value);
	}
	
	public static <T> T getValue(MultipartHttpServletRequest request, String key) {
		return getLogin((HttpServletRequest) request, key);
	}
	
	public static <T> T getValue(HttpServletRequest request, String key) {
		return getLogin(request.getSession(), key);
	}
	
	public static <T> T getValue(HttpSession session, String key) {
		T t = (T) session.getAttribute(key);
		
		if ( t == null ) {
			return null;
		}
		
		return t;
	}
	
}
