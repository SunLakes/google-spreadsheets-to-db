package ua.mibal.googleSpreadsheetsToDb.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class Application {

    private final SpreadSheetsDataOperator spreadSheetsDataOperator;

    public Application(SpreadSheetsDataOperator spreadSheetsDataOperator) {
        this.spreadSheetsDataOperator = spreadSheetsDataOperator;
    }

    public void start() {
        // TODO
        try {
            System.out.println(spreadSheetsDataOperator.getData());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
