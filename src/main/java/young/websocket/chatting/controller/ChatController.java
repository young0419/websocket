package young.websocket.chatting.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import young.websocket.chatting.vo.ChatMessage;

@Controller
public class ChatController {

	private Map<String, HttpSession> userList = Collections.synchronizedMap(new HashMap<String, HttpSession>());

	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping("/")
	public String mainPage() {
		return "main";
	}

	@RequestMapping("/joinchat")
	public String goLobby(@RequestParam String userName, HttpSession session, Model model) {
		session.setAttribute("userName", userName);
		userList.put(userName, session);
		return "chat";
	}

	@RequestMapping("/getUserList")
	@ResponseBody
	public Map<String, List<String>> getUserList() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("userList", new ArrayList<String>(userList.keySet()));

		return map;
	}

	@MessageMapping("/sendChat")
	public void chatting(ChatMessage message, StompHeaderAccessor headerAccessor) {
		String sender = (String) headerAccessor.getSessionAttributes().get("userName");
		message.setSender(sender);
		template.convertAndSend("/chat/public", message);
	}
	
	@MessageMapping("/sendWisper")
	@SendToUser("/user/{sessionId}/chat/private")
	public ChatMessage sendWisper(ChatMessage message, StompHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId) {
		String sender = (String) headerAccessor.getSessionAttributes().get("userName");
		String content = message.getContent();
		String receiver = content.substring(1, content.indexOf(":"));
		content = content.substring(content.indexOf(":"));

		message.setSender(sender);
		message.setReceiver(receiver);
		message.setContent(content);
		return message;
	}

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String sender = (String) headerAccessor.getSessionAttributes().get("userName");
		ChatMessage message = new ChatMessage();
		message.setSender(sender);
		message.setContent(sender + " 입장");

		template.convertAndSend("/chat/join", message);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("userName");
		if (username != null) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSender(username);
			chatMessage.setContent(username + " 퇴장");
			userList.remove(username);
			template.convertAndSend("/chat/leave", chatMessage);
		}
	}

}