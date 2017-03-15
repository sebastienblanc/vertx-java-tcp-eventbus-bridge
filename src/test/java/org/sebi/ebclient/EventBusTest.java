package org.sebi.ebclient;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by sblanc on 3/11/17.
 */
public class EventBusTest {
    EventBus eventBus;

    @Before
    public void setup() throws IOException{
      //  VertxServer vertxServer = new VertxServer();
        eventBus = new EventBus("localhost", 7000, new MessageHandler() {
            @Override
            public void handle(Message message) {
               System.out.println("Something went wrong : " + message.getBody());
            }
        });

    }

    @BeforeClass
    public static void startVertxServer() {

    }

    @Test
    public void sendRegisterMessageTest() throws IOException, InterruptedException {

        eventBus.register("popo", null, new MessageHandler() {
            public void handle(Message message) {

            }

            public void handleError(Message errorMessage) {

            }
        });
        Thread.sleep(500);
    }

    @Test
    public void sendMessageTest() throws IOException, InterruptedException {
        Message message = new Message();
        message.setAddress("hello");
        message.setReplyAdress("dsadsad");
        message.setBody("{\"value\":\"Message Constructed\"}");
        eventBus.register("hello", null,new MessageHandler() {
            public void handle(Message responseMessage) {
                System.out.println(responseMessage.getBody());
                assertEquals(responseMessage.getType(),"message");
            }
        });
        eventBus.publishMessage(message);
        Thread.sleep(500);
    }

    @Test
    public void sendAccessDeniedMessageTest() throws IOException, InterruptedException {
        Message message = new Message();
        message.setType("send");
        message.setAddress("lala");
        message.setReplyAdress("dsadsad");
        message.setBody("{\"value\":\"Message Constructed\"}");
        eventBus.sendMessage(message, new MessageHandler() {
            public void handle(Message responseMessage) {
                fail();
            }
        });
        Thread.sleep(500);
    }



}
