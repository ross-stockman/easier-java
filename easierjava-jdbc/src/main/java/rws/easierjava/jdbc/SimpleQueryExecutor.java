package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple query executor that will return a list of {@code R} objects.
 * If a more complex object, like a map, is required then use
 * {@code ComplexQueryExecutor} instead.
 * 
 * @see ComplexQueryExecutor
 *
 * @param <R>
 *            The type of object returned as a list
 */
public class SimpleQueryExecutor<R> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQueryExecutor.class);

	private DataSource dataSource;

	public SimpleQueryExecutor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Returns a list of objects using a query that has no parameters.
	 * 
	 * @param query
	 *            query string
	 * @param rowMapper
	 *            mapper that will transform a single {@code ResultSet} row to an
	 *            object
	 * @return A list of type {@code R}
	 * @throws SQLException
	 */
	public List<R> query(String query, RowMapper<R> rowMapper) throws SQLException {
		return query(query, new Object[0], rowMapper);
	}

	/**
	 * Returns a list of objects using a prepared statement query.
	 * 
	 * @param query
	 *            query string
	 * @param params
	 *            a list of parameters for the prepared statement
	 * @param rowMapper
	 *            mapper that will transform a single {@code ResultSet} row to an
	 *            object
	 * @return A list of type {@code R}
	 * @see java.sql.PreparedStatement
	 * @throws SQLException
	 */
	public List<R> query(String query, Object[] params, RowMapper<R> rowMapper) throws SQLException {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			LOGGER.debug("Preparing query : {}", query);
			for (int i = 0; i < params.length; i++) {
				LOGGER.debug("Param[{}] : {}", i + 1, params[i]);
				statement.setObject(i + 1, params[i]);
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				List<R> results = new ArrayList<>();
				while (resultSet.next()) {
					R result = rowMapper.map(resultSet);
					LOGGER.trace("Result : {}", result);
					results.add(result);
				}
				return results;
			}
		}
	}
}
