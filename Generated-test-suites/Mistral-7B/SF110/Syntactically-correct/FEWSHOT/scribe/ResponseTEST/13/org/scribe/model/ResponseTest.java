package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ResponseTest {

    private HttpURLConnection connectionMock;
    private Response response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionMock = mock(HttpURLConnection.class);
        response = new Response(connectionMock);
    }

    @Test
    public void testResponseWithValidConnection() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getHeaderFields()).thenReturn(new HeaderFields(new HashMap<>()));

        response.parseHeaders(connectionMock);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream());
        assertNull(response.getBody());
    }

    @Test
    public void testResponseWithErrorConnection() {
        when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

        response.parseHeaders(connectionMock);
        assertEquals(404, response.getCode());
        assertNull(response.getHeaders());
        assertNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        when(connectionMock.getHeaderFields()).thenReturn(headerFields);

        response.parseHeaders(connectionMock);
        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertNull(headers.get("Content-Length"));
    }

    @Test
    public void testGetBodyWithEmptyBody() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));

        response.parseBodyContents();
        assertEquals("", response.getBody());
    }

    @Test
    public void testGetBodyWithNonEmptyBody() throws IOException {
        String body = "{\"name\":\"John\",\"age\":30}";
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));

        response.parseBodyContents();
        assertEquals(body, response.getBody());
    }

    @Test
    public void testGetCode() {
        when(connectionMock.getResponseCode()).thenReturn(200);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        when(connectionMock.getHeaderFields()).thenReturn(headerFields);

        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertNull(headers.get("Content-Length"));
    }

    @Test
    public void testGetHeader() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        when(connectionMock.getHeaderFields()).thenReturn(headerFields);

        String contentType = response.getHeader("Content-Type");
        assertEquals("application/json", contentType);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBodyAfterGetStream() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        response.parseHeaders(connectionMock);
        response.getStream();
        response.parseBodyContents();
    }
}