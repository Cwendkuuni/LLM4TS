package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {

    private Request request;
    private HttpURLConnection connectionMock;
    private Verb verb;
    private String url;
    private String charset;

    @Before
    public void setUp() {
        verb = Verb.GET;
        url = "http://example.com?param1=value1&param2=value2";
        charset = StandardCharsets.UTF_8.name();
        request = new Request(verb, url);
        connectionMock = mock(HttpURLConnection.class);
    }

    @After
    public void tearDown() {
        reset(connectionMock);
    }

    @Test
    public void testRequestInitialization() {
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertEquals(Collections.emptyMap(), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
        assertNull(request.getConnection());
        assertEquals(StandardCharsets.UTF_8, request.getCharset());
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Header1", "Value1");
        assertEquals("Value1", request.getHeaders().get("Header1"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key", "value");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value", params.get("key"));
        assertEquals("http://example.com?param1=value1&param2=value2&key=value", request.getUrl());
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = "payload".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);
        assertEquals(payload, request.getBytePayload());
        assertNull(request.getPayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("key1", "value1");
        request.addQuerystringParameters(queryStringParams);

        Map<String, String> params = request.getQueryStringParams();
        assertEquals(queryStringParams, params);
    }

    @Test
    public void testGetBodyParams() {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("key1", "value1");
        request.setBodyParams(bodyParams);

        Map<String, String> bodyParamsFromRequest = request.getBodyParams();
        assertEquals(bodyParams, bodyParamsFromRequest);
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(url, request.getQuerystringParams());
        request.createConnection();

        verify(connectionMock, times(1)).openConnection();
        verify(connectionMock, times(1)).setRequestProperty("http.keepAlive", request.isConnectionKeepAlive() ? "true" : "false");
        assertEquals(effectiveUrl, request.connection.getURL().toString());
    }

    @Test
    public void testCreateConnectionWithExistingConnection() throws IOException {
        request.connection = connectionMock;
        request.createConnection();

        verify(connectionMock, never()).openConnection();
    }

    @Test
    public void testDoSend() throws IOException {
        when(connectionMock.setRequestMethod(eq(verb.name()))).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);

        String payload = "payload";
        request.addPayload(payload);

        Response response = request.doSend();
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());

        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq(payload.length() + ""));
        verify(connectionMock, times(1)).getOutputStream();
    }

    @Test
    public void testDoSendWithoutPayload() throws IOException {
        when(connectionMock.setRequestMethod(eq(verb.name()))).thenReturn(connectionMock);

        Response response = request.doSend();
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());

        verify(connectionMock, never()).setDoOutput(anyBoolean());
        verify(connectionMock, never()).getOutputStream();
    }

    @Test
    public void testSend() throws IOException {
        when(connectionMock.setRequestMethod(eq(verb.name()))).thenReturn(connectionMock);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
    }

    @Test
    public void testSendWithException() throws IOException {
        when(connectionMock.setRequestMethod(eq(verb.name()))).thenThrow(new IOException("Test exception"));

        assertThrows(OAuthException.class, () -> request.send());
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);

        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testGetUrl() {
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws IOException {
        String body = "body";
        request.addPayload(body);

        String bodyContents = request.getBodyContents();
        assertEquals(body, bodyContents);
    }

    @Test
    public void testGetByteBodyContents() throws IOException {
        byte[] body = "body".getBytes(StandardCharsets.UTF_8);
        request.addPayload(body);

        byte[] byteBodyContents = request.getByteBodyContents();
        assertArrayEquals(body, byteBodyContents);
    }

    @Test
    public void testGetCharset() {
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.connection.getReadTimeout());
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}