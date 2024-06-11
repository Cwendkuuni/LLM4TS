package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullifyingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("{}");
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("[]");
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(parser, new TestContext(), null);
        assertNull(result);
    }
}