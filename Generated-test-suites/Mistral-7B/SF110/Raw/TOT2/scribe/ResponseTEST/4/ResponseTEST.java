package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = createResponseInstance();
        String body = "ESample Body";
        response.setBody(body);
        assertEquals(body, response.getBody());
    }

    @Test
    public void testParseBodyContents() {
        Response response = createResponseInstance();
        String body = "Test Body";
        response.setBody(null);
        assertEquals(body, response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = createResponseInstance();
        InputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        response.setStream(inputStream);
        assertEquals(inputStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = createResponseInstance();
        response.setCode(200);
        assertEquals(200, response.getCode());

        response.setCode(404);
        assertEquals(404, response.getCode());

        response.setCode(500);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = createResponseInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        response.setHeaders(headers);

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = createResponseInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        response.setHeaders(headers);

        assertEquals("value1", response.getHeader("key1"));
        assertEquals("value2", response.getHeader("key2"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = createResponseInstance();
        response.setCode(200);
        assertTrue(response.wasSuccessful());

        response.setCode(404);
        assertFalse(response.wasSuccessful());
    }

    private Response createResponseInstance() {
        // Implement this method to return a mock Response object
    }
}