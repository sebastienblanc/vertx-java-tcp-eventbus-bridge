# vertx-java-eventbus-bridge

This is a vertx TCP eventbus java cliient implementation.

## Create a new `EventBus`

```
EventBus eventBus new EventBus("localhost",7000, new MessageHandler() {
            @Override
            public void handle(Message message) {
               System.out.println("Something went wrong : " + message.getMessage());
             }
           });
```
## Register to a topic

`eventBus.register("myTopic", headers, myHandler)`

## Unregister a topic

`eventBus.unregister("myTopic")`

## Send a message

```

        Message message = new Message();
        message.setAddress("hello");
        message.setReplyAdress("dsadsad");
        Map bodyMap = new HashMap();
        bodyMap.put("value","message constructed");
        message.setBody(bodyMap);
        eventBus.sendMessage(message, new MessageHandler() {
            public void handle(Message responseMessage) {
                //handle response message;
            }
        });

```

## Publish a message

```

        Message message = new Message();
        message.setAddress("hello");
        message.setReplyAdress("dsadsad");
        Map bodyMap = new HashMap();
        bodyMap.put("value","message constructed");
        message.setBody(bodyMap);
        eventBus.publishMessage(message);

```
