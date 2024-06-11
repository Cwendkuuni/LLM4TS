package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RequestTest {

    private static final String BASE_URL = "http://example.com";
    private static final Verb GET = Verb.GET;
    private static final Verb POST = Verb.POST;
    private static final Verb PUT = Verb.PUT;

    private HttpURLConnection connectionMock;
    private HttpURLConnection httpURLConnection;
    private Request request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionMock = mock(HttpURLConnection.class);
        httpURLConnection = mock(HttpURLConnection.class);

        request = new Request(GET, BASE_URL);
    }

    @Test
    public void testNewInstance() {
        assertEquals(GET, request.getVerb());
        assertNull(request.getUrl());
        assertEquals(0, request.getBodyParams().size());
        assertEquals(0, request.getHeaders().size());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
        assertNull(request.getConnection());
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Key", "Value");
        assertEquals("Value", request.getHeaders().get("Key"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("Key", "Value");
        assertEquals("Value", request.getBodyParams().get("Key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("Key", "Value");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("Key=Value", params.get("Key"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("Payload");
        assertEquals("Payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        byte[] bytePayload = { 1, 2, 3 };
        request.addPayload(bytePayload);
        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> params = request.getQueryStringParams();
        assertEquals(2, params.size());
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }

    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(2, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void testGetUrl() {
        assertEquals(BASE_URL, request.getUrl());
    }

    @Test
    public void testGetBodyContents() throws IOException, UnsupportedEncodingException {
        byte[] bytePayload = { 1, 2, 3 };
        request.addPayload(new String(bytePayload, StandardCharsets.UTF_8));

        String expectedBodyContents = new String(bytePayload, StandardCharsets.UTF_8);
        assertEquals(expectedBodyContents, request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void testGetBodyContentsUnsupportedCharset() throws IOException, UnsupportedEncodingException {
        request.setCharset("InvalidCharset");
        request.addPayload("Payload");

        request.getByteBodyContents();
    }

    @Test
    public void testGetVerb() {
        assertEquals(GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        request.addHeader("Key", "Value");

        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("Value", headers.get("Key"));
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
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSend() throws IOException {
        when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        request.addHeader("Content-Type", "application/json");
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("key1", "value1");
        bodyParams.put("key2", "value2");
        request.addBodyParameters(bodyParams);

        request.setCharset("UTF-8");
        request.setConnectionKeepAlive(true);

        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        when(httpURLConnection.getInputStream()).thenReturn(inputStream);

        Request actualRequest = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, actualRequest.getResponseCode());
        assertEquals(GET, actualRequest.getVerb());
        assertEquals(BASE_URL, actualRequest.getUrl());
        assertEquals(Arrays.asList("Content-Type: application/json"), actualRequest.getHeaders().keySet());
        assertEquals(bodyParams, actualRequest.getBodyParams());
        assertNull(actualRequest.getPayload());
        assertNull(actualRequest.getBytePayload());
    }

    @Test
    public void testSendWithPayload() throws IOException, UnsupportedEncodingException {
        String payload = "{\"key\":\"value\"}";
        when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        request.setCharset("UTF-8");
        request.setConnectionKeepAlive(true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8.name());
        writer.write(payload);
        writer.flush();

        request.setOutputStream(outputStream);

        Request actualRequest = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, actualRequest.getResponseCode());
        assertEquals(POST, actualRequest.getVerb());
        assertEquals(BASE_URL, actualRequest.getUrl());
        assertEquals(Arrays.asList("Content-Type: application/json"), actualRequest.getHeaders().keySet());
        assertEquals(payload, actualRequest.getPayload());
        assertNull(actualRequest.getBytePayload());
    }

    @Test
    public void testSendWithBytePayload() throws IOException {
        byte[] bytePayload = { 1, 2, 3 };
        when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        request.setCharset("UTF-8");
        request.setConnectionKeepAlive(true);

        when(connectionMock.setRequestProperty(eq("Content-Length"), anyInt())).thenAnswer(invocation -> {
            int contentLength = invocation.getArgument(1).intValue();
            request.setOutputStream(new ByteArrayOutputStream());
            return request.getOutputStream().write(bytePayload);
        });
        when(httpURLConnection.getOutputStream()).thenReturn(connectionMock);

        Request actualRequest = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, actualRequest.getResponseCode());
        assertEquals(PUT, actualRequest.getVerb());
        assertEquals(BASE_URL, actualRequest.getUrl());
        assertEquals(Arrays.asList("Content-Length: " + bytePayload.length), actualRequest.getHeaders().keySet());
        assertEquals(bytePayload, actualRequest.getBytePayload());
        assertNull(actualRequest.getPayload());
    }

    @After
    public void tearDown() {
        reset(connectionMock, httpURLConnection);
    }
}