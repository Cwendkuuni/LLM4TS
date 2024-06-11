package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

public class ResponseTest {

    @Test
    public void testResponseWithValidConnection() throws Exception {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderFields()).thenReturn(new HttpHeaders());
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithErrorConnection() throws Exception {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertNull(response.getHeader("Content-Type"));
    }

    @Test
    public void testParseBodyContents() throws Exception {
        Response response = new Response(mock(HttpURLConnection.class));
        String expected = "The body of the response";
        when(response.getStream()).thenReturn(new ByteArrayInputStream(expected.getBytes()));

        assertEquals(expected, response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() throws Exception {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new HttpHeaders() {{
            add("Content-Type", "application/json");
            add("Location", "/newlocation");
        }});

        Response response = new Response(connection);

        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("/newlocation", response.getHeader("Location"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        assertFalse(response.wasSuccessful());

        response = new Response(mock(HttpURLConnection.class));
        response.setCode(201);
        assertTrue(response.wasSuccessful());

        response.setCode(399);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetters() {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderFields()).thenReturn(new HttpHeaders());

        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getHeader("Content-Type"));
    }
}