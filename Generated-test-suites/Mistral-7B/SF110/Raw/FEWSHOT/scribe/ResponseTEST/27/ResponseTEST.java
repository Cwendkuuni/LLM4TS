package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessCode() throws Exception {
        URL url = mock(URL.class);
        HttpURLConnection connection = mock(HttpURLConnection.class);

        when(url.openConnection()).thenReturn(connection);

        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).then(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocation) throws Throwable {
                return new ByteArrayInputStream(new byte[0]);
            }
        });
        when(connection.getErrorStream()).thenReturn(null);
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());

        Response response = new Response(url.openConnection());

        assertEquals(200, response.getCode());
        assertNull(response.getErrorStream());
        assertNotNull(response.getInputStream());
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }

    @Test
    public void testResponseWithErrorCode() throws Exception {
        URL url = mock(URL.class);
        HttpURLConnection connection = mock(HttpURLConnection.class);

        when(url.openConnection()).thenReturn(connection);

        when(connection.getResponseCode()).thenReturn(404);
        when(connection.getInputStream()).thenReturn(null);
        when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());

        Response response = new Response(url.openConnection());

        assertEquals(404, response.getCode());
        assertNull(response.getInputStream());
        assertNotNull(response.getErrorStream());
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }

    @Test
    public void testParseBodyContents() throws Exception {
        String body = "The body of the response";
        InputStream stream = new ByteArrayInputStream(body.getBytes());

        Response response = new Response(null);
        response.setStream(stream);

        assertEquals(body, response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() throws Exception {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Collections.singletonList("application/json"));

        when(connection.getHeaderFields()).thenReturn(headerFields);

        Response response = new Response(connection);

        Map<String, String> headers = response.parseHeaders(connection);

        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testWasSuccessful() throws Exception {
        Response response = new Response(null);

        assertTrue(response.wasSuccessful());

        response.setCode(399);

        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws Exception {
        String body = "The body of the response";
        Response response = new Response(null);
        response.setBody(body);

        assertEquals(body, response.getBody());

        InputStream stream = new ByteArrayInputStream(body.getBytes());
        response.setStream(stream);

        assertEquals(body, response.getBody());
    }

    @Test
    public void testGetStream() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        Response response = new Response(null);
        response.setStream(stream);

        assertSame(stream, response.getStream());

        response.setBody("The body of the response");

        assertNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.setCode(500);

        assertEquals(500, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Collections.singletonList("application/json"));

        Response response = new Response(null);
        response.setHeaders(headerFields);

        Map<String, String> headers = response.getHeaders();

        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetHeader() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Collections.singletonList("application/json"));

        Response response = new Response(null);
        response.setHeaders(headerFields);

        assertEquals("application/json", response.getHeader("Content-Type"));
        assertNull(response.getHeader("Content-Disposition"));
    }
}