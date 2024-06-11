package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private final String HEADER_KEY = "HeaderKey";
    private final String HEADER_VALUE = "HeaderValue";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionMock = mock(HttpURLConnection.class);
        response = new Response(connectionMock);
    }

    @Test
    public void testResponseInitializationWithSuccess() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        response = new Response(connectionMock);

        assertEquals(200, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertNull(response.getHeaders());
    }

    @Test
    public void testResponseInitializationWithError() throws IOException {
        when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());
        response = new Response(connectionMock);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertNull(response.getHeaders());
    }

    @Test
    public void testParseHeaders() throws IOException {
        when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put(HEADER_KEY, Collections.singletonList(HEADER_VALUE));
        }});

        Response response = new Response(connectionMock);
        Map<String, String> headers = response.getHeaders();

        assertEquals(HEADER_VALUE, headers.get(HEADER_KEY));
        assertEquals(1, headers.size());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String bodyContent = "Body content.";
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(bodyContent.getBytes()));

        Response response = new Response(connectionMock);
        String body = response.getBody();

        assertEquals(bodyContent, body);
    }

    @Test
    public void testGetStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(connectionMock.getInputStream()).thenReturn(inputStream);

        Response response = new Response(connectionMock);
        InputStream stream = response.getStream();

        assertSame(inputStream, stream);
    }

    @Test
    public void testGetCode() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(200);

        Response response = new Response(connectionMock);
        int code = response.getCode();

        assertEquals(200, code);
    }

    @Test
    public void testGetHeaders() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_KEY, HEADER_VALUE);
        when(connectionMock.getHeaderFields()).thenReturn(headers);

        Response response = new Response(connectionMock);
        Map<String, String> responseHeaders = response.getHeaders();

        assertEquals(HEADER_VALUE, responseHeaders.get(HEADER_KEY));
        assertEquals(headers, responseHeaders);
    }

    @Test
    public void testGetHeader() throws IOException {
        String headerValue = "Header Value";
        when(connectionMock.getHeaderFields()).thenReturn(Collections.singletonMap(HEADER_KEY, Collections.singletonList(headerValue)));

        Response response = new Response(connectionMock);
        String responseHeader = response.getHeader(HEADER_KEY);

        assertEquals(headerValue, responseHeader);
    }
}