package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":18,\"cars\":[{\"color\":\"red\",\"model\":\"corvette\"}]}";

        try {
            mapper.readValue(new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes())), Person.class);
        } catch (JsonMappingException e) {
            // Test getting the path of references
            List<JsonMappingException.Reference> refs = e.getPath();
            assertEquals(3, refs.size());
            assertEquals("Person$1", refs.get(0).getDescription());
            assertEquals("age", refs.get(1).getFieldName());
            assertEquals(1, refs.get(2).getIndex());

            // Test getting the underlying exception
            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof NumberFormatException);

            // Test getting the processor
            Closeable processor = e.getProcessor();
            assertTrue(processor instanceof JsonParser);
        }
    }

    @Test
    public void testJsonMappingExceptionWithCustomMessage() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":[\"INVALID\"],\"cars\":[{\"color\":\"red\",\"model\":\"corvette\"}]}";

        try {
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.readValue(new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes())), Person.class);
        } catch (JsonMappingException e) {
            assertEquals("Invalid property 'age' of bean [class com.fasterxml.jackson.databind.test.Person]: expected a value of type [int] but found [java.lang.String] (for 'age' property)", e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithCustomMessageAndPath() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":[{\"name\":\"InvalidAge\",\"value\":\"INVALID\"}],\"cars\":[{\"color\":\"red\",\"model\":\"corvette\"}]}";

        try {
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.readValue(new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes())), Person.class);
        } catch (JsonMappingException e) {
            assertEquals("Invalid property 'age' of bean [class com.fasterxml.jackson.databind.test.Person]: expected a value of type [java.lang.Integer] but found [com.fasterxml.jackson.databind.test.InvalidAge]", e.getMessage());
            List<JsonMappingException.Reference> refs = e.getPath();
            assertEquals(3, refs.size());
            assertEquals("Person$1", refs.get(0).getDescription());
            assertEquals("age", refs.get(1).getFieldName());
            assertEquals(0, refs.get(2).getIndex());
        }
    }

    static class Person {
        private int age;
        private String name;
        private Car car;

        @JsonIgnore
        private List<InvalidAge> ages = new ArrayList<>();

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }

        public List<InvalidAge> getAges() {
            return ages;
        }

        public static class Car {
        }

        public static class InvalidAge {
            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}

class ByteArrayInputStream extends InputStream {
    private final byte[] bytes;

    public ByteArrayInputStream(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int read() {
        return (bytes.length > 0) ? bytes[0] : -1;
    }
}