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
