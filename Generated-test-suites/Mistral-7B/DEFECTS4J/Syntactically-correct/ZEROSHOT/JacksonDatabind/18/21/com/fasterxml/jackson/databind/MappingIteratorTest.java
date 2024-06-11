package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer.UnwrappingDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.StdFromStringDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappingIteratorTest {

    private static ObjectMapper objectMapper;

    @org.junit.BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @org.junit.Test
    public void testEmptyIterator() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = MappingIterator.emptyIterator();

        assertFalse("iterator should not have next element", iterator.hasNext());
        assertNull("iterator should not have next value", iterator.next());
    }

    @org.junit.Test
    public void testHasNextAndNextWithDefaultValues() throws Exception {
        ObjectReader reader = objectMapper.readerForElement(String.class);
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[]");

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be null", null, iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be null", null, iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNextAndNextWithUpdatedValue() throws Exception {
        ObjectReader reader = objectMapper.readerForValue(new String[]{"test1", "test2"});
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, "test1");

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be test1", "test1", iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be test2", "test2", iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNextAndNextWithClosedParser() throws Exception {
        ObjectReader reader = objectMapper.readerForValue(new String[]{"test1", "test2"});
        JsonParser jsonParser = reader.getParser();
        jsonParser.close();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, true, null);

        assertFalse("iterator should not have next element", iterator.hasNext());
        assertNull("iterator should not have next value", iterator.next());
    }

    @org.junit.Test
    public void testHasNextAndNextWithWrappedArray() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be test1", "test1", iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be test2", "test2", iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNextAndNextWithUnwrappedArray() throws Exception {
        ObjectReader reader = objectMapper.readerForValue(new String[]{"test1", "test2"}
                , ObjectReader.Feature.UNWRAP_ROOT_VALUE);
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be test1", "test1", iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be test2", "test2", iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNextAndNextWithCustomDeserializer() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[1, 2, 3]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> customDeserializer = new StdFromStringDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), customDeserializer, false, null);

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be 1", 1, iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be 2", 2, iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("third element should be 3", 3, iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNextAndNextWithCustomDeserializerAndUpdatedValue() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[1, 2, 3]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> customDeserializer = new StdFromStringDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), customDeserializer, false, 0);

        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("first element should be 1", 1, iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("second element should be 2", 2, iterator.next());
        assertTrue("iterator should have next element", iterator.hasNext());
        assertEquals("third element should be 3", 3, iterator.next());
        assertFalse("iterator should not have next element", iterator.hasNext());

        // check that updated value is used
        assertEquals("updated value should be 3", 3, iterator.getUpdatedValue());
    }

    @org.junit.Test
    public void testReadAllWithDefaultValues() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 0", 0, resultList.size());
    }

    @org.junit.Test
    public void testReadAllWithUpdatedValue() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, "test1");

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 2", 2, resultList.size());
        assertEquals("first element of result list should be test1", "test1", resultList.get(0));
        assertEquals("second element of result list should be test2", "test2", resultList.get(1));
    }

    @org.junit.Test
    public void testReadAllWithClosedParser() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();
        jsonParser.close();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, true, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 0", 0, resultList.size());
    }

    @org.junit.Test
    public void testReadAllWithWrappedArray() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 2", 2, resultList.size());
        assertEquals("first element of result list should be test1", "test1", resultList.get(0));
        assertEquals("second element of result list should be test2", "test2", resultList.get(1));
    }

    @org.junit.Test
    public void testReadAllWithUnwrappedArray() throws Exception {
        ObjectReader reader = objectMapper.readerForValue(new String[]{"test1", "test2"}
                , ObjectReader.Feature.UNWRAP_ROOT_VALUE);
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 2", 2, resultList.size());
        assertEquals("first element of result list should be test1", "test1", resultList.get(0));
        assertEquals("second element of result list should be test2", "test2", resultList.get(1));
    }

    @org.junit.Test
    public void testReadAllWithCustomDeserializer() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[1, 2, 3]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> customDeserializer = new StdFromStringDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), customDeserializer, false, null);

        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 3", 3, resultList.size());
        assertEquals("first element of result list should be 1", 1, resultList.get(0));
        assertEquals("second element of result list should be 2", 2, resultList.get(1));
        assertEquals("third element of result list should be 3", 3, resultList.get(2));
    }

    @org.junit.Test
    public void testReadAllWithCustomDeserializerAndUpdatedValue() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[1, 2, 3]");
        JsonParser jsonParser = reader.getParser();

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> customDeserializer = new StdFromStringDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), customDeserializer, false, 0);

        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("size of result list should be 3", 3, resultList.size());
        assertEquals("first element of result list should be 1", 1, resultList.get(0));
        assertEquals("second element of result list should be 2", 2, resultList.get(1));
        assertEquals("third element of result list should be 3", 3, resultList.get(2));

        // check that updated value is used
        assertEquals("updated value should be 3", 3, iterator.getUpdatedValue());
    }

    @org.junit.Test
    public void testGetParser() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        MappingIterator<String> iterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        JsonParser parser = iterator.getParser();
        assertEquals("parsers should be the same", jsonParser, parser);
    }

    @org.junit.Test
    public void testGetParserSchema() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        MappingIterator<String> iterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        FormatSchema parserSchema = iterator.getParserSchema();
        assertNotNull("parser schema should not be null", parserSchema);
    }

    @org.junit.Test
    public void testGetterCurrentLocation() throws Exception {
        ObjectReader reader = objectMapper.readerForValue("[\"test1\",\"test2\"]");
        JsonParser jsonParser = reader.getParser();

        MappingIterator<String> iterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, objectMapper.getDeserializationContext(), null, false, null);

        JsonLocation currentLocation = iterator.getCurrentLocation();
        assertNotNull("current location should not be null", currentLocation);
    }
}