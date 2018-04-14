package rws.easierjava.messaging.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

import rws.easierjava.messaging.MessagingException;

public class ConnectionFactory {

	protected final javax.jms.ConnectionFactory connectionFactory;

	public ConnectionFactory(javax.jms.ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Connection getConnection() {
		try {
			return connectionFactory.createConnection();
		} catch (JMSException e) {
			throw new MessagingException(e);
		}
	}

}
