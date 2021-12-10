package com.example.demoinitial.web.websocket;

import com.example.demoinitial.web.dto.ChatMessageDto;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketBroadcastController implements HasLogger {

    @GetMapping("/stomp-broadcast")
    public String getWebSocketBroadcast() {
        return "stomp-broadcast";
    }

    @MessageMapping("/broadcast")
    @SendTo("/topic/messages")
    public ChatMessageDto send(ChatMessageDto chatMessage) throws Exception {
        getLogger().info("Broadcast Message Received : " + chatMessage.getText());
        return new ChatMessageDto(chatMessage.getFrom(), chatMessage.getText(), "ALL");
    }
}
