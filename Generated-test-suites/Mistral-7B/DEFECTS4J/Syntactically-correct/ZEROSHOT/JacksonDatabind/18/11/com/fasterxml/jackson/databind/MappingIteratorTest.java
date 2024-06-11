package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer.ContextualDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory factory;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        reader = mapper.readerForListOf(String.class);
        factory = mapper.getFactory();
        parser = factory.createParser("[]");
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<?> emptyIterator = MappingIterator.emptyIterator();

        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());

        emptyIterator.close();
    }

    @Test
    public void testIteratorCreation() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenReturn(Collections.emptyList());

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());
        assertSame(Collections.emptyList(), iterator.nextValue());

        iterator.close();
    }

    @Test
    public void testIteratorCreationWithProvidedParser() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenReturn(Collections.singletonList("test"));

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, false, null);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNextValue());
        List<String> nextValue = iterator.nextValue();
        assertSame("test", nextValue.get(0));
        assertFalse(iterator.hasNextValue());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testIteratorCreationWithProvidedValue() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenReturn(Collections.singletonList("test"));

        Object value = new Object();
        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, false, value);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNextValue());
        List<String> nextValue = iterator.nextValue();
        assertSame("test", nextValue.get(0));
        assertSame(value, iterator.getUpdatedValue());
        assertFalse(iterator.hasNextValue());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testReadAll() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenReturn(Arrays.asList("test1", "test2"));

        List<String> expectedResult = Arrays.asList("test1", "test2");

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);

        List<String> result = iterator.readAll();

        assertSame(expectedResult, result);

        iterator.close();
    }

    @Test
    public void testReadAllWithProvidedList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenReturn(Arrays.asList("test1", "test2"));

        List<String> resultList = new ArrayList<>();
        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, resultList);

        List<String> expectedResult = Arrays.asList("test1", "test2");

        iterator.readAll();

        assertSame(expectedResult, resultList);

        iterator.close();
    }

    @Test
    public void testReadAllWithContextualDeserializer() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        ContextualDeserializer<List<String>> deserializer = mock(ContextualDeserializer.class);
        when(deserializer.createContextual(mapper.getDeserializationContext(), null)).thenReturn(mock(ListDeserializer.class));

        List<String> expectedResult = Arrays.asList("test1", "test2");

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);

        List<String> result = iterator.readAll();

        assertSame(expectedResult, result);

        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);

        iterator.next();
        iterator.next();
        iterator.next();

        iterator.next(); // should throw NoSuchElementException

        iterator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenThrow(new JsonMappingException("Test Exception"));

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, false, null);

        iterator.nextValue(); // should throw JsonMappingException

        iterator.close();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, mapper.getDeserializationContext(), null)).thenThrow(new IOException("Test Exception"));

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, false, null);

        iterator.nextValue(); // should throw IOException

        iterator.close();
    }

    @Test
    public void testCloseParser() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<List<String>> deserializer = mock(JsonDeserializer.class);

        MappingIterator<List<String>> iterator = new MappingIterator<List<String>>(type, parser, mapper.getDeserializationContext(), deserializer, true, null);

        assertTrue(parser.isOpen());

        iterator.nextValue();
        iterator.close();

        assertFalse(parser.isOpen());
    }
}