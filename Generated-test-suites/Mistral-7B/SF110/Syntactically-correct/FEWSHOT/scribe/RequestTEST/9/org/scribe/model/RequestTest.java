package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.Mockito;

public class RequestTest {

    @Test
    public void testRequestInstance() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertNull(request.getConnection());
        assertEquals(false, request.isConnectionKeepAlive());
        assertNull(request.getCharset());
        assertNotNull(request.getUrl());
        assertEquals("http://example.com", request.getUrl());
        assertEquals("", request.getSanitizedUrl());
    }

    @Test
    public void testSendRequestWithGET() throws Exception {
        String expectedUrl = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(Verb.GET, expectedUrl);
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
        verify(connectionMock).disconnect();
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithIOException() throws Exception {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        IOException ioException = Mockito.mock(IOException.class);

        when(new URL(url).openConnection()).thenThrow(ioException);

        try {
            request.send();
            fail("Should throw OAuthException");
        } catch (OAuthException oe) {
            assertEquals("Problems while creating connection", oe.getMessage());
            assertNotNull(oe.getCause());
            assertEquals(ioException, oe.getCause());
        }
    }

    @Test
    public void testSendRequestWithPUT() throws Exception {
        String url = "http://example.com";
        String payload = "{\"name\": \"John\", \"age\": 30}";
        Request request = new Request(Verb.PUT, url);
        request.addPayload(payload);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
        verify(connectionMock).disconnect();
        assertEquals(payload, request.getByteBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithPUTInvalidPayload() throws Exception {
        String url = "http://example.com";
        Request request = new Request(Verb.PUT, url);
        request.addPayload("invalidPayload");

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        try {
            request.send();
            fail("Should throw OAuthException");
        } catch (OAuthException oe) {
            assertEquals("Problems while sending request: HTTP response code: 400", oe.getMessage());
        }
    }

    @Test
    public void testSendRequestWithPOST() throws Exception {
        String url = "http://example.com";
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("param1", "value1");
        bodyParams.put("param2", "value2");
        Request request = new Request(Verb.POST, url);
        request.addBodyParameters(bodyParams);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
        verify(connectionMock).disconnect();
        assertEquals(URLUtils.formURLEncodeMap(bodyParams), request.getQueryStringParams().get("body"));
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithPOSTInvalidBodyParams() throws Exception {
        String url = "http://example.com";
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(null, "value1");
        Request request = new Request(Verb.POST, url);
        request.addBodyParameters(bodyParams);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        try {
            request.send();
            fail("Should throw OAuthException");
        } catch (OAuthException oe) {
            assertEquals("Invalid body parameters", oe.getMessage());
        }
    }

    @Test
    public void testAddHeaders() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token123");
        request.addHeaders(headers);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);

        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);

        assertEquals(2, request.getHeaders().size());
        assertEquals("Content-Type", request.getHeaders().keySet().iterator().next());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Authorization", request.getHeaders().keySet().iterator().next());
        assertEquals("Bearer token123", request.getHeaders().get("Authorization"));
    }

    @Test(expected = OAuthException.class)
    public void testAddInvalidHeaders() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        Map<String, String> headers = new HashMap<>();
        headers.put(null, "value1");
        request.addHeaders(headers);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);

        try {
            request.send();
            fail("Should throw OAuthException");
        } catch (OAuthException oe) {
            assertEquals("Invalid header key", oe.getMessage());
        }
    }

    @Test
    public void testAddBodyParameter() {
        String url = "http://example.com";
        Request request = new Request(Verb.POST, url);
        request.addBodyParameter("param1", "value1");

        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(1, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
    }

    @Test(expected = OAuthException.class)
    public void testAddQuerystringParameterWithNullKey() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter(null, "value1");

        try {
            request.send();
            fail("Should throw OAuthException");
        } catch (OAuthException oe) {
            assertEquals("Invalid querystring parameter key", oe.getMessage());
        }
    }

    @Test
    public void testGetQueryStringParams() {
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(Verb.GET, url);

        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("param1", "value1");
        expectedParams.put("param2", "value2");

        Map<String, String> params = request.getQueryStringParams();
        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetBodyContents() throws Exception {
        String url = "http://example.com";
        String payload = "{\"name\": \"John\", \"age\": 30}";
        Request request = new Request(Verb.PUT, url);
        request.addPayload(payload);

        String expectedBodyContents = "{\"name\": \"John\", \"age\": 30}";
        assertEquals(expectedBodyContents, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() throws Exception {
        String url = "http://example.com";
        String payload = "{\"name\": \"John\", \"age\": 30}";
        Request request = new Request(Verb.PUT, url);
        request.addPayload(payload);

        byte[] expectedByteBodyContents = payload.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteBodyContents = request.getByteBodyContents();
        assertArrayEquals(expectedByteBodyContents, actualByteBodyContents);
    }

    @Test
    public void testGetHeaders() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token123");
        request.addHeaders(headers);

        Map<String, String> actualHeaders = request.getHeaders();
        assertEquals(headers, actualHeaders);
    }

    @Test
    public void testGetCharset() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        String charset = "UTF-8";
        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() throws Exception {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        int duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;

        request.setConnectTimeout(duration, unit);

        assertEquals(5, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() throws Exception {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        int duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;

        request.setReadTimeout(duration, unit);

        assertEquals(5, request.connection.getReadTimeout());
    }

    @Test
    public void testSetConnectionKeepAlive() throws Exception {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        boolean connectionKeepAlive = true;

        request.setConnectionKeepAlive(connectionKeepAlive);

        assertEquals(connectionKeepAlive, request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        String url = "http://example.com";
        Verb verb = Verb.GET;
        Request request = new Request(verb, url);

        assertEquals("@Request(GET http://example.com)", request.toString());
    }

    private static void setUpCloseableHttpURLConnection(Request request, HttpURLConnection connectionMock) {
        RequestTestUtil.setUpCloseableHttpURLConnection(request, connectionMock);
    }

    private static class RequestTestUtil {
        public static void setUpCloseableHttpURLConnection(Request request, HttpURLConnection connectionMock) {
            when(new URL(request.getUrl()).openConnection()).thenReturn(connectionMock);
            request.createConnection();
        }
    }
}