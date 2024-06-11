package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseConstructor() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        connection.disconnect();
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostException() throws IOException {
        URL url = new URL("http://nonexistentserver.com");
        Response response = new Response(url.openConnection());
        assertEquals(404, response.getCode());
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String body = "{\"success\":true,\"message\":\"Welcome!\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, inputStream, new HashMap<>());
        assertEquals(body, response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);
        Response response = new Response(connection);
        Map<String, String> parsedHeaders = response.getHeaders();
        assertEquals(headers, parsedHeaders);
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(new URL("http://example.com"), 200, null, new HashMap<>());
        assertTrue(response.wasSuccessful());
        response = new Response(new URL("http://example.com"), 401, null, new HashMap<>());
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        String body = "{\"success\":true,\"message\":\"Welcome!\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, inputStream, new HashMap<>());
        assertEquals(body, response.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        String body = "{\"success\":true,\"message\":\"Welcome!\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, inputStream, new HashMap<>());
        InputStream actualStream = response.getStream();
        assertEquals(inputStream, actualStream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(new URL("http://example.com"), 200, null, new HashMap<>());
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);
        Response response = new Response(connection);
        Map<String, String> parsedHeaders = response.getHeaders();
        assertEquals(headers, parsedHeaders);
        assertEquals("application/json; charset=UTF-8", response.getHeader("Content-Type"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetHeaderNullName() {
        Response response = new Response(new URL("http://example.com"), 200, null, new HashMap<>());
        response.getHeader(null);
    }

    @Test
    public void testGetHeaderEmptyList() {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new HashMap<>());
        Response response = new Response(connection);
        assertEquals(EMPTY, response.getHeader(HttpURLConnection.KEY_DATE));
    }

    @Test
    public void testGetHeaderInvalidName() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);
        Response response = new Response(connection);
        assertEquals(EMPTY, response.getHeader("Invalid-Header"));
    }
}