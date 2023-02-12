package com.example.demoinitial.web.api.response;


import com.example.demoinitial.utils.StringUtils;

public class ChatMessageResponse {

    private String from;
    private String text;
    private String recipient;
    private String time;

    public ChatMessageResponse() {}

    public ChatMessageResponse(String from, String text, String recipient) {
        this.from = from;
        this.text = text;
        this.recipient = recipient;
        this.time = StringUtils.getCurrentTimeStamp();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
