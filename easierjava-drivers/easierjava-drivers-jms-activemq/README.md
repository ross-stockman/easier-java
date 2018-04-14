Sample producer and consumer usage:

```
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import rws.easierjava.drivers.jms.activemq.ActivemqConnectionFactory;
import rws.easierjava.messaging.MessagingException;
import rws.easierjava.messaging.jms.ConnectionFactory;
import rws.easierjava.messaging.jms.Consumer;
import rws.easierjava.messaging.jms.LookupPolicy;
import rws.easierjava.messaging.jms.Producer;

public class Demo {

	private static LookupPolicy lookupPolicy = () -> {
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

	};

	public static void main(String[] args) throws JMSException {
		ConnectionFactory connectionFactory = ActivemqConnectionFactory
				.createConnectionFactory("tcp://localhost:61616");
		Producer producer = new Producer(connectionFactory, lookupPolicy);

		producer.sendMessage("testing1");
		producer.sendMessage("testing2");
		producer.sendMessage("testing3");
		producer.sendMessage("testing4");
		producer.sendMessage("testing5");
		producer.cleanup();

		Consumer consumer = new Consumer(connectionFactory, lookupPolicy);

		System.out.println(consumer.receiveMessage(100));
		System.out.println(consumer.receiveMessage(100));
		System.out.println(consumer.receiveMessage(100));
		System.out.println(consumer.receiveMessage(100));
		System.out.println(consumer.receiveMessage(100));
		System.out.println(consumer.receiveMessage(100));
		consumer.cleanup();

	}
}

```