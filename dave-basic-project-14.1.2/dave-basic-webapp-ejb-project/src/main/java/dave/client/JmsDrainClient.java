package dave.client;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsDrainClient {

    private static final String CONNECTION_FACTORY_JNDI = "myConnectionFactory";
    private static final String QUEUE_JNDI = "myJmsQueue";
    private static final String PROVIDER_URL = "t3://127.0.0.1:7001";
    private static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";

    public static void main(String[] args) {
        JmsDrainClient client = new JmsDrainClient();
        try {
            client.drainQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drainQueue() throws NamingException, JMSException {
        Context ctx = getInitialContext();
        ConnectionFactory connectionFactory = null;
        Queue queue = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;

        try {
            System.out.println("Looking up ConnectionFactory: " + CONNECTION_FACTORY_JNDI);
            connectionFactory = (ConnectionFactory) ctx.lookup(CONNECTION_FACTORY_JNDI);

            System.out.println("Looking up Queue: " + QUEUE_JNDI);
            queue = (Queue) ctx.lookup(QUEUE_JNDI);

            System.out.println("Creating Connection...");
            connection = connectionFactory.createConnection();
            connection.start(); // Must start connection to receive messages
            
            System.out.println("Creating Session...");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("Creating Consumer...");
            consumer = session.createConsumer(queue);

            System.out.println("Draining queue...");
            int count = 0;
            while (true) {
                // Wait 1 second for a message
                Message message = consumer.receive(1000);
                if (message == null) {
                    break;
                }
                count++;
                if (message instanceof TextMessage) {
                    System.out.println("Removed message: " + ((TextMessage) message).getText());
                } else {
                    System.out.println("Removed message of type: " + message.getClass().getName());
                }
            }
            
            System.out.println("Queue drained. Total messages removed: " + count);
            
        } finally {
            if (consumer != null) try { consumer.close(); } catch (JMSException e) {}
            if (session != null) try { session.close(); } catch (JMSException e) {}
            if (connection != null) try { connection.close(); } catch (JMSException e) {}
            if (ctx != null) try { ctx.close(); } catch (NamingException e) {}
        }
    }

    private Context getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        env.put(Context.SECURITY_PRINCIPAL, "weblogic");
        env.put(Context.SECURITY_CREDENTIALS, "weblogic123");
        return new InitialContext(env);
    }
}
