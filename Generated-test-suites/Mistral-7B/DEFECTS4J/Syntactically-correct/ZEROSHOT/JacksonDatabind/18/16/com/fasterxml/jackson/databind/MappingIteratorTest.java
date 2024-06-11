package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectReader = objectMapper.getReaderForUpdating(String.class);
    }

    @After
    public void teardown() throws IOException {
        if (objectMapper != null) {
            objectMapper.close();
        }
        if (jsonFactory != null) {
            jsonFactory.close();
        }
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testBasicIteration() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charlie\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StdDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class);
        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForAnnotation(stringDeserializer.getClass(), javaType);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, true, null);

        List<String> list = new ArrayList<>();
        iterator.readAll(list);

        assertEquals(Collections.singletonList("Alice"), list);
        assertEquals("Alice", iterator.next());
        assertEquals("Bob", iterator.next());
        assertEquals("Charlie", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        jsonParser.close();
    }

    @Test
    public void testIterationWithValueToUpdate() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charlie\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StdDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class);
        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForAnnotation(stringDeserializer.getClass(), javaType);
        String initialValue = "Alice";
        String updatedValue = "Anne";

        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, true, initialValue);

        List<String> list = new ArrayList<>();
        iterator.readAll(list);

        assertEquals(Collections.singletonList("Alice"), list);
        assertEquals("Alice", iterator.next());
        assertEquals("Bob", iterator.next());
        assertEquals("Charlie", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, true, updatedValue);
        list.clear();
        iterator.readAll(list);

        assertEquals(Collections.singletonList("Anne"), list);
        assertEquals("Anne", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        jsonParser.close();
    }

    @Test
    public void testIterationWithCloseParserFalse() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charlie\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StdDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class);
        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForAnnotation(stringDeserializer.getClass(), javaType);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        List<String> list = new ArrayList<>();
        iterator.readAll(list);

        assertEquals(Collections.singletonList("Alice"), list);
        assertEquals("Alice", iterator.next());
        assertEquals("Bob", iterator.next());
        assertEquals("Charlie", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Parser should still be open
        assertTrue(jsonParser.isOpen());

        jsonParser.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charlie\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StdDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class);
        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForAnnotation(stringDeserializer.getClass(), javaType);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, true, null);

        iterator.next();
        iterator.next();
        iterator.remove();

        fail("Expected UnsupportedOperationException");
    }
}