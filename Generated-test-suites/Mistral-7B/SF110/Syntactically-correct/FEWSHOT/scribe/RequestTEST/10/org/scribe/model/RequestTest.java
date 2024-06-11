package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testRequestConstruction() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertTrue(request.isConnectionKeepAlive());
        assertEquals(0, request.getConnection().getDoOutput());
        assertEquals(0, request.getConnection().getRequestPropertyCount());
    }

    @Test
    public void testRequestWithHeaders() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testRequestWithBody() {
        Verb verb = Verb.POST;
        String url = "http://example.com";
        String payload = "{\"key\":\"value\"}";
        Request request = new Request(verb, url);
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
        assertEquals(payload.length(), request.getByteBodyContents().length);
    }

    @Test
    public void testRequestWithBodyAndParams() {
        Verb verb = Verb.POST;
        String url = "http://example.com?key=value";
        String payload = "{\"key\":\"value\"}";
        Request request = new Request(verb, url);
        request.addPayload(payload);
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value", params.get("key"));
    }

    @Test
    public void testRequestWithBodyAndHeadersAndParams() {
        Verb verb = Verb.POST;
        String url = "http://example.com?key=value";
        String payload = "{\"key\":\"value\"}";
        Request request = new Request(verb, url);
        request.addPayload(payload);
        request.addHeader("Content-Type", "application/json");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value", params.get("key"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testRequestWithBodyStream() throws IOException {
        Verb verb = Verb.POST;
        String url = "http://example.com";
        String payload = "{\"key\":\"value\"}";
        Request request = new Request(verb, url);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        request.addPayload(payload);
        request.setCharset("UTF-8");
        request.addBody(request.getConnection(), outputStream.toByteArray());
        InputStream is = new ByteArrayInputStream(request.getByteBodyContents());
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        Streams.copy(is, expectedOutput);
        assertEquals(expectedOutput.toString(), payload);
    }

    @Test
    public void testRequestWithConnection() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        request.setConnection(connectionMock);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        Response response = request.send();
        assertTrue(response.isSuccess());
    }

    @Test(expected = OAuthException.class)
    public void testRequestWithMalformedUrl() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com:";
        Request request = new Request(verb, url);
        try {
            request.send();
            fail();
        } catch (OAuthException e) {
            assertEquals("Malformed URL", e.getMessage());
        }
    }

    @Test(expected = OAuthException.class)
    public void testRequestWithUnsupportedCharset() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.setCharset("X-UNSUPPORTED");
        try {
            request.send();
            fail();
        } catch (OAuthException e) {
            assertEquals("Unsupported Charset: X-UNSUPPORTED", e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testRequestWithIOException() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        doThrow(new IOException("Test IOException")).when(connectionMock).connect();
        request.setConnection(connectionMock);
        try {
            request.send();
            fail();
        } catch (OAuthException e) {
            // Ignore this exception
        } catch (IOException ex) {
            assertEquals("Test IOException", ex.getMessage());
            throw ex;
        }
    }
}