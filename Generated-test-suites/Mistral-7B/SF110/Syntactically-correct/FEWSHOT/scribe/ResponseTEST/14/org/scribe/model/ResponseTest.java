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
    public void testResponseWithSuccessfulStatusCode() throws Exception {
        URL url = new URL("http://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream());

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getInputStream();
    }

    @Test
    public void testResponseWithErrorStatusCode() throws Exception {
        URL url = new URL("http://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(500);

        Response response = new Response(connection);

        assertEquals(500, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getHeaders());
        assertNotNull(response.getStream());

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getErrorStream();
    }

    @Test
    public void testResponseWithEmptyBody() throws Exception {
        URL url = new URL("http://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(204);

        Response response = new Response(connection);

        assertEquals(204, response.getCode());
        assertNotNull(response.getBody());
        assertEquals(EMPTY, response.getBody());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream());

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getInputStream();
    }

    @Test
    public void testParseBodyContents() throws Exception {
        URL url = new URL("http://www.example.com");
        String expectedBody = "This is the body.";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(new ByteArrayInputStream(expectedBody.getBytes())).when(connection).getInputStream();
        when(connection.getResponseCode()).thenReturn(200);

        Response response = new Response(connection);

        String actualBody = response.getBody();

        assertEquals(expectedBody, actualBody);

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getInputStream();
    }

    @Test
    public void testParseHeaders() throws Exception {
        URL url = new URL("http://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Collections.singletonList("application/json"));
        when(connection.getHeaderFields()).thenReturn(headerFields);

        Response response = new Response(connection);

        Map<String, String> actualHeaders = response.getHeaders();

        assertEquals("application/json", actualHeaders.get("Content-Type"));

        verify(connection, times(1)).getHeaderFields();
    }

    @Test
    public void testWasSuccessful() throws Exception {
        Response response = new Response(null);

        assertFalse(response.wasSuccessful());

        response = new Response(mock(HttpURLConnection.class));
        response.code = 201;

        assertTrue(response.wasSuccessful());

        response.code = 401;

        assertFalse(response.wasSuccessful());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}