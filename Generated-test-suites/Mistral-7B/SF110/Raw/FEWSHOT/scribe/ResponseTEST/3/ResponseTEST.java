package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.Test;
import org.scribe.utils.*;
import org.mockito.Mockito;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessfulStatus() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Success".getBytes()));
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertNotNull(response.getStream());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody());
        assertFalse(response.getStream().available() > 0);
    }

    @Test
    public void testResponseWithErrorStatus() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(500);
        Response response = new Response(connectionMock);

        assertEquals(500, response.getCode());
        assertNull(response.getStream());
        assertNull(response.getBody());
    }

    @Test
    public void testResponseWithEmptyBody() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertNotNull(response.getStream());
        assertNull(response.getBody());
    }

    @Test
    public void testResponseWithHeaders() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getHeaderFields()).thenReturn(new BasicHeaderElement[] { new BasicHeaderElement("Content-Type", "application/json") });
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertNotNull(response.getStream());
        assertNotNull(response.getBody());
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test(expected = IOException.class)
    public void testResponseWithUnknownHostException() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());
        new Response(connectionMock);
    }
}