package org.sebi.ebclient;


import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class VertxServer {

    public VertxServer() {
        Vertx vertx = Vertx.vertx();

        vertx.eventBus().consumer("hello", (Message<JsonObject> msg) -> {
            System.out.println(msg.body().getString("value"));
            msg.reply(new JsonObject().put("value", "Hello " + msg.body().getString("value")));
        });

        vertx.eventBus().consumer("echo",
                (Message<JsonObject> msg) -> msg.reply(msg.body()));

        TcpEventBusBridge bridge = TcpEventBusBridge.create(
                vertx,
                new BridgeOptions()
                        .addInboundPermitted(new PermittedOptions().setAddress("hello"))
                        .addInboundPermitted(new PermittedOptions().setAddress("echo"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("echo"))
                        .addInboundPermitted(new PermittedOptions().setAddress("popo"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("popo"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("hello")));

        bridge.listen(7000, res -> {
            System.out.println("Ready");
//	    	vertx.setPeriodic(1000, _id -> {
//	    		vertx.eventBus().send("gdgfdg", "hello", reply -> {
//    	        if (reply.succeeded()) {
//	    	          CustomMessage replyMessage = (CustomMessage) reply.result().body();
//	    	          System.out.println("Received reply: "+replyMessage.getSummary());
//	    	        } else {
//	    	          System.out.println("No reply from cluster receiver");
//	    	        }
//	    	      });
//	    	    });
        });
    }
}
