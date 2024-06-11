package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {

    private static final Verb GET = Verb.GET;
    private static final Verb POST = Verb.POST;
    private static final Verb PUT = Verb.PUT;

    private Map<String, String> headers;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Request request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        headers = new HashMap<>();
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        request = new Request(GET, "https://example.com");
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString("https://example.com", querystringParams);
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(request).createConnection();
        request.createConnection();
        verify(request).createConnection();
        verify(connection).setRequestProperty("http.keepAlive", request.connectionKeepAlive ? "true" : "false");
        verify(connection).setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
        verify(connection).setRequestProperty("User-Agent", "Scribe Java Library");
    }

    @Test
    public void testSend_GET() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString("https://example.com", querystringParams);
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(request).createConnection();
        Response response = request.send();
        assertEquals(GET, request.getVerb());
        assertEquals(effectiveUrl, request.getUrl());
        assertEquals(Collections.emptyMap(), request.getQueryStringParams());
        assertEquals(Collections.emptyMap(), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(0, response.getResponseCode());
        verify(request).createConnection();
        verify(connection).setRequestMethod(GET.name());
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSend_POST() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString("https://example.com", querystringParams);
        String body = "Hello World";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(request).createConnection();
        request.addBodyParameter("key", "value");
        request.addPayload(body);
        Response response = request.send();
        assertEquals(POST, request.getVerb());
        assertEquals(effectiveUrl, request.getUrl());
        assertEquals(Collections.singletonMap("key", "value"), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getQueryStringParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(200, response.getResponseCode());
        verify(request).createConnection();
        verify(connection).setRequestMethod(POST.name());
        verify(connection).setDoOutput(true);
        verify(connection).setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        verify(connection).setRequestProperty("Content-Length", String.valueOf(body.length()));
        verify(connection).getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        verify(connection).setRequestProperty("Accept", "*/*");
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSend_PUT() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString("https://example.com", querystringParams);
        String body = "Hello World";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(request).createConnection();
        request.addBodyParameter("key", "value");
        request.addPayload(body);
        Response response = request.send();
        assertEquals(PUT, request.getVerb());
        assertEquals(effectiveUrl, request.getUrl());
        assertEquals(Collections.singletonMap("key", "value"), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getQueryStringParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(200, response.getResponseCode());
        verify(request).createConnection();
        verify(connection).setRequestMethod(PUT.name());
        verify(connection).setDoOutput(true);
        verify(connection).setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        verify(connection).setRequestProperty("Content-Length", String.valueOf(body.length()));
        verify(connection).getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        verify(connection).setRequestProperty("Accept", "*/*");
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = OAuthException.class)
    public void testSend_IOException() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString("https://example.com", querystringParams);
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doThrow(new IOException("Problems while creating connection")).when(request).createConnection();
        request.send();
    }

    @Test
    public void testAddHeaders() {
        String headerKey = "X-Custom-Header";
        String headerValue = "X-Custom-Value";
        request.addHeader(headerKey, headerValue);
        assertEquals(Collections.singletonMap(headerKey, headerValue), request.getHeaders());
    }

    @Test
    public void testAddBodyParameter() {
        String key = "key";
        String value = "value";
        request.addBodyParameter(key, value);
        assertEquals(Collections.singletonMap(key, value), request.getBodyParams());
    }

    @Test
    public void testAddQuerystringParameter() {
        String key = "key";
        String value = "value";
        request.addQuerystringParameter(key, value);
        assertEquals(Collections.singletonMap(key, value), request.getQueryStringParams());
    }

    @Test
    public void testGetQueryStringParams() {
        String queryString = "foo=bar&baz=qux";
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("foo", "bar");
        expectedParams.put("baz", "qux");
        request = new Request(GET, "https://example.com?" + queryString);
        Map<String, String> params = request.getQueryStringParams();
        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetBodyParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        request = new Request(POST, "https://example.com")
                .addBodyParameter("key1", "value1")
                .addBodyParameter("key2", "value2");
        Map<String, String> params = request.getBodyParams();
        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetUrl() {
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        String body = "Hello World";
        request.addPayload(body);
        String expectedBody = "Hello World";
        assertEquals(expectedBody, request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testGetBodyContents_UnsupportedEncodingException() throws UnsupportedEncodingException {
        String body = "Hello World";
        request.addPayload(body);
        String unsupportedEncoding = "X-Custom-Charset";
        throw new UnsupportedEncodingException(unsupportedEncoding);
        // Unreachable code due to exception
        assertEquals("Hello World", request.getBodyContents());
    }

    @Test
    public void testGetVerb() {
        assertEquals(GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        String headerKey = "X-Custom-Header";
        String headerValue = "X-Custom-Value";
        request.addHeader(headerKey, headerValue);
        assertEquals(Collections.singletonMap(headerKey, headerValue), request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        String charset = "UTF-8";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        int duration = 500;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setConnectTimeout(duration, unit);
        assertEquals(unit.toMillis(duration), request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        int duration = 500;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setReadTimeout(duration, unit);
        assertEquals(unit.toMillis(duration), request.connection.getReadTimeout());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        boolean connectionKeepAlive = true;
        request.setConnectionKeepAlive(connectionKeepAlive);
        assertEquals(connectionKeepAlive, request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
}