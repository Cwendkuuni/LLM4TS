package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

    // Test for serializing Date object
    @Test
    public void testSerializeDate() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectNode rootNode = ObjectMapper.getDefaultInstance().createObjectNode();
        rootNode.put("key", new Date());
        ObjectWriter writer = ObjectMapper.getDefaultInstance().writerWithRootValue(rootNode);
        String jsonString = writer.writeValueAsString(rootNode);
        JsonParser parser = ObjectMapper.getDefaultInstance().parser();
        Map<String, Object> map = parser.readValueAs(Map.class, jsonString);
        assertEquals("key", map.keySet().iterator().next());
        assertTrue(map.get("key") instanceof Date);
    }

    // Test for serializing String object
    @Test
    public void testSerializeString() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectNode rootNode = ObjectMapper.getDefaultInstance().createObjectNode();
        rootNode.put("key", "test_string");
        ObjectWriter writer = ObjectMapper.getDefaultInstance().writerWithRootValue(rootNode);
        String jsonString = writer.writeValueAsString(rootNode);
        JsonParser parser = ObjectMapper.getDefaultInstance().parser();
        Map<String, Object> map = parser.readValueAs(Map.class, jsonString);
        assertEquals("test_string", map.keySet().iterator().next());
        assertTrue(map.get("test_string") instanceof String);
    }

    // Test for serializing null value
    @Test
    public void testSerializeNull() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectNode rootNode = ObjectMapper.getDefaultInstance().createObjectNode();
        rootNode.putNull("key");
        ObjectWriter writer = ObjectMapper.getDefaultInstance().writerWithRootValue(rootNode);
        String jsonString = writer.writeValueAsString(rootNode);
        JsonParser parser = ObjectMapper.getDefaultInstance().parser();
        Map<String, Object> map = parser.readValueAs(Map.class, jsonString);
        assertNull(map.get("key"));
    }

    // Test for serializing empty Map
    @Test
    public void testSerializeEmptyMap() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter writer = mapper.writerWithRootValue(rootNode);
        String jsonString = writer.writeValueAsString(rootNode);
        JsonParser parser = mapper.parser();
        Map<String, Object> map = parser.readValueAs(Map.class, jsonString);
        assertTrue(map.isEmpty());
    }

    // Test for serializing Map with different key types
    @Test
    public void testSerializeMixedKeys() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("intKey", 1);
        rootNode.put("dateKey", new Date());
        rootNode.put("stringKey", "test_string");
        ObjectWriter writer = mapper.writerWithRootValue(rootNode);
        String jsonString = writer.writeValueAsString(rootNode);
        JsonParser parser = mapper.parser();
        Map<String, Object> map = parser.readValueAs(Map.class, jsonString);
        assertEquals(3, map.size());
        assertTrue(map.containsKey("intKey"));
        assertTrue(map.containsKey("dateKey"));
        assertTrue(map.containsKey("stringKey"));
    }

    // Test for serializing Map with duplicate keys
    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testSerializeDuplicateKeys() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("key", "test_string");
        rootNode.put("key", 1);
        ObjectWriter writer = mapper.writerWithRootValue(rootNode);
        writer.writeValueAsString(rootNode);
    }

    // Test for getting schema
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        assertEquals("string", serializer.getSchema(new SerializerProvider(), Object.class).getTextValue());
    }

    // Test for accepting JsonFormatVisitor
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        assertEquals(SerializerProvider.StandardSerializers.StringSerializer, visitor.getSerializerProvider().findValueSerializer(String.class));
    }
}