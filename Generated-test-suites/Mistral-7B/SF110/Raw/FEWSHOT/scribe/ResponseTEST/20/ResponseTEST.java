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

    private HttpURLConnection connection;
    private Response response;

    @Before
    public void setUp() throws IOException {
        connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("body".getBytes()));
        when(connection.getErrorStream()).thenReturn(null);
        when(connection.getHeaderFields()).thenReturn(new HeaderFields(new HashMap<String, String[]>() {{
            put("Content-Type", new String[]{"text/plain"});
        }})).thenReturn(new HeaderFields(new HashMap<String, String[]>() {{
            put("Content-Length", new String[]{"123"});
        }}));

        response = new Response(connection);
    }

    @Test
    public void testResponseSuccess() {
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertEquals("text/plain", response.getHeaders().get("Content-Type"));
        assertEquals("123", response.getHeaders().get("Content-Length"));
        verify(connection, times(1)).connect();
    }

    @Test
    public void testResponseFailure() {
        when(connection.getResponseCode()).thenReturn(404);
        response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        verify(connection, times(1)).connect();
    }

    @Test
    public void testParseBodyContents() throws IOException {
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("body".getBytes()));
        response.parseBodyContents();
        assertEquals("body", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<String, String>() {{
            put("Content-Type", "text/plain");
            put("Content-Length", "123");
        }};
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody_whenNullBody_parseBodyContents() {
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("body".getBytes()));
        response = new Response(connection);
        assertEquals("body", response.getBody());
    }

    @Test(expected = NullPointerException.class)
    public void testGetBody_whenNullResponse() {
        assertNull(Response.getBody(null));
    }

    @Test
    public void testGetStream_whenNullResponse() {
        assertNull(Response.getStream(null));
    }

    @Test
    public void testGetCode_whenNullResponse() {
        assertNotEquals(200, Response.getCode(null));
    }

    @Test
    public void testGetHeaders_whenNullResponse() {
        assertNull(Response.getHeaders(null));
    }

    @Test
    public void testGetHeader_whenNullResponse() {
        assertNull(Response.getHeader(null, ""));
    }
}

class HeaderFields {
    Map<String, String[]> headers;

    public HeaderFields(Map<String, String[]> headers) {
        this.headers = headers;
    }

    public String getFirst(String name) {
        return headers.get(name)[0];
    }
}