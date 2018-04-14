package rws.easierjava.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

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
			connection = connectionFactory.getConnection();
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

	private void close(Session closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				LOGGER.warn("Failed to close.", e);
			}
		}
	}

	private void close(Connection closeable) {
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
