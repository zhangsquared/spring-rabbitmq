package demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";
    private final static String HOSTNAME = "localhost";

    public static void run() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // create a non-durable, exclusive, auto-delete queue with a generated name
        String queueName = channel.queueDeclare().getQueue();
        // we need to tell the exchange to send messages to our queue
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        // The messages will be lost if no queue is bound to the exchange yet,
        // in this case it is okay
        // if no consumer is listening yet we can safely discard the message.
        // so in order to get message, we need to launch consumer before producer

        System.out.println(" [*] Get queueName: " + queueName);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
            }
        };

        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, consumer);
    }

}
