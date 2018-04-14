package rws.easierjava.messaging.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rws.easierjava.messaging.MessagingException;

public class Consumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private MessageConsumer messageConsumer;
	private Destination destination;
	private LookupPolicy lookupPolicy;

	public Consumer(ConnectionFactory connectionFactory, LookupPolicy lookupPolicy) {
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
			messageConsumer = session.createConsumer(destination);
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

	public String receiveMessage(long timeout) {
		try {
			TextMessage message = (TextMessage) messageConsumer.receive(timeout);
			if (message != null) {
				return message.getText();
			} else {
				return null;
			}
		} catch (JMSException e) {
			throw new MessagingException("Failed to send message.", e);
		}
	}

	public String receiveMessage() {
		try {
			TextMessage message = (TextMessage) messageConsumer.receive();
			if (message != null) {
				return message.getText();
			} else {
				return null;
			}
		} catch (JMSException e) {
			throw new MessagingException("Failed to send message.", e);
		}
	}
}
