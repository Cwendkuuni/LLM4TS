import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionWithNullMessage() {
        JsonMappingException e = new JsonMappingException(null);
        assertNull(e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonMappingException e = new JsonMappingException("Test message");
        assertEquals("Test message", e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocation() {
        JsonParser parser = mock(JsonParser.class);
        JsonMappingException e = new JsonMappingException(parser, "Test message");
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationAndRootCause() {
        JsonParser parser = mock(JsonParser.class);
        Throwable rootCause = new IOException("IO Error");
        JsonMappingException e = new JsonMappingException(parser, "Test message", rootCause);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(rootCause, e.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationAndPath() {
        JsonParser parser = mock(JsonParser.class);
        ObjectNode node = mock(ObjectNode.class);
        JsonMappingException e = new JsonMappingException(parser, "Test message", node);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(node, e.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationPathAndRootCause() {
        JsonParser parser = mock(JsonParser.class);
        ObjectNode node = mock(ObjectNode.class);
        Throwable rootCause = new IOException("IO Error");
        JsonMappingException e = new JsonMappingException(parser, "Test message", node, rootCause);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(node, e.getPath());
        assertEquals(rootCause, e.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationPathAndReference() {
        JsonParser parser = mock(JsonParser.class);
        Object node = new Object();
        String fieldName = "field";
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(node, fieldName));
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(node, e.getPath().get(0).getFrom());
        assertEquals(fieldName, e.getPath().get(0).getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithMessageLocationPathAndReferenceNullFrom() {
        JsonParser parser = mock(JsonParser.class);
        String fieldName = "field";
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(null, fieldName));
        fail("Should throw JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithMessageLocationPathAndReferenceNullFieldName() {
        JsonParser parser = mock(JsonParser.class);
        Object from = new Object();
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(from, null));
        fail("Should throw JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithMessageLocationPathAndReferenceInvalidType() {
        JsonParser parser = mock(JsonParser.class);
        Object from = new ArrayList<>();
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(from, "field"));
        fail("Should throw JsonMappingException");
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOE() {
        IOException ioException = new IOException("Unexpected IO Exception");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO Exception", e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOEWithMessage() {
        IOException ioException = new IOException("Unexpected IO Exception");
        String message = "Custom Message";
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException, message);
        assertEquals("Custom Message: Unexpected IOException (of type IOException): Unexpected IO Exception", e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationPathAndReferenceAndProcessor() {
        JsonParser parser = mock(JsonParser.class);
        Object node = new Object();
        String fieldName = "field";
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(node, fieldName), parser);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(node, e.getPath().get(0).getFrom());
        assertEquals(fieldName, e.getPath().get(0).getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationPathAndReferenceAndProcessorNull() {
        JsonParser parser = null;
        Object node = new Object();
        String fieldName = "field";
        JsonMappingException e = new JsonMappingException(parser, "Test message", new Reference(node, fieldName));
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(node, e.getPath().get(0).getFrom());
        assertEquals(fieldName, e.getPath().get(0).getFieldName());
    }

    @Test
    public void testSerializeJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = new JsonMappingException("Test message", new ObjectNode(null));
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = mapper.writeValueAsString(e);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        assertEquals("Test message", node.get("message").asText());
        assertEquals("JsonMappingException", node.get("className").asText());
        assertEquals(e.getPath(), node.get("path"));
    }

    // Add more test cases as needed
}