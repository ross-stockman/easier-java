package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Factory class to create datasource objects for various jdbc compatible
 * databases
 */
public abstract class DataSource {

	private static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";

	public abstract Connection getConnection();

	/**
	 * Factory method to create oracle datasource
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource createOracleDataSource(String connectionString, String username, String password) {
		try {
			OracleDataSource oracleDataSource = new OracleDataSource();
			oracleDataSource.setURL(connectionString);
			oracleDataSource.setUser(username);
			oracleDataSource.setPassword(password);
			return new SimpleDataSource(oracleDataSource);
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

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
