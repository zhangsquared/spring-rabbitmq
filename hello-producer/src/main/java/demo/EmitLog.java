package demo;

import com.rabbitmq.client.*;


public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";
    private final static String HOSTNAME = "localhost";

    public static void run(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // The core idea in the messaging model in RabbitMQ is
        // that the producer never sends any messages directly to a queue.
        // Actually, quite often the producer doesn't even know if a message
        // will be delivered to any queue at all.
        // Instead, the producer can only send messages to an exchange.
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // FANOUT: it just broadcasts all the messages it receives to all the queues it knows.

        String message = getMessage(argv);

        // publish to our named exchange
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes()); // non-persistent msg
        System.out.println(" [x] Sent '" + message + "' to exchange " + EXCHANGE_NAME);

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings){
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
