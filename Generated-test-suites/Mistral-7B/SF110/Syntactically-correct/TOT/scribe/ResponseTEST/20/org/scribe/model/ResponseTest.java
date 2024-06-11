package org.scribe.model;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {

    private Response response;

    @Before
    public void setup() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        response = new Response(connection);
        connection.disconnect();
    }

    @Test
    public void testGetBody() {
        String expectedBody = "Hello, World!";
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(expectedBody.getBytes());
        os.flush();
        connection.setRequestMethod("GET");
        connection.connect();
        response = new Response(connection);
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("X-Custom-Header", "value");
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testConstructorError() {
        try {
            new Response(new URL("http://non-existent-host.com"));
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            Response response = new Response((HttpURLConnection) null);
            assertEquals("404", String.valueOf(response.getCode()));
            assertEquals("", response.getBody());
        }
    }

}