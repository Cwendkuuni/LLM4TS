package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    private static final String EFFECTIVE_URL = "https://example.com?param1=value1&param2=value2";
    private static final String CONTENT_TYPE = "application/json";
    private static final String PAYLOAD = "{\"key\":\"value\"}";
    private static final String CHARSET = "UTF-8";

    private Request request;
    private HttpURLConnection connectionMock;

    @Before
    public void setUp() {
        this.request = new Request(Verb.POST, EFFECTIVE_URL);
        this.connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testCreateConnection() throws IOException {
        when(new URL(EFFECTIVE_URL).openConnection()).thenReturn(this.connectionMock);
        request.createConnection();
        verify(this.connectionMock, times(1)).openConnection();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithMalformedUrl() {
        when(new URL(EFFECTIVE_URL).openConnection()).thenThrow(new MalformedURLException("Malformed URL"));
        request.send();
    }

    @Test
    public void testDoSend() throws IOException {
        when(this.connectionMock.setRequestMethod(eq(Verb.POST.name()))).thenReturn(this.connectionMock);
        when(this.connectionMock.setDoOutput(true)).thenReturn(this.connectionMock);
        when(this.connectionMock.setRequestProperty(eq("Content-Length"), anyString())).thenReturn(this.connectionMock);
        when(this.connectionMock.getOutputStream()).thenReturn(mock(OutputStream.class));

        request.addBodyParameter("key", "value");
        request.send();

        verify(this.connectionMock, times(1)).setRequestProperty("Content-Type", eq(CONTENT_TYPE));
        verify(this.connectionMock, times(1)).setRequestProperty("Content-Length", eq(PAYLOAD.getBytes(StandardCharsets.UTF_8).length + ""));
        verify(this.connectionMock, times(1)).setDoOutput(true);
        verify(this.connectionMock, times(1)).getOutputStream();
    }

    @Test
    public void testAddHeaders() throws IOException {
        request.addHeader("key", "value");
        request.doSend();

        verify(this.connectionMock, times(1)).setRequestProperty("key", eq("value"));
    }

    @Test
    public void testAddBody() throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                byteOutputStream.write(b);
            }
        };
        when(this.connectionMock.getOutputStream()).thenReturn(outputStream);

        request.addBodyParameter("key", "value");
        request.send();

        assertEquals(PAYLOAD.getBytes(StandardCharsets.UTF_8), byteOutputStream.toByteArray());
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("param1", "value1");
        expectedParams.put("param2", "value2");

        Map<String, String> actualParams = request.getQueryStringParams();
        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetBodyParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key", "value");

        request.addBodyParameter("key", "value");
        Map<String, String> actualParams = request.getBodyParams();
        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetUrl() {
        assertEquals(EFFECTIVE_URL, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        assertEquals(PAYLOAD, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException, IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(PAYLOAD.getBytes(StandardCharsets.UTF_8));
        when(this.connection.getInputStream()).thenReturn(byteInputStream);

        byte[] actualBytePayload = request.getByteBodyContents();
        assertEquals(PAYLOAD.getBytes(StandardCharsets.UTF_8), actualBytePayload);
    }

    @Test
    public void testGetVerb() {
        assertEquals(Verb.POST, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        request.addHeader("key", "value");
        Map<String, String> actualHeaders = request.getHeaders();
        assertEquals(1, actualHeaders.size());
        assertEquals("value", actualHeaders.get("key"));
    }

    @Test
    public void testGetCharset() {
        assertEquals(CHARSET, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.charset);
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
}