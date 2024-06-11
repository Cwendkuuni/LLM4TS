package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @BeforeClass
    public void setup() {
        jsonFactory = new ObjectMapper().getFactory();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicUsage() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                javaType, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), true, null);

        List<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "abc");
        Assert.assertEquals(list.get(1), "def");
        Assert.assertEquals(list.get(2), "ghi");
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testCloseParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                javaType, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), false, null);

        List<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "abc");
        Assert.assertEquals(list.get(1), "def");
        Assert.assertEquals(list.get(2), "ghi");
        Assert.assertFalse(mappingIterator.hasNext());

        jsonParser.close();

        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testMappingException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                null, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), true, null);

        mappingIterator.next();

        Assert.fail("Should have thrown JsonMappingException");
    }

    @Test(expectedExceptions = IOException.class)
    public void testIOException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                null, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), true, null);

        mappingIterator.nextValue();

        jsonParser.close();

        mappingIterator.nextValue();

        Assert.fail("Should have thrown IOException");
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                javaType, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), true, null);

        List<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "abc");
        Assert.assertEquals(list.get(1), "def");
        Assert.assertEquals(list.get(2), "ghi");

        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testReadAllCustomCollection() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ArrayList.class);
        JsonParser jsonParser = jsonFactory.createParser("[\"abc\", \"def\", \"ghi\"]");
        ObjectReader objectReader = objectMapper.readerForElementValueType(String.class);
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                javaType, jsonParser, objectMapper.getDeserializationContext(),
                objectReader.findDeserializer(String.class), true, null);

        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "abc");
        Assert.assertEquals(list.get(1), "def");
        Assert.assertEquals(list.get(2), "ghi");

        Assert.assertFalse(mappingIterator.hasNext());
    }
}