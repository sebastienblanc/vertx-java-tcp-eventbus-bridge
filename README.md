# vertx-java-eventbus-bridge

This is a vertx TCP eventbus java cliient implementation.

## Create a new `EventBus`

`EventBus eventBus new EventBus("localhost",7000, errorHandler)`

## Register to a topic

`eventBus.register("myTopic", headers, myHandler)`

## Unregister a topic

`eventBus.unregister("myTopic")`

## Send a message

```

        Message message = new Message();
        message.setAddress("hello");
        message.setReplyAdress("dsadsad");
        message.setBody("{\"value\":\"Message Constructed\"}");
        eventBus.sendMessage(message, new MessageHandler() {
            public void handle(Message responseMessage) {
                //handle response message;
            }

            public void handleError(Message errorMessage) {
                //no
            }

        });

```

## Publish a message

```

        Message message = new Message();
        message.setAddress("hello");
        message.setReplyAdress("dsadsad");
        message.setBody("{\"value\":\"Message Constructed\"}");
        eventBus.publishMessage(message);

```
