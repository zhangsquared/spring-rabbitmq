package demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Receive {
    private final static String QUEUE_NAME = "hello";
    private final static String HOSTNAME = "localhost";

    public static void run() throws Exception {
        // connect to server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declare a channel
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // async get message
        // DefaultConsumer: provide a callback in the form of an object
        // that will buffer the messages until we're ready to use them.
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };

        // Manual message acknowledgments are turned on by default.
        // Here we explicitly turned them off via the autoAck=true flag.
        channel.basicConsume(QUEUE_NAME, true, consumer);

        // question: no need to close channel and connection?
        // question: heartbeat?
    }
}
