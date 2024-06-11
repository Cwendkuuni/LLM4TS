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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private String payload;
    private byte[] bytePayload;
    private HttpURLConnection connection;
    private URL u;

    @Before
    public void setUp() {
        verb = Verb.GET;
        url = "http://example.com";
        querystringParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        request = new Request(verb, url);
        u = new URL(url);
    }

    @Test
    public void testRequestConstructor() {
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertEquals(false, request.isConnectionKeepAlive());
    }

    @Test
    public void testAddHeaders() throws IOException {
        request.addHeader("Key", "Value");
        request.addHeaders(getMockHttpURLConnection());
        assertEquals("Value", request.getConnection().getRequestProperty("Key"));
    }

    @Test
    public void testSend() throws IOException {
        when(getMockHttpURLConnection().getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        request.addQuerystringParameter("Key", "Value");
        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        when(getMockHttpURLConnection().getResponseCode()).thenThrow(new IOException("Error"));
        request.addQuerystringParameter("Key", "Value");
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        String effectiveUrl = URLUtils.appendParametersToQueryString(url, querystringParams);
        request.createConnection();
        assertEquals(effectiveUrl, request.getConnection().getRequestURL().toString());
    }

    @Test
    public void testDoSend() throws IOException {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Key", "Value");
        when(getMockHttpURLConnection().getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(getMockHttpURLConnection().getHeaderFields()).thenReturn(headersMap);
        request.addBodyParameter("Key", "Value");
        Response response = request.send();
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testDoSendIOException() throws IOException {
        when(getMockHttpURLConnection().getResponseCode()).thenThrow(new IOException("Error"));
        request.addBodyParameter("Key", "Value");
        request.doSend();
    }

    @Test
    public void testAddBody() throws IOException {
        byte[] content = "Content".getBytes();
        request.addBody(content);
        request.addBody(getMockHttpURLConnection());
        assertEquals(content.length, request.getConnection().getRequestProperty("Content-Length").split(":")[1].trim().longValue());
    }

    @Test
    public void testAddBodyParameters() throws IOException {
        request.addBodyParameter("Key", "Value");
        String body = URLUtils.formURLEncodeMap(request.getBodyParams());
        request.doSend();
        InputStream inputStream = request.getConnection().getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer, 0, buffer.length))) {
            outputStream.write(buffer, 0, n);
        }
        outputStream.flush();
        assertEquals(body, new String(outputStream.toByteArray(), Charset.defaultCharset()));
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> params = request.getQueryStringParams();
        assertEquals(querystringParams.size(), params.size());
        for (Map.Entry<String, String> entry : querystringParams.entrySet()) {
            assertTrue(params.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), params.get(entry.getKey()));
        }
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
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        request.addBodyParameter("Key", "Value");
        assertEquals("Key=Value", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException {
        request.addBodyParameter("Key", "Value");
        byte[] byteBodyContents = request.getByteBodyContents();
        assertEquals("Key=Value".getBytes(Charset.defaultCharset()), byteBodyContents);
    }

    @Test
    public void testGetVerb() {
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        request.addHeader("Key", "Value");
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5, TimeUnit.SECONDS);
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5, TimeUnit.SECONDS);
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
    public void testSetConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET http://example.com)", request.toString());
    }

    private HttpURLConnection getMockHttpURLConnection() {
        return mock(HttpURLConnection.class);
    }
}