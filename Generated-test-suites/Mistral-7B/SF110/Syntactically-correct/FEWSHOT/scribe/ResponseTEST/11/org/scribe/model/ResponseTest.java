package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionMock = mock(HttpURLConnection.class);
        response = new Response(connectionMock);
    }

    @Test
    public void testResponseWithSuccessfulStatusCode() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));

        Map<String, String> headers = new HashMap<>();
        headers.put("Test-Header", "Test Value");
        when(connectionMock.getHeaderFields()).thenReturn(headers);

        assertEquals(200, response.getCode());
        assertEquals("Test body", response.getBody());
        assertEquals("Test Value", response.getHeader("Test-Header"));
        assertNotNull(response.getStream());
    }

    @Test
    public void testResponseWithErrorStatusCode() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(404);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
    }

    @Test
    public void testResponseWithEmptyBody() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        assertEquals(200, response.getCode());
        assertEquals(EMPTY, response.getBody());
        assertNotNull(response.getStream());
    }

    @Test
    public void testResponseWithNullConnection() {
        assertNull(new Response(null));
    }

    @Test(expected = IOException.class)
    public void testExceptionThrownWhenConnectionFails() throws IOException {
        when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException("Test Error"));

        throw response.new Response(connectionMock);
    }

    @Test
    public void testParseBodyContents() throws IOException {
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));

        String body = response.parseBodyContents();
        assertEquals("Test body", body);
    }

    @Test
    public void testParseHeaders() {
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Test-Header", Arrays.asList("Test Value"));

        Map<String, String> headers = response.parseHeaders(connectionMock);
        assertEquals("Test Value", headers.get("Test-Header"));
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
        assertFalse(response.wasSuccessful());
        response.code = 400;
        assertFalse(response.wasSuccessful());
    }
}