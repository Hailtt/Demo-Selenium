package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONUtils {

    private static final String DATA_PATH = "src/test/resources/testdata/";

    public static JsonNode getTestData(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(new File(DATA_PATH + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
