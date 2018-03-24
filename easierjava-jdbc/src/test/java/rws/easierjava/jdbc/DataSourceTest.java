package rws.easierjava.jdbc;

import org.junit.Assert;
import org.junit.Test;

public class DataSourceTest {

	@Test
	public void testCreateOracleDataSource() {
		DataSource ds = DataSource.createOracleDataSource("connectionString", "username", "password");
		Assert.assertTrue(ds instanceof SimpleDataSource);
	}

	@Test
	public void testCreateHiveDataSource() {
		DataSource ds = DataSource.createHiveDataSource("connectionString", "username", "password");
		Assert.assertTrue(ds instanceof DriverDataSource);
	}
}
