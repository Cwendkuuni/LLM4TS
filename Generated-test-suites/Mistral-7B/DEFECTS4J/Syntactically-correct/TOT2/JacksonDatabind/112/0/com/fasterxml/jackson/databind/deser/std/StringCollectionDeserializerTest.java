package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class StringCollectionDeserializerTest extends TestBase {

    public void testEmptyStringCollection() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode());
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertTrue(result.isEmpty());
    }

    public void testStringCollectionWithStandardDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().add("test"));
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    public void testStringCollectionWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
                return p.getValueAsString();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, customDeserializer, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().add("test1"));
        node.put("items", mapper.createArrayNode().add("test2"));
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    public void testStringCollectionWithNullValues() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().addNull());
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertTrue(result.contains(null));
    }

    public void testStringCollectionWithEmptyString() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().add(""));
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(1, result.size());
        assertEquals("", result.iterator().next());
    }

    public void testStringCollectionWithInvalidJson() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().add("test"));
        node.put("invalid", "invalidJson");
        try {
            mapper.readValue(node, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected field: [class com.fasterxml.jackson.core.JsonParseException$: unexpected value found, expecting START_ARRAY", e.getMessage());
        }
    }

    public void testStringCollectionWithEmptyArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode());
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertTrue(result.isEmpty());
    }

    public void testStringCollectionWithListInsteadOfArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createObjectNode().put("test", "testValue"));
        try {
            mapper.readValue(node, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of java.util.Collection<java.lang.String> from JSON Object", e.getMessage());
        }
    }

    public void testStringCollectionWithDifferentType() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode().add(1));
        try {
            mapper.readValue(node, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Type error: Unrecognized token 'INTEGER' (expecting STRING)", e.getMessage());
        }
    }

    public void testStringCollectionWithUnexpectedType() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                context.constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", mapper.createArrayNode());
        try {
            mapper.readValue(node, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to introspect Class org.junit.Assert for Serialization: no Single Type class found for [class java.util.List, class java.lang.String]", e.getMessage());
        }
    }

    @JsonDeserialize(using = StringCollectionDeserializer.class)
    static class TestClass {
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private Collection<String> items;

        public Collection<String> getItems() {
            return items;
        }

        public void setItems(Collection<String> items) {
            this.items = items;
        }
    }

    public void testStringCollectionWithAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass result = mapper.readValue(mapper.createObjectNode().put("items", mapper.createArrayNode().add("test")), TestClass.class);
        Collection<String> items = result.getItems();
        assertEquals(1, items.size());
        assertEquals("test", items.iterator().next());
    }
}