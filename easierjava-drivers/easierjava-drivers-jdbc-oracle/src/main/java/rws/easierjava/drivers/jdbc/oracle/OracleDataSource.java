package rws.easierjava.drivers.jdbc.oracle;

import java.sql.SQLException;

import rws.easierjava.core.jdbc.DataSource;
import rws.easierjava.core.jdbc.SimpleDataSource;

public class OracleDataSource extends SimpleDataSource {

	public OracleDataSource(javax.sql.DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * Factory method to create oracle datasource
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource createDataSource(String connectionString, String username, String password) {
		try {
			oracle.jdbc.pool.OracleDataSource oracleDataSource = new oracle.jdbc.pool.OracleDataSource();
			oracleDataSource.setURL(connectionString);
			oracleDataSource.setUser(username);
			oracleDataSource.setPassword(password);
			return new OracleDataSource(oracleDataSource);
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

}
