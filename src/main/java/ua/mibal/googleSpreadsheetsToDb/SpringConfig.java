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
    Map<String, Function<List<Object>, Object>> rowNamesValuesMap() {
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
            put("name", row -> row.get(1).toString().trim());
            put("primary_phone", row -> row.get(2).toString().trim());
            put("secondary_phone", row -> row.get(7).toString().trim());
            put("available_messenger", row -> {
                final String val = row.get(6).toString();
                return String.join(",",
                        Arrays.stream(val.split(", "))
                                .map(MESSENGER_MAP::get)
                                .collect(Collectors.toSet())
                );
            });
            put("soc_link", row -> row.get(8).toString().trim());
            put("city", row -> row.get(10).toString().trim());
//            TODO put("birth_date", row -> String.join("-",
//                    row.get(9).toString().split("\\.")));
            put("days", row -> {
                final String val = row.get(12).toString();
                return String.join(",",
                        Arrays.stream(val.split(", "))
                                .map(DAY_MAP::get)
                                .collect(Collectors.toSet()));
            });
            put("need_tent", row -> {
                final String val = row.get(11).toString();
                return TENT_MAP.get(val);
            });
            put("need_bus", row -> {
                final String val = row.get(13).toString();
                return BUS_MAP.get(val);
            });
        }};
    }
}
