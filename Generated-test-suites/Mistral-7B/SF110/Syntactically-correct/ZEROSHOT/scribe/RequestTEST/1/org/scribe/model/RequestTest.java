package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://example.com");
    }

    @Test
    public void testRequestWithNoHeadersOrBody() throws IOException, OAuthException {
        Response response = request.send();
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testRequestWithQueryStringParameters() throws IOException, OAuthException, UnsupportedEncodingException {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        String effectiveUrl = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQueryStringParams());
        assertEquals(effectiveUrl, "http://example.com?param1=value1&param2=value2");

        response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testRequestWithHeaders() throws IOException, OAuthException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request.addHeaders(headers);

        HttpURLConnection connection = mockHttpURLConnection(200);
        request.setConnection(connection);

        response = request.send();
        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", connection.getRequestProperty("Content-Type"));
    }

    @Test
    public void testRequestWithBody() throws IOException, OAuthException, UnsupportedEncodingException {
        String body = "{\"key\":\"value\"}";
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        request.addPayload(body);

        byte[] bytePayload = body.getBytes(StandardCharsets.UTF_8);
        request.setBytePayload(bytePayload);

        HttpURLConnection connection = mockHttpURLConnectionWithOutput(200, bytePayload);
        request.setConnection(connection);

        response = request.send();
        assertEquals(200, response.getStatusCode());
        assertEquals(body, new String(response.getBody(), StandardCharsets.UTF_8));
    }

    @Test(expected = OAuthException.class)
    public void testRequestWithInvalidUrl() throws IOException, OAuthException {
        request.setUrl("http://example.com:");
        request.send();
    }

    private HttpURLConnection mockHttpURLConnection(int statusCode) throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(statusCode);
        return connection;
    }

    private HttpURLConnection mockHttpURLConnectionWithOutput(int statusCode, byte[] output) throws IOException {
        HttpURLConnection connection = mockHttpURLConnection(statusCode);

        InputStream inputStream = new ByteArrayInputStream(output);
        OutputStream outputStream = new ByteArrayOutputStream();
        connection.setDoOutput(true);
        when(connection.getOutputStream()).then(invocation -> outputStream);

        when(connection.getInputStream()).thenReturn(inputStream);
        when(connection.getContentLength()).thenReturn(output.length);
        return connection;
    }
}