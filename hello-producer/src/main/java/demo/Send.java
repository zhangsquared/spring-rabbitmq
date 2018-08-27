package demo;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


public class Send {
    private final static String QUEUE_NAME = "hello";
    private final static String HOSTNAME = "localhost";

    public static void run() throws Exception {

        // create a connection to the server (running on docker)
        // The connection abstracts the socket connection,
        // and takes care of protocol version negotiation and authentication and so on for us.
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declare a queue
        // if it doesn't exist already, it will be created
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // create a message
        String message = "Hello World, ZZ!";
        // message content is a byte array
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}