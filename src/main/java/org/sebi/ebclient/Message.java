package org.sebi.ebclient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sblanc on 3/12/17.
 */
public class Message {
    private String type;
    private String address;
    private String replyAdress;
    private Object body;
    private String message;
    private Map<String, String> headers;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReplyAdress() {
        return replyAdress;
    }

    public void setReplyAdress(String replyAdress) {
        this.replyAdress = replyAdress;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String errorMessage) {
        this.message = message;
    }

    public boolean isError() {
        return "err".equals(getType());
    }
}
