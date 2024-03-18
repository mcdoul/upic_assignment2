package com.upic.server.sevlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer {
    private static final String QUEUE_NAME = "SkierServletPostQueue";
    private static final Integer NUM_THREADS = 10;

    public Consumer() {
    }

    public static void main(String[] args) throws Exception {
        final Gson gson = new Gson();
        ConnectionFactory factory = new ConnectionFactory();
        final ConcurrentHashMap<Integer, List<JsonObject>> map = new ConcurrentHashMap();
        factory.setHost("35.90.118.182");
        factory.setPort(5672);
        factory.setUsername("mario");
        factory.setPassword("mariobar");
        System.out.println("try to connect");
        final Connection connection = factory.newConnection();
        System.out.println("connection successful");
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Channel channel = connection.createChannel();
                    channel.queueDeclare("SkierServletPostQueue", false, false, false, (Map)null);
                    channel.basicQos(30);
                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), "UTF-8");
                        JsonObject jsonObject = (JsonObject)gson.fromJson(message, JsonObject.class);
                        Integer key = Integer.valueOf(String.valueOf(jsonObject.get("skierID")));
                        if (map.contains(key)) {
                            ((List)map.get(key)).add(jsonObject);
                        } else {
                            List<JsonObject> value = new ArrayList();
                            value.add(jsonObject);
                            map.put(key, value);
                        }

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    };
                    channel.basicConsume("SkierServletPostQueue", false, deliverCallback, (consumerTag) -> {
                    });
                } catch (IOException var3) {
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, (String)null, var3);
                }

            }
        };
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        for(int i = 0; i < NUM_THREADS; ++i) {
            pool.execute(runnable);
        }
    }

}
