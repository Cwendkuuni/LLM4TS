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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;

    @Before
    public void setUp() {
        verb = Verb.GET;
        url = "http://example.com";
        request = new Request(verb, url);
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key", "value");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value", params.get("key"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        String payload = request.getPayload();
        assertEquals("payload", payload);
    }

    @Test
    public void testAddPayloadBytes() {
        String charset = "UTF-8";
        byte[] payload = "payload".getBytes(Charset.forName(charset));
        request.addPayload(payload);
        byte[] bytePayload = request.getBytePayload();
        assertArrayEquals(payload, bytePayload);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSendWithQueryStringParams() throws IOException, OAuthException {
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("key1", "value1");
        queryStringParams.put("key2", "value2");

        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);

        request.setConnection(connection);
        Response response = request.send();

        Map<String, String> actualParams = request.getQueryStringParams();
        Map<String, String> expectedParams = new HashMap<>(queryStringParams);
        expectedParams.putAll(request.querystringParams);
        assertEquals(expectedParams, actualParams);

        verify(connection, times(1)).setRequestProperty(eq("Content-Length"), null);
        verify(connection, times(1)).setDoOutput(false);

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        is.close();
        String responseBody = new String(baos.toByteArray(), Charset.defaultCharset());
        assertEquals("expected response body", responseBody);
    }

    @Test
    public void testSendWithRequestBody() throws IOException, OAuthException {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("key1", "value1");
        bodyParams.put("key2", "value2");

        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);

        request.setConnection(connection);
        request.setBytePayload(new byte[]{"payload1".getBytes()});
        request.setCharset("UTF-8");
        Response response = request.send();

        Map<String, String> actualParams = request.getBodyParams();
        Map<String, String> expectedParams = new HashMap<>(bodyParams);
        assertEquals(expectedParams, actualParams);

        verify(connection, times(1)).setRequestProperty(eq("Content-Length"), eq(request.bytePayload.length + ""));
        verify(connection, times(1)).setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(request.bytePayload);
        os.flush();
        os.close();

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        is.close();
        String responseBody = new String(baos.toByteArray(), Charset.defaultCharset());
        assertEquals("expected response body", responseBody);
    }

    @Test(expected = OAuthException.class)
    public void testSendWithInvalidUrl() throws IOException, OAuthException {
        url = "http://example.com:/invalid";
        request = new Request(verb, url);
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithNullConnection() throws IOException, OAuthException {
        request.setConnection(null);
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithNullVerb() throws IOException, OAuthException {
        url = "http://example.com";
        request = new Request(null, url);
        request.send();
    }

    @Test(expected = IOException.class)
    public void testSendWithIOException() throws IOException, OAuthException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        doThrow(new IOException("IOException")).when(connection).getInputStream();

        request.setConnection(connection);
        try {
            request.send();
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("IOException", e.getMessage());
        }
    }
}