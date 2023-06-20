package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class Application {

    private final SpreadSheetsDataOperator spreadSheetsDataOperator;

    private final DatabaseOperator databaseOperator;

    private final JsonDataOperator jsonDataOperator;

    public Application(SpreadSheetsDataOperator spreadSheetsDataOperator,
                       DatabaseOperator databaseOperator, JsonDataOperator jsonDataOperator) {
        this.spreadSheetsDataOperator = spreadSheetsDataOperator;
        this.databaseOperator = databaseOperator;
        this.jsonDataOperator = jsonDataOperator;
    }

    public void start() {
        try {
            List<List<String>> data = spreadSheetsDataOperator.getData();
            databaseOperator.writeData(data);
            Map<Integer, Integer> braceletsData = jsonDataOperator.readBraceletsData();
            databaseOperator.writeBraceletsData(braceletsData);
            databaseOperator.insertBraceletsId();
        } catch (IOException | GeneralSecurityException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
