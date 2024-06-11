package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.scribe.utils.StreamUtils;

public class ResponseTest {

    private HttpURLConnection connectionMock;
    private Response response;

    @Before
    public void setUp() {
        connectionMock = mock(HttpURLConnection.class);
        response = new Response(connectionMock);
    }

    @Test
    public void testResponseWithSuccessfulStatusCode() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        response.parseBodyContents();
        assertEquals("", response.getBody());
        assertNotNull(response.getStream());
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("Content-Type: application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithErrorStatusCode() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(404);
        response = new Response(connectionMock);
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        assertEquals(404, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals(EMPTY, response.getHeader(null));
    }

    @Test
    public void testParseHeaders() {
        when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("Content-Type", Collections.singletonList("application/json"));
            put("Location", Collections.singletonList("http://example.com"));
        }});
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("http://example.com", response.getHeader("Location"));
        assertNull(response.getHeader("Content-Length"));
    }

    @Test
    public void testWasSuccessfulWithSuccessfulStatusCode() {
        when(connectionMock.getResponseCode()).thenReturn(200);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWithErrorStatusCode() {
        when(connectionMock.getResponseCode()).thenReturn(404);
        assertFalse(response.wasSuccessful());
    }
}