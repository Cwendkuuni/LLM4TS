package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import org.scribe.model.Request;
import org.scribe.model.Verb;

public class RequestTest {

    private Request request;
    private HttpURLConnection connectionMock;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://test.com");
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testCreateRequest() {
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://test.com", request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertNull(request.getConnection());
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSendRequestWithGETVerb() throws Exception {
        when(connectionMock.setRequestMethod(eq("GET"))).thenReturn(connectionMock);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        Response response = request.send();
        verify(connectionMock).setRequestMethod(eq("GET"));
        assertEquals(200, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithIOException() throws Exception {
        when(connectionMock.setRequestMethod(eq("GET"))).thenThrow(new IOException());

        request.send();
    }

    @Test
    public void testSetHeaders() {
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testSetBodyParameters() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testSetQueryStringParameters() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key", "value");
        request.addQuerystringParameter("key", "value");
        assertEquals("value", request.getQueryStringParams().get("key"));
    }

    @Test
    public void testSetPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }

    @Test
    public void testSetBytePayload() {
        byte[] bytePayload = "payload".getBytes();
        request.addPayload(bytePayload);
        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());

        request.setConnectionKeepAlive(false);
        assertFalse(request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        request.setConnection(connectionMock);
        assertEquals(connectionMock, request.getConnection());
    }

    @Test
    public void testGetQueryStringParamsWithEmptyUrl() {
        assertThrows(MalformedURLException.class, () -> request.getQueryStringParams());
    }

    @Test
    public void testGetBodyParamsWithEmptyBody() {
        assertEquals(new HashMap<>(), request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        assertEquals("http://test.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://test.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContentsWithEmptyBody() {
        assertEquals("", request.getBodyContents());
    }

    @Test
    public void testGetBodyContentsWithByteBody() throws UnsupportedEncodingException {
        byte[] byteBody = "payload".getBytes("UTF-8");
        request.setBytePayload(byteBody);
        String expectedBody = new String(byteBody, "UTF-8");
        assertEquals(expectedBody, request.getBodyContents());
    }

    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        assertEquals(new HashMap<>(), request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getReadTimeout());
    }
}