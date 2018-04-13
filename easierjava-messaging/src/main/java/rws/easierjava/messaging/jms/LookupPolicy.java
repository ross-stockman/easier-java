package rws.easierjava.messaging.jms;

import javax.jms.Destination;

@FunctionalInterface
public interface LookupPolicy {
	Destination lookup();
}
