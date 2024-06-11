package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test class for {@link FromStringDeserializer}
 */
public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Test deserialization of various types from string representation
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserialization() throws IOException, JsonProcessingException {
        // Test serialization and deserialization of various types
        testSerializationAndDeserialization(File.class, "path/to/file.txt");
        testSerializationAndDeserialization(URL.class, "http://example.com");
        testSerializationAndDeserialization(URI.class, "http://example.com/path/to/resource");
        testSerializationAndDeserialization(Class.class, String.class.getName());
        testSerializationAndDeserialization(JavaType.class, mapper.getTypeFactory().constructType(String.class));
        testSerializationAndDeserialization(Currency.class, "USD");
        testSerializationAndDeserialization(Pattern.class, "\\w+@\\w+\\.\\w+");
        testSerializationAndDeserialization(Locale.class, "en_US");
        testSerializationAndDeserialization(Charset.class, "UTF-8");
        testSerializationAndDeserialization(TimeZone.class, "America/New_York");
        testSerializationAndDeserialization(InetAddress.class, "192.168.0.1");
        testSerializationAndDeserialization(InetSocketAddress.class, "192.168.0.1:8080");
        testSerializationAndDeserialization(StringBuilder.class, "");
    }

    /**
     * Test deserialization of empty string for various types
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationEmptyString() throws IOException, JsonProcessingException {
        // Test deserialization of empty string for various types
        testDeserializationEmptyString(File.class, null);
        testDeserializationEmptyString(URL.class, null);
        testDeserializationEmptyString(URI.class, new URI(""));
        testDeserializationEmptyString(Class.class, null);
        testDeserializationEmptyString(JavaType.class, null);
        testDeserializationEmptyString(Currency.class, null);
        testDeserializationEmptyString(Pattern.class, null);
        testDeserializationEmptyString(Locale.class, Locale.ROOT);
        testDeserializationEmptyString(Charset.class, Charset.forName(""));
        testDeserializationEmptyString(TimeZone.class, TimeZone.getDefault());
        testDeserializationEmptyString(InetAddress.class, null);
        testDeserializationEmptyString(InetSocketAddress.class, null);
        testDeserializationEmptyString(StringBuilder.class, new StringBuilder());
    }

    /**
     * Test serialization and deserialization of a specific type
     * @param clazz Class to test
     * @param value String representation of the object
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void testSerializationAndDeserialization(Class<?> clazz, String value) throws IOException, JsonProcessingException {
        Object deserialized = mapper.readValue(mapper.writeValueAsString(new Object() {}.getClass().cast(new Object[]{clazz.cast(value)})[0]), clazz);
        assertEquals(value, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserialized));
    }

    /**
     * Test deserialization of empty string for a specific type
     * @param clazz Class to test
     * @param expectedResult Expected result of deserialization
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void testDeserializationEmptyString(Class<?> clazz, Object expectedResult) throws IOException, JsonProcessingException {
        Object deserialized = mapper.readValue(mapper.readTree("{}"), clazz);
        if (expectedResult != null) {
            assertEquals(expectedResult, deserialized);
        } else {
            assertNull(deserialized);
        }
    }
}