package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.mockito.Mockito;

public class RequestTest {

    @Test
    public void testRequestInstance() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getConnection());
        assertTrue(request.getHeaders().isEmpty());
        assertTrue(request.getBodyParams().isEmpty());
        assertTrue(request.getQuerystringParams().isEmpty());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddHeaders() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        Map<String, String> headers = new HashMap<>();
        headers.put("Test-Header", "Test-Value");
        request.addHeaders(headers);

        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testAddBodyParameter() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.addBodyParameter("paramKey", "paramValue");
        assertEquals("paramValue", request.getBodyParams().get("paramKey"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.addQuerystringParameter("queryParamKey", "queryParamValue");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("queryParamValue", params.get("queryParamKey"));
    }

    @Test
    public void testAddPayload() {
        Verb verb = Verb.POST;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.addPayload("plainTextPayload");
        assertEquals("plainTextPayload", request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddPayloadByte() {
        Verb verb = Verb.POST;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        String payload = "bytePayload";
        request.addPayload(payload.getBytes("UTF-8"));
        assertEquals(payload, new String(request.getBytePayload(), "UTF-8"));
    }

    @Test
    public void testCreateConnection() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(verb, url);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        request.createConnection();
        verify(connectionMock, times(1)).openConnection();
        assertEquals(request.getConnection(), connectionMock);
    }

    @Test
    public void testSend() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(verb, url);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Response responseMock = Mockito.mock(Response.class);
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("{}".getBytes()));
        when(connectionMock.getResponseMessage()).thenReturn("OK");
        when(connectionMock.getContentType()).thenReturn("application/json");

        request.setKeepAlive(true);
        request.createConnection();
        Response response = request.send();
        assertEquals(responseMock, response);
        verify(connectionMock, times(1)).setRequestMethod(verb.name());
        verify(connectionMock, times(1)).setDoOutput(false);
        verify(connectionMock, never()).setRequestProperty("Content-Length", anyString());
        verify(connectionMock, never()).getOutputStream();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithErrors() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(verb, url);

        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenThrow(new IOException("Connection error"));

        request.setKeepAlive(true);
        request.createConnection();
        request.send();
        fail("Exception was not thrown");
    }

    @Test
    public void testGetQueryStringParams() {
        Verb verb = Verb.GET;
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(verb, url);
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("param1", "value1");
        expectedParams.put("param2", "value2");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetBodyParams() {
        Verb verb = Verb.POST;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.addBodyParameter("paramKey", "paramValue");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("paramKey", "paramValue");
        Map<String, String> params = request.getBodyParams();
        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());

        request = new Request(Verb.GET, "http://example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(5, TimeUnit.SECONDS);
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(5, TimeUnit.SECONDS);
        assertEquals(5000, request.getConnection().getReadTimeout());
    }
}