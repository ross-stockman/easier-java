package rws.easierjava.jms;

public class MessagingException extends RuntimeException {

	private static final long serialVersionUID = 5287576161048805912L;

	public MessagingException(String msg, Throwable t) {
		super(msg, t);
	}

	public MessagingException(Throwable t) {
		super(t);
	}
}
