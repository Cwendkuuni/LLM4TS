package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser p = mapper.getFactory().createParser("{}");

        Object result = mapper.readValue(p, deserializer.getType());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[]");
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);

        Object result = mapper.readerForType(Object.class).readValue(p, new TypeReference<NullifyingDeserializer>() {});
        assertNull(result);

        result = mapper.readerForType(int[].class).readValue(p, typeDeserializer);
        assertNull(result);

        p.nextToken(); // Consume START_ARRAY token
        result = mapper.readerForType(Object.class).readValue(p, new TypeReference<NullifyingDeserializer>() {});
        assertNull(result);

        p.nextToken(); // Consume VALUE_STRING token with array element type name
        result = mapper.readerForType(int[].class).readValue(p, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": null}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        Object result = mapper.treeToValue(node, deserializer.getType());
        assertNull(result);
    }

    @Test
    public void testDeserializeEmpty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("null");

        Object result = mapper.readValue(p, NullifyingDeserializer.instance.getType());
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"invalid\": 123}");

        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(p, deserializer.getType());

        fail("Expected IOException, but got: " + result);
    }

    @Test
    public void testDeserializeWithNullType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[]");
        TypeDeserializer typeDeserializer = null;

        try {
            Object result = mapper.readerForType(Object.class).readValue(p, typeDeserializer);
            fail("Expected NullPointerException, but got: " + result);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}