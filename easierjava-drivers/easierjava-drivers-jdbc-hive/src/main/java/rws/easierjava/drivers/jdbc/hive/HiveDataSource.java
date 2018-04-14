package rws.easierjava.drivers.jdbc.hive;

import rws.easierjava.core.jdbc.DataSource;
import rws.easierjava.core.jdbc.DriverDataSource;

public class HiveDataSource extends DriverDataSource {

	private static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";

	public HiveDataSource(String connectionString, String username, String password, String driver) {
		super(connectionString, username, password, driver);
	}

	/**
	 * Factory method to create hive datasource
	 * 
	 * @param connectionString
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource createDataSource(String connectionString, String username, String password) {
		return new HiveDataSource(connectionString, username, password, HIVE_DRIVER);
	}
}
