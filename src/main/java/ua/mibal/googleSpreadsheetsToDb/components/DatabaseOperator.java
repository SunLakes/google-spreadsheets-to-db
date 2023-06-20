package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class DatabaseOperator {

    private final Connection connection;

    public DatabaseOperator(@Value("${jdbcDriverPackage}") final String jdbcDriverPackage,
                            @Value("${spring.datasource.url}") final String dbUrl,
                            @Value("${spring.datasource.username}") final String user,
                            @Value("${spring.datasource.password}") final String password) {

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
        // TODO
    }
}
