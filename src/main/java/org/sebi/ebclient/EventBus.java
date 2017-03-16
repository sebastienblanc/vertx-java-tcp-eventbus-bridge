package org.sebi.ebclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by sblanc on 3/12/17.
 */
public class EventBus {
    public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();
    final Socket clientSocket;
    final DataOutputStream outToServer;
    final DataInputStream inFromServer;
    private Map<String,MessageHandler> handlers;
    private MessageHandler errorHandler;
    private Gson gson;

    final Thread inThread = new Thread() {
        @Override
        public void run() {
            Scanner in = null;
            try {

                while (true) {
                    int count = inFromServer.readInt();
                    byte[] buffer = new byte[count];
                    inFromServer.readFully(buffer);
                    String jsonString = new String(buffer);
                    //Message message = Message.toMessage(jsonString);
                    Message message = toMessage(jsonString);
                    if(message.isError()){
                        errorHandler.handle(message);
                    }
                    else {
                        MessageHandler handler = handlers.get(message.getAddress());
                        handler.handle(message);
                    }

                }

            } catch (Exception e) {
                  e.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        };
    };

    public EventBus(String host, int port, MessageHandler errorHandler) throws IOException {
       clientSocket = new Socket(host, port);
       outToServer = new DataOutputStream(clientSocket.getOutputStream());
       inFromServer = new DataInputStream(clientSocket.getInputStream());
       handlers = new HashMap<String, MessageHandler>();
       this.errorHandler = errorHandler;
       gson = new GsonBuilder().disableHtmlEscaping().create();
       inThread.start();
    }



    public static ByteBuffer stringToByteBuffer(String msg){
        try{

            return encoder.encode(CharBuffer.wrap(msg));
        }catch(Exception e){e.printStackTrace();}
        return null;
    }

    public void sendMessage(Message message, MessageHandler responseHandler) throws IOException{
       message.setType("send");
       if("".equals(message.getReplyAdress())){
           message.setReplyAdress(UUID.randomUUID().toString());
        }
       handlers.put(message.getReplyAdress(),responseHandler);
       sendFrame(message);
    }

    public void publishMessage(Message message) throws IOException{
        message.setType("publish");
        sendFrame(message);
    }

    public void register(String address, Map<String,String> headers,  MessageHandler handler) throws IOException{
        handlers.put(address,handler);
        Message registerMessage = createMessage("register",address,headers);
        sendFrame(registerMessage);
    }

    public void unregister(String address, Map<String,String> headers) throws IOException{
        handlers.remove(address);
        Message unregisterMessage = createMessage("unregister",address,headers);
        sendFrame(unregisterMessage);
    }

    private Message createMessage(String type, String address, Map<String,String> headers) {
        Message registerMessage = new Message();
        registerMessage.setType(type);
        registerMessage.setAddress(address);
        registerMessage.setHeaders(headers);
        return registerMessage;
    }

    private void sendFrame(Message message) throws IOException{
        String jsonString = gson.toJson(message);
        outToServer.writeInt(jsonString.getBytes().length);
        outToServer.write(jsonString.getBytes());
    }

    private Message toMessage(String jsonMessage) {
        return gson.fromJson(jsonMessage, Message.class);
    }

}
