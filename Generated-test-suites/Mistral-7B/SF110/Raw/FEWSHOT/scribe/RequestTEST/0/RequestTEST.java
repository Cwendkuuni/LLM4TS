package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class RequestTest {

    @Test
    public void testRequestInstance() {
        Verb verb = Verb.GET;
        Request request = new Request(verb, "http://example.com");

        assertNotNull(request);
        assertEquals(verb, request.getVerb());
        assertNull(request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertNull(request.getConnection());
        assertNull(request.getCharset());
        assertEquals(false, request.isConnectionKeepAlive());
        assertEquals(new HashMap<String, String>(), request.getHeaders());
        assertEquals(new HashMap<String, String>(), request.getQuerystringParams());
        assertEquals(new HashMap<String, String>(), request.getBodyParams());
    }

    @Test
    public void testConstructWithNullVerb() {
        assertNull(new Request(null, "http://example.com"));
    }

    @Test
    public void testConstructWithNullUrl() {
        Verb verb = Verb.GET;
        assertNull(new Request(verb, null));
    }

    @Test
    public void testUrlEncodedQuerystringParams() {
        Request request = new Request(Verb.GET, "http://example.com?id=1&name=%65%6e%63%6F%64%65%64");
        Map<String, String> params = request.getQueryStringParams();

        assertEquals(2, params.size());
        assertEquals("id", params.get("id"));
        assertEquals("name", params.get("name"));
        assertEquals("Thomas", params.get("name"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("foo", "bar");

        Map<String, String> params = request.getQueryStringParams();
        assertEquals("foo", params.get("foo"));
        assertEquals("bar", params.get("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddQuerystringParameterWithNullKey() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter(null, "bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddQuerystringParameterWithNullValue() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("foo", null);
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("foo", "bar");

        Map<String, String> params = request.getBodyParams();
        assertEquals("bar", params.get("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBodyParameterWithNullKey() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter(null, "bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBodyParameterWithNullValue() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("foo", null);
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.PUT, "http://example.com");
        request.addPayload("Hello, World!");

        String payload = request.getPayload();
        assertEquals("Hello, World!", payload);
    }

    @Test
    public void testAddPayloadWithByteArray() {
        Request request = new Request(Verb.PUT, "http://example.com");
        String payload = "Hello, World!";
        request.addPayload(payload.getBytes(StandardCharsets.UTF_8));

        byte[] bytePayload = request.getBytePayload();
        assertEquals(payload.getBytes(StandardCharsets.UTF_8), bytePayload);
    }

    @Test
    public void testSendRequest() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        Response response = request.send();

        assertEquals(200, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendRequestWithIOException() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        doReturn(null).when(request).createConnection();

        Response response = request.send();

        fail("An IOException should have been thrown");
    }

    @Test
    public void testAddHeaders() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");

        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("application/json", headers.get("Accept"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddHeadersWithNullKey() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.addHeaders(null, "application/json");

        fail("An IllegalArgumentException should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddHeadersWithNullValue() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.addHeaders("Content-Type", null);

        fail("An IllegalArgumentException should have been thrown");
    }

    @Test
    public void testSendRequestWithHttpURLConnection() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(request.createConnection()).thenReturn(connection);

        Response response = request.send();

        verify(connection).setRequestProperty("Content-Type", "application/json");
        verify(connection).setRequestProperty("Accept", "application/json");
        verifyNoMoreInteractions(connection);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testSendRequestWithBody() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.POST;

        Request request = new Request(verb, url);
        String body = "Hello, World!";
        request.addBodyParameter("foo", body);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(request.createConnection()).thenReturn(connection);

        Response response = request.send();

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read = 0;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }

        is.close();
        byte[] responseBody = baos.toByteArray();
        assertEquals(body, new String(responseBody, StandardCharsets.UTF_8));

        verify(connection).setDoOutput(true);
        verify(connection).getOutputStream().write(Arrays.copyOf(request.getByteBodyContents(), request.getByteBodyContents().length));
        verifyNoMoreInteractions(connection);

        assertEquals(200, response.getStatusCode());
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testUnsupportedCharset() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.setCharset("x-unicode-foobar");

        try {
            request.getByteBodyContents();
            fail("An UnsupportedEncodingException should have been thrown");
        } catch (UnsupportedEncodingException uee) {
            // expected
        }
    }

    @Test
    public void testConnectionKeepAlive() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.setConnectionKeepAlive(true);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(request.createConnection()).thenReturn(connection);

        Response response = request.send();

        assertEquals(true, connection.getDoOutput());
        assertEquals(true, connection.getDoInput());
        verifyNoMoreInteractions(connection);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testConnectionTimeout() throws Exception {
        String url = "http://example.com";
        Verb verb = Verb.GET;

        Request request = new Request(verb, url);
        request.setConnectTimeout(2000, TimeUnit.MILLISECONDS);
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(request.createConnection()).thenReturn(connection);

        Response response = request.send();

        verify(connection).setConnectTimeout(2000);
        verify(connection).setReadTimeout(1000);
        verifyNoMoreInteractions(connection);

        assertEquals(200, response.getStatusCode());
    }

    private HttpURLConnection mock(Class<?> clazz) throws Exception {
        return mock(clazz, new Object[] {});
    }

    private <T> T mock(Class<T> clazz, Object[] args) throws Exception {
        return Mockito.mock(clazz, withSettings().defaultAnswer(callRealMethod()));
    }
}