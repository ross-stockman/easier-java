package rws.easierjava.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<R> {
	R extract(ResultSet resultSet) throws SQLException;
}
