package young.websocket.chatting.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
	private String sender;
	private String receiver;
	private String content;
	private String userColor;
}
