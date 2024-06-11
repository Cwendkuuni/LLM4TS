package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RequestTest {

    private static final String TEST_URL = "http://test.com";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_VERB = "GET";
    private static final int TEST_CONNECT_TIMEOUT = 3000;
    private static final int TEST_READ_TIMEOUT = 5000;

    private Request request;
    private Verb verb;
    private HttpURLConnection connectionMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        verb = new Verb("GET");
        request = new Request(verb, TEST_URL);
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testRequestInstance() {
        assertEquals(TEST_URL, request.getUrl());
        assertEquals(TEST_VERB, request.getVerb());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertEquals(0, request.getConnectionKeepAlive() ? 0 : 1);
        assertEquals(TEST_CHARSET, request.getCharset());
        assertEquals(new HashMap<>(), request.getBodyParams());
        assertEquals(new HashMap<>(), request.getQuerystringParams());
        assertEquals(new HashMap<>(), request.getHeaders());
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(TEST_URL, new HashMap<>());
        when(new URL(effectiveUrl).openConnection()).thenReturn(connectionMock);
        request.createConnection();
        verify(connectionMock, times(1)).openConnection();
        verifyNoMoreInteractions(connectionMock);
    }

    @Test(expected = OAuthException.class)
    public void testCreateConnectionWithException() throws IOException {
        when(new URL(TEST_URL).openConnection()).thenThrow(new IOException("Test exception"));
        request.createConnection();
    }

    @Test
    public void testDoSend() throws IOException {
        when(connectionMock.setRequestMethod(TEST_VERB)).thenReturn(this);
        when(connectionMock.setDoOutput(true)).thenReturn(this);
        when(connectionMock.getOutputStream()).thenReturn(mock(OutputStream.class));

        request.addHeader("Content-Type", "application/json");
        byte[] testBody = "test body".getBytes(StandardCharsets.UTF_8);
        request.addPayload(testBody);

        request.doSend();

        verify(connectionMock, times(1)).setRequestProperty("Content-Type", "application/json");
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).getOutputStream();
        verify(connectionMock, times(1)).setRequestProperty("Content-Length", String.valueOf(testBody.length));
    }

    @Test(expected = IOException.class)
    public void testDoSendWithException() throws IOException {
        when(connectionMock.setRequestMethod(TEST_VERB)).thenThrow(new IOException("Test exception"));
        request.doSend();
    }

    @Test
    public void testGetByteBodyContents() {
        String testPayload = "test payload";
        request.addPayload(testPayload);
        byte[] expectedByteBody = testPayload.getBytes(StandardCharsets.UTF_8);

        byte[] result = request.getByteBodyContents();

        assertArrayEquals(expectedByteBody, result);
    }

    @Test(expected = OAuthException.class)
    public void testGetByteBodyContentsWithUnsupportedEncodingException() {
        String testCharset = "unsupported-charset";
        request.setCharset(testCharset);
        String testPayload = "test payload";
        request.addPayload(testPayload);

        request.getByteBodyContents();
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");

        Map<String, String> headers = request.getHeaders();

        assertEquals(2, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("application/json", headers.get("Accept"));
    }

    @Test
    public void testAddBodyParameters() {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("testKey", "testValue");

        request.addBodyParameter("testKey", "testValue");

        Map<String, String> result = request.getBodyParams();

        assertEquals(1, result.size());
        assertEquals("testValue", result.get("testKey"));
    }

    @Test
    public void testAddQuerystringParameters() {
        Map<String, String> querystringParams = new HashMap<>();
        querystringParams.put("testKey", "testValue");

        request.addQuerystringParameter("testKey", "testValue");

        Map<String, String> result = request.getQueryStringParams();

        assertEquals(1, result.size());
        assertEquals("testValue", result.get("testKey"));
    }

    @Test
    public void testSend() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        Response response = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() throws IOException {
        when(connectionMock.getResponseCode()).thenThrow(new IOException("Test exception"));
        request.send();
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(TEST_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(TEST_CONNECT_TIMEOUT, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(TEST_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(TEST_READ_TIMEOUT, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset(TEST_CHARSET);
        assertEquals(TEST_CHARSET, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertEquals(1, request.connectionKeepAlive ? 1 : 0);
    }
}