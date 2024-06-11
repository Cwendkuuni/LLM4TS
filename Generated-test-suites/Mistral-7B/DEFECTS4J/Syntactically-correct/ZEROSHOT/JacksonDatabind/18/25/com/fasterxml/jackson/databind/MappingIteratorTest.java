package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer.BeanDeserializerBase;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @BeforeMethod
    public void setUp() {
        jsonFactory = new ObjectMapper().getFactory();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicUsage() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        String[] values = {"Hello", "World"};
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(values);

        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<String> deserializer = context.findValueDeserializer(String.class);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, false, null);

        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals(mappingIterator.next(), "Hello");
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals(mappingIterator.next(), "World");
        Assert.assertFalse(mappingIterator.hasNext());

        mapping.close();
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchElementException() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new String[]{"Hello", "World"});

        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<String> deserializer = context.findValueDeserializer(String.class);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, false, null);

        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.remove(); // UnsupportedOperationException should be thrown here

        mappingIterator.next(); // NoSuchElementException should be thrown here
    }

    @Test
    public void testReadAll() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        String[] values = {"Hello", "World"};
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(values);

        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<String> deserializer = context.findValueDeserializer(String.class);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, false, null);

        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(list.size(), 2);
        Assert.assertEquals(list.get(0), "Hello");
        Assert.assertEquals(list.get(1), "World");
    }
}