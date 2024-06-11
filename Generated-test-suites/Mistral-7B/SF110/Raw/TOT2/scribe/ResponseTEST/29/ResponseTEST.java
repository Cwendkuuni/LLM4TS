package org.scribe.model;

import static org.junit.Assert.*; import org.junit.*; import org.mockito.*; import java.io.*; import java.net.*; import java.util.*;

public class ResponseTest {

    @Test
    public void testResponseConstructionWithValidConnection() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderFields()).thenReturn(new HeaderFieldsImpl());
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertTrue(StreamUtils.isStreamEmpty(response.getStream()));
        assertNotNull(response.getHeaders());
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testResponseConstructionWithUnknownHostException() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertTrue(StreamUtils.isStreamEmpty(response.getStream()));
    }

    @Test
    public void testParseBodyContentsWithValidInputStream() throws IOException {
        String expectedBody = "Hello, World!";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedBody.getBytes()));
        Response response = new Response(connection);
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test(expected = IOException.class)
    public void testParseBodyContentsWithInvalidInputStream() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenThrow(new IOException());
        Response response = new Response(connection);
        response.parseBodyContents();
    }

    @Test
    public void testParseHeadersWithValidConnection() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new HeaderFieldsImpl(new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Cache-Control", "no-cache");
        }}));
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("no-cache", headers.get("Cache-Control"));
    }

    @Test
    public void testParseHeadersWithEmptyHeaders() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new HeaderFieldsImpl(new HashMap<String, String>() {}));
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertTrue(headers.isEmpty());
    }

    @Test
    public void testWasSuccessfulWithValidResponseCode() {
        Response response = new Response(mock(HttpURLConnection.class));
        response.setCode(200);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWithInvalidResponseCode() {
        Response response = new Response(mock(HttpURLConnection.class));
        response.setCode(500);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBodyWithValidBody() {
        String expectedBody = "Hello, World!";
        Response response = new Response(mock(HttpURLConnection.class));
        response.setBody(expectedBody);
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetBodyWithEmptyBody() {
        Response response = new Response(mock(HttpURLConnection.class));
        String actualBody = response.getBody();
        assertEquals(EMPTY, actualBody);
    }

    @Test
    public void testGetStreamWithValidInputStream() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Hello, World!".getBytes()));
        Response response = new Response(connection);
        InputStream actualStream = response.getStream();
        assertNotNull(actualStream);
    }

    @Test
    public void testGetStreamWithNullInputStream() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(null);
        Response response = new Response(connection);
        assertNull(response.getStream());
    }

    @Test
    public void testGetCodeWithValidResponseCode() {
        Response response = new Response(mock(HttpURLConnection.class));
        response.setCode(200);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetCodeWithInvalidResponseCode() {
        Response response = new Response(mock(HttpURLConnection.class));
        response.setCode(-1);
        assertEquals(-1, response.getCode());
    }

    @Test
    public void testGetHeadersWithValidHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");
        Response response = new Response(mock(HttpURLConnection.class));
        response.setHeaders(headers);
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(headers, actualHeaders);
    }

    @Test
    public void testGetHeadersWithEmptyHeadersMap() {
        Response response = new Response(mock(HttpURLConnection.class));
        response.setHeaders(new HashMap<String, String>());
        Map<String, String> actualHeaders = response.getHeaders();
        assertTrue(actualHeaders.isEmpty());
    }

    @Test
    public void testGetHeaderWithValidHeaderName() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Response response = new Response(mock(HttpURLConnection.class));
        response.setHeaders(headers);
        String actualHeader = response.getHeader("Content-Type");
        assertEquals("application/json", actualHeader);
    }

    @Test
    public void testGetHeaderWithInvalidHeaderName() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Response response = new Response(mock(HttpURLConnection.class));
        response.setHeaders(headers);
        String actualHeader = response.getHeader("Cache-Control");
        assertNull(actualHeader);
    }

    @Test
    public void testGetHeaderWithNullName() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Response response = new Response(mock(HttpURLConnection.class));
        response.setHeaders(headers);
        String actualHeader = response.getHeader(null);
        assertNull(actualHeader);
    }
}