package rws.easierjava.core.jdbc;

import java.sql.Connection;

/**
 * Factory class to create datasource objects for various jdbc compatible
 * databases
 */
public abstract class DataSource {

	private static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";

	public abstract Connection getConnection();

	/**
	 * Factory method to create hive datasource
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource createHiveDataSource(String connectionString, String username, String password) {
		return new DriverDataSource(connectionString, username, password, HIVE_DRIVER);
	}

	protected static class DataSourceException extends RuntimeException {

		private static final long serialVersionUID = 7030044874788001394L;

		public DataSourceException(Throwable t) {
			super(t);
		}
	}

}
