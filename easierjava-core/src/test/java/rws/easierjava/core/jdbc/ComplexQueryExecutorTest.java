package rws.easierjava.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rws.easierjava.core.jdbc.ComplexQueryExecutor;
import rws.easierjava.core.jdbc.DataSource;
import rws.easierjava.core.jdbc.ResultSetExtractor;

public class ComplexQueryExecutorTest {

	@Mock
	private Connection connection;

	@Mock
	private PreparedStatement statement;

	@Mock
	private ResultSet resultSet;

	@Mock
	private DataSource dataSource;

	private ComplexQueryExecutor<String> systemUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		systemUnderTest = new ComplexQueryExecutor<>(dataSource);
	}

	@Test
	public void testQueryNoParams() throws SQLException {

		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getInt(1)).thenReturn(10).thenReturn(20).thenReturn(30);

		ResultSetExtractor<String> resultSetExtractor = r -> {
			StringBuilder builder = new StringBuilder();
			while (r.next()) {
				builder.append(r.getInt(1));
			}
			return builder.toString();
		};

		String result = systemUnderTest.query("query", null, resultSetExtractor);
		Assert.assertThat(result, Is.is("102030"));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();
	}

	@Test
	public void testQueryNoParamsNoResults() throws SQLException {

		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);

		ResultSetExtractor<String> resultSetExtractor = r -> {
			StringBuilder builder = new StringBuilder();
			while (r.next()) {
				Assert.fail("should not be reached!");
			}
			return builder.toString();
		};

		String result = systemUnderTest.query("query", null, resultSetExtractor);
		Assert.assertThat(result, Is.is(""));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();
	}

	@Test
	public void testQueryOneParam() throws SQLException {

		Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
		Mockito.when(statement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getInt(1)).thenReturn(10).thenReturn(20).thenReturn(30);

		ResultSetExtractor<String> resultSetExtractor = r -> {
			StringBuilder builder = new StringBuilder();
			while (r.next()) {
				builder.append(r.getInt(1));
			}
			return builder.toString();
		};

		String result = systemUnderTest.query("query", new Object[] { 1, 2 }, resultSetExtractor);
		Assert.assertThat(result, Is.is("102030"));

		Mockito.verify(resultSet).close();
		Mockito.verify(statement).close();
		Mockito.verify(connection).close();
	}

}
