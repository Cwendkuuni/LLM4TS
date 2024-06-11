package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ResponseTest {

    @Test
    public void testResponse() throws Exception {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.wasSuccessful());

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getResponseCode();
        verify(connection, times(1)).getInputStream();
        verify(connection, never()).getErrorStream();
    }

    @Test
    public void testResponseWithError() throws Exception {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doThrow(UnknownHostException.class).when(connection).connect();

        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertFalse(response.wasSuccessful());

        verify(connection, times(1)).connect();
        verify(connection, never()).getResponseCode();
        verify(connection, never()).getInputStream();
        verify(connection, never()).getErrorStream();
    }

    @Test
    public void testParseBodyContents() throws Exception {
        String body = "Hello, World!";
        ByteArrayInputStream stream = new ByteArrayInputStream(body.getBytes());
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(stream);

        Response response = new Response(connection);

        assertEquals(body, response.getBody());
    }

    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHttpHeaders());
        when(connection.getHeaderFields().get("Content-Type")).thenReturn(Collections.singletonList("text/plain"));

        Response response = new Response(connection);

        assertEquals("text/plain", response.getHeader("Content-Type"));
        assertNull(response.getHeader("Content-Length"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);

        assertFalse(response.wasSuccessful());

        response = new Response(mock(HttpURLConnection.class));

        assertTrue(response.wasSuccessful());

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(201);

        response = new Response(connection);

        assertTrue(response.wasSuccessful());

        connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(404);

        response = new Response(connection);

        assertFalse(response.wasSuccessful());
    }
}