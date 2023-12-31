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

package ua.mibal.googleSpreadsheetsToDb;

import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Configuration
@ComponentScan("ua.mibal.googleSpreadsheetsToDb")
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Bean
    Map<String, Function<List<String>, String>> rowNamesValuesMap() {
        // 20 червня - 1 день
        final Map<String, String> DAY_MAP = Map.of(
                "20", "1",
                "21", "2",
                "22", "3",
                "23", "4",
                "24", "5",
                "25", "6",
                "26", "7",
                "Всі дні (20-26 липня)", "1,2,3,4,5,6,7"
        );
        final Map<String, String> MESSENGER_MAP = Map.of(
                "Telegram", "Telegram",
                "Viber", "Viber",
                "Мессенджер відсутній", "None"
        );
        final Map<String, String> TENT_MAP = Map.of(
                "Так", "T",
                "Ні", "F"
        );
        final Map<String, String> BUS_MAP = Map.of(
                "На замовному автобусі (приблизна ціна 200 грн в дві сторони)", "T",
                "Особистим транспортом", "F"
        );

        return new LinkedHashMap<>() {{
            put("id", row -> row.get(0));
            put("name", row -> row.get(2));
            put("primary_phone", row -> row.get(3));
            put("secondary_phone", row -> row.get(8));
            put("available_messenger", row -> {
                final String val = row.get(7);
                return String.join(",",
                        Arrays.stream(val.split(", "))
                                .map(MESSENGER_MAP::get)
                                .collect(Collectors.toSet())
                );
            });
            put("soc_link", row -> row.get(9));
            put("city", row -> row.get(11));
//            TODO put("birth_date", row -> String.join("-",
//                    row.get(9).split("\\.")));
            put("days", row -> {
                final String val = row.get(13);
                return String.join(",",
                        Arrays.stream(val.split(", "))
                                .map(DAY_MAP::get)
                                .collect(Collectors.toSet()));
            });
            put("need_tent", row -> {
                final String val = row.get(12);
                return TENT_MAP.get(val);
            });
            put("need_bus", row -> {
                final String val = row.get(14);
                return BUS_MAP.get(val);
            });
        }};
    }
}
