package org.scribe.model;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testResponseCreationWithSuccessfulConnection() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("Content-Type: text/html;charset=UTF-8", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testResponseCreationWithErrorConnection() throws IOException {
        URL url = new URL("http://nonexistentserver.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.getOutputStream().write("Hello World".getBytes());
        connection.getOutputStream().flush();
        Response response = new Response(connection);
        assertEquals("Hello World", response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        Map<String, List<String>> headers = connection.getHeaderFields();
        Map<String, String> parsedHeaders = new Response(connection).parseHeaders(connection);
        assertEquals("en-US,en;q=0.5", parsedHeaders.get("Accept-Language"));
        assertEquals("gzip", parsedHeaders.get("Accept-Encoding"));
    }

    @Test
    public void testWasSuccessful() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.getOutputStream().write("HTTP/1.1 401 Unauthorized".getBytes());
        connection.getOutputStream().flush();
        Response response = new Response(connection);
        assertFalse(response.wasSuccessful());

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.getOutputStream().write("HTTP/1.1 200 OK".getBytes());
        connection.getOutputStream().flush();
        response = new Response(connection);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.getOutputStream().write("Hello World".getBytes());
        connection.getOutputStream().flush();
        Response response = new Response(connection);
        assertEquals("Hello World", response.getBody());

        connection.setRequestMethod("GET");
        response = new Response(connection);
        assertNull(response.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.getOutputStream().write("Hello World".getBytes());
        connection.getOutputStream().flush();
        InputStream expectedStream = connection.getInputStream();
        Response response = new Response(connection);
        InputStream actualStream = response.getStream();
        byte[] buffer = new byte[1024];
        int length;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((length = expectedStream.read(buffer)) > 0) {
            baos.write(buffer, 0, length);
            length = actualStream.read(buffer);
            assertEquals(length, length);
        }
        byte[] expectedContent = baos.toByteArray();
        byte[] actualContent = new byte[actualStream.available()];
        actualStream.read(actualContent);
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        assertEquals(0, response.getCode());

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        Response responseWithConnection = new Response(connection);
        assertEquals(200, responseWithConnection.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, List<String>> headersMap = new HashMap<>();
        headersMap.put("Content-Type", Collections.singletonList("text/html"));
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headersMap);
        Response responseWithConnection = new Response(connection);
        Map<String, String> headers = responseWithConnection.getHeaders();
        assertEquals("text/html", headers.get("Content-Type"));
    }

    @Test
    public void testGetHeader() {
        Map<String, List<String>> headersMap = new HashMap<>();
        headersMap.put("Content-Type", Collections.singletonList("text/html"));
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headersMap);
        Response responseWithConnection = new Response(connection);
        assertEquals("text/html", responseWithConnection.getHeader("Content-Type"));
    }
}