package rws.easierjava.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rws.easierjava.core.Nullable;

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
	 * Returns a single object using a prepared statement query.
	 * 
	 * @param query
	 *            query string
	 * @param params
	 *            a possibly-null list of parameters for the prepared statement
	 * @param resultSetExtractor
	 *            extractor that will transform the entire query result to an object
	 * @return An object of type {@code R}
	 * @see java.sql.PreparedStatement
	 * @throws SQLException
	 */
	public R query(String query, @Nullable Object[] params, ResultSetExtractor<R> resultSetExtractor)
			throws SQLException {

		Object[] internalParams = Optional.ofNullable(params).orElse(new Object[0]);

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			LOGGER.debug("Preparing query : {}", query);
			for (int i = 0; i < internalParams.length; i++) {
				LOGGER.debug("Param[{}] : {}", i + 1, internalParams[i]);
				statement.setObject(i + 1, internalParams[i]);
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				R result = resultSetExtractor.extract(resultSet);
				LOGGER.trace("Result : {}", result);
				return result;
			}
		}
	}
}
