package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        mapper.close();
    }

    @Test
    public void testSimpleSerializationException() {
        try {
            mapper.writeValueAsString(null);
        } catch (JsonMappingException e) {
            assertEquals("Null value (not serializeable)", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertNull(e.getPath());
            assertNull(e.getProcessor());
            return;
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test
    public void testSerializationExceptionWithPath() {
        String jsonInput = "{\"name\": null, \"age\": [null, 2, null]}";
        try {
            mapper.readValue(jsonInput, MyClass.class);
        } catch (JsonMappingException e) {
            assertEquals("Invalid definition for serialization field \"name\" (expected class java.lang.String, got null)", e.getMessage());
            List<Reference> path = e.getPath();
            assertEquals(3, path.size());
            Reference ref0 = path.get(0);
            assertEquals("MyClass", ref0.getFrom().getClass().getName());
            assertEquals("name", ref0.getFieldName());
            Reference ref1 = path.get(1);
            assertEquals("MyClass", ref1.getFrom().getClass().getName());
            assertEquals("age", ref1.getFieldName());
            Reference ref2 = path.get(2);
            assertEquals(int[].class, ref2.getFrom().getClass());
            assertEquals(2, ref2.getIndex());
            return;
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test
    public void testDeserializationException() {
        String jsonInput = "{\"name\": \"John\", \"age\": null}";
        try {
            mapper.readValue(jsonInput, MyClass.class);
        } catch (JsonMappingException e) {
            assertEquals("Invalid definition for deserialization field \"age\" (Cannot construct instance of java.lang.Integer from value (null) (expected java.lang.Number but found null))", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertNull(e.getPath());
            assertNull(e.getProcessor());
            return;
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test
    public void testDeserializationExceptionWithPath() {
        String jsonInput = "{\"employees\": [{\"name\": \"John\", \"department\": null}]}";
        try {
            mapper.readValue(jsonInput, Employees.class);
        } catch (JsonMappingException e) {
            assertEquals("Invalid definition for deserialization field \"department\" of com.fasterxml.jackson.databind.test.Employees.Department (Cannot construct instance of com.fasterxml.jackson.databind.test.Employees$Department from value (null) (expected java.lang.String but found null))", e.getMessage());
            List<Reference> path = e.getPath();
            assertEquals(2, path.size());
            Reference ref0 = path.get(0);
            assertEquals("Employees", ref0.getFrom().getClass().getName());
            assertEquals("employees", ref0.getFieldName());
            Reference ref1 = path.get(1);
            assertEquals("Employees$Department", ref1.getFrom().getClass().getName());
            return;
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    static class MyClass {
        private String name;
        private Integer age;
    }

    static class Employees {
        private List<Employee> employees;

        static class Employee {
            private String name;
            private Department department;
        }

        static class Department {}
    }
}