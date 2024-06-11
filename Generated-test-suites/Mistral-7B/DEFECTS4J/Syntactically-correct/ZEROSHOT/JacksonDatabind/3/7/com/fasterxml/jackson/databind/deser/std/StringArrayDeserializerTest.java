package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

/**
 * Test class for {@link StringArrayDeserializer}
 */
public class StringArrayDeserializerTest {

    @JacksonStdImpl
    public static class TestStringArrayDeserializer
            extends StringArrayDeserializer
            implements ContextualDeserializer {

        // Override for testing
        @Override
        public JsonDeserializer<String> _elementDeserializer;

        public TestStringArrayDeserializer() {
            super(String[].class);
            this._elementDeserializer = null;
        }

        @SuppressWarnings("unchecked")
        public TestStringArrayDeserializer(JsonDeserializer<String> deser) {
            super(String[].class);
            this._elementDeserializer = (JsonDeserializer<String>) deser;
        }

        /**
         * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
         * when no custom value deserializer is used.
         * @throws IOException
         * @throws JsonProcessingException
         */
        @Override
        public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return super.deserialize(jp, ctxt);
        }

        /**
         * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
         * when a custom value deserializer is used.
         * @throws IOException
         * @throws JsonProcessingException
         */
        @Override
        protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return super._deserializeCustom(jp, ctxt);
        }
    }

    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }

    @ org.junit.Test
    public void testDeserializeEmptyArray() throws Exception {
        String[] expected = new String[0];
        String[] result = mapper.readValue(new ObjectMapper().createParser("[]"), new TestStringArrayDeserializer());
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeSingleValue() throws Exception {
        String[] expected = new String[] {"test"};
        String[] result = mapper.readValue(new ObjectMapper().createParser("\"test\""), new TestStringArrayDeserializer());
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeSingleValueNull() throws Exception {
        String[] expected = new String[] {null};
        String[] result = mapper.readValue(new ObjectMapper().createParser("null"), new TestStringArrayDeserializer());
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeArrayWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new LocalDateDeserializer();
        TestStringArrayDeserializer deserializer = new TestStringArrayDeserializer(customDeserializer);
        String[] expected = new String[] {"2022-01-01", "2022-01-02"};
        String[] json = {"\"2022-01-01\"", "\"2022-01-02\""};
        String[] result = mapper.readValue(new ObjectMapper().createParser(new ObjectMapper().writeValueAsString(json)), deserializer);
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeArrayWithNullValue() throws Exception {
        TestStringArrayDeserializer deserializer = new TestStringArrayDeserializer();
        String[] expected = new String[] {"test1", null, "test3"};
        String[] json = {"\"test1\"", null, "\"test3\""};
        String[] result = mapper.readValue(new ObjectMapper().createParser(new ObjectMapper().writeValueAsString(json)), deserializer);
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeArrayWithEmptyString() throws Exception {
        TestStringArrayDeserializer deserializer = new TestStringArrayDeserializer();
        String[] expected = new String[] {"test1", "", "test3"};
        String[] json = {"\"test1\"", "", "\"test3\""};
        String[] result = mapper.readValue(new ObjectMapper().createParser(new ObjectMapper().writeValueAsString(json)), deserializer);
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test
    public void testDeserializeArrayWithEmptyStringAsNull() throws Exception {
        ObjectMapper mapperWithEmptyStringAsNull = new ObjectMapper();
        mapperWithEmptyStringAsNull.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        TestStringArrayDeserializer deserializer = new TestStringArrayDeserializer();
        String[] expected = new String[] {"test1", null, "test3"};
        String[] json = {"\"test1\"", "", "\"test3\""};
        String[] result = mapperWithEmptyStringAsNull.readValue(new ObjectMapper().createParser(new ObjectMapper().writeValueAsString(json)), deserializer);
        assertArrayEquals(expected, result);
    }

    @ org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeArrayWithInvalidJson() throws Exception {
        TestStringArrayDeserializer deserializer = new TestStringArrayDeserializer();
        mapper.readValue(new ObjectMapper().createParser("{\"test\":1}"), deserializer);
    }

    private void assertArrayEquals(Object expected, Object actual) {
        assertArrayEquals(expected.getClass(), actual.getClass());
        assertArrayEquals((String[]) expected, (String[]) actual);
    }
}