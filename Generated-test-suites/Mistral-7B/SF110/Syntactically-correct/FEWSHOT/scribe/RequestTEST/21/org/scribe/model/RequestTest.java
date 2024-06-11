package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    private Request request;
    private URL url;
    private Verb verb;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private HttpURLConnection connectionMock;

    @Before
    public void setUp() {
        verb = Verb.GET;
        url = new URL("http://example.com/path/to/resource?param1=value1&param2=value2");
        request = new Request(verb, url.toString());
        querystringParams = new HashMap<String, String>() {{
            put("param1", "value1");
            put("param2", "value2");
        }};
        bodyParams = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        headers = new HashMap<String, String>() {{
            put("Header1", "value1");
            put("Header2", "value2");
        }};
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testSend() throws IOException, OAuthException {
        when(connectionMock.setRequestMethod(eq(verb.name()))).thenReturn(connectionMock);
        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString())).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);
        when(connectionMock.getOutputStream()).thenReturn(mock(OutputStream.class));

        request.addHeaders(headers);
        request.send();

        ArgumentCaptor<String> requestMethodArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(connectionMock).setRequestMethod(requestMethodArgumentCaptor.capture());
        assertEquals(verb.name(), requestMethodArgumentCaptor.getValue());

        ArgumentCaptor<Map<String, String>> headersArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(connectionMock).setRequestProperty(eq("Content-Length"), eq(""));
        verify(connectionMock).setDoOutput(true);
        verify(connectionMock).getOutputStream();
        verify(connectionMock, times(0)).setRequestProperty(eq("Content-Type"));
        verify(connectionMock, times(0)).setRequestProperty(eq("Authorization"));
        verify(connectionMock).setRequestProperty(headersArgumentCaptor.capture());
        assertEquals(headers, headersArgumentCaptor.getValue());

        verifyNoMoreInteractions(connectionMock);
    }

    @Test(expected = OAuthException.class)
    public void testSend_IOException() throws IOException, OAuthException {
        when(connectionMock.setRequestMethod(anyString())).thenThrow(new IOException());
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(url.toString(), querystringParams);
        request.createConnection();

        ArgumentCaptor<String> effectiveUrlArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(new URL(effectiveUrl)).openConnection();
        verify(request).connection = any(HttpURLConnection.class);
    }

    @Test
    public void testDoSend() throws IOException {
        ByteArrayOutputStream outputStreamMock = new ByteArrayOutputStream();
        InputStream inputStreamMock = new ByteArrayInputStream(new byte[0]);

        when(connectionMock.getInputStream()).thenReturn(inputStreamMock);
        when(connectionMock.getOutputStream()).thenReturn(outputStreamMock);

        request.addHeaders(headers);
        request.doSend();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(request.getByteBodyContents().getBytes(Charset.defaultCharset()));
        Mockito.doReturn(byteInputStream).when(connectionMock).getInputStream();

        ArgumentCaptor<byte[]> contentArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(connectionMock).setRequestProperty(eq("Content-Length"), String.valueOf(contentArgumentCaptor.capture().length));
        verify(connectionMock).setDoOutput(true);
        verify(connectionMock).getOutputStream().write(contentArgumentCaptor.capture());

        assertEquals(request.getByteBodyContents().getBytes(Charset.defaultCharset()), contentArgumentCaptor.getValue());
        assertEquals(outputStreamMock.toByteArray(), request.getByteBodyContents());
    }

    @Test
    public void testAddHeaders() throws IOException {
        request.addHeaders(headers);

        verify(request).headers.putAll(headers);
        verify(request).connection.setRequestProperty(eq("Header1"), eq("value1"));
        verify(request).connection.setRequestProperty(eq("Header2"), eq("value2"));
    }

    @Test
    public void testAddBody() throws IOException {
        request.addBody(bodyParams);
        request.addPayload(bodyParams.get("key1"));

        assertEquals(bodyParams.get("key1"), request.getPayload());
        assertEquals(bodyParams.get("key1"), request.getByteBodyContents().toString(Charset.defaultCharset()));
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> expected = new HashMap<String, String>() {{
            put("param1", "value1");
            put("param2", "value2");
            putAll(request.querystringParams);
        }};

        assertEquals(expected, request.getQueryStringParams());
    }

    @Test
    public void testGetBodyParams() {
        assertEquals(bodyParams, request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://example.com/path/to/resource", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        String body = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        request.addPayload(body);

        assertEquals(body, request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testGetBodyContents_UnsupportedEncodingException() throws UnsupportedEncodingException {
        request.addPayload("body");
        request.setCharset("unsupportedCharset");

        throw new AssertionError("Test should fail with OAuthException");
    }

    @Test
    public void testGetVerb() {
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        assertEquals(1000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(2, TimeUnit.SECONDS);
        assertEquals(2000, request.connection.getReadTimeout());
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
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET http://example.com/path/to/resource)", request.toString());
    }
}