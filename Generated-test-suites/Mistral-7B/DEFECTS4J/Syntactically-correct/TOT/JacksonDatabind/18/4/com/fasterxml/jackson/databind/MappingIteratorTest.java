package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.junit.BaseTest;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class MappingIteratorTest extends BaseTest {

    private JsonFactory factory;
    private ObjectMapper objectMapper;
    private DeserializationContext context;

    @BeforeMethod
    public void setUp() throws Exception {
        factory = new JsonFactory();
        JavaTimeModule module = new JavaTimeModule();
        module.findAndRegisterModules();
        objectMapper = new ObjectMapper(factory);
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testSingleElementIterator() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDeserializer() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, LocalDateTime.class);
        Converter<String, LocalDateTime> dateConverter = context.findValueDeserializer(LocalDateTime.class);
        MappingIterator<LocalDateTime> iterator = new MappingIterator<>(type, parser, context, dateConverter, false, null);
        assertTrue(iterator.hasNext());
        assertEquals(LocalDateTime.now(), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(LocalDateTime.now().plusMinutes(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithClosedParser() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JsonParser closedParser = parser;
        JsonFactory newFactory = new JsonFactory();
        JsonParser newParser = newFactory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, true, null);
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next());
        assertFalse(iterator.hasNext());
        closeParser(closedParser);
        assertThrows(IOException.class, () -> iterator.next());
        MappingIterator<String> newIterator = new MappingIterator<>(type, newParser, context, null, false, null);
        assertTrue(newIterator.hasNext());
        assertEquals("", newIterator.next());
        assertTrue(newIterator.hasNext());
        assertEquals("", newIterator.next());
        assertFalse(newIterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidInput() throws IOException {
        JsonParser parser = factory.createParser("123");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputEndArray() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithInputEndObject() throws IOException {
        JsonParser parser = factory.createParser("{}");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputEmptyArray() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputEmptyObject() throws IOException {
        JsonParser parser = factory.createParser("{}");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputNull() throws IOException {
        JsonParser parser = factory.createParser(null);
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputException() throws IOException {
        JsonParser parser = factory.createParser("{\"invalid\":"+JsonParseException.class.getName()+"()}");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithInputEmptyList() throws IOException {
        JsonParser parser = factory.createParser("[]");
        JavaType type = context.constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testIteratorWithInputList() throws IOException {
        JsonParser parser = factory.createParser("[1,2,3]");
        JavaType type = context.constructCollectionType(List.class, Integer.class);
        List<Integer> resultList = new ArrayList<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        iterator.readAll(resultList);
        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testIteratorWithInputCollection() throws IOException {
        JsonParser parser = factory.createParser("[1,2,3]");
        JavaType type = context.constructCollectionType(HashSet.class, Integer.class);
        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        Collection<Integer> resultList = new ArrayList<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testIteratorWithInputEmptyCollection() throws IOException {
        JsonParser parser = factory.createParser("{}");
        JavaType type = context.constructCollectionType(HashSet.class, Integer.class);
        Collection<Integer> resultList = new ArrayList<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(type, parser, context, null, false, null);
        iterator.readAll(resultList);
        assertTrue(resultList.isEmpty());
    }

    private static void closeParser(JsonParser parser) throws IOException {
        parser.close();
    }
}