package rws.easierjava.drivers.jms.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import rws.easierjava.messaging.jms.ConnectionFactory;

public class ActivemqConnectionFactory extends ConnectionFactory {

	public ActivemqConnectionFactory(javax.jms.ConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	public static ConnectionFactory createConnectionFactory(String brokerUrl) {
		return new ActivemqConnectionFactory(new ActiveMQConnectionFactory(brokerUrl));
	}

}
