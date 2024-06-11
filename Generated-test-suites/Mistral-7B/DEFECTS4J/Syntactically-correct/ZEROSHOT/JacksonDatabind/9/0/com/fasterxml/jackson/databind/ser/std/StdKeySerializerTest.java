package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Test class for {@link StdKeySerializer}.
 */
public class StdKeySerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeDate() throws JsonProcessingException, IOException {
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        String output = System.out.getOutputStream().toString();
        String expected = "\"$date\""; // Quote marks added for comparison
        assertEquals(expected, output);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException, IOException {
        String key = "testKey";
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(key, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        String output = System.out.getOutputStream().toString();
        String expected = "\"testKey\""; // Quote marks added for comparison
        assertEquals(expected, output);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        serializer.acceptJsonFormatVisitor(visitor, javaType);
        String expected = "string";
        assertEquals(expected, visitor.getFormatName());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonProcessingException, JsonMappingException {
        Integer integer = 1;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(integer, jsonGenerator, new SerializerProvider());
    }
}