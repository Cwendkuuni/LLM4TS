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
    public void testResponseWithSuccessfulStatus() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Success".getBytes()));
        when(connectionMock.getHeaderFields()).thenReturn(new HttpHeaders());
        Response response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getBody());
        assertNotNull(response.getStream());
        assertEquals(Map.of(), response.getHeaders());
    }

    @Test
    public void testResponseWithErrorStatusAndEmptyBody() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(404);
        when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connectionMock);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertEquals(Map.of(), response.getHeaders());
    }

    @Test
    public void testResponseWithErrorStatusAndNonEmptyBody() throws Exception {
        String errorMessage = "Not Found";
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(404);
        when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream(errorMessage.getBytes()));
        Response response = new Response(connectionMock);

        assertEquals(404, response.getCode());
        assertEquals(errorMessage, response.getBody());
        assertNull(response.getStream());
        assertEquals(Map.of(), response.getHeaders());
    }

    @Test
    public void testParseBodyContentsWhenBodyIsNotNull() throws Exception {
        String body = "Response body";
        Response response = new Response(null);
        response.setBody(body);

        assertEquals(body, response.parseBodyContents());
    }

    @Test
    public void testParseBodyContentsWhenBodyIsEmpty() throws Exception {
        Response response = new Response(null);

        assertEquals(Response.EMPTY, response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() throws Exception {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", List.of("application/json"));
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getHeaderFields()).thenReturn(headerFields);

        Map<String, String> headers = new Response(connectionMock).parseHeaders(connectionMock);

        assertEquals(Map.of("Content-Type", "application/json"), headers);
    }

    @Test
    public void testWasSuccessfulWithSuccessfulStatus() {
        assertTrue(new Response(null, 200).wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWithErrorStatus() {
        assertFalse(new Response(null, 404).wasSuccessful());
    }
}