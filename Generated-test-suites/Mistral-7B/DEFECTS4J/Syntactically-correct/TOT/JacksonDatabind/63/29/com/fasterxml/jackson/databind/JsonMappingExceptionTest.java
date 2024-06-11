package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingException() throws Exception {
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3], \"city\":\"New York\"}";
        Class<?> clazz = List.class;
        try {
            mapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("Expected an instance of java.util.List but was started with a string",
                    ref.getDescription());
            assertEquals("name", ref.getFieldName());
            assertEquals(String.class, ref.getFrom().getClass());
            assertEquals(JsonMappingException.Reference.class, ref.getClass());
        }
    }

    @Test
    public void testJsonMappingExceptionArrayIndex() throws Exception {
        String json = "[\"John\", 2, 3, null]";
        Class<?> clazz = int[].class;
        try {
            mapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("Expected an instance of int[] but was started with a string",
                    ref.getDescription());
            assertEquals("2", ref.getFieldName());
            assertEquals("[Ljava.lang.String;@78a49762", ref.getFrom().toString());
            assertEquals(JsonMappingException.Reference.class, ref.getClass());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionExpected() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, false);
        String json = "{}";
        mapper.readValue(json, MyBean.class);
    }

    @Test
    public void testJsonMappingExceptionNested() throws Exception {
        String json = "{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}}";
        Class<?> clazz = Map.class;
        try {
            mapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("Expected an instance of java.util.Map<String, java.lang.String> but was started with an instance of com.fasterxml.jackson.databind.JsonMappingException",
                    ref.getDescription());
            assertEquals("name", ref.getFieldName());
            assertEquals(Object.class, ref.getFrom().getClass());
            assertEquals(JsonMappingException.class, ref.getFrom().getClass());
            Reference ref2 = e.getPath().get(1);
            assertEquals("Expected an instance of java.util.Map<String, java.lang.String> but was started with a JsonMappingException",
                    ref2.getDescription());
            assertEquals("first", ref2.getFieldName());
            assertEquals(String.class, ref2.getFrom().getClass());
            assertEquals(JsonMappingException.Reference.class, ref2.getClass());
        }
    }

    static class MyBean {
        private String name;
    }
}