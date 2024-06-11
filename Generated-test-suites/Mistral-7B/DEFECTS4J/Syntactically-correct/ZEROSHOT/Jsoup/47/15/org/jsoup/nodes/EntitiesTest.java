package org.jsoup.nodes;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class EntitiesTest {

    private static Map<String, Character> full;
    private static Map<Character, String> xhtmlByVal;
    private static Map<Character, String> base;
    private static Map<Character, String> baseByVal;
    private static Map<Character, String> fullByVal;

    @BeforeClass
    public static void setupClass() {
        Entities.full = new HashMap<>();
        Entities.xhtmlByVal = new HashMap<>();
        Entities.base = new HashMap<>();
        Entities.baseByVal = new HashMap<>();
        Entities.fullByVal = new HashMap<>();

        Entities.xhtmlByVal.put((char) 0x00022, "quot");
        Entities.xhtmlByVal.put((char) 0x00026, "amp");
        Entities.xhtmlByVal.put((char) 0x0003C, "lt");
        Entities.xhtmlByVal.put((char) 0x0003E, "gt");

        Entities.base = loadEntities("entities-base.properties");
        Entities.baseByVal = toCharacterKey(Entities.base);
        Entities.full = loadEntities("entities-full.properties");
        Entities.fullByVal = toCharacterKey(Entities.full);
    }

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue().toString();
            boolean expectedResult = true;

            boolean result = Entities.isNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        for (Map.Entry<Character, String> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;

            boolean result = Entities.isNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        for (Map.Entry<Character, String> entry : Entities.baseByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;

            boolean result = Entities.isNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        String nonEntity = "non_entity";
        boolean result = Entities.isNamedEntity(nonEntity);
        assertThat(result, is(false));
        result = Entities.isNamedEntity(String.valueOf((char) 0x0001));
        assertThat(result, is(false));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.baseByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;

            boolean result = Entities.isBaseNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isBaseNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        for (Map.Entry<Character, String> entry : Entities.fullByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = false;

            boolean result = Entities.isBaseNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isBaseNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        for (Map.Entry<Character, String> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = false;

            boolean result = Entities.isBaseNamedEntity(name);
            assertThat(result, is(expectedResult));
            result = Entities.isBaseNamedEntity(String.valueOf(key));
            assertThat(result, is(expectedResult));
        }

        Character nonEntity = (char) 0x0001;
        boolean result = Entities.isBaseNamedEntity(String.valueOf(nonEntity));
        assertThat(result, is(false));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue().toString();

            Character result = Entities.getCharacterByName(name);
            assertThat(result, is(key));
            result = Entities.getCharacterByName(String.valueOf(key));
            assertThat(result, is(key));
        }

        for (Map.Entry<Character, String> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();

            Character result = Entities.getCharacterByName(name);
            assertThat(result, is(key));
            result = Entities.getCharacterByName(String.valueOf(key));
            assertThat(result, is(key));
        }

        for (Map.Entry<Character, String> entry : Entities.baseByVal.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();

            Character result = Entities.getCharacterByName(name);
            assertThat(result, is(key));
            result = Entities.getCharacterByName(String.valueOf(key));
            assertThat(result, is(key));
        }

        String nonEntity = "non_entity";
        Character result = Entities.getCharacterByName(nonEntity);
        assertThat(result, is(nullValue()));
        result = Entities.getCharacterByName(String.valueOf((char) 0x0001));
        assertThat(result, is(nullValue()));
    }

    private static Map<String, Character> loadEntities(String filename) {
        Map<String, Character> entities = new HashMap<>();
        try (InputStream inputStream = Entities.class.getResourceAsStream(filename)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String name = (String) entry.getKey();
                int codePoint = Integer.parseInt((String) entry.getValue(), 16);
                Character character = (char) codePoint;

                entities.put(name, character);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load entities: " + e.getMessage(), e);
        }

        return entities;
    }

    private static Map<Character, String> toCharacterKey(Map<String, Character> inMap) {
        Map<Character, String> outMap = new HashMap<>();

        for (Map.Entry<String, Character> entry : inMap.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();

            if (outMap.containsKey(character)) {
                outMap.put(character, name);
            } else {
                outMap.put(character, name);
            }
        }

        return outMap;
    }
}