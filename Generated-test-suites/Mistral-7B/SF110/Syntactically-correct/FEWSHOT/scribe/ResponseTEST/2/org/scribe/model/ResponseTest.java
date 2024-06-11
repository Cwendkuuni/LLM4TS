package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseWithValidConnection() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("content-type: text/html;charset=UTF-8", response.getHeaders().get("Content-Type"));

        connection.disconnect();
    }

    @Test
    public void testResponseWithErrorConnection() throws IOException {
        URL url = new URL("http://non-existent-server.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html;charset=UTF-8");
        expectedHeaders.put("Date", "Thu, 01 Jan 1998 00:00:01 GMT");

        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Header1", "Value1");
        connection.setRequestProperty("Header2", "Value2");
        connection.setRequestMethod("GET");

        Response response = new Response(connection);

        assertEquals(expectedHeaders, response.getHeaders());

        connection.disconnect();
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(new Response(new HttpURLConnection(new URL("http://example.com"))).wasSuccessful());
        assertFalse(new Response(new HttpURLConnection(new URL("http://example.com")) {
            {
                setResponseCode(404);
            }
        }).wasSuccessful());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String expectedBody = "This is the response body.";
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(expectedBody.getBytes());
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(expectedBody.length()));
        connection.setOutputStream(new ByteArrayOutputStream());
        connection.getOutputStream().write(expectedBody.getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        Response response = new Response(connection);

        assertEquals(expectedBody, response.getBody());

        connection.disconnect();
    }
}