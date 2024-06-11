package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser parser = mapper.getFactory().createParser(json);

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.deserializeValue(parser, deserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithArray() throws JsonProcessingException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.deserializeValue(parser, deserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithObject() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser parser = mapper.getFactory().createParser(json);

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        ObjectNode result = mapper.readValue(parser, ObjectNode.class);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        String json = "[]";
        JsonParser parser = mapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String[].class);

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(parser, deserializer, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithCustomType() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser parser = mapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = new StdTypeDeserializer() {
            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new MyClass();
            }
        };

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(parser, deserializer, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithBase64() throws JsonProcessingException {
        String json = "data:image/jpeg;base64,/9j/2wBDAAQEBQYBFgQhE0YSRUUGAg...";
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(new StringReader(json));
        parser.nextToken(); // skip "[data:image/jpeg;base64,"
        parser.nextToken(); // skip "data:"
        parser.nextToken(); // skip "image/jpeg"
        parser.nextToken(); // skip ";"
        parser.nextToken(); // skip "base64,"
        parser.nextToken(); // skip ","

        // now parse the base64 data
        Base64Variants base64Variants = Base64Variants.STANDARD;
        ObjectNode node = mapper.readTree(parser, base64Variants);

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(parser, deserializer);
        assertNull(result);
    }
}