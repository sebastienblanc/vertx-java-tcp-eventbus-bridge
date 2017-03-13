package org.sebi.ebclient;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by sblanc on 3/13/17.
 */
public class MessageTest {

    @Test
    public void toMessageTest(){
        String testString = "{\"type\":\"message\",\"address\":\"dsadsad\",\"headers\":{\"myHeader\":\"myHeaderValue\"},\"body\":{\"value\":\"Hello Message Constructed\"}}";
        Message message = Message.toMessage(testString);
        assertEquals(message.getType(),"message");
        assertEquals(message.getHeaders().get("myHeader"),"myHeaderValue");
    }
}
