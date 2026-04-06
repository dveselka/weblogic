package dave.client;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsTestClient {

    // JNDI names as configured in your python script
    private static final String CONNECTION_FACTORY_JNDI = "myConnectionFactory";
    private static final String QUEUE_JNDI = "myJmsQueue";
    private static final String PROVIDER_URL = "t3://127.0.0.1:7001";
    private static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";

    public static void main(String[] args) {
        JmsTestClient client = new JmsTestClient();
        try {
            client.sendMessage("Hello WebLogic JMS World!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String messageText) throws NamingException, JMSException {
        Context ctx = getInitialContext();
        ConnectionFactory connectionFactory = null;
        Queue queue = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            System.out.println("Looking up ConnectionFactory: " + CONNECTION_FACTORY_JNDI);
            connectionFactory = (ConnectionFactory) ctx.lookup(CONNECTION_FACTORY_JNDI);

            System.out.println("Looking up Queue: " + QUEUE_JNDI);
            queue = (Queue) ctx.lookup(QUEUE_JNDI);

            System.out.println("Creating Connection...");
            connection = connectionFactory.createConnection();
            
            System.out.println("Creating Session...");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("Creating Producer...");
            producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage(messageText);
            // Set properties required by QueueMDB
            message.setStringProperty("name", "john.doe");
            message.setFloatProperty("amount", 100.50f);
            
            System.out.println("Sending message: " + messageText);
            producer.send(message);
            
            System.out.println("Message sent successfully!");
            
        } finally {
            if (producer != null) try { producer.close(); } catch (JMSException e) {}
            if (session != null) try { session.close(); } catch (JMSException e) {}
            if (connection != null) try { connection.close(); } catch (JMSException e) {}
            if (ctx != null) try { ctx.close(); } catch (NamingException e) {}
        }
    }

    private Context getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        // If authentication is required, default weblogic/weblogic123 is usually fine for dev
        env.put(Context.SECURITY_PRINCIPAL, "weblogic");
        env.put(Context.SECURITY_CREDENTIALS, "weblogic123");
        return new InitialContext(env);
    }
}
