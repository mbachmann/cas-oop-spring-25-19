package com.example.demoinitial.client.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * Stand alone WebSocketStompClient.
 *
 */
public final class StompClient {

    private StompClient() {
    }


    public static void main(String[] args) {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
        WebSocketClient client = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        String url = "ws://localhost:8080/broadcast";
        stompClient.connect(url, sessionHandler);
        // Don't close immediately - Type <Enter> to exit
        new Scanner(System.in).nextLine();
    }
}
