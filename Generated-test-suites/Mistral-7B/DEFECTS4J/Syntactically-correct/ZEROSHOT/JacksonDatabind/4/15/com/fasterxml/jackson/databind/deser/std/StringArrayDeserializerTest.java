package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test class for {@link StringArrayDeserializer}.
 */
public class StringArrayDeserializerTest extends BaseTest {

    /**
     * Verifies that default instance of {@link StringArrayDeserializer} deserializes String array correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testDefaultInstance() throws IOException {
        String json = "[\"abc\", \"def\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readValue(json, deserializer.getType());

        assertArrayEquals(new String[]{"abc", "def"}, result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} instance with assigned value deserializer deserializes String array
     * correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String json = "[\"abc\", \"def\"]";
        String[] result = mapper.readValue(json, deserializer.getType());

        assertArrayEquals(new String[]{"ABC", "DEF"}, result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} handles custom type deserializer correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testCustomTypeDeserializer() throws IOException {
        String json = "[1, 2, 3]";
        TypeDeserializer customTypeDeserializer = new StdTypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                ObjectBuffer buffer = ctxt.leaseObjectBuffer();
                Object[] chunk = buffer.resetAndStart();

                int ix = 0;
                JsonToken t;

                while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                    int value = jp.getIntValue();
                    chunk[ix++] = value;
                }

                return buffer.completeAndClearBuffer(chunk, ix, Integer.TYPE);
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = mapper.readValue(json, deserializer.withTypeDeserializer(customTypeDeserializer).getType());

        assertArrayEquals(new Integer[]{1, 2, 3}, result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} handles null values correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testNullValues() throws IOException {
        String json = "[null, \"abc\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readValue(json, deserializer.getType());

        assertArrayEquals(new String[]{null, "abc"}, result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} handles single value correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testSingleValue() throws IOException {
        String json = "\"abc\"";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readValue(json, deserializer.getType());

        assertArrayEquals(new String[]{"abc"}, result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} handles empty String array correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test
    public void testEmptyArray() throws IOException {
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readValue(json, deserializer.getType());

        assertArrayEquals(new String[0], result);
    }

    /**
     * Verifies that {@link StringArrayDeserializer} handles invalid JSON correctly.
     *
     * @throws IOException If {@code JsonParser} encounters any I/O errors.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        String json = "{\"invalid\": \"JSON\"}";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        mapper.readValue(json, deserializer.getType());
    }
}