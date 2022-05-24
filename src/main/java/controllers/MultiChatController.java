package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dao.MultichatDAO;
import dto.MultichatDTO;

@ServerEndpoint("/multi.chat")
public class MultiChatController {
	
	MultichatDAO dao = MultichatDAO.getInstance();
	
	private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());
	private static Pattern pattern = Pattern.compile("^\\{\\{.*?\\}\\}");

	@OnOpen
	public void handleOpen(Session userSession) {
		sessionUsers.add(userSession);
		System.out.println("고유번호" + userSession.getId());
		System.out.println("client is now connected...");
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		String name = "anonymous";
		Matcher matcher = pattern.matcher(message);

		if (matcher.find()) {
			name = matcher.group();
		}

		final String msg = message.replaceAll(pattern.pattern(), "");
		final String username = name.replaceFirst("^\\{\\{", "").replaceFirst("\\}\\}$", "");

		// multichat 테이블에 저장
		try {
		int result = dao.insert(new MultichatDTO(0, username, msg));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		sessionUsers.forEach(session -> {
			// 리스트에 있는 세션과 메시지를 보낸 세션이 같으면 메시지 송신할 필요없다.
			if (session == userSession) {
				return;
			}
			try {
				// 리스트에 있는 모든 세션(메시지 보낸 유저 제외)에 메시지를 보낸다. (형식: 유저명 => 메시지)
				session.getBasicRemote().sendText(username + "!%!" + msg);
			} catch (IOException e) {
				// 에러가 발생하면 콘솔에 표시한다.
				e.printStackTrace();
			}
		});
	}

	// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose(Session userSession) {
		// session 리스트로 접속 끊은 세션을 제거한다.
		sessionUsers.remove(userSession);
		// 콘솔에 접속 끊김 로그를 출력한다.
		System.out.println("client is now disconnected...");
	}
	
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}

}
