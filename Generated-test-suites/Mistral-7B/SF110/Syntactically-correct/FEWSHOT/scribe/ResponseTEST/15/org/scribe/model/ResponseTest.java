package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessfulStatusCode() throws Exception {
        // Given
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Expected body".getBytes()));
        when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("Content-Type", new ArrayList<String>() {{ add("text/plain"); }});
        }});

        // When
        Response response = new Response(connectionMock);

        // Then
        assertEquals(200, response.getCode());
        assertEquals("Expected body", response.getBody());
        assertEquals("text/plain", response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithErrorStatusCode() throws Exception {
        // Given
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(404);
        when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream("Error message".getBytes()));

        // When
        Response response = new Response(connectionMock);

        // Then
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
    }

    @Test
    public void testResponseWithEmptyBody() throws Exception {
        // Given
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // When
        Response response = new Response(connectionMock);

        // Then
        assertEquals(200, response.getCode());
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        // Given
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("Content-Type", new ArrayList<String>() {{ add("text/plain"); add("application/json"); }});
            put("Cache-Control", new ArrayList<String>() {{ add("max-age=3600"); }});
        }});

        // When
        Map<String, String> headers = new Response(connectionMock).getHeaders();

        // Then
        assertEquals(2, headers.size());
        assertEquals("text/plain", headers.get("Content-Type"));
        assertEquals("max-age=3600", headers.get("Cache-Control"));
    }

    @Test
    public void testWasSuccessful() throws Exception {
        // Given
        Response response = new Response(mock(HttpURLConnection.class));

        // When
        boolean result = response.wasSuccessful();

        // Then
        assertFalse(result);
    }

    @Test
    public void testWasSuccessfulWithSuccessfulStatusCode() throws Exception {
        // Given
        Response response = new Response(mock(HttpURLConnection.class));
        when(response.getCode()).thenReturn(200);

        // When
        boolean result = response.wasSuccessful();

        // Then
        assertTrue(result);
    }
}