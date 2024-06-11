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
import org.mockito.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;

public class RequestTest {

    private static final String TEST_URL = "http://test.com";
    private static final String TEST_PAYLOAD = "test payload";
    private static final Verb TEST_VERB = Verb.GET;
    private static final String TEST_CHARSET = "UTF-8";
    private static final int TEST_CONNECT_TIMEOUT = 5000;
    private static final int TEST_READ_TIMEOUT = 10000;

    private Request request;
    private Verb verb;
    private String url;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private String payload;
    private HttpURLConnection connection;

    @Before
    public void setUp() {
        verb = Mockito.mock(Verb.class);
        url = TEST_URL;
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        request = new Request(verb, url);
        connection = mock(HttpURLConnection.class);
    }

    @Test
    public void testRequest() {
        assertEquals(TEST_VERB, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getBytePayload());
        assertEquals(TEST_CHARSET, request.getCharset());
        assertEquals(false, request.isConnectionKeepAlive());
    }

    @Test
    public void testSend_NoBodyOrHeaders_ReturnsResponse() throws Exception {
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
        verify(connection, times(1)).setRequestMethod(TEST_VERB.name());
        verify(connection, times(1)).setRequestProperty("Content-Length", "0");
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSend_WithBody_SetsContentLengthAndWritesToOutputStream() throws Exception {
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        byte[] bytePayload = TEST_PAYLOAD.getBytes(StandardCharsets.UTF_8);

        request.addPayload(TEST_PAYLOAD);
        request.send();

        verify(connection, times(1)).setRequestProperty("Content-Length", String.valueOf(bytePayload.length));
        verify(connection, times(1)).setDoOutput(true);
        verify(connection, times(1)).getOutputStream().write(bytePayload);
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSend_WithHeaders_SetsHeaders() throws Exception {
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        String key = "Content-Type";
        String value = "application/json";

        request.addHeader(key, value);
        request.send();

        verify(connection, times(1)).setRequestProperty(eq(key), eq(value));
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = OAuthException.class)
    public void testSend_MalformedURL_ThrowsException() throws Exception {
        when(connection.getResponseCode()).thenThrow(new MalformedURLException("Test exception"));
        request.send();
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");

        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");

        Map<String, String> actualParams = request.getQueryStringParams();

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetBodyParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");

        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");

        Map<String, String> actualParams = request.getBodyParams();

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        String expectedBody = "expected body";
        request.addPayload(expectedBody);

        String actualBody = request.getBodyContents();

        assertEquals(expectedBody, actualBody);
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
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("@Request(%s GET %s)", TEST_VERB, url), request.toString());
    }
}