package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;
import org.mockito.MockitoAnnotations;

public class RequestTest {

    private static final String TEST_URL = "https://api.example.com/test";
    private static final String TEST_CHARSET = "UTF-8";
    private static final Verb TEST_VERB = Verb.GET;
    private Request request;
    private HttpURLConnection connectionMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        request = new Request(TEST_VERB, TEST_URL);
        connectionMock = mock(HttpURLConnection.class);
    }

    @After
    public void tearDown() throws Exception {
        request = null;
        connectionMock = null;
    }

    @Test
    public void testRequestInstance() {
        assertEquals(TEST_VERB, request.getVerb());
        assertEquals(TEST_URL, request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getConnection());
        assertNull(request.getCharset());
        assertEquals(false, request.isConnectionKeepAlive());
        assertNotNull(request.getHeaders());
        assertTrue(request.getHeaders().isEmpty());
        assertNotNull(request.getBodyParams());
        assertTrue(request.getBodyParams().isEmpty());
        assertNotNull(request.getQuerystringParams());
        assertTrue(request.getQuerystringParams().isEmpty());
        assertNull(request.getPayload());
        assertNull(request.getSanitizedUrl());
    }

    @Test
    public void testSend() throws Exception {
        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString())).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);
        when(connectionMock.setRequestMethod(eq(TEST_VERB.name()))).thenReturn(connectionMock);
        when(connectionMock.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        });
        when(connectionMock.setRequestProperty(eq("Content-Type"), anyString())).thenReturn(connectionMock);
        when(connectionMock.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        });
        request.setConnection(connectionMock);
        request.send();

        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), null);
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Type"), eq("application/x-www-form-urlencoded"));
        verify(connectionMock, times(1)).getOutputStream();
        verify(connectionMock, times(1)).setRequestMethod(eq(TEST_VERB.name()));
        verify(connectionMock, times(1)).getInputStream();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() throws Exception {
        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString()))
                .thenThrow(new IOException("Connection error"));
        request.setConnection(connectionMock);
        request.send();
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
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
    }

    @Test
    public void testGetQueryStringParams() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key1", "value1");
        queryParams.put("key2", "value2");

        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");

        Map<String, String> params = request.getQueryStringParams();
        assertEquals(queryParams, params);
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Authorization", "Bearer token");
        assertEquals("Bearer token", request.headers.get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.bodyParams.get("key"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.payload);
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException {
        final String body = "body";
        request.addPayload(body);
        byte[] byteBody = request.getByteBodyContents();
        assertEquals(body.getBytes(TEST_CHARSET), byteBody);
    }

    @Test
    public void testGetByteBodyContentsWithBytePayload() throws UnsupportedEncodingException {
        final byte[] byteBody = new byte[]{"b", "o", "d", "y"};
        request.addPayload(byteBody);
        byte[] getByteBody = request.getByteBodyContents();
        assertArrayEquals(byteBody, getByteBody);
    }

    @Test(expected = OAuthException.class)
    public void testGetByteBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
        request.setCharset("invalid_charset");
        request.addPayload("payload");
    }
}