package rws.easierjava.drivers.jdbc.mysql;

import rws.easierjava.core.jdbc.DataSource;
import rws.easierjava.core.jdbc.DriverDataSource;

public class MysqlDataSource extends DriverDataSource {

	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	public MysqlDataSource(String connectionString, String username, String password, String driver) {
		super(connectionString, username, password, driver);
	}

	/**
	 * Factory method to create mysql datasource
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource createDataSource(String connectionString, String username, String password) {
		return new MysqlDataSource(connectionString, username, password, MYSQL_DRIVER);
	}

}
