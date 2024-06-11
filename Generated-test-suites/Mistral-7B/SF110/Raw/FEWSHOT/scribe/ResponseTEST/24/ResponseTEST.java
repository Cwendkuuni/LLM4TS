package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HTTPURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessfulConnection() throws IOException {
        URL url = new URL("http://example.com");
        HTTPURLConnection connection = (HTTPURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("text/html; charset=ISO-8859-1", response.getHeader("Content-Type"));
        assertNotNull(response.getStream());

        connection.disconnect();
    }

    @Test
    public void testResponseWithErrorConnection() throws IOException {
        URL url = new URL("http://non-existent-server.com");
        HTTPURLConnection connection = (HTTPURLConnection) url.openConnection();

        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream());

        connection.disconnect();
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String testBody = "Test body content";
        ByteArrayInputStream testStream = new ByteArrayInputStream(testBody.getBytes());
        HTTPURLConnection connection = (HTTPURLConnection) new URL("http://example.com").openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setFixedLengthStreamingMode(testBody.length());
        OutputStream os = connection.getOutputStream();
        os.write(testBody.getBytes());
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(response.getBody(), testBody);

        connection.disconnect();
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html; charset=ISO-8859-1");
        expectedHeaders.put("Set-Cookie", "_ga=GA1.2.12345678.1516041234; expires=Thu, 21-Jan-2016 01:23:45 GMT; max-age=31536000; path=/; domain=.example.com; Secure");

        URL url = new URL("http://example.com");
        HTTPURLConnection connection = (HTTPURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);
        Map<String, String> actualHeaders = response.getHeaders();

        assertEquals(expectedHeaders, actualHeaders);

        connection.disconnect();
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        assertFalse(response.wasSuccessful());

        response = new Response(new HTTPURLConnection(new URL("http://example.com")) {
            @Override
            public int getResponseCode() {
                return 200;
            }
        });
        assertTrue(response.wasSuccessful());

        response = new Response(new HTTPURLConnection(new URL("http://example.com")) {
            @Override
            public int getResponseCode() {
                return 400;
            }
        });
        assertFalse(response.wasSuccessful());
    }
}