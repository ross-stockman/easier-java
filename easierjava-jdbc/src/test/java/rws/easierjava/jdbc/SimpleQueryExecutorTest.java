package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rws.easierjava.test.TestHelper;

public class SimpleQueryExecutorTest {
	@Mock
	private Connection connection;

	@Mock
	private PreparedStatement statement;

	@Mock
	private ResultSet resultSet;

	@Mock
	private DataSource dataSource;

	private SimpleQueryExecutor<Integer> systemUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		systemUnderTest = new SimpleQueryExecutor<>(dataSource);
	}

	@Test
	public void testQueryNoParams() throws Throwable {
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getInt(1)).thenReturn(10).thenReturn(20).thenReturn(30);

		RowMapper<Integer> rowMapper = r -> r.getInt(1);

		List<Integer> result = systemUnderTest.query("query", null, rowMapper);
		Assert.assertThat(result.size(), Is.is(3));
		TestHelper.assertAll(() -> Assert.assertThat(result.get(0), Is.is(10)),
				() -> Assert.assertThat(result.get(1), Is.is(20)), () -> Assert.assertThat(result.get(2), Is.is(30)));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();

	}

	@Test
	public void testQueryNoParamsNoResults() throws SQLException {
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);

		RowMapper<Integer> rowMapper = r -> {
			Assert.fail("should not be reached!");
			return null;
		};

		List<Integer> result = systemUnderTest.query("query", null, rowMapper);
		Assert.assertThat(result.size(), Is.is(0));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();

	}

	@Test
	public void testQueryOneParam() throws Throwable {
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getInt(1)).thenReturn(10).thenReturn(20).thenReturn(30);

		RowMapper<Integer> rowMapper = r -> r.getInt(1);

		List<Integer> result = systemUnderTest.query("query", new Object[] { 1, 2 }, rowMapper);
		Assert.assertThat(result.size(), Is.is(3));
		TestHelper.assertAll(() -> Assert.assertThat(result.get(0), Is.is(10)),
				() -> Assert.assertThat(result.get(1), Is.is(20)), () -> Assert.assertThat(result.get(2), Is.is(30)));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();

	}

}
