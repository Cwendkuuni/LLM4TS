package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    @Test
    public void testResponseWithSuccessfulConnection() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
        Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test response body".getBytes()));
        Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("Content-Type", Collections.singletonList("application/json"));
        }});

        Response response = new Response(connectionMock);
        assertEquals(200, response.getCode());
        assertEquals("Test response body", response.getBody());
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithErrorConnection() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connectionMock.getResponseCode()).thenReturn(404);
        Mockito.when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream("Test error body".getBytes()));

        Response response = new Response(connectionMock);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String testBody = "Test body content";
        InputStream testStream = new ByteArrayInputStream(testBody.getBytes());
        Response response = new Response(Mockito.mock(HttpURLConnection.class));
        response.parseBodyContents();
        assertEquals(testBody, response.getBody());
    }

    @Test
    public void testParseHeaders() throws UnsupportedEncodingException {
        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Map<String, List<String>> headersMock = new HashMap<String, List<String>>() {{
            put("Content-Type", Collections.singletonList("application/json; charset=UTF-8"));
            put("Cache-Control", Collections.singletonList("no-cache"));
        }};
        Mockito.when(connectionMock.getHeaderFields()).thenReturn(headersMock);

        Response response = new Response(connectionMock);
        assertEquals("application/json; charset=UTF-8", response.getHeader("Content-Type"));
        assertEquals("no-cache", response.getHeader("Cache-Control"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(Mockito.mock(HttpURLConnection.class));
        assertTrue(response.wasSuccessful());

        response.code = 401;
        assertFalse(response.wasSuccessful());
    }
}