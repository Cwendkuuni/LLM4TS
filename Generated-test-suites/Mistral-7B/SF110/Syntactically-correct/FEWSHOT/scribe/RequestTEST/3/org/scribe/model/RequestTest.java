package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private String payload;
    private byte[] bytePayload;
    private HttpURLConnection connectionMock;

    @Before
    public void setup() {
        verb = Mockito.mock(Verb.class);
        url = "http://example.com";
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        payload = "payload";
        bytePayload = "bytePayload".getBytes(StandardCharsets.UTF_8);
        connectionMock = Mockito.mock(HttpURLConnection.class);
    }

    @Test
    public void testRequest() {
        request = new Request(verb, url);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertEquals(false, request.isConnectionKeepAlive());
        assertEquals(new HashMap<>(), request.getHeaders());
        assertEquals(new HashMap<>(), request.getBodyParams());
        assertEquals(new HashMap<>(), request.getQueryStringParams());
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key", "value");
        assertEquals("value", request.getQueryStringParams().get("key"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByte() {
        request.addPayload(bytePayload);
        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test
    public void testSend() throws Exception {
        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString())).thenReturn(connectionMock);
        when(connectionMock.setRequestProperty(eq("Content-Type"), anyString())).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);
        when(connectionMock.getOutputStream()).thenReturn(mock(OutputStream.class));

        request.addHeader("Content-Type", "application/json");

        when(connectionMock.getInputStream()).thenReturn(mock(InputStream.class));

        request.addBodyParameter("key", "value");

        request.addPayload(payload);

        Request requestSpy = spy(request);
        Response response = requestSpy.send();

        verify(connectionMock).setRequestMethod(verb.name());
        verify(connectionMock).setRequestProperty("Content-Length", String.valueOf(payload.length()));
        verify(connectionMock).setRequestProperty("Content-Type", "application/json");
        verify(connectionMock).setDoOutput(true);
        verify(connectionMock).getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        verify(connectionMock).setRequestProperty("Content-Length", String.valueOf(bytePayload.length));
        verify(connectionMock).setRequestProperty("Content-Type", "application/json");
        verify(connectionMock).getInputStream().close();
        verifyNoMoreInteractions(connectionMock);

        assertEquals(response, requestSpy.doSend());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithMalformedUrl() throws Exception {
        request.url = "http://example.com:";
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithUnsupportedCharset() throws Exception {
        request.charset = "unsupportedCharset";
        request.addPayload("payload");
        request.send();
    }

}