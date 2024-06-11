package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.type.ClassIdType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.core.util.TestUtils.createParser;
import static org.mockito.Mockito.*;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonParser parser;

    @BeforeClass
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        parser = createParser(new ByteArrayInputStream(
                Base64Variants.BASE64_STANDARD.decodeBase64("[" +
                        "{\"name\":\"Alice\",\"age\":30}," +
                        "{\"name\":\"Bob\",\"age\":25}," +
                        "{\"name\":\"Charlie\",\"age\":35}" +
                        "]")), new Version(2, 11, 2));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        iterator.hasNext();
        iterator.next();
    }

    @Test
    public void testHasNextAndNextWhenNotClosed() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(String.class);
        MappingIterator<String> iterator = new MappingIterator<>(
                new ClassIdType(String.class), parser, mapper.getDeserializationContext(),
                stringDeserializer, true, null);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Alice", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Bob", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Charlie", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expectedExceptions = IOException.class)
    public void testHasNextAndNextWhenClosed() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(String.class);
        MappingIterator<String> iterator = new MappingIterator<>(
                new ClassIdType(String.class), parser, mapper.getDeserializationContext(),
                stringDeserializer, true, null);
        iterator.close();
        iterator.hasNext();
        iterator.next();
    }

    @Test
    public void testCloseParserWhenManaged() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(String.class);
        MappingIterator<String> iterator = new MappingIterator<>(
                new ClassIdType(String.class), parser, mapper.getDeserializationContext(),
                stringDeserializer, true, null);
        parser.nextToken();
        iterator.next();
        iterator.close();
        verify(parser, times(1)).close();
    }

    @Test
    public void testCloseParserWhenUnmanaged() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(String.class);
        MappingIterator<String> iterator = new MappingIterator<>(
                new ClassIdType(String.class), parser, mapper.getDeserializationContext(),
                stringDeserializer, false, null);
        parser.nextToken();
        iterator.next();
        iterator.close();
        verify(parser, never()).close();
    }

    @Test
    public void testReadAllWithArrayList() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(List.class,
                        new ClassIdType(String.class));
        MappingIterator<String> iterator = new MappingIterator<>(
                javaType, parser, mapper.getDeserializationContext(),
                stringDeserializer, true, null);
        List<String> resultList = iterator.readAll();
        Assert.assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), resultList);
    }

    @Test
    public void testReadAllWithArrayListAndUpdatedValue() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(List.class,
                        new ClassIdType(String.class));
        String value = "value";
        MappingIterator<String> iterator = new MappingIterator<>(
                javaType, parser, mapper.getDeserializationContext(),
                stringDeserializer, true, value);
        List<String> resultList = iterator.readAll();
        Assert.assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), resultList);
        Assert.assertEquals(value, "Charlie");
    }

    @Test
    public void testReadAllWithCollection() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(ArrayList.class,
                        new ClassIdType(String.class));
        MappingIterator<String> iterator = new MappingIterator<>(
                javaType, parser, mapper.getDeserializationContext(),
                stringDeserializer, true, null);
        ArrayList<String> resultList = (ArrayList<String>) iterator.readAll();
        Assert.assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), resultList);
    }

    @Test
    public void testReadAllWithCollectionAndUpdatedValue() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        JavaType javaType = TypeFactory.defaultInstance()
                .constructSimpleType(ArrayList.class,
                        new ClassIdType(String.class));
        String value = "value";
        MappingIterator<String> iterator = new MappingIterator<>(
                javaType, parser, mapper.getDeserializationContext(),
                stringDeserializer, true, value);
        ArrayList<String> resultList = (ArrayList<String>) iterator.readAll();
        Assert.assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), resultList);
        Assert.assertEquals(value, "Charlie");
    }
}