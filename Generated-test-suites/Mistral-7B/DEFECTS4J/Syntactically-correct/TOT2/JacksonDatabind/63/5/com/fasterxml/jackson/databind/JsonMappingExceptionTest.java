import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionNoPath() {
        String message = "Invalid JSON format";
        JsonMappingException exception = new JsonMappingException(message);
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        String message = "Invalid JSON format";
        Object from = new Object();
        JsonMappingException exception = new JsonMappingException(message, new Reference(from, "fieldName"));
        List<Reference> path = exception.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(from, reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndMultipleObjects() {
        String message = "Invalid JSON format";
        Object obj1 = new Object();
        Object obj2 = new Object();
        JsonMappingException exception = new JsonMappingException(message, new Reference(obj1, "fieldName1"),
                new Reference(obj2, "fieldName2"));
        List<Reference> path = exception.getPath();
        assertEquals(2, path.size());
        Reference reference1 = path.get(0);
        assertEquals(obj1, reference1.getFrom());
        assertEquals("fieldName1", reference1.getFieldName());
        Reference reference2 = path.get(1);
        assertEquals(obj2, reference2.getFrom());
        assertEquals("fieldName2", reference2.getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithException() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException exception = new JsonMappingException("Unexpected JsonMappingException", ioException);
        assertEquals("Unexpected JsonMappingException (was Unexpected IOException): Unexpected IOException",
                exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithExceptionAndPath() {
        IOException ioException = new IOException("Unexpected IOException");
        Object from = new Object();
        JsonMappingException exception = new JsonMappingException("Unexpected JsonMappingException", ioException,
                new Reference(from, "fieldName"));
        assertEquals("Unexpected JsonMappingException (was Unexpected IOException): Unexpected IOException",
                exception.getMessage());
        List<Reference> path = exception.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(from, reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithExceptionAndMultipleObjects() {
        IOException ioException = new IOException("Unexpected IOException");
        Object obj1 = new Object();
        Object obj2 = new Object();
        JsonMappingException exception = new JsonMappingException("Unexpected JsonMappingException", ioException,
                new Reference(obj1, "fieldName1"), new Reference(obj2, "fieldName2"));
        assertEquals("Unexpected JsonMappingException (was Unexpected IOException): Unexpected IOException",
                exception.getMessage());
        List<Reference> path = exception.getPath();
        assertEquals(2, path.size());
        Reference reference1 = path.get(0);
        assertEquals(obj1, reference1.getFrom());
        assertEquals("fieldName1", reference1.getFieldName());
        Reference reference2 = path.get(1);
        assertEquals(obj2, reference2.getFrom());
        assertEquals("fieldName2", reference2.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionToString() {
        String message = "Invalid JSON format";
        Object from = new Object();
        JsonMappingException exception = new JsonMappingException(message, new Reference(from, "fieldName"));
        assertEquals("JsonMappingException: " + message + " (through reference chain: " + exception.getPathReference() + ")",
                exception.toString());
    }
}