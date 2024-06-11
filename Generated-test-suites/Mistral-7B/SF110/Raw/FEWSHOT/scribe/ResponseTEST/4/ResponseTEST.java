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
import org.mockito.MockitoAnnotations;

public class ResponseTest {

    private HttpURLConnection connectionMock;
    private Response response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionMock = mock(HttpURLConnection.class);
        response = new Response(connectionMock);
    }

    @Test
    public void testResponseWithSuccessCode() throws Exception {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Success body".getBytes()));
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertEquals("Success body", response.getBody());
        assertNotNull(response.getStream());
    }

    @Test
    public void testResponseWithErrorCode() throws Exception {
        when(connectionMock.getResponseCode()).thenReturn(404);
        when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream("Error body".getBytes()));
        Response response = new Response(connectionMock);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNotNull(response.getStream());
    }

    @Test
    public void testParseHeaders() throws Exception {
        when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("Content-Type", Collections.singletonList("application/json"));
            put("Location", Collections.singletonList("/api/v1/users"));
        }});

        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("/api/v1/users", headers.get("Location"));
        assertNull(headers.get("Unknown-Header"));
    }

    @Test
    public void testWasSuccessful() throws Exception {
        assertTrue(response.wasSuccessful());

        response.code = 399;
        assertFalse(response.wasSuccessful());
    }
}