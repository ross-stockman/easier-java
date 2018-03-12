package rws.easierjava.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<R> {
	R map(ResultSet resultSet) throws SQLException;
}
