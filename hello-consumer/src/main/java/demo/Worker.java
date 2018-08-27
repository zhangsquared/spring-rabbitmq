package demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker {

    private final static String QUEUE_NAME = "task-queue";
    private final static String HOSTNAME = "localhost";

    public static void run(int workerID) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // When RabbitMQ quits or crashes it will forget the queues and messages
        // mark queue durable so tasks won't be lost even if RabbitMQ server stops
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages for worker " + workerID + ". To exit press CTRL+C");

        // This tells RabbitMQ not to give more than one message to a worker at a time.
        // Don't dispatch a new message to a worker until it has processed and acknowledged the previous one.
        // Instead, it will dispatch it to the next worker that is not still busy.
        int preFetchCount = 1;
        channel.basicQos(preFetchCount);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "' for worker "+ workerID);
                try {
                    doWork(message);
                } finally {
                    System.out.println(" [x] Done for worker " + workerID);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // turn on manual message acknowledgment
        // set autoAck to be false
        // send a proper acknowledgment from the worker, once we're done with a task.
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    // We'll take the number of dots in the string as its complexity
    // every dot will account for one second of "work"
    // pretending we're busy - by using the Thread.sleep() function
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
