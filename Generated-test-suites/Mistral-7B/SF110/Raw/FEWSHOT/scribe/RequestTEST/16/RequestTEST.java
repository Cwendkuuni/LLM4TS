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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.Mockito;

public class RequestTest {

    @Test
    public void testCreateRequestWithNullVerbAndUrl() {
        try {
            new Request(null, null);
            fail("Expected OAuthException to be thrown");
        } catch (OAuthException e) {
            // Expected
        }
    }

    @Test
    public void testCreateRequestWithNullVerb() {
        try {
            new Request(null, "http://example.com");
            fail("Expected OAuthException to be thrown");
        } catch (OAuthException e) {
            // Expected
        }
    }

    @Test
    public void testCreateRequestWithNullUrl() {
        try {
            new Request(Verb.GET, null);
            fail("Expected OAuthException to be thrown");
        } catch (OAuthException e) {
            // Expected
        }
    }

    @Test
    public void testCreateRequestWithValidVerbAndUrl() {
        new Request(Verb.GET, "http://example.com");
    }

    @Test
    public void testSendRequestWithValidVerbAndUrlAndEmptyHeadersAndBody() {
        final Verb verb = Verb.GET;
        final String url = "http://example.com";
        final Request request = new Request(verb, url);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        request.setConnection(connection);
        final Response response = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertNull(request.getPayload());
    }

    @Test
    public void testSendRequestWithValidVerbAndUrlAndHeaders() {
        final Verb verb = Verb.GET;
        final String url = "http://example.com";
        final String key1 = "header1";
        final String value1 = "value1";
        final String key2 = "header2";
        final String value2 = "value2";

        final Request request = new Request(verb, url);
        request.addHeader(key1, value1);
        request.addHeader(key2, value2);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        request.setConnection(connection);
        final Response response = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        assertEquals(Map.of(key1, value1, key2, value2), request.getHeaders());
        assertNull(request.getPayload());
    }

    @Test
    public void testSendRequestWithValidVerbAndUrlAndBody() throws UnsupportedEncodingException, IOException {
        final Verb verb = Verb.POST;
        final String url = "http://example.com";
        final String key = "bodyParam";
        final String value = "test";

        final Request request = new Request(verb, url);
        request.addBodyParameter(key, value);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        final byte[] bytePayload = value.getBytes(StandardCharsets.UTF_8);
        request.setPayload(bytePayload);
        request.setConnection(connection);
        request.addHeaders(connection);
        final Response response = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        assertEquals(Collections.emptyMap(), request.getHeaders());
        assertEquals(Map.of(key, value), request.getBodyParams());
        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test
    public void testSendRequestWithValidVerbAndUrlAndBodyAndHeaders()
            throws UnsupportedEncodingException, IOException {
        final Verb verb = Verb.POST;
        final String url = "http://example.com";
        final String key1 = "header1";
        final String value1 = "value1";
        final String key2 = "header2";
        final String value2 = "value2";
        final String keyBody = "bodyParam";
        final String valueBody = "test";

        final Request request = new Request(verb, url);
        request.addBodyParameter(keyBody, valueBody);
        request.addHeader(key1, value1);
        request.addHeader(key2, value2);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        final byte[] bytePayload = valueBody.getBytes(StandardCharsets.UTF_8);
        request.setPayload(bytePayload);
        request.setConnection(connection);
        request.addHeaders(connection);
        final Response response = request.send();

        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        assertEquals(Map.of(key1, value1, key2, value2), request.getHeaders());
        assertEquals(Map.of(keyBody, valueBody), request.getBodyParams());
        assertEquals(bytePayload, request.getBytePayload());
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithInvalidUrl() throws IOException {
        final Verb verb = Verb.GET;
        final String url = "http://example.com:";
        final Request request = new Request(verb, url);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new IOException("Connection refused"));

        request.setConnection(connection);
        request.send();
    }

    @Test
    public void testGetQueryStringParamsWithEmptyQueryString() throws MalformedURLException {
        final String url = "http://example.com";
        final Map<String, String> expectedParams = new HashMap<>();
        final Request request = new Request(Verb.GET, url);

        final Map<String, String> params = request.getQueryStringParams();

        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetQueryStringParamsWithValidQueryString() throws MalformedURLException {
        final String url = "http://example.com?param1=value1&param2=value2";
        final Map<String, String> expectedParams = Map.of("param1", "value1", "param2", "value2");
        final Request request = new Request(Verb.GET, url);

        final Map<String, String> params = request.getQueryStringParams();

        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetBodyParamsWithEmptyBody() {
        final Map<String, String> expectedParams = new HashMap<>();
        final Request request = new Request(Verb.GET, "http://example.com");

        final Map<String, String> params = request.getBodyParams();

        assertEquals(expectedParams, params);
    }

    @Test
    public void testGetBodyParamsWithValidBody() {
        final String key = "bodyParam";
        final String value = "test";
        final Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter(key, value);

        final Map<String, String> params = request.getBodyParams();

        assertEquals(Map.of(key, value), params);
    }

    @Test
    public void testGetUrl() {
        final String url = "http://example.com?param1=value1&param2=value2";
        final Request request = new Request(Verb.GET, url);

        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        final String url = "http://example.com?param1=value1&param2=value2";
        final Request request = new Request(Verb.GET, url);

        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContentsWithEmptyBody() throws UnsupportedEncodingException {
        final String charset = "UTF-8";
        final Request request = new Request(Verb.GET, "http://example.com");

        final String contents = request.getBodyContents();

        assertEquals("", contents);
    }

    @Test
    public void testGetBodyContentsWithValidBody() throws UnsupportedEncodingException {
        final String charset = "UTF-8";
        final String body = "test body";
        final Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload(body);

        final String contents = request.getBodyContents();

        assertEquals(body, contents);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException, IOException {
        final String charset = "INVALID_CHARSET";
        final String body = "test body";
        final Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload(body);

        request.setCharset(charset);
        request.getBodyContents();
    }
}