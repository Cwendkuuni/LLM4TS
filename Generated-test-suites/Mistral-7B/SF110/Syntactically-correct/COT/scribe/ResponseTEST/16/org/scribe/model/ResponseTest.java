package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
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
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        connection.disconnect();
    }

    @Test
    public void testResponseWithEmptyBody() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(false);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNull(response.getBody());
        assertEquals(0, response.getStream().available());
        connection.disconnect();
    }

    @Test(expected = UnknownHostException.class)
    public void testResponseWithUnknownHost() throws IOException {
        URL url = new URL("http://nonexistenthost.com");
        Response response = new Response(url.openConnection());
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String testBody = "Test Response Body";
        InputStream testStream = new ByteArrayInputStream(testBody.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, testStream, new HashMap<>());
        assertEquals(testBody, response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(new URL("http://example.com"), 200, new ByteArrayInputStream("Response Body".getBytes()), new HashMap<>());
        assertTrue(response.wasSuccessful());
        response = new Response(new URL("http://example.com"), 404, new ByteArrayInputStream("Response Body".getBytes()), new HashMap<>());
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        String testBody = "Test Response Body";
        InputStream testStream = new ByteArrayInputStream(testBody.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, testStream, new HashMap<>());
        assertEquals(testBody, response.getBody());
        testStream = new ByteArrayInputStream(new byte[0]);
        response = new Response(new URL("http://example.com"), 200, testStream, new HashMap<>());
        assertNull(response.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        String testBody = "Test Response Body";
        InputStream testStream = new ByteArrayInputStream(testBody.getBytes());
        Response response = new Response(new URL("http://example.com"), 200, testStream, new HashMap<>());
        assertSame(testStream, response.getStream());
        testStream = new ByteArrayInputStream(new byte[0]);
        response = new Response(new URL("http://example.com"), 200, testStream, new HashMap<>());
        assertNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(new URL("http://example.com"), 200, new ByteArrayInputStream("Response Body".getBytes()), new HashMap<>());
        assertEquals(200, response.getCode());
        response = new Response(new URL("http://example.com"), 404, new ByteArrayInputStream("Response Body".getBytes()), new HashMap<>());
        assertEquals(404, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headerFields);
        Response response = new Response(connection);
        assertEquals(headerFields.size(), response.getHeaders().size());
    }

    @Test
    public void testGetHeader() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headerFields);
        when(headerFields.get("Content-Type")).thenReturn(Arrays.asList("application/json"));
        Response response = new Response(connection);
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}