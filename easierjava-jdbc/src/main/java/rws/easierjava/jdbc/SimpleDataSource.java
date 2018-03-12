package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleDataSource extends DataSource {

	private final javax.sql.DataSource dataSource;

	/**
	 * private constructor
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 */
	protected SimpleDataSource(javax.sql.DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}
}
