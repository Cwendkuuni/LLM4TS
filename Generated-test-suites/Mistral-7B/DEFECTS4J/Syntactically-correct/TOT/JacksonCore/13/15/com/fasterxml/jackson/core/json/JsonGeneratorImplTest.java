package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link JsonGeneratorImpl}.
 */
public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConfiguration() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(System.out);

        // write simple JSON object
        generator.writeStartObject();
        generator.writeStringField("name", "Alice");
        generator.writeNumberField("age", 30);
        generator.writeEndObject();

        // check generated JSON string
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object() {{
            String name = "Alice";
            int age = 30;
        }});
        String expectedJson = "{\"name\":\"Alice\",\"age\":30}";
        assertEquals(expectedJson, generator.getOutputContent().toString());

        // write JSON array
        generator.writeStartArray();
        generator.writeValue(1);
        generator.writeValue("Alice");
        generator.writeEndArray();
        String jsonArray = generator.getOutputContent().toString();
        String expectedJsonArray = "[1,\"Alice\"]";
        assertEquals(expectedJsonArray, jsonArray);

        // write JSON null value
        generator.writeNull();
        String jsonNull = generator.getOutputContent().toString();
        String expectedJsonNull = "null";
        assertEquals(expectedJsonNull, jsonNull);

        // write JSON boolean value
        generator.writeBoolean(true);
        String jsonBoolean = generator.getOutputContent().toString();
        String expectedJsonBoolean = "true";
        assertEquals(expectedJsonBoolean, jsonBoolean);

        // write JSON number
        generator.writeNumber(1.23);
        String jsonNumber = generator.getOutputContent().toString();
        String expectedJsonNumber = "1.23";
        assertEquals(expectedJsonNumber, jsonNumber);

        generator.close();
    }

    @Test
    public void testQuoteFieldNames() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        factory.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        JsonGenerator generator = factory.createGenerator(System.out);

        // write JSON object without quoting field names
        generator.writeStartObject();
        generator.writeNumberField("age", 30);
        generator.writeEndObject();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object() {{
            int age = 30;
        }});
        String expectedJson = "{\"age\":30}";
        assertEquals(expectedJson, generator.getOutputContent().toString());

        generator.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxNonEscapedChar() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(System.out);
        generator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testCustomCharacterEscapes() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();

        // create custom character escapes that escape 'a' and 'e' characters
        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int charId) {
                switch (charId) {
                    case CharTypes.CHAR_A:
                        return "aaa";
                    case CharTypes.CHAR_E:
                        return "eee";
                    default:
                        return null;
                }
            }
        };

        factory.setCharacterEscapes(customEscapes);
        JsonGenerator generator = factory.createGenerator(System.out);

        // write JSON object using custom character escapes
        generator.writeStartObject();
        generator.writeStringField("name", "Alice");
        generator.writeEndObject();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object() {{
            String name = "Alice";
        }});
        String expectedJson = "{\"name\":\"Alice\"}";
        assertEquals(expectedJson, generator.getOutputContent().toString());

        String jsonWithCustomEscapes = generator.getOutputContent().toString();
        String expectedJsonWithCustomEscapes = "{\"name\":\"Alice\"}";
        assertEquals(expectedJsonWithCustomEscapes, json); // JSON string should be the same as without custom escapes

        String jsonStringWithA = generator.writeValueAsString("Alice");
        String expectedJsonStringWithA = "Alice";
        assertEquals(expectedJsonStringWithA, jsonStringWithA); // JSON string should be the same as without custom escapes

        String jsonStringWithAAA = generator.writeValueAsString("Alice");
        String expectedJsonStringWithAAA = "Alice"; // JSON string should not be changed
        assertEquals(expectedJsonStringWithAAA, jsonStringWithAAA);

        String jsonStringWithAE = generator.writeValueAsString("Alice");
        String expectedJsonStringWithAE = "Alice"; // JSON string should not be changed
        assertEquals(expectedJsonStringWithAE, jsonStringWithAE);

        generator.close();
    }

    @Test
    public void testNullCharacterEscapes() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        factory.setCharacterEscapes(null);

        JsonGenerator generator = factory.createGenerator(System.out);

        // write JSON string with null character escapes
        generator.writeString("Alice\0Bob");

        String json = generator.getOutputContent().toString();
        String expectedJson = "Alice\\0Bob";
        assertEquals(expectedJson, json);

        generator.close();
    }

    @Test
    public void testRootValueSeparator() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();

        // set custom root value separator
        factory.setRootValueSeparator("/");
        JsonGenerator generator = factory.createGenerator(System.out);

        // write JSON object with custom root value separator
        generator.writeStartObject();
        generator.writeStringField("name", "Alice");
        generator.writeEndObject();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object() {{
            String name = "Alice";
        }});
        String expectedJson = "/{\"name\":\"Alice\"}";
        assertEquals(expectedJson, generator.getOutputContent().toString());

        generator.close();
    }

    @Test
    public void testMaxNonEscapedChar() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        factory.setHighestNonEscapedChar(0xFF);
        JsonGenerator generator = factory.createGenerator(System.out);

        // write JSON string with highest non-escaped char set to 0xFF (255)
        generator.writeString("Alice\u00FFBob");

        String json = generator.getOutputContent().toString();
        String expectedJson = "Alice\u00FFBob";
        assertEquals(expectedJson, json);

        generator.close();
    }

    @Test
    public void testEmptyOutput() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(new byte[0]);

        generator.writeStartObject();
        generator.writeEndObject();

        String json = generator.getOutputContent().toString();
        String expectedJson = "{}";
        assertEquals(expectedJson, json);

        generator.close();
    }

    @Test
    public void testCloseGenerator() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(System.out);

        generator.writeStartObject();
        generator.writeEndObject();
        generator.close();

        generator = factory.createGenerator(System.out);
        generator.writeStartObject();
        generator.writeEndObject();
        generator.close();

        generator = factory.createGenerator(System.out);
        generator.writeStartObject();
        generator.writeStringField("name", "Alice");
        generator.writeEndObject();
        generator.close();
    }
}