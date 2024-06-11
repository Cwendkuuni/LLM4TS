package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

    @Test
    public void testRequestwithValidArguments() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertEquals(Collections.emptyMap(), request.getQueryStringParams());
        assertEquals(Collections.emptyMap(), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(null, request.getPayload());
        assertEquals(null, request.getBytePayload());
        assertNull(request.getConnection());
        assertNull(request.getCharset());
        assertFalse(request.isConnectionKeepAlive());
    }

    @Test
    public void testSendMethod() throws Exception {
        Verb verb = mock(Verb.class);
        when(verb.name()).thenReturn("GET");

        Request request = new Request(verb, "http://example.com");

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        doReturn(connection).when(request).createConnection();
        Response response = request.send();

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(connection).setRequestMethod(urlCaptor.capture());
        assertEquals("GET", urlCaptor.getValue());

        ArgumentCaptor<Map<String, String>> headersCaptor = ArgumentCaptor.forClass(Map.class);
        request.addHeaders(connection);
        verify(connection).setRequestProperty(eq("Content-Type"), anyMap().containsKey("Content-Type"));
        verify(connection).setRequestProperty(eq("Accept"), anyMap().containsKey("Accept"));

        assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(expected = OAuthException.class)
    public void testSendMethodThrowsIOException() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com");

        doThrow(new IOException("Error creating connection")).when(request).createConnection();
        request.send();
    }

    @Test
    public void testAddHeadersMethod() {
        Request request = new Request(Verb.GET, "http://example.com");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request.addHeaders(headers);

        Map<String, String> headersMap = request.getHeaders();
        assertEquals(headers, headersMap);
    }

    @Test
    public void testAddBodyParameterMethod() {
        Request request = new Request(Verb.POST, "http://example.com");

        request.addBodyParameter("name", "John Doe");

        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("John Doe", bodyParams.get("name"));
    }

    @Test
    public void testAddQuerystringParameterMethod() {
        Request request = new Request(Verb.GET, "http://example.com?name=John%20Doe");

        request.addQuerystringParameter("age", "30");

        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("age=30", queryStringParams.get("age"));
    }

    @Test
    public void testAddPayloadMethod() {
        Request request = new Request(Verb.POST, "http://example.com");

        request.addPayload("{\"name\":\"John Doe\", \"age\":30}");

        String payload = request.getPayload();
        assertEquals("{\"name\":\"John Doe\", \"age\":30}", payload);
    }

    @Test
    public void testGetByteBodyContentsMethod() throws UnsupportedEncodingException {
        Request request = new Request(Verb.POST, "http://example.com");

        request.addBodyParameter("name", "John Doe");

        String payload = request.getPayload();
        byte[] bytePayload = payload.getBytes(StandardCharsets.UTF_8);

        byte[] result = request.getByteBodyContents();
        assertArrayEquals(bytePayload, result);
    }

    @Test
    public void testGetQueryStringParamsMethod() {
        Request request = new Request(Verb.GET, "http://example.com?name=John%20Doe&age=30");

        Map<String, String> queryStringParams = request.getQueryStringParams();

        assertEquals("John Doe", queryStringParams.get("name"));
        assertEquals("30", queryStringParams.get("age"));
    }

    @Test
    public void testGetBodyParamsMethod() {
        Request request = new Request(Verb.POST, "http://example.com");

        request.addBodyParameter("name", "John Doe");

        Map<String, String> bodyParams = request.getBodyParams();

        assertEquals("John Doe", bodyParams.get("name"));
    }

    @Test
    public void testGetUrlMethod() {
        Request request = new Request(Verb.GET, "http://example.com?name=John%20Doe&age=30");

        assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrlMethod() {
        Request request = new Request(Verb.GET, "http://example.com:8080/api/users?name=John%20Doe&age=30");

        assertEquals("/api/users", request.getSanitizedUrl());
    }

    @Test
    public void testCreateConnectionMethod() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com?name=John%20Doe&age=30");

        HttpURLConnection connection = mock(HttpURLConnection.class);

        doReturn(connection).when(request).createConnection();

        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        request.setCharset("UTF-8");
        request.setConnectionKeepAlive(true);

        request.createConnection();

        verify(connection).setConnectTimeout(1000);
        verify(connection).setReadTimeout(1000);
        verify(connection).setRequestProperty("User-Agent", containsString("ScribeJava"));
        verify(connection).setRequestProperty("Accept-Encoding", "gzip, deflate");
        verify(connection).setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    }
}