package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dao.ChatDAO;
import dto.ChatDTO;
import dao.ChatListDAO;
import dto.ChatListDTO;

@ServerEndpoint("/broadsocket")
public class BroadSocket {

	private static List<Session> userList = Collections.synchronizedList(new ArrayList<>());

	String loginId = null;
	String with = null;

	ChatDAO dao = new ChatDAO();
	ChatListDAO ldao = new ChatListDAO();

	@OnOpen
	public void handleOpen(Session userSession) {
		userList.add(userSession);

		loginId = userSession.getRequestParameterMap().get("loginId").get(0);
		System.out.println(loginId + " is now connected...");
		System.out.println("현재 접속자 수 : " + userList.size());

	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {

		try {

			String[] result = message.split("\\{=\\}");	// result[0]:나 result[1]:상대방 result[2]:메세지

			System.out.println(result[0] + " :" + result[1] + " :" +result[2]);

			//dao.insert(new ChatDTO(0, result[0], result[1], result[2], null,0));


			int count = 0;
			for(Session s : userList) {
				// userList에서 상대 아이디값을 뽑아서 나랑 상대랑 같이 세션에 저장되어있다는게 인증되면 read를 1로 바꾸어도 괜찮지 않을까?

				//				if(s == userSession) {
				//					System.out.println("test");
				//					
				if(s.getRequestParameterMap().get("loginId").get(0).equals(result[1])) {
					try {
						System.out.println("test2");
						s.getBasicRemote().sendText(result[0] + "!%!" + result[2]);

						dao.insert(new ChatDTO(0, result[0], result[1], result[2], null,1));
						count++;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(count == 0) {
				dao.insert(new ChatDTO(0, result[0], result[1], result[2], null,0));
			}
			List<ChatListDTO> list = ldao.isChatExist(result[0], result[1]);

			System.out.println(list.size());

			if(list.get(0).getInvisibleTo().equals("nothing")) {
				ldao.insert(new ChatListDTO(0, result[0], result[1]));
			}else {	// 나간 채팅방에서 다시 같은 상대에게 말을 건다.
				ldao.setNobody(result[0], result[1]);
			}



		}catch (Exception e) {
			e.printStackTrace();

		}
	}

	// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose(Session userSession) {
		// session 리스트로 접속 끊은 세션을 제거한다.
		userList.remove(userSession);
		// 콘솔에 접속 끊김 로그를 출력한다.
		System.out.println("client is now disconnected...");
		System.out.println("현재 접속자 수 : " + userList.size());
	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}

}
