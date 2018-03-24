package rws.easierjava.mapper;

public class ParseErrorException extends RuntimeException {

	private static final long serialVersionUID = 7237824585824839818L;

	public ParseErrorException(Throwable t) {
		super("parse error", t);
	}

}
