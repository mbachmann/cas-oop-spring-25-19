package com.example.demoinitial.web.websocket;

import com.example.demoinitial.web.api.request.ChatMessageRequest;
import com.example.demoinitial.utils.HasLogger;
import com.example.demoinitial.web.api.response.ChatMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketBroadcastController implements HasLogger {

    @GetMapping("/stomp-broadcast")
    public String getWebSocketBroadcast(Model model,HttpServletRequest request) {
        model.addAttribute("servletPath", request.getServletPath());
        model.addAttribute("serverName", request.getServerName());
        model.addAttribute("serverPort", request.getServerPort());
        return "stomp-broadcast";
    }

    @MessageMapping("/broadcast")
    @SendTo("/topic/messages")
    public ChatMessageResponse send(ChatMessageRequest chatMessageRequest) {
        getLogger().info("Broadcast Message Received : " + chatMessageRequest.getText());
        return new ChatMessageResponse(chatMessageRequest.getFrom(), chatMessageRequest.getText(), "ALL");
    }
}
