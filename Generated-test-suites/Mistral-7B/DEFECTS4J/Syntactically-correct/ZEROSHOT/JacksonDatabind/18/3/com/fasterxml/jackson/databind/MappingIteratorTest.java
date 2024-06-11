package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testMappingIteratorEmpty() {
        MappingIterator<?> emptyIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testMappingIteratorHasNext() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        List<String> resultList = new ArrayList<>();

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);

        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("apple", mappingIterator.next());
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("banana", mappingiterator.next());
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("cherry", mappingIterator.next());
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testMappingIteratorNoSuchElement() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<String> mappingIterator = new MappingIterator<>(null, jsonParser, null, null, true, null);
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
    }

    @Test
    public void testMappingIteratorReadAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        List<String> resultList = new ArrayList<>();

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.readAll(resultList);

        Assert.assertEquals(Arrays.asList("apple", "banana", "cherry"), resultList);
    }

    @Test
    public void testMappingIteratorReadAllCollection() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.readAll(new ArrayList<>());
    }
}