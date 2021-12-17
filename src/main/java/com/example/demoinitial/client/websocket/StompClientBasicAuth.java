package com.example.demoinitial.client.websocket;

import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Stand alone WebSocketStompClient.
 */
public class StompClientBasicAuth {

    public static void main(String[] args) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders webSocketHeaders = createWebSocketHeaders("admin@example.com", "admin");
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        String URL = "ws://localhost:8080/broadcast";
        stompClient.connect(URL, webSocketHeaders, sessionHandler);
        // Don't close immediately - Type <Enter> to exit
        new Scanner(System.in).nextLine();
    }

    static WebSocketHttpHeaders createWebSocketHeaders(String username, String password) {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", getBase64Auth(username, password));
        return headers;
    }

    static StompHeaders createStompHeaders(String username, String password) {
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", getBase64Auth(username, password));
        return headers;

    }

    private static String getBase64Auth (String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));

        return "Basic " + new String(encodedAuth);
    }

}
