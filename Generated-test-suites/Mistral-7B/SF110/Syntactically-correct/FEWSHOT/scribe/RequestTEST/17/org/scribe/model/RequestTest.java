package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    private Map<String, String> headers;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Verb verb;
    private String url;
    private HttpURLConnection connection;
    private Response response;

    @Before
    public void setUp() {
        headers = new HashMap<>();
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        verb = Verb.GET;
        url = "http://example.com";
        connection = mock(HttpURLConnection.class);
        response = mock(Response.class);
    }

    @Test
    public void testRequestInstance() {
        Request request = new Request(verb, url);
        assertNull(request.getBytePayload());
        assertNull(request.getConnection());
        assertNull(request.getCharset());
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertEquals(Collections.emptyMap(), request.getBodyParams());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(Collections.emptyMap(), request.getQuerystringParams());
        assertEquals(null, request.getPayload());
    }

    @Test
    public void testSend_Get() throws IOException {
        given(connection.getResponseCode()).willReturn(200);
        given(connection.getInputStream()).willReturn(new ByteArrayInputStream(new byte[0]));

        Request request = new Request(Verb.GET, url);
        response = request.send();

        assertEquals(200, response.getResponseCode());
        verify(connection, times(1)).setRequestMethod(verb.name());
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = OAuthException.class)
    public void testSend_Get_IOException() throws IOException {
        given(connection.getResponseCode()).willThrow(new IOException("Test error"));

        Request request = new Request(Verb.GET, url);
        request.send();

        fail("An exception was expected");
    }

    @Test
    public void testCreateConnection() throws IOException {
        final String effectiveUrl = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        request.createConnection();

        String actualUrl = new URL(request.getUrl()).toString();
        assertEquals(effectiveUrl, actualUrl);
    }

    @Test
    public void testDoSend_Get() throws IOException {
        given(connection.getResponseCode()).willReturn(200);
        given(connection.getInputStream()).willReturn(new ByteArrayInputStream(new byte[0]));

        Request request = new Request(Verb.GET, url);
        response = request.doSend();

        assertEquals(200, response.getResponseCode());
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = OAuthException.class)
    public void testDoSend_Get_IOException() throws IOException {
        given(connection.getResponseCode()).willThrow(new IOException("Test error"));

        Request request = new Request(Verb.GET, url);
        response = request.doSend();

        fail("An exception was expected");
    }

    @Test
    public void testAddHeaders() throws IOException {
        final String testKey = "test-header";
        final String testValue = "test-header-value";

        Request request = new Request(Verb.GET, url);
        request.addHeader(testKey, testValue);
        request.addHeaders(connection);

        String actualValue = connection.getRequestProperty(testKey);
        assertEquals(testValue, actualValue);
    }

    @Test
    public void testAddBody() throws IOException {
        final String testKey = "test-body";
        final String testValue = "test-body-value";

        Request request = new Request(Verb.POST, url);
        request.addBodyParameter(testKey, testValue);
        request.send();

        byte[] actualBytes = connection.getOutputStream().toByteArray();
        String actualBody = new String(actualBytes, Charset.defaultCharset());
        assertEquals(testValue, actualBody);
    }

    @Test
    public void testAddBody_ByteArray() throws IOException {
        final String testKey = "test-body";
        final String testValue = "test-body-value";

        Request request = new Request(Verb.POST, url);
        request.addPayload(testValue.getBytes());
        request.send();

        byte[] actualBytes = connection.getOutputStream().toByteArray();
        byte[] expectedBytes = testValue.getBytes(Charset.defaultCharset());
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> actualParams = request.getQueryStringParams();

        assertEquals("param1=value1", actualParams.get("param1"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, url);
        request.addBodyParameter("param1", "value1");
        Map<String, String> actualParams = request.getBodyParams();

        assertEquals("value1", actualParams.get("param1"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, url);
        String payload = "test-payload";
        request.addPayload(payload);

        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, url);
        String charset = "UTF-8";
        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        final String testBody = "test-body";

        Request request = new Request(Verb.POST, url);
        request.addPayload(testBody);

        String actualBody = request.getBodyContents();

        assertEquals(testBody, actualBody);
    }
}