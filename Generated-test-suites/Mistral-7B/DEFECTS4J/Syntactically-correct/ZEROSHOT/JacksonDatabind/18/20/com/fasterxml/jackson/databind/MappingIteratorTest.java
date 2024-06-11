package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.testng.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicIterator() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        assertEquals(mappingIterator.next().intValue(), 1);
        assertEquals(mappingIterator.next().intValue(), 2);
        assertEquals(mappingIterator.next().intValue(), 3);
        assertFalse(mappingIterator.hasNext());

        jsonParser.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        assertTrue(mappingIterator.hasNextValue());
        jsonParser.nextToken();
        assertTrue(mappingIterator.hasNextValue());
        jsonParser.nextToken();
        assertTrue(mappingIterator.hasNextValue());
        jsonParser.nextToken();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        List<Integer> resultList = new ArrayList<>();
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        assertEquals(mappingIterator.readAll(resultList).size(), 3);
        assertEquals(mappingIterator.readAll(new ArrayList<>()).get(0).intValue(), 1);
        assertEquals(mappingIterator.readAll(new ArrayList<>()).get(1).intValue(), 2);
        assertEquals(mappingIterator.readAll(new ArrayList<>()).get(2).intValue(), 3);

        jsonParser.close();
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        Collection<Integer> results = new ArrayList<>();
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        mappingIterator.readAll(results);
        assertEquals(results.size(), 3);
        assertEquals(results.get(0).intValue(), 1);
        assertEquals(results.get(1).intValue(), 2);
        assertEquals(results.get(2).intValue(), 3);

        jsonParser.close();
    }

    @Test
    public void testGetParser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        assertEquals(mappingIterator.getParser().getCurrentLocation().getLineNr(), 1);
        jsonParser.nextToken();
        assertEquals(mappingIterator.getParser().getCurrentLocation().getLineNr(), 1);
        jsonParser.nextToken();
        assertEquals(mappingIterator.getParser().getCurrentLocation().getLineNr(), 1);
        jsonParser.nextToken();
        assertEquals(mappingIterator.getParser().getCurrentLocation().getLineNr(), 1);
        jsonParser.nextToken();
        assertNull(mappingIterator.getParser());

        jsonParser.close();
    }

    @Test
    public void testGetParserSchema() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(
                "[1,2,3]".getBytes()));

        JsonSchema schema = objectMapper.getFactory().getSchemaProvider()
                .findSchemaForContext(new DeserializationContext());
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("$schema", new TextNode("http://json-schema.org/draft-07/schema#"));
        rootNode.set("title", new TextNode("MappingIteratorTest"));
        rootNode.set("type", new TextNode("object"));
        rootNode.put("properties", new ObjectNode());
        rootNode.findValue("properties")
                .set("mappingIterator", createObjectNodeForMappingIterator());

        MappingIterator<Integer> mappingIterator = new MappingIterator<>(
                javaTypeOf(Integer.class), jsonParser, new DeserializationContext(),
                new StdSerializer<>(Integer.class), false, null);

        assertEquals(mappingIterator.getParserSchema().getSchemaProvider()
                .getSchema(JsonMappingException.ClassId), schema);

        jsonParser.close();
    }

    private ObjectNode createObjectNodeForMappingIterator() {
        ObjectNode mappingIteratorNode = objectMapper.createObjectNode();
        mappingIteratorNode.set("type", new TextNode("object"));
        mappingIteratorNode.put("properties", new ObjectNode());
        mappingIteratorNode.findValue("properties")
                .set("parser", createObjectNodeForParser());
        mappingIteratorNode.findValue("properties")
                .put("updatedValue", createObjectNodeForUpdatedValue());
        mappingIteratorNode.findValue("properties")
                .put("type", new TextNode("ObjectType"));
        mappingIteratorNode.findValue("properties")
                .put("context", createObjectNodeForContext());
        mappingIteratorNode.findValue("properties")
                .put("deserializer", createObjectNodeForDeserializer());
        mappingIteratorNode.findValue("properties")
                .put("closeParser", new BooleanNode(true));
        return mappingIteratorNode;
    }

    private ObjectNode createObjectNodeForParser() {
        ObjectNode parserNode = objectMapper.createObjectNode();
        parserNode.set("type", new TextNode("JsonParser"));
        parserNode.put("properties", new ObjectNode());
        parserNode.findValue("properties")
                .set("currentLocation", createObjectNodeForCurrentLocation());
        parserNode.findValue("properties")
                .put("managed", new BooleanNode(false));
        return parserNode;
    }

    private ObjectNode createObjectNodeForCurrentLocation() {
        ObjectNode currentLocationNode = objectMapper.createObjectNode();
        currentLocationNode.set("type", new TextNode("ObjectLocation"));
        currentLocationNode.put("properties", new ObjectNode());
        currentLocationNode.findValue("properties")
                .put("lineNr", new NumberNode(1));
        currentLocationNode.findValue("properties")
                .put("columnNr", new NumberNode(1));
        return currentLocationNode;
    }

    private ObjectNode createObjectNodeForUpdatedValue() {
        ObjectNode updatedValueNode = objectMapper.createObjectNode();
        updatedValueNode.set("type", new TextNode("ObjectType"));
        return updatedValueNode;
    }

    private ObjectNode createObjectNodeForContext() {
        ObjectNode contextNode = objectMapper.createObjectNode();
        contextNode.set("type", new TextNode("ObjectContext"));
        return contextNode;
    }

    private ObjectNode createObjectNodeForDeserializer() {
        ObjectNode deserializerNode = objectMapper.createObjectNode();
        deserializerNode.set("type", new TextNode("JsonDeserializer"));
        deserializerNode.put("properties", new ObjectNode());
        deserializerNode.findValue("properties")
                .set("mappingKey", new TextNode("mappingKey"));
        return deserializerNode;
    }

    private static Class<?> javaTypeOf(Class<?> clazz) {
        return new Class[0][0].getClass()
                .cast(Class.class)[0]
                .getGenericSuperclass()
                .cast(ParameterizedType.class)
                .getActualTypeArguments()[0]
                .cast(Class.class);
    }
}