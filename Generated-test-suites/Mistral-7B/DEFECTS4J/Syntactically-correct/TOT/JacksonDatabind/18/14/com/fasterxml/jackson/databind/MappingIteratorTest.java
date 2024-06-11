package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.util.FinderUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private InputStream inputStream;

    @Before
    public void setup() throws IOException {
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeatures(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        mapper = new ObjectMapper(jsonFactory);
        reader = mapper.readerForListOf(String.class);
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void cleanup() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testEmptyIterator() {
        Iterator<String> emptyIterator = reader.readValues(inputStream);
        Assert.assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testIteratorWithSingleElement() throws IOException {
        inputStream = new ByteArrayInputStream("[\"test\"]".getBytes());
        Iterator<String> iterator = reader.readValues(inputStream);
        Assert.assertTrue(iterator.hasNext());
        String firstElement = iterator.next();
        Assert.assertEquals("test", firstElement);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleElements() throws IOException {
        inputStream = new ByteArrayInputStream("[\"test1\", \"test2\", \"test3\"]".getBytes());
        List<String> resultList = new ArrayList<>();
        iterator = reader.readValues(inputStream);
        while (iterator.hasNext()) {
            String element = iterator.next();
            resultList.add(element);
        }
        Assert.assertEquals(Arrays.asList("test1", "test2", "test3"), resultList);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        Iterator<String> iterator = reader.readValues(inputStream);
        iterator.next();
        iterator.next();
        inputStream.close();
        iterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        inputStream = new ByteArrayInputStream("[1, 2, 3.5]".getBytes());
        reader = mapper.readerForListOf(Integer.class);
        Iterator<Integer> iterator = reader.readValues(inputStream);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testReadAllMethod() throws IOException {
        inputStream = new ByteArrayInputStream("[\"test1\", \"test2\", \"test3\"]".getBytes());
        List<String> resultList = reader.readValueAs(inputStream, new TypeReference<List<String>>() {
        });
        Iterator<String> iterator = reader.readValues(inputStream);
        List<String> list = reader.readAll(new ArrayList<>());
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Assert.assertEquals(resultList, list);
    }

    @Test
    public void testReadAllMethodWithEmptyList() throws IOException {
        inputStream = new ByteArrayInputStream("[]".getBytes());
        List<String> resultList = reader.readValueAs(inputStream, new TypeReference<List<String>>() {
        });
        List<String> emptyList = new ArrayList<>();
        reader.readAll(emptyList);
        Assert.assertEquals(resultList, emptyList);
    }

    @Test
    public void testReadAllMethodWithCollection() throws IOException {
        inputStream = new ByteArrayInputStream("[\"test1\", \"test2\", \"test3\"]".getBytes());
        List<String> expectedList = Collections.singletonList("test3");
        ObjectReader objectReader = mapper.readerForListOf(String.class);
        List<String> resultList = objectReader.readAll(new ArrayList<>());
        reader.readValues(inputStream);
        resultList.remove(0);
        Assert.assertEquals(expectedList, resultList);
    }
}