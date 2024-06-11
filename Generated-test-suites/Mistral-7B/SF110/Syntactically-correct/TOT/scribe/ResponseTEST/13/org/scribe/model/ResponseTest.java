package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Before
    public void setUp() throws Exception {
    }

    // Test cases for the constructor, getCode(), parseHeaders(), getHeaders(), parseBodyContents(), getBody(), wasSuccessful(), and getHeader() methods go here.

    // Test case for the constructor and getCode() method
    @Test
    public void testResponseWithSuccessfulStatusCode() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    // Test case for the parseHeaders() method and getHeader() method
    @Test
    public void testResponseWithValidHeaders() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);
        when(connection.getHeaderFields().get(ArgumentMatchers.eq("Content-Type"))).thenReturn(new BasicHeaderElement("text/plain", null));
        Response response = new Response(connection);
        assertEquals("text/plain", response.getHeader("Content-Type"));
    }

    // Test case for the getBody() method with empty body
    @Test
    public void testResponseWithEmptyBody() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connection);
        assertEquals(Response.EMPTY, response.getBody());
    }

    // Test case for the getBody() method with non-empty body
    @Test
    public void testResponseWithNonEmptyBody() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Hello World".getBytes()));
        Response response = new Response(connection);
        assertEquals("Hello World", response.getBody());
    }

    // Test case for the constructor, parseBodyContents(), getBody(), and wasSuccessful() methods
    @Test
    public void testResponseWithBodyAndSuccessful() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Hello World".getBytes()));
        Response response = new Response(connection);
        assertEquals("Hello World", response.getBody());
        assertTrue(response.wasSuccessful());
    }

    // Test case for the constructor, parseHeaders(), getHeaders(), and getHeader() methods
    @Test
    public void testResponseWithValidHeadersAndHeader() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("text/plain", "charset=UTF-8"));
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        assertEquals(1, response.getHeaders().size());
        assertEquals("text/plain", response.getHeaders().get("Content-Type"));
        assertEquals("UTF-8", response.getHeader("charset"));
    }

    // Test case for the constructor, wasSuccessful(), and parseHeaders() methods
    @Test
    public void testResponseWithErrorStatusCode() throws IOException, UnknownHostException {
        URL url = new URL("http://nonexistent.com/");
        Response response = new Response(url.openConnection());
        assertEquals(404, response.getCode());
        assertFalse(response.wasSuccessful());
    }

    // Test case for the parseHeaders() method with empty headers
    @Test
    public void testResponseWithEmptyHeaders() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);
        Response response = new Response(connection);
        assertTrue(response.getHeaders().isEmpty());
    }

    // Test case for the getHeaders() method with valid headers
    @Test
    public void testResponseWithValidHeaders() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("text/plain", "charset=UTF-8"));
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        assertEquals(1, response.getHeaders().size());
        assertEquals("text/plain", response.getHeaders().get("Content-Type"));
    }

    // Test case for the getHeader() method with invalid header name
    @Test(expected = NullPointerException.class)
    public void testResponseWithNullHeaderName() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        response.getHeader(null);
    }

    // Test case for the constructor and parseBodyContents() method
    @Test
    public void testResponseWithBodyAndEmptyBody() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connection);
        assertEquals(Response.EMPTY, response.getBody());
    }

    // Test case for the constructor and parseHeaders() method
    @Test
    public void testResponseWithValidHeadersAndInvalidContentType() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json", "charset=UTF-8"));
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
        assertFalse(response.getBody().isEmpty());
    }

    // Test case for the constructor and parseHeaders() method
    @Test
    public void testResponseWithValidHeadersAndMissingContentType() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        assertTrue(response.getHeaders().isEmpty());
    }

    // Test case for the constructor and parseHeaders() method
    @Test
    public void testResponseWithEmptyHeadersAndEmptyBody() throws IOException {
        URL url = new URL("http://example.com/");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connection);
        assertTrue(response.getHeaders().isEmpty());
        assertEquals(Response.EMPTY, response.getBody());
    }
}