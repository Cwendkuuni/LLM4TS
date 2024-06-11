package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
    }

    // Test for basic functionality

    @Test
    public void testDeserializeEmptyCollection() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[]"), null);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testDeserializeSingleElementCollection() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[\"" + "test" + "\"]"), null);
        assertEquals(1, collection.size());
        assertEquals("test", collection.get(0));
    }

    @Test
    public void testDeserializeMultipleElementsCollection() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[\"" + "test1" + "\", \"" + "test2" + "\"]"), null);
        assertEquals(2, collection.size());
        assertEquals("test1", collection.get(0));
        assertEquals("test2", collection.get(1));
    }

    // Test for edge cases and exception handling scenarios

    @Test(expected = IOException.class)
    public void testDeserializeEmptyStream() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(""), null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser("{ \"name\": \"John\" }"), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullCollection() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("null"), null);
        assertNull(collection);
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[]"), null);
        assertTrue(collection.isEmpty());
        List<String> collectionWithEmptyString = deserializer.deserialize(mapper.getFactory().createParser("[\"\"]"), null);
        assertEquals(1, collectionWithEmptyString.size());
        assertEquals("", collectionWithEmptyString.get(0));
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidString() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser("[\"]"), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullValue() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[\":null]"), null);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals(null, collection.get(0));
    }

    @Test
    public void testDeserializeEmptyArrayWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, customDeserializer, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[]"), null);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testDeserializeSingleElementArrayWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return "test";
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, customDeserializer, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[\"" + "test" + "\"]"), null);
        assertEquals(1, collection.size());
        assertEquals("test", collection.get(0));
    }

    @Test
    public void testDeserializeMultipleElementsArrayWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                if (p.nextToken() == JsonToken.START_ARRAY) {
                    List<String> result = new ArrayList<>();
                    while (p.nextToken() != JsonToken.END_ARRAY) {
                        result.add(deserializeString(p, ctxt));
                    }
                    p.nextToken(JsonToken.END_ARRAY);
                    return StdSerializerUtils.constructValueFromSerializable(result, String[].class);
                }
                return deserializeString(p, ctxt);
            }

            private String deserializeString(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.nextTextValue();
                p.nextToken(JsonToken.COMMA);
                return text;
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, customDeserializer, null, null);
        List<String> collection = deserializer.deserialize(mapper.getFactory().createParser("[\"" + "test1" + "\", \"" + "test2" + "\"]"), null);
        assertEquals(2, collection.size());
        assertEquals("test1", collection.get(0));
        assertEquals("test2", collection.get(1));
    }
}