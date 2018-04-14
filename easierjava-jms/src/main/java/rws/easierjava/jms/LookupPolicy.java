package rws.easierjava.jms;

import javax.jms.Destination;

@FunctionalInterface
public interface LookupPolicy {
	Destination lookup();
}
