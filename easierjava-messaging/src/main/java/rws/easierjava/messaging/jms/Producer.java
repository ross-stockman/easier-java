package rws.easierjava.messaging.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rws.easierjava.messaging.MessagingException;

public class Producer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Producer producer = new Producer(connectionFactory, () -> {
			Properties properties = new Properties();
			properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			properties.put("java.naming.provider.url", "tcp://localhost:61616");
			properties.put("queue.demo-queue", "demo-queue");
			InitialContext context;
			try {
				context = new InitialContext(properties);
				return (Destination) context.lookup("demo-queue");
			} catch (NamingException e) {
				throw new MessagingException("Failed to lookup context.", e);
			}

		});
		producer.sendMessage("testing1");
		producer.sendMessage("testing2");
		producer.sendMessage("testing3");
		producer.sendMessage("testing4");
		producer.sendMessage("testing5");
		producer.cleanup();

	}

	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private MessageProducer messageProducer;
	private Destination destination;
	private LookupPolicy lookupPolicy;

	public Producer(ConnectionFactory connectionFactory, LookupPolicy lookupPolicy) {
		this.connectionFactory = connectionFactory;
		this.lookupPolicy = lookupPolicy;
		initialize();
	}

	public void initialize() {
		LOGGER.info("Initializing connection.");
		cleanup();
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = lookupPolicy.lookup();
			messageProducer = session.createProducer(destination);
			connection.start();
		} catch (JMSException e) {
			throw new MessagingException("Failed to connect.", e);
		}
	}

	public void cleanup() {
		close(session);
		close(connection);
	}

	private void close(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				LOGGER.warn("Failed to close.", e);
			}
		}
	}

	public void sendMessage(String msg) {
		try {
			messageProducer.send(session.createTextMessage(msg));
		} catch (JMSException e) {
			throw new MessagingException("Failed to send message.", e);
		}
	}
}
