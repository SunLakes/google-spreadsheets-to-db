package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class Application {

    private final SpreadSheetsDataOperator spreadSheetsDataOperator;

    private final DatabaseOperator databaseOperator;

    public Application(SpreadSheetsDataOperator spreadSheetsDataOperator,
                       DatabaseOperator databaseOperator) {
        this.spreadSheetsDataOperator = spreadSheetsDataOperator;
        this.databaseOperator = databaseOperator;
    }

    public void start() {
        try {
            List<List<String>> data = spreadSheetsDataOperator.getData();
            databaseOperator.writeData(data);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
