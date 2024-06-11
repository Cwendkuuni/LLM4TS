package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeEmpty() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"Test String\"");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeBoolean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(Boolean.TRUE.toString());
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeCustomClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"className\": \"CustomClass\", \"value\": 123}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"fieldName\": null}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidJson");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    static class TestContext extends DeserializationContext {
        // Empty implementation for testing
    }
}

//