package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String inputJson = "{\"name\":\"John Doe\",\"age\":-1}";
        JsonMappingException exception = new JsonMappingException(mapper.getFactory(), "Invalid input", mapper.readValue(inputJson, Object.class));

        String expectedJson = "{\"className\":\"com.fasterxml.jackson.databind.JsonMappingException\",\"message\":\"Invalid input\",\"path\":[{\"class\":\"com.fasterxml.jackson.core.JsonParser\",\"description\":\"JsonParser@276234742\"}]}";
        String actualJson = mapper.writeValueAsString(exception);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testJsonMappingExceptionDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String inputJson = "{\"className\":\"JsonMappingException\",\"message\":\"Invalid input\",\"path\":[{\"class\":\"JsonParser\",\"description\":\"JsonParser@276234742\",\"fieldName\":\"name\"}]}";
        JsonMappingException exception = mapper.readValue(inputJson, JsonMappingException.class);

        assertEquals("Invalid input", exception.getMessage());
        assertEquals("JsonParser@276234742", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithArrayReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String inputJson = "[10,\"hello\",null]";
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory(), "Invalid input", mapper.readValue(inputJson, Object[].class));

        assertEquals("Invalid input", exception.getMessage());
        assertArrayEquals(new Object[]{"JsonFactory@631710661", "ObjectMapper@1632008477", "ObjectMapper@1632008477", "ObjectArray@483021349", "[10, hello, null]"}, exception.getPath().toArray());
    }

    @Test
    public void testJsonMappingExceptionWithStringReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String inputJson = "{\"name\":{},\"age\":10}";
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory(), "Invalid input", mapper.readValue(inputJson, Map.class));

        assertEquals("Invalid input", exception.getMessage());
        assertEquals("JsonFactory@631710661->JsonParser@550056420->MapNode@418174134", exception.getPathReference());
    }
}