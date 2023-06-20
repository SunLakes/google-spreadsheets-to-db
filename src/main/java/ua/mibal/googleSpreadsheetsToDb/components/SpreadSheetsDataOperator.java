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

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static ua.mibal.googleSpreadsheetsToDb.utils.GoogleAuthorizeUtil.APPLICATION_NAME;
import static ua.mibal.googleSpreadsheetsToDb.utils.GoogleAuthorizeUtil.JSON_FACTORY;
import static ua.mibal.googleSpreadsheetsToDb.utils.GoogleAuthorizeUtil.getCredentials;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class SpreadSheetsDataOperator {

    private final String spreadsheetId;
    private final String pageName;
    private final String range;

    public SpreadSheetsDataOperator(@Value("${spreadsheetId}") final String spreadsheetId,
                                    @Value("${pageName}") final String pageName,
                                    @Value("${dataRange}") final String range) {
        this.spreadsheetId = spreadsheetId;
        this.pageName = pageName;
        this.range = range;
    }

    public List<List<Object>> getData() throws IOException, GeneralSecurityException {
        System.out.println("Taking data" + '\n' +
                           "    spreadsheetId: " + spreadsheetId + '\n' +
                           "    pageName: " + pageName + '\n' +
                           "    dataRange: " + range + '\n');

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        final String pageAndRangeParameter = pageName + "!" + range;

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, pageAndRangeParameter)
                .execute();

        return response.getValues();
    }
}
