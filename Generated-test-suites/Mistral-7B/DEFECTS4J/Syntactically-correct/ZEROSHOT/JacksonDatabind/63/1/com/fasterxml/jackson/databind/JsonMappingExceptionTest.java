package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default constructor
        JsonMappingException jsonMappingException1 = new JsonMappingException("Test message");
        Assert.assertEquals("Test message", jsonMappingException1.getMessage());
        Assert.assertNull(jsonMappingException1.getFrom());
        Assert.assertNull(jsonMappingException1.getLocation());
        Assert.assertNull(jsonMappingException1.getPath());
        Assert.assertNull(jsonMappingException1.getProcessor());

        // Test constructor with message and location
        JsonParser jsonParser = mapper.getFactory().createParser(getClass().getResource("/test.json"));
        JsonMappingException jsonMappingException2 = JsonMappingException.from(jsonParser, "Test message");
        Assert.assertEquals("Test message", jsonMappingException2.getMessage());
        Assert.assertNotNull(jsonMappingException2.getLocation());
        Assert.assertNull(jsonMappingException2.getFrom());
        Assert.assertNull(jsonMappingException2.getPath());
        Assert.assertNull(jsonMappingException2.getProcessor());

        // Test constructor with message, location and root cause
        JsonMappingException jsonMappingException3 = new JsonMappingException("Test message", new IOException("Test IO exception"));
        Assert.assertEquals("Test message", jsonMappingException3.getMessage());
        Assert.assertEquals("Test IO exception", jsonMappingException3.getCause().getMessage());
        Assert.assertNull(jsonMappingException3.getLocation());
        Assert.assertNull(jsonMappingException3.getFrom());
        Assert.assertNull(jsonMappingException3.getPath());
        Assert.assertNull(jsonMappingException3.getProcessor());

        // Test constructor with message, location, root cause and processor
        Closeable closeable = mapper.getFactory().createGenerator(System.out);
        JsonMappingException jsonMappingException4 = new JsonMappingException(closeable, "Test message", new IOException("Test IO exception"));
        Assert.assertEquals("Test message", jsonMappingException4.getMessage());
        Assert.assertEquals("Test IO exception", jsonMappingException4.getCause().getMessage());
        Assert.assertNotNull(jsonMappingException4.getLocation());
        Assert.assertEquals(closeable, jsonMappingException4.getProcessor());
        Assert.assertNull(jsonMappingException4.getPath());

        // Test wrapWithPath method with non-indexed object
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key1", new Object());
        Object refFrom = map;
        String refFieldName = "key1";
        JsonMappingException jsonMappingException5 = JsonMappingException.wrapWithPath(new IOException("Test IO exception"), refFrom, refFieldName);
        Assert.assertEquals("Test IO exception (was java.io.IOException)", jsonMappingException5.getMessage());
        Assert.assertEquals("java.util.LinkedHashMap$Entry[key=key1, value=TestObject@5b4b6b82]", jsonMappingException5.getPathReference().toString());

        // Test wrapWithPath method with indexed object
        Object[] array = new Object[1];
        array[0] = new Object();
        int index = 0;
        JsonMappingException jsonMappingException6 = JsonMappingException.wrapWithPath(new IOException("Test IO exception"), array, index);
        Assert.assertEquals("Test IO exception (was java.io.IOException)", jsonMappingException6.getMessage());
        Assert.assertEquals("[Ljava.lang.Object;@55ab1b23->[Ljava.lang.Object;@1542af9d]", jsonMappingException6.getPathReference().toString());

        // Test getPath method
        JsonParser jsonParser1 = mapper.getFactory().createParser(getClass().getResource("/test.json"));
        JsonMappingException jsonMappingException7 = JsonMappingException.from(jsonParser1, "Test message");
        jsonMappingException7.prependPath(jsonParser1, "parser");
        jsonMappingException7.prependPath(map, "map");
        map.put("key2", new Object());
        jsonMappingException7.prependPath(map.get("key2"), "field2");
        jsonMappingException7.prependPath(array, 0);
        Assert.assertEquals(Arrays.asList(
                new Reference(map, "map"),
                new Reference(map.get("key2"), "field2"),
                new Reference(array, 0)
        ), jsonMappingException7.getPath());

        // Test getPathReference method
        StringBuilder sb = new StringBuilder();
        JsonMappingException jsonMappingException8 = JsonMappingException.from(jsonParser1, "Test message");
        jsonMappingException8.prependPath(jsonParser1, "parser");
        jsonMappingException8.prependPath(map, "map");
        map.put("key2", new Object());
        jsonMappingException8.prependPath(map.get("key2"), "field2");
        jsonMappingException8.prependPath(array, 0);
        Assert.assertEquals("parser->map->field2->[0]", jsonMappingException8.getPathReference().toString());
    }
}