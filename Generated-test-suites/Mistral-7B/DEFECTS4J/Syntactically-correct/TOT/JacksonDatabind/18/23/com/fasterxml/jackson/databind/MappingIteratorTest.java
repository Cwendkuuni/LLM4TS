package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonMapping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory jsonFactory = new JsonFactory();
    private JsonParser parser;

    @Before
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        reader = mapper.readerForListOf(String.class);
        parser = jsonFactory.createParser("[]");
    }

    @After
    public void tearDown() throws IOException {
        if (parser != null) {
            parser.close();
        }
    }

    @Test
    public void testEmptyIterator() throws IOException {
        Iterator<String> iterator = reader.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleValueIterator() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar");
        JsonNode source = mapper.readTree(node.toString());

        Iterator<String> iterator = reader.iterator(source);
        assertTrue(iterator.hasNext());
        assertEquals("bar", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleValueIterator() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar1");
        node.put("bar", "bar2");
        JsonNode source = mapper.readTree(node.toString());

        List<String> result = new ArrayList<>();
        Iterator<String> iterator = reader.iterator(source);
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertEquals(2, result.size());
        assertEquals("bar1", result.get(0));
        assertEquals("bar2", result.get(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreValues() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar1");
        node.put("bar", "bar2");
        JsonNode source = mapper.readTree(node.toString());

        Iterator<String> iterator = reader.iterator(source);
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationError() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "not a number");
        JsonNode source = mapper.readTree(node.toString());

        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonMappingException {
                throw new JsonMappingException(JsonMappingException.FromContext.ROOT_VALUE, "Not a number");
            }
        };

        reader = mapper.readerForListOf(String.class).withValueDeserializer(deserializer);
        Iterator<String> iterator = reader.iterator(source);
        iterator.next();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar1");
        node.put("bar", "bar2");
        JsonNode source = mapper.readTree(node.toString());

        ObjectReader reader = mapper.readerForListOf(String.class);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonMappingException {
                throw new IOException("Test IOException");
            }
        };
        reader = reader.withValueDeserializer(deserializer);

        Iterator<String> iterator = reader.iterator(source);
        iterator.next();
    }

    @Test
    public void testReadAllList() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar1");
        node.put("bar", "bar2");
        JsonNode source = mapper.readTree(node.toString());

        List<String> result = reader.readAll(Collections.synchronizedList(new ArrayList<>()));
        assertEquals(2, result.size());
        assertEquals("bar1", result.get(0));
        assertEquals("bar2", result.get(1));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar1");
        node.put("bar", "bar2");
        JsonNode source = mapper.readTree(node.toString());

        Collection<String> result = new ArrayList<>();
        reader.readAll(result);
        assertEquals(2, result.size());
        assertEquals("bar1", result.iterator().next());
        assertEquals("bar2", result.iterator().next());
    }
}