package rws.easierjava.messaging;

public class MessagingException extends RuntimeException {

	private static final long serialVersionUID = 5287576161048805912L;

	public MessagingException(String msg, Throwable t) {
		super(msg, t);
	}
}
