package rws.easierjava.drivers.jdbc;

import rws.easierjava.core.jdbc.DataSource;
import rws.easierjava.drivers.jdbc.hive.HiveDataSource;
import rws.easierjava.drivers.jdbc.mysql.MysqlDataSource;
import rws.easierjava.drivers.jdbc.oracle.OracleDataSource;

public class DataSourceFactory {

	public static DataSource createOracleDataSource(String connectionString, String username, String password) {
		return OracleDataSource.createDataSource(connectionString, username, password);
	}

	public static DataSource createHiveDataSource(String connectionString, String username, String password) {
		return HiveDataSource.createDataSource(connectionString, username, password);
	}

	public static DataSource createMysqlDataSource(String connectionString, String username, String password) {
		return MysqlDataSource.createDataSource(connectionString, username, password);
	}

}
