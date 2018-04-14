package rws.easierjava.core.jdbc;

import java.sql.Connection;

/**
 * Factory class to create datasource objects for various jdbc compatible
 * databases
 */
public abstract class DataSource {

	public abstract Connection getConnection();

	protected static class DataSourceException extends RuntimeException {

		private static final long serialVersionUID = 7030044874788001394L;

		public DataSourceException(Throwable t) {
			super(t);
		}
	}

}
