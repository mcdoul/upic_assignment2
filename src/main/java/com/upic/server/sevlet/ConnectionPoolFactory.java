package com.upic.server.sevlet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ConnectionPoolFactory extends BasePooledObjectFactory<Channel> {
    ConnectionFactory factory = new ConnectionFactory();

    public ConnectionPoolFactory() {
    }

    public Channel create() throws Exception {
        this.factory.setHost("35.90.118.182");
        this.factory.setPort(5672);
        this.factory.setUsername("mario");
        this.factory.setPassword("mariobar");
        Connection connection = this.factory.newConnection();
        return connection.createChannel();
    }

    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject(channel);
    }

    public void destroyObject(PooledObject<Channel> p) throws Exception {
        ((Channel)p.getObject()).close();
    }
}
