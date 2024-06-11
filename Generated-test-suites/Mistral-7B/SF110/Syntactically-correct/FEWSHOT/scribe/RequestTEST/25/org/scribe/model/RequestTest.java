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
import org.mockito.MockitoAnnotations;
import org.scribe.exceptions.*;
import org.scribe.utils.*;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private byte[] bytePayload;
    private HttpURLConnection connectionMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        verb = Verb.GET;
        url = "http://example.com";
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        request = new Request(verb, url);
        connectionMock = mock(HttpURLConnection.class);
    }

    @Test
    public void testRequestInitialization() {
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getByteBodyContents());
        assertNull(request.getBytePayload());
        assertNull(request.getConnection());
        assertTrue(request.isConnectionKeepAlive());
        assertEquals(mapsEqual(new HashMap<>(), request.getHeaders()), true);
        assertEquals(mapsEqual(new HashMap<>(), request.getBodyParams()), true);
        assertEquals(mapsEqual(new HashMap<>(), request.getQuerystringParams()), true);
        assertEquals(null, request.getPayload());
        assertEquals(null, request.getCharset());
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(url, querystringParams);
        when(new URL(effectiveUrl).openConnection()).thenReturn(connectionMock);

        request.createConnection();

        verify(connectionMock, times(1)).openConnection();
        verify(connectionMock, times(1)).setRequestProperty("http.keepAlive", request.isConnectionKeepAlive() ? "true" : "false");
    }

    @Test
    public void testDoSend() throws IOException {
        when(connectionMock.setRequestMethod(verb.name())).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);
        when(connectionMock.setRequestProperty("Content-Length", "123")).thenReturn(connectionMock);
        when(connectionMock.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // noop
            }

            @Override
            public void write(byte[] b) throws IOException {
                this.bytePayload = b;
            }
        });
        when(connectionMock.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        request.addBodyParameter("key", "value");
        request.addQuerystringParameter("param", "test");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        request.setCharset("UTF-8");
        request.setConnectionKeepAlive(true);

        Response response = request.send();

        assertEquals(verb, response.getVerb());
        assertEquals(url, response.getUrl());
        assertEquals(mapsEqual(querystringParams, response.getQuerystringParams()), true);
        assertEquals(mapsEqual(bodyParams, response.getBodyParams()), true);
        assertEquals(mapsEqual(headers, response.getHeaders()), true);
        assertEquals("UTF-8", response.getCharset());
        assertNotNull(response.getByteBodyContents());
        assertArrayEquals(bytePayload, response.getByteBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testDoSendWithIOException() throws IOException {
        when(connectionMock.setRequestMethod(verb.name())).thenThrow(new IOException());

        request.send();

        fail("IOException expected");
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Cache-Control", "no-cache");

        Map<String, String> headersMap = request.getHeaders();

        assertEquals(mapsEqual(new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Cache-Control", "no-cache");
        }}, headersMap), true);
    }

    @Test
    public void testAddBody() throws IOException {
        request.addBodyParameter("key", "value");

        byte[] byteBody = request.getByteBodyContents();
        String body = new String(byteBody, "UTF-8");

        assertEquals("value", bodyParams.get("key"));
        assertEquals("{\"key\":\"value\"}", body);
    }

    @Test
    public void testAddPayload() {
        request.addPayload("test payload");

        assertEquals("test payload", request.getPayload());
    }

    @Test
    public void testAddPayloadWithByteArray() {
        byte[] bytePayload = "test payload".getBytes("UTF-8");
        request.addPayload(bytePayload);

        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("param1", "value1");
        expectedParams.put("param2", "value2");

        url += "?param1=value1&param2=value2";

        Map<String, String> actualParams = request.getQueryStringParams();

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetBodyParams() {
        assertEquals(mapsEqual(new HashMap<>(), request.getBodyParams()), true);

        request.addBodyParameter("key", "value");

        Map<String, String> actualParams = request.getBodyParams();

        assertEquals(mapsEqual(new HashMap<String, String>() {{
            put("key", "value");
        }}, actualParams), true);
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        byte[] byteBody = "test payload".getBytes("UTF-8");

        request.addPayload(byteBody);

        assertEquals(new String(byteBody, "UTF-8"), request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
        request.setCharset("InvalidCharset");

        request.getBodyContents();

        fail("UnsupportedCharsetException expected");
    }

    @Test
    public void testGetVerb() {
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

        assertEquals(5000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);

        assertEquals(5000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);

        assertEquals(true, request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);

        assertEquals(connection, request.getConnection());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private boolean mapsEqual(Map<String, String> map1, Map<String, String> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }

        for (Map.Entry<String, String> entry1 : map1.entrySet()) {
            String key = entry1.getKey();
            String value1 = entry1.getValue();
            String value2 = map2.get(key);

            if (value1 == null && value2 != null || value1 != null && !value1.equals(value2)) {
                return false;
            }
        }

        return true;
    }
}