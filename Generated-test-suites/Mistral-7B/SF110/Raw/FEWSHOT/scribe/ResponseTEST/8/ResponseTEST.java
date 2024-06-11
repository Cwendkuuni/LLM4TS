package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessfulConnection() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getHeaderFields()).thenReturn(new HttpHeaders());

        URL url = new URL("http://example.com");
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertTrue(response.getStream() instanceof InputStream);
        assertNotNull(response.getHeaders());
        assertNull(response.getBody());

        verify(connectionMock, times(1)).connect();
        verify(connectionMock, times(1)).getInputStream();
    }

    @Test
    public void testResponseWithErrorConnection() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

        URL url = new URL("http://example.com");
        Response response = new Response(connectionMock);

        assertEquals(404, response.getCode());
        assertNull(response.getStream());
        assertNull(response.getBody());

        verify(connectionMock, times(1)).connect();
        verify(connectionMock, never()).getInputStream();
        verify(connectionMock, never()).getErrorStream();
    }

    @Test
    public void testParseBodyContents() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));

        URL url = new URL("http://example.com");
        Response response = new Response(connectionMock);

        assertEquals("Test body", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getHeaderFields()).thenReturn(new HttpHeaders() {{
            add("Content-Type", "application/json");
            add("X-Powered-By", "Scribe Java SDK");
        }});

        URL url = new URL("http://example.com");
        Response response = new Response(connectionMock);

        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("Scribe Java SDK", headers.get("X-Powered-By"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        assertFalse(response.wasSuccessful());

        response = new Response(mock(HttpURLConnection.class));
        response.setCode(200);
        assertTrue(response.wasSuccessful());

        response.setCode(401);
        assertFalse(response.wasSuccessful());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}