package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {

    @Test
    public void testRequestInitialization() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertEquals(false, request.isConnectionKeepAlive());
        assertEquals(null, request.getCharset());
        assertEquals(null, request.getConnection());
        assertEquals(null, request.getHeaders());
        assertEquals(new HashMap<>(), request.getBodyParams());
        assertEquals(new HashMap<>(), request.getQueryStringParams());
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        Map<String, String> headers = new HashMap<>();
        headers.put("Test-Header", "Test-Value");
        request.addHeaders(headers);
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com?param=value");
        request.addQuerystringParameter("key", "value");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value", params.get("key"));
        assertEquals(2, params.size());
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.PUT, "http://example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }

    @Test
    public void testAddPayloadWithByteArray() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload(new byte[] { 1, 2, 3 });
        assertEquals(Arrays.toString(new byte[] { 1, 2, 3 }), Arrays.toString(request.getBytePayload()));
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("payload");
        String charset = "UTF-8";
        request.setCharset(charset);
        byte[] expectedBytePayload = "payload".getBytes(Charset.forName(charset));
        byte[] bytePayload = request.getByteBodyContents();
        assertEquals(expectedBytePayload, bytePayload);
    }

    @Test
    public void testSendWithGETRequest() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com?param=value");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testSendWithPOSTRequest() throws IOException {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testSendWithPUTRequest() throws IOException {
        Request request = new Request(Verb.PUT, "http://example.com");
        request.addPayload("payload");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testSendWithInvalidVerb() {
        Request request = new Request(null, "http://example.com");
        assertThrows(IllegalArgumentException.class, () -> request.send());
    }

    @Test
    public void testCreateConnectionWithNullUrl() {
        Request request = new Request(Verb.GET, null);
        assertThrows(NullPointerException.class, () -> request.send());
    }

    @Test
    public void testCreateConnectionWithInvalidUrl() {
        Request request = new Request(Verb.GET, "http://example:%");
        assertThrows(MalformedURLException.class, () -> request.send());
    }

    @Test
    public void testCreateConnectionWithTimeout() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        request.setReadTimeout(1, TimeUnit.SECONDS);
        try {
            request.send();
            fail("Expected OAuthException to be thrown");
        } catch (OAuthException e) {
            assertEquals("Problems while creating connection", e.getMessage());
        }
    }

    @Test
    public void testCreateConnectionWithUnexpectedStatusCode() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com/404");
        Response response = request.send();
        assertEquals(404, response.getStatusCode());
    }
}