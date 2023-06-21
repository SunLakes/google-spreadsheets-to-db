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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class JsonDataOperator {

    private final String dataPath;

    public JsonDataOperator(@Value("${braceletsMapPath}") final String dataPath) {
        this.dataPath = dataPath;
    }

    public Map<Integer, Integer> readBraceletsData() {
        try {
            return new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .readValue(
                            new File(dataPath),
                            new TypeReference<Map<Integer, Integer>>() {
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
