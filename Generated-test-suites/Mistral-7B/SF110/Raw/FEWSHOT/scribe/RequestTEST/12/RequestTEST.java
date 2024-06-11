package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private HttpURLConnection connectionMock;

    @Before
    public void setUp() {
        verb = Verb.GET;
        url = "http://example.com";
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        request = new Request(verb, url);
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testCreateConnection() throws IOException {
        final String effectiveUrl = URLUtils.appendParametersToQueryString(url, querystringParams);
        when(new URL(effectiveUrl).openConnection()).thenReturn(connectionMock);

        request.createConnection();
        verify(connectionMock).connect();
    }

    @Test(expected = IOException.class)
    public void testCreateConnectionWithError() throws IOException {
        when(new URL(url).openConnection()).thenThrow(new IOException("Test error"));

        request.createConnection();
    }

    @Test
    public void testSend() throws IOException {
        givenRequestSettings();
        givenResponseSettings();

        final Response response = request.send();
        assertEquals("Test response", response.getStatusMessage());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() throws IOException {
        givenRequestSettings();
        when(connectionMock.getErrorStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test IOException");
            }
        });

        request.send();
    }

    @Test
    public void testDoSend() throws IOException {
        givenRequestSettings();
        givenResponseSettings();

        final Response response = request.doSend();
        assertEquals("Test response", response.getStatusMessage());
    }

    @Test(expected = IOException.class)
    public void testDoSendWithIOException() throws IOException {
        givenRequestSettings();
        when(connectionMock.getErrorStream()).thenThrow(new IOException("Test IOException"));

        request.doSend();
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");

        final Map<String, String> headers = request.getHeaders();

        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");

        final Map<String, String> bodyParams = request.getBodyParams();

        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key", "value");

        final Map<String, String> params = request.getQueryStringParams();

        assertEquals("value", params.get("key"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");

        final String payload = request.getPayload();

        assertEquals("payload", payload);
    }

    @Test
    public void testAddPayloadByteArray() {
        final byte[] payload = "payload".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);

        final byte[] payloadFromRequest = request.getByteBodyContents();

        assertArrayEquals(payload, payloadFromRequest);
    }

    @Test
    public void testGetCharset() {
        request.setCharset("UTF-8");

        final String charset = request.getCharset();

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetUrl() {
        final String expectedUrl = "http://example.com?key=value";
        request.addQuerystringParameter("key", "value");

        final String url = request.getUrl();

        assertEquals(expectedUrl, url);
    }

    @Test
    public void testGetSanitizedUrl() {
        final String expectedUrl = "http://example.com";

        final String url = request.getSanitizedUrl();

        assertEquals(expectedUrl, url);
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        final String payload = "payload";
        request.addPayload(payload);

        final String bodyContents = request.getBodyContents();

        assertEquals(payload, bodyContents);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
        request.setCharset("X-UTF-16BE");

        request.getBodyContents();
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException {
        final String payload = "payload";
        request.addPayload(payload);

        final byte[] byteBodyContents = request.getByteBodyContents();

        assertArrayEquals(payload.getBytes(StandardCharsets.UTF_8), byteBodyContents);
    }

    @Test
    public void testGetVerb() {
        final Verb expectedVerb = Verb.POST;

        final Verb verb = request.getVerb();

        assertEquals(expectedVerb, verb);
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

        final int connectTimeout = request.connection.getConnectTimeout();

        assertEquals(5000, connectTimeout);
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);

        final int readTimeout = request.connection.getReadTimeout();

        assertEquals(5000, readTimeout);
    }

    @Test
    public void testToString() {
        final String expectedToString = "@Request(GET http://example.com)";

        final String toString = request.toString();

        assertEquals(expectedToString, toString);
    }

    private void givenRequestSettings() throws IOException {
        when(new URL(url).openConnection()).thenReturn(connectionMock);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connectionMock.getResponseMessage()).thenReturn("Test response");
    }

    private void givenResponseSettings() {
        final Response expectedResponse = new Response(HttpURLConnection.HTTP_OK, "Test response");
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connectionMock.getResponseMessage()).thenReturn("Test response");
    }
}