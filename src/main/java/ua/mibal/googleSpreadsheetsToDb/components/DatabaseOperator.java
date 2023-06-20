package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private final Map<String, Function<List<String>, String>> rowNamesValuesMap;

    public DatabaseOperator(@Value("${jdbcDriverPackage}") final String jdbcDriverPackage,
                            @Value("${spring.datasource.url}") final String dbUrl,
                            @Value("${spring.datasource.username}") final String user,
                            @Value("${spring.datasource.password}") final String password,
                            final Map<String, Function<List<String>, String>> rowNamesValuesMap) {
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

    public void writeData(List<List<String>> data) {
        data.forEach(System.out::println);
        try {
            insertPeople(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertPeople(List<List<String>> data) throws SQLException {
        final String insertingRowNames = String.join(", ", rowNamesValuesMap.keySet());
        final String insertingPlaces = "?" + ", ?".repeat(rowNamesValuesMap.size() - 1);
        final String SQL = format(
                "INSERT INTO people (%s) VALUES (%S)", insertingRowNames, insertingPlaces
        );
        for (List<String> row : data) {
            int count = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            for (Function<List<String>, String> func : rowNamesValuesMap.values()) {
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

    public void insertBraceletsId() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM bracelets"
        );
        while (resultSet.next()) {
            final int braceletId = resultSet.getInt("bracelet_id");
            final int personId = resultSet.getInt("person_id");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE people " +
                    "SET bracelet_id = ? " +
                    "WHERE id = ?"
            );
            preparedStatement.setInt(1, braceletId);
            preparedStatement.setInt(2, personId);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }
    }

    public void writeBraceletsData(Map<Integer, Integer> braceletsData) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO bracelets " +
                "VALUES (?, ?)"
        );
        for (Map.Entry<Integer, Integer> pair : braceletsData.entrySet()) {
            preparedStatement.setInt(1, pair.getKey());
            preparedStatement.setInt(2, pair.getValue());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }
    }
}
