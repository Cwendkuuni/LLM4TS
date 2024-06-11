package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionCreation() {
        String message = "Test JsonMappingException creation";
        JsonMappingException e = new JsonMappingException(message);
        assertEquals(message, e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCreationWithLocation() {
        String message = "Test JsonMappingException creation with location";
        JsonNode location = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        JsonMappingException e = new JsonMappingException(message, location);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCreationWithProcessor() {
        String message = "Test JsonMappingException creation with processor";
        Closeable processor = null;
        JsonMappingException e = new JsonMappingException(processor, message);
        assertNull(e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCreationWithLocationAndProcessor() {
        String message = "Test JsonMappingException creation with location and processor";
        JsonNode location = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        Closeable processor = null;
        JsonMappingException e = new JsonMappingException(processor, message, location);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCreationWithRootCause() {
        String message = "Test JsonMappingException creation with root cause";
        Throwable rootCause = new IOException("Root cause test");
        JsonMappingException e = new JsonMappingException(rootCause, message);
        assertEquals(message, e.getMessage());
        assertNull(e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertEquals(rootCause, e.getRootCause());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionCreationWithLocationAndRootCause() {
        String message = "Test JsonMappingException creation with location and root cause";
        JsonNode location = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        Throwable rootCause = new IOException("Root cause test");
        JsonMappingException e = new JsonMappingException(rootCause, message, location);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertEquals(rootCause, e.getRootCause());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionCreationWithProcessorAndRootCause() {
        String message = "Test JsonMappingException creation with processor and root cause";
        Closeable processor = null;
        Throwable rootCause = new IOException("Root cause test");
        JsonMappingException e = new JsonMappingException(processor, message, rootCause);
        assertNull(e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertEquals(rootCause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCreationWithLocationProcessorAndRootCause() {
        String message = "Test JsonMappingException creation with location, processor, and root cause";
        JsonNode location = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        Closeable processor = null;
        Throwable rootCause = new IOException("Root cause test");
        JsonMappingException e = new JsonMappingException(processor, message, location, rootCause);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertEquals(rootCause, e.getRootCause());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOException() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        String expectedMessage = "Unexpected IOException (of type java.io.IOException): Unexpected IOException";
        assertEquals(expectedMessage, e.getMessage());
        assertNull(e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromParser() {
        String input = "{\"name\": \"John\", \"age\": 30, \"cars\": [1, 2, 3]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = null;
        try {
            mapper.readValue(new ByteArrayInputStream(input.getBytes()), JsonNode.class);
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from ArrayValue(number,[1, 2, 3])", e.getMessage());
        JsonNode expectedLocation = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 5).build();
        assertEquals(expectedLocation, e.getLocation());
        assertNull(e.getFrom());
        JsonNode expectedPath = JsonNode.builder()
                .put("0", JsonNode.builder()
                        .put("name", JsonNode.Value.textValue("name"))
                        .put("cars", JsonNode.Value.arrayValue(
                                JsonNode.Value.numberValue(1),
                                JsonNode.Value.numberValue(2),
                                JsonNode.Value.numberValue(3)))
                        .build())
                .build();
        assertEquals(expectedPath, e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromParserWithRootCause() {
        String input = "{\"name\": \"John\", \"age\": 30, \"cars\": [1, 2, 3]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = null;
        try {
            mapper.readValue(new ByteArrayInputStream(input.getBytes()), String.class);
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("Cannot deserialize instance of java.lang.String out of START_ARRAY token", e.getMessage());
        JsonNode expectedLocation = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        assertEquals(expectedLocation, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        IOException rootCause = new IOException("Test root cause");
        assertEquals(rootCause.getClass().getName(), e.getRootCause().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromGenerator() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = JsonNode.builder().put("name", "John").put("cars", JsonNode.arrayValue()).build();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        JsonMappingException e = null;
        try {
            writer.writeValueAsString(new ObjectNode(rootNode));
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("No serializer found for class com.fasterxml.jackson.databind.JsonNode and no properties discovered to serialize", e.getMessage());
        assertNull(e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromGeneratorWithRootCause() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = JsonNode.builder().put("name", "John").put("cars", JsonNode.arrayValue()).build();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        JsonMappingException e = null;
        try {
            writer.writeValueAsString(new ArrayNode(rootNode));
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("No serializer found for class com.fasterxml.jackson.databind.node.ArrayNode and no properties discovered to serialize", e.getMessage());
        JsonNode expectedLocation = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        assertEquals(expectedLocation, e.getLocation());
        assertNull(e.getFrom());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
        IOException rootCause = new IOException("Test root cause");
        assertEquals(rootCause.getClass().getName(), e.getRootCause().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("name", "John");
        rootNode.set("age", 30);
        rootNode.set("cars", mapper.createArrayNode());
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        JsonMappingException e = null;
        try {
            writer.writeValueAsString(new ObjectNode(rootNode));
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("No serializer found for class com.fasterxml.jackson.databind.JsonNode and no properties discovered to serialize", e.getMessage());
        JsonNode expectedLocation = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 1).build();
        assertEquals(expectedLocation, e.getLocation());
        assertNull(e.getFrom());
        List<Reference> path = e.getPath();
        assertNotNull(path);
        assertEquals(3, path.size());
        Reference reference0 = path.get(0);
        assertEquals("name", reference0.getFieldName());
        assertNull(reference0.getIndex());
        Reference reference1 = path.get(1);
        assertEquals("cars", reference1.getFieldName());
        assertNull(reference1.getIndex());
        Reference reference2 = path.get(2);
        assertEquals("0", reference2.getFieldName());
        assertEquals(-1, reference2.getIndex());
        assertNull(e.getProcessor());
        assertNull(e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndRootCause() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("name", "John");
        rootNode.set("age", 30);
        rootNode.set("cars", mapper.createArrayNode());
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        JsonMappingException e = null;
        try {
            writer.writeValueAsString(new ObjectNode(rootNode).put("0", "test"));
        } catch (JsonMappingException ex) {
            e = ex;
        }
        assertEquals("Cannot deserialize instance of java.lang.String out of VALUE_NUMBER_INT type", e.getMessage());
        JsonNode expectedLocation = JsonNode.builder().put("lineNumber", 1).put("columnNumber", 38).build();
        assertEquals(expectedLocation, e.getLocation());
        assertNull(e.getFrom());
        List<Reference> path = e.getPath();
        assertNotNull(path);
        assertEquals(3, path.size());
        Reference reference0 = path.get(0);
        assertEquals("name", reference0.getFieldName());
        assertNull(reference0.getIndex());
        Reference reference1 = path.get(1);
        assertEquals("cars", reference1.getFieldName());
        assertNull(reference1.getIndex());
        Reference reference2 = path.get(2);
        assertEquals("0", reference2.getFieldName());
        assertEquals(-1, reference2.getIndex());
        assertNull(e.getProcessor());
        IOException rootCause = new IOException("Test root cause");
        assertEquals(rootCause.getClass().getName(), e.getRootCause().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getRootCause().getMessage());
    }
}