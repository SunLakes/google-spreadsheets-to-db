/*
 * Copyright (c) 2023. http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
