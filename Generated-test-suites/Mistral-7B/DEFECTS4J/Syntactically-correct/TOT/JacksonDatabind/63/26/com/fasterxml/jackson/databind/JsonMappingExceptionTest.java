package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link JsonMappingException}.
 */
public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSimpleError() throws IOException {
        String json = "{\"name\":\"John\",\"age\":-1}";
        JsonMappingException e = mapper.readValue(json, String.class);
        assertEquals("Cannot deserialize value of type java.lang.String from ValueToken {value=''} of type [SimpleType, class java.lang.String] (for input \"\"): Failed to read ValueToken as String (class java.lang.String, value -1)", e.getMessage());
        assertNotNull(e.getPath());
        assertTrue(e.getPath().isEmpty());
    }

    @Test
    public void testErrorWithPath() throws IOException {
        Person person = new Person("John", new ArrayList<>());
        person.getChildren().add(new Person("Mike"));
        String json = mapper.writeValueAsString(person);

        JsonMappingException e = null;
        try {
            mapper.readValue(json, Apple.class);
        } catch (JsonMappingException ex) {
            e = ex;
        }

        assertEquals(5, e.getPath().size());
        assertEquals("root", e.getPath().get(0).getFieldName());
        assertEquals("children", e.getPath().get(1).getFieldName());
        assertEquals(0, e.getPath().get(2).getIndex());
        assertEquals("children", e.getPath().get(3).getFieldName());
        assertNull(e.getPath().get(4).getFieldName());

        assertEquals("Unexpected type found. Expected: class com.fasterxml.jackson.test.apple.Apple, actual: class com.fasterxml.jackson.test.person.Person", e.getMessage());
    }

    @Test
    public void testErrorWithCircularReference() throws IOException {
        ClassWithReference ref = new ClassWithReference();
        ref.setReference(ref);

        String json = mapper.writeValueAsString(ref);

        JsonMappingException e = null;
        try {
            mapper.readValue(json, ClassWithReference.class);
        } catch (JsonMappingException ex) {
            e = ex;
        }

        assertEquals(2, e.getPath().size());
        assertEquals("root", e.getPath().get(0).getFieldName());
        assertEquals("reference", e.getPath().get(1).getFieldName());

        assertEquals("Loop detected with reference: 'this' -> 'this'", e.getMessage());
    }

    @Test
    public void testErrorWithMultipleReferences() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("foo", new Person("John"));
        map.put("bar", new Person("Mike"));

        String json = mapper.writeValueAsString(map);

        JsonMappingException e = null;
        try {
            mapper.readValue(json, Array.class);
        } catch (JsonMappingException ex) {
            e = ex;
        }

        assertEquals(6, e.getPath().size());
        assertEquals("root", e.getPath().get(0).getFieldName());
        assertEquals("foo", e.getPath().get(1).getFieldName());
        assertEquals(0, e.getPath().get(2).getIndex());
        assertEquals("name", e.getPath().get(3).getFieldName());
        assertEquals("bar", e.getPath().get(4).getFieldName());
        assertEquals(0, e.getPath().get(5).getIndex());

        assertEquals("Expected array but was BEGIN_OBJECT", e.getMessage());
    }

    @Test
    public void testErrorWithUnexpectedIOException() throws IOException {
        String json = "{\"name\":\"John\",\"age\":new IOException()}";
        JsonMappingException e = mapper.readValue(json, String.class);

        assertTrue(e instanceof JsonMappingException);
        JsonMappingException jsonMappingException = (JsonMappingException) e;
        assertTrue(jsonMappingException.getCause() instanceof IOException);
        assertEquals("Unexpected IOException (of type java.io.IOException): java.io.IOException: Test IOException", jsonMappingException.getMessage());
    }

    static class Apple {}
    static class Person {
        private String name;
        private List<Person> children = new ArrayList<>();

        public Person(String name) {
            this.name = name;
        }

        public List<Person> getChildren() {
            return children;
        }

        public void setChildren(List<Person> children) {
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Array {}

    static class ClassWithReference {
        private ClassWithReference reference;

        public ClassWithReference() {
        }

        public void setReference(ClassWithReference reference) {
            this.reference = reference;
        }
    }
}