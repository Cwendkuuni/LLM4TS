package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper = new JsonMapper();

    @Test
    public void testBasicSerialization() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJOIn("myBean", new MyBean());
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        mapper.readerForValue(JsonMappingException.class).readValue(json);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJOIn("myBean", new MyBean());
        rootNode.put("invalidField", mapper.readerForValue(MyOtherClass.class));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        mapper.readerForValue(JsonMappingException.class).readValue(json);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"myBean\": {\"field\": [1, 2, 3], \"invalidField\": \"invalidValue\"}}";
        JsonMappingException exception = mapper.readerForValue(JsonMappingException.class).readValue(json);
        assertNotNull(exception.getPath());
        assertEquals(3, exception.getPath().size());
        assertEquals("field[2]", exception.getPath().get(0).getFieldName());
        assertEquals("myBean", exception.getPath().get(1).getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidReferenceAndErrorMessage() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"myBean\": {\"field\": [1, 2, null], \"invalidField\": \"invalidValue\"}}";
        JsonMappingException exception = mapper.readerForValue(JsonMappingException.class).readValue(json);
        assertNotNull(exception.getPath());
        assertEquals(3, exception.getPath().size());
        assertEquals("field[2]", exception.getPath().get(0).getFieldName());
        assertEquals("myBean", exception.getPath().get(1).getFieldName());
        assertEquals("Error message", exception.getMessage());
    }

    @Test
    public void testDeserializationWithNoReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field\": 1, \"invalidField\": null}";
        JsonMappingException exception = mapper.readerForValue(JsonMappingException.class).readValue(json);
        assertNull(exception.getPath());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithMaximumRefsReached() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            rootNode.putPOJOIn("bean" + i, new MyBean());
        }
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        mapper.readerForValue(JsonMappingException.class).readValue(json);
    }

    @Test
    public void testSerializationWithCircularReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode childNode = mapper.createObjectNode();
        rootNode.putPOJOIn("parent", new ParentChild());
        childNode.putPOJOIn("child", new ParentChild());
        childNode.put("parent", rootNode);
        rootNode.put("child", childNode);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        mapper.readerForValue(JsonMappingException.class).readValue(json);
    }

    static class MyBean {
    }

    static class MyOtherClass {
    }

    static class ParentChild {
        ParentChild parent;
        ChildChild child;

        static class ChildChild {
            ParentChild parent;
        }
    }
}