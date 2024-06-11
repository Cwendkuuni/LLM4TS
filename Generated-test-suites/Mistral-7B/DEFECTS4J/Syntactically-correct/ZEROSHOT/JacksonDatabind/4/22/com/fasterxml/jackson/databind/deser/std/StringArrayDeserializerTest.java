package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultDeserialization() throws IOException, JsonMappingException {
        String json = "[\"test1\",\"test2\",\"test3\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        String[] result = deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        assertArrayEquals(new String[] { "test1", "test2", "test3" }, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException, JsonMappingException {
        String json = "[null, test2, \"test3\"]";

        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        String[] result = deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        assertArrayEquals(new String[] { null, "test2", "test3" }, result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException, JsonMappingException {
        String json = "[]";

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        String[] result = deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        assertArrayEquals(new String[0], result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException, JsonMappingException {
        String json = "\"test\"";

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        fail("Expected JsonMappingException but it didn't occur.");
    }

    @Test(expected = JsonMappingException.class)
    public void testNonStringElementDeserialization() throws IOException, JsonMappingException {
        String json = "[1, 2, 3]";

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        fail("Expected JsonMappingException but it didn't occur.");
    }

    @Test(expected = JsonMappingException.class)
    public void testNullElementDeserialization() throws IOException, JsonMappingException {
        String json = "[null, null, null]";

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        deserializer.deserialize(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, null, null));

        fail("Expected JsonMappingException but it didn't occur.");
    }

    @Test
    public void testContextualDeserialization() throws IOException, JsonMappingException {
        String json = "[null, test2, \"test3\"]";

        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(new TypeReference<String[]>() {}.getType());

        String[] result = deserializer.deserializeWithType(parser, new DeserializationContext(JsonFactory.getDefaultInstance(), null, typeDeserializer, null), typeDeserializer);

        assertArrayEquals(new String[] { null, "test2", "test3" }, result);
    }
}