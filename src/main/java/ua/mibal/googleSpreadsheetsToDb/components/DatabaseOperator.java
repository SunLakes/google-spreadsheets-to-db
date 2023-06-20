package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class DatabaseOperator {

    private final Connection connection;

    private final Map<String, Function<List<Object>, Object>> rowNamesValuesMap;

    public DatabaseOperator(@Value("${jdbcDriverPackage}") final String jdbcDriverPackage,
                            @Value("${spring.datasource.url}") final String dbUrl,
                            @Value("${spring.datasource.username}") final String user,
                            @Value("${spring.datasource.password}") final String password,
                            final Map<String, Function<List<Object>, Object>> rowNamesValuesMap) {
        this.rowNamesValuesMap = rowNamesValuesMap;
        try {
            Class.forName(jdbcDriverPackage);
            this.connection = DriverManager.getConnection(
                    dbUrl,
                    user,
                    password
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeData(List<List<Object>> data) {
        data.forEach(System.out::println);
        try {
            insertPerson(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertPerson(List<List<Object>> data) throws SQLException {
        final String insertingRowNames = String.join(", ", rowNamesValuesMap.keySet());
        final String insertingPlaces = "?" + ", ?".repeat(rowNamesValuesMap.size() - 1);
        final String SQL = format(
                "INSERT INTO people (%s) VALUES (%S)", insertingRowNames, insertingPlaces
        );
        for (List<Object> row : data) {
            int count = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            for (Function<List<Object>, Object> func : rowNamesValuesMap.values()) {
                preparedStatement.setObject(count++, func.apply(row));
            }
            System.out.println(preparedStatement);
            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
