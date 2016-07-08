package kr.co.hucloud.utilities.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Session 을 관리하기 위한 Utility
 * 분산환경에서는 사용을 권장하지 않는다.
 * 미완성
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 *
 */
public class SessionStore {

	public static final int ADD_SUCCESS = 0;
	public static final int REMOVE_SUCCESS = 1;
	public static final int ALREADY_EXISTS = -1;
	public static final int SESSION_NOT_EXISTS = -2;
	
	private volatile static Map<String, HttpSession> sessions;
	
	static {
		sessions = new HashMap<String, HttpSession>();
	}
	
	public synchronized static int addSession(String userId, HttpSession session) {
		if ( !isExistsSession(userId) ) {
			sessions.put(userId, session);
			return ADD_SUCCESS;
		}
		return ALREADY_EXISTS;
	}
	
	public synchronized static HttpSession getSession(String userId) {
		if ( isExistsSession(userId) ) {
			return sessions.get(userId);
		}
		return null;
	}
	
	public synchronized static int removeSession(String userId) {
		if ( isExistsSession(userId) ) {
			sessions.remove(userId);
			return REMOVE_SUCCESS;
		}
		return SESSION_NOT_EXISTS;
	}
	
	public synchronized static boolean isExistsSession(String userId) {
		return sessions.containsKey(userId);
	}
	
	public synchronized static int getSessionSize() {
		return sessions.size();
	}
	
	
}
