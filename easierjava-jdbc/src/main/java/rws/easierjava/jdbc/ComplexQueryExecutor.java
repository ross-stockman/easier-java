package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This complex query executor will return the entire result as a single object
 * {@code R}, such as a map or other complex domain object. If the output should
 * be a list then use {@code SimpleQueryExecutor} instead.
 * 
 * @see SimpleQueryExecutor
 *
 * @param <R>
 *            The type of object returned
 */
public class ComplexQueryExecutor<R> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComplexQueryExecutor.class);

	private DataSource dataSource;

	public ComplexQueryExecutor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Returns a single object using a query that has no parameters.
	 * 
	 * @param query
	 *            query string
	 * @param resultSetExtractor
	 *            extractor that will transform the entire query result to an object
	 * @return An object of type {@code R}
	 * @throws SQLException
	 */
	public R query(String query, ResultSetExtractor<R> resultSetExtractor) throws SQLException {
		return query(query, new Object[0], resultSetExtractor);
	}

	/**
	 * Returns a single object using a prepared statement query.
	 * 
	 * @param query
	 *            query string
	 * @param params
	 *            a list of parameters for the prepared statement
	 * @param resultSetExtractor
	 *            extractor that will transform the entire query result to an object
	 * @return An object of type {@code R}
	 * @see java.sql.PreparedStatement
	 * @throws SQLException
	 */
	public R query(String query, Object[] params, ResultSetExtractor<R> resultSetExtractor) throws SQLException {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			LOGGER.debug("Preparing query : {}", query);
			for (int i = 0; i < params.length; i++) {
				LOGGER.debug("Param[{}] : {}", i + 1, params[i]);
				statement.setObject(i + 1, params[i]);
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				R result = resultSetExtractor.extract(resultSet);
				LOGGER.trace("Result : {}", result);
				return result;
			}
		}
	}
}
