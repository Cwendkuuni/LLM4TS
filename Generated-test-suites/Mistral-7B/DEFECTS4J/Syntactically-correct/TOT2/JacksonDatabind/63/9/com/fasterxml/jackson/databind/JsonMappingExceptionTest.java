import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * JsonMappingExceptionTest
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionWithMessage() {
        // Given
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = new JsonMappingException(expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocation() {
        // Given
        String expectedMessage = "Expected message";
        JsonLocation location = new JsonLocation();
        // When
        JsonMappingException jsonMappingException = new JsonMappingException(null, expectedMessage, location);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndRootCause() {
        // Given
        String expectedMessage = "Expected message";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = new JsonMappingException(expectedMessage, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationAndRootCause() {
        // Given
        String expectedMessage = "Expected message";
        JsonLocation location = new JsonLocation();
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = new JsonMappingException(null, expectedMessage, location, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParser() {
        // Given
        JsonParser jsonParser = null; // Initialize your JsonParser instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessage() {
        // Given
        JsonParser jsonParser = null; // Initialize your JsonParser instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageAndRootCause() {
        // Given
        JsonParser jsonParser = null; // Initialize your JsonParser instance
        String expectedMessage = "Expected message";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, expectedMessage, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithLocation() {
        // Given
        JsonParser jsonParser = null; // Initialize your JsonParser instance
        String expectedMessage = "Expected message";
        JsonLocation location = new JsonLocation();
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, expectedMessage, location);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageLocationAndRootCause() {
        // Given
        JsonParser jsonParser = null; // Initialize your JsonParser instance
        String expectedMessage = "Expected message";
        JsonLocation location = new JsonLocation();
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, expectedMessage, location, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGenerator() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessage() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessageAndRootCause() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContext() {
        // Given
        JsonProcessingException jsonProcessingException = null; // Initialize your JsonProcessingException instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonProcessingException, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContextWithMessage() {
        // Given
        JsonProcessingException jsonProcessingException = null; // Initialize your JsonProcessingException instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonProcessingException, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContextWithMessageAndRootCause() {
        // Given
        JsonProcessingException jsonProcessingException = null; // Initialize your JsonProcessingException instance
        String expectedMessage = "Expected message";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonProcessingException, expectedMessage, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProvider() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProviderWithMessage() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProviderWithMessageAndRootCause() {
        // Given
        JsonGenerator jsonGenerator = null; // Initialize your JsonGenerator instance
        String expectedMessage = "Expected message";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, expectedMessage, rootCause);
        // Then
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOE() {
        // Given
        IOException ioException = new IOException("Unexpected IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        // Then
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO Exception", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        // Given
        Object referrer = new Object();
        String refFieldName = "fieldName";
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(rootCause, referrer, refFieldName);
        // Then
        List<JsonMappingException.Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithPathArray() {
        // Given
        Object referrer = new Object();
        int index = 0;
        Throwable rootCause = new IOException("IO Exception");
        // When
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(rootCause, referrer, index);
        // Then
        List<JsonMappingException.Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(index, reference.getIndex());
    }

    @Test
    public void testJsonMappingExceptionWithPathReference() {
        // Given
        Object referrer = new Object();
        String refFieldName = "fieldName";
        // When
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(new RuntimeException("Test"), referrer, refFieldName);
        // Then
        List<JsonMappingException.Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithPathIndex() {
        // Given
        Object referrer = new Object();
        int index = 0;
        // When
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(new RuntimeException("Test"), referrer, index);
        // Then
        List<JsonMappingException.Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(index, reference.getIndex());
    }
}