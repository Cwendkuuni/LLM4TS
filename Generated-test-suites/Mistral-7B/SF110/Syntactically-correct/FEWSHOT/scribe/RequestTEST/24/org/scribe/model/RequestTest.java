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
import org.scribe.model.*;
import org.scribe.utils.*;
import org.mockito.MockitoAnnotations;

public class RequestTest {

    private static final String URL = "http://test.com";
    private static final Verb VERB_GET = Verb.GET;
    private static final Verb VERB_POST = Verb.POST;
    private static final String HEADER_KEY = "Header-Key";
    private static final String HEADER_VALUE = "Header-Value";
    private static final String QUERY_STRING_KEY = "Query-String-Key";
    private static final String QUERY_STRING_VALUE = "Query-String-Value";
    private static final String BODY_STRING_KEY = "Body-String-Key";
    private static final String BODY_STRING_VALUE = "Body-String-Value";
    private static final String BODY_BYTE_KEY = "Body-Byte-Key";
    private static final byte[] BODY_BYTE_VALUE = "Body-Byte-Value".getBytes();

    private Request request;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private HttpURLConnection connectionMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new Request(VERB_GET, URL);
        querystringParams = new HashMap<>();
        querystringParams.put(QUERY_STRING_KEY, QUERY_STRING_VALUE);
        bodyParams = new HashMap<>();
        bodyParams.put(BODY_STRING_KEY, BODY_STRING_VALUE);
        headers = new HashMap<>();
        headers.put(HEADER_KEY, HEADER_VALUE);
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testNewInstance() {
        assertEquals(VERB_GET, request.getVerb());
        assertEquals(URL, request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertNull(request.getConnection());
        assertNull(request.getCharset());
        assertTrue(request.isConnectionKeepAlive());
        assertTrue(request.getHeaders().isEmpty());
        assertTrue(request.getBodyParams().isEmpty());
        assertTrue(request.getQueryStringParams().isEmpty());
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter(QUERY_STRING_KEY, QUERY_STRING_VALUE);
        Map<String, String> params = request.getQueryStringParams();
        assertEquals(QUERY_STRING_VALUE, params.get(QUERY_STRING_KEY));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter(BODY_STRING_KEY, BODY_STRING_VALUE);
        Map<String, String> params = request.getBodyParams();
        assertEquals(BODY_STRING_VALUE, params.get(BODY_STRING_KEY));
    }

    @Test
    public void testAddHeader() {
        request.addHeader(HEADER_KEY, HEADER_VALUE);
        Map<String, String> headersMap = request.getHeaders();
        assertEquals(HEADER_VALUE, headersMap.get(HEADER_KEY));
    }

    @Test
    public void testAddPayload() {
        request.addPayload(BODY_STRING_VALUE);
        assertEquals(BODY_STRING_VALUE, request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddPayloadByte() {
        request.addPayload(BODY_BYTE_VALUE);
        assertEquals(BODY_BYTE_VALUE, request.getBytePayload());
        assertNull(request.getPayload());
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(URL, querystringParams);
        request.createConnection();
        assertEquals(effectiveUrl, request.getUrl());
        verify(connectionMock, times(1)).openConnection();
    }

    @Test
    public void testCreateConnectionWithExistingConnection() throws IOException {
        request.setConnection(connectionMock);
        request.createConnection();
        verify(connectionMock, never()).openConnection();
    }

    @Test(expected = OAuthException.class)
    public void testCreateConnectionWithMalformedURL() throws IOException {
        request.url = "http:/test.com";
        request.createConnection();
    }

    @Test
    public void testDoSend() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
    }

    @Test(expected = OAuthException.class)
    public void testDoSendWithIOException() throws IOException {
        when(connectionMock.getResponseCode()).thenThrow(new IOException());
        request.send();
    }

    @Test
    public void testSendWithGETRequest() throws IOException {
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
        verify(connectionMock, times(1)).setRequestMethod(VERB_GET.name());
        verify(connectionMock, never()).setDoOutput(true);
        verify(connectionMock, never()).getOutputStream();
    }

    @Test
    public void testSendWithPOSTRequest() throws IOException {
        String testPayload = "test payload";
        request.addPayload(testPayload);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
        verify(connectionMock, times(1)).setRequestMethod(VERB_POST.name());
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).getOutputStream().write(testPayload.getBytes());
    }

    @Test
    public void testAddHeaders() throws IOException {
        request.addHeaders();
        request.addHeader(HEADER_KEY, HEADER_VALUE);
        Map<String, String> headersMap = request.getHeaders();
        assertEquals(HEADER_VALUE, headersMap.get(HEADER_KEY));
        verify(connectionMock, times(1)).setRequestProperty(HEADER_KEY, HEADER_VALUE);
    }

    @Test
    public void testAddBody() throws IOException {
        byte[] testBody = "test body".getBytes();
        request.addBody(testBody);
        request.addHeaders();
        request.addBodyParameter(BODY_STRING_KEY, BODY_STRING_VALUE);
        request.addHeaders();
        request.createConnection();
        request.doSend();

        verify(connectionMock, times(1)).setRequestProperty("Content-Length", String.valueOf(testBody.length));
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).getOutputStream().write(testBody);
        verify(connectionMock, times(1)).setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }

    @Test(expected = OAuthException.class)
    public void testAddBodyWithNullBytePayload() throws IOException {
        request.addBody(null);
    }

    @Test(expected = OAuthException.class)
    public void testAddBodyWithNullPayload() throws IOException {
        request.addPayload(null);
    }

    @Test
    public void testGetQueryStringParams() throws MalformedURLException {
        Map<String, String> params = request.getQueryStringParams();
        assertEquals(QUERY_STRING_VALUE, params.get(QUERY_STRING_KEY));
        assertTrue(params.containsKey(QUERY_STRING_KEY));
    }

    @Test
    public void testGetBodyParams() {
        assertEquals(bodyParams, request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        assertEquals(URL, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://test.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        String testPayload = "test payload";
        request.addPayload(testPayload);
        assertEquals(testPayload, request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
        request.charset = "unsupported-charset";
        request.addPayload("test payload");
    }

    @Test
    public void testGetVerb() {
        assertEquals(VERB_GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConnectTimeoutWithNegativeDuration() {
        request.setConnectTimeout(-1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReadTimeoutWithNegativeDuration() {
        request.setReadTimeout(-1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        request.setConnection(connectionMock);
        assertEquals(connectionMock, request.connection);
    }
}