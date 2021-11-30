package com.example.demoinitial.client.websocket;

import com.example.demoinitial.domain.dto.ChatMessageDto;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

/**
 * This class is an implementation for <code>StompSessionHandlerAdapter</code>.
 * Once a connection is established, We subscribe to /topic/messages and
 * send a sample message to server.
 *
 */
public class MyStompSessionHandler extends StompSessionHandlerAdapter implements HasLogger {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        getLogger().info("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);
        getLogger().info("Subscribed to /topic/messages");
        session.send("/app/broadcast", getSampleMessage());
        getLogger().info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        getLogger().error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessageDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessageDto msg = (ChatMessageDto) payload;
        getLogger().info("Received : " + msg.getText() + " from : " + msg.getFrom());
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private ChatMessageDto getSampleMessage() {
        ChatMessageDto msg = new ChatMessageDto();
        msg.setFrom("JavaStompClient");
        msg.setText("Here I am!!");
        return msg;
    }
}

