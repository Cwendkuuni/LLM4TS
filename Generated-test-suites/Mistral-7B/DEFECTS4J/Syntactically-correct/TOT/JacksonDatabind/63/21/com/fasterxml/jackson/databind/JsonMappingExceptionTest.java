package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingException() throws Exception {
        String jsonInput = "{\"name\": \"John\", \"age\": 30}";
        String jsonInputWithInvalidType = "{\"name\": 30, \"age\": \"Thirty\"}";

        try {
            mapper.readValue(jsonInput, String.class);
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("name", reference.getFieldName());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference", reference.getDescription());
            assertEquals("class [Ljava.lang.String;]", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("Can not deserialize instance of java.lang.String out of START_ARRAY token"));
            return;
        }

        try {
            mapper.readValue(jsonInputWithInvalidType, String.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("name", reference.getFieldName());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference", reference.getDescription());
            assertEquals("class [Ljava.lang.String;]", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("Cannot construct instance of java.lang.String from String value ('30')"));
            return;
        }

        try {
            mapper.readValue(jsonInput, Integer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("name", reference.getFieldName());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference", reference.getDescription());
            assertEquals("class [Ljava.lang.String;]", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("Can not deserialize instance of java.lang.Integer out of VALUE_STRING token"));
            return;
        }

        try {
            mapper.writeValueAsString(new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.lang.Object", reference.getDescription());
            assertEquals("null", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.lang.Object and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new Object[]{1, 2, 3});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.lang.Object[]", reference.getDescription());
            assertEquals("[Ljava.lang.Object;", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.lang.Object[] and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new String[]{"one", "two", "three"});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.lang.String[]", reference.getDescription());
            assertEquals("[Ljava.lang.String;", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.lang.String[] and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new HashMap<>());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.util.HashMap", reference.getDescription());
            assertEquals("class java.util.HashMap<Object,Object>", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.util.HashMap and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new LinkedHashMap<>());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.util.LinkedHashMap", reference.getDescription());
            assertEquals("class java.util.LinkedHashMap<Object,Object>", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.util.LinkedHashMap and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new TreeMap<>());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.util.TreeMap", reference.getDescription());
            assertEquals("class java.util.TreeMap<Object,Object>", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.util.TreeMap and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new Hashtable<>());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.util.Hashtable", reference.getDescription());
            assertEquals("class java.util.Hashtable<Object,Object>", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.util.Hashtable and no properties"));
            return;
        }

        try {
            mapper.writeValueAsString(new Properties());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.util.Properties", reference.getDescription());
            assertEquals("class java.util.Properties", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class java.util.Properties and no properties"));
            return;
        }
    }

    @Test
    public void testJsonMappingExceptionWithCustomExceptionType() throws Exception {
        String jsonInput = "{\"name\": \"John\", \"age\": 30}";

        try {
            mapper.readValue(jsonInput, CustomJsonMappingException.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("name", reference.getFieldName());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference", reference.getDescription());
            assertEquals("class [Ljava.lang.String;]", reference.toString());
            assertTrue(e.getCause() instanceof CustomJsonMappingException);
            CustomJsonMappingException customException = (CustomJsonMappingException) e.getCause();
            assertEquals("custom error message", customException.getMessage());
            return;
        }

        try {
            CustomJsonMappingException customException = new CustomJsonMappingException("custom error message");
            mapper.writeValueAsString(customException);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference reference = e.getPath().get(0);
            assertEquals("java.lang.Object", reference.getDescription());
            assertEquals("class com.fasterxml.jackson.databind.test.CustomJsonMappingException", reference.toString());
            assertEquals(JsonMappingException.class, e.getClass());
            assertTrue(e.getMessage().contains("No serializer found for class com.fasterxml.jackson.databind.test.CustomJsonMappingException and no properties"));
            return;
        }
    }
}

class CustomJsonMappingException extends JsonMappingException {
    public CustomJsonMappingException(String message) {
        super(message);
    }
}