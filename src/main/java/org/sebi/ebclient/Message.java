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
    private String body;
    private String errorMessage;
    private boolean error;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String toJsonString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        if(getType() != null){
            stringBuffer.append("\"type\":\"" + getType() + "\",");
        }
        if(getAddress() != null){
            stringBuffer.append("\"address\":\"" + getAddress() + "\"");
        }
        if(getType().equals("send") || getType().equals("publish")) {

            if (getReplyAdress() != null) {
                stringBuffer.append(",\"replyAddress\":\"" + getReplyAdress() + "\"");
            }
            if (getBody() != null) {
                stringBuffer.append(",\"body\":" + getBody());
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    public static Message toMessage(String jsonMessage) {
        Message message = new Message();
        Map<String, String> map = new HashMap();
        //Do we have an error ?
        if(jsonMessage.indexOf("err") == 9 ){
            message.setError(true);
        }
        else {
            String[] firstPart = jsonMessage.substring(1,jsonMessage.lastIndexOf("headers")-1).split(",");
            toMap(map, firstPart);
            String[] headers = jsonMessage.substring(jsonMessage.lastIndexOf("headers")+10,jsonMessage.indexOf("body")-3).split(",");
            if(!"".equals(headers[0])){
                message.setHeaders(new HashMap<String, String>());
                toMap(message.getHeaders(), headers);
            }

            map.put("body",jsonMessage.substring(jsonMessage.indexOf("body")+6,jsonMessage.length()-1));
            message.setType(map.get("type"));
            message.setReplyAdress(map.get("replyAdress"));
            message.setAddress(map.get("address"));
        }


        return message;
    }

    private static void toMap(Map<String, String> map, String[] entries) {
        for(String entry: entries){
            String[] data = entry.split(":");
            String key = data[0].substring(data[0].indexOf('"')+1,data[0].lastIndexOf('"'));
            String value = data[1].substring(data[1].indexOf('"')+1,data[1].lastIndexOf('"'));;
            map.put(key,value);
        }
    }
}
