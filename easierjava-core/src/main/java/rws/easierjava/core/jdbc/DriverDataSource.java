package rws.easierjava.core.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import rws.easierjava.core.ClassUtils;

public class DriverDataSource extends DataSource {

	private final String connectionString;
	private final String username;
	private final String password;
	private final String driver;

	/**
	 * private constructor
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 */
	protected DriverDataSource(String connectionString, String username, String password, String driver) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
		this.driver = driver;
	}

	@Override
	public Connection getConnection() {
		register(driver);
		try {
			return DriverManager.getConnection(connectionString, username, password);
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

	private static void register(String driver) {
		try {
			Class.forName(driver, true, ClassUtils.getDefaultClassLoader());
		} catch (ClassNotFoundException e) {
			throw new DriverException(e);
		}
	}

	private static class DriverException extends DataSourceException {

		private static final long serialVersionUID = -5644164974697713298L;

		public DriverException(Throwable t) {
			super(t);
		}
	}

}
