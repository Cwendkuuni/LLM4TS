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
    public void testResponseWithSuccessStatusCode() throws IOException {
        HttpURLConnection connection = openConnection("http://example.com");
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertEquals("content-type: text/html; charset=UTF-8", response.getHeader("Content-Type"));
        assertEquals("Server: Apache/2.4.41 (Win64) OpenSSL/1.1.1i PHP/7.3.23", response.getHeader("Server"));
    }

    @Test
    public void testResponseWithErrorStatusCode() throws IOException {
        HttpURLConnection connection = openConnection("http://example.com/not_found");
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String expectedBody = "Hello, World!";
        HttpURLConnection connection = openConnection("http://example.com/test");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(expectedBody.getBytes());
        os.flush();
        os.close();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html; charset=UTF-8");
        expectedHeaders.put("Server", "Apache/2.4.41 (Win64) OpenSSL/1.1.1i PHP/7.3.23");
        HttpURLConnection connection = openConnection("http://example.com/test");
        Response response = new Response(connection);
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders.size(), actualHeaders.size());
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertEquals(entry.getValue(), actualHeaders.get(entry.getKey()));
        }
    }

    @Test
    public void testWasSuccessful() throws IOException {
        HttpURLConnection connection = openConnection("http://example.com");
        Response response = new Response(connection);
        assertTrue(response.wasSuccessful());
        connection = openConnection("http://example.com/not_found");
        response = new Response(connection);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetters() throws IOException {
        String expectedBody = "Hello, World!";
        HttpURLConnection connection = openConnection("http://example.com/test");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(expectedBody.getBytes());
        os.flush();
        os.close();
        Response response = new Response(connection);
        assertEquals(expectedBody, response.getBody());
        assertEquals(200, response.getCode());
        ByteArrayInputStream boneInputStream = new ByteArrayInputStream(expectedBody.getBytes());
        response.setStream(boneInputStream);
        assertEquals(expectedBody, response.getBody());
        assertEquals(200, response.getCode());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html; charset=UTF-8");
        expectedHeaders.put("Server", "Apache/2.4.41 (Win64) OpenSSL/1.1.1i PHP/7.3.23");
        HttpURLConnection connectionWithHeaders = openConnection("http://example.com/test");
        response = new Response(connectionWithHeaders);
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders.size(), actualHeaders.size());
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertEquals(entry.getValue(), actualHeaders.get(entry.getKey()));
        }
        assertEquals(expectedBody, response.getBody());
        assertEquals(200, response.getCode());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals(expectedBody, response.getBody());
        assertEquals(200, response.getCode());
        assertEquals(expectedHeaders, response.getHeaders());
    }

    private HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection;
    }

}