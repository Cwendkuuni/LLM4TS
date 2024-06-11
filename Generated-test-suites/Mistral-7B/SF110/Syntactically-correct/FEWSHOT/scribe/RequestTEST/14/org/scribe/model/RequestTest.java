package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {

    private static final String TEST_BASE_URL = "http://test.com";
    private static final Verb TEST_VERB = Verb.GET;
    private static final Map<String, String> TEST_QUERY_STRING_PARAMS = new HashMap<>();
    static {
        TEST_QUERY_STRING_params.put("key1", "value1");
        TEST_QUERY_STRING_params.put("key2", "value2");
    }

    private static final Map<String, String> TEST_BODY_PARAMS = new HashMap<>();
    static {
        TEST_BODY_params.put("key1", "value1");
        TEST_BODY_params.put("key2", "value2");
    }

    private static final Map<String, String> TEST_HEADERS = new HashMap<>();
    static {
        TEST_HEADERS.put("header1", "value1");
        TEST_HEADERS.put("header2", "value2");
    }

    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_PAYLOAD = "test payload";
    private static final byte[] TEST_BYTE_PAYLOAD = TEST_PAYLOAD.getBytes(StandardCharsets.UTF_8);

    private Request request;
    private HttpURLConnection connection;

    @Before
    public void setUp() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");
        request.addHeader("header1", "value1");
        request.addHeader("header2", "value2");
        request.addPayload(TEST_PAYLOAD);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Test
    public void testRequest() {
        assertEquals(TEST_VERB, request.getVerb());
        assertEquals(TEST_BASE_URL, request.getUrl());
        assertEquals(TEST_QUERY_STRING_Params, request.getQueryStringParams());
        assertEquals(TEST_BODY_params, request.getBodyParams());
        assertEquals(TEST_HEADERS, request.getHeaders());
        assertEquals(TEST_CHARSET, request.getCharset());
        assertFalse(request.hasBytePayload());
        assertNull(request.getByteBodyContents());
        assertNull(request.getPayload());
    }

    @Test
    public void testRequestWithBytePayload() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        request.addHeader("header1", "value1");
        request.addHeader("header2", "value2");
        request.addPayload(TEST_PAYLOAD);
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);
        request.setCharset(TEST_CHARSET);
        request.setConnectionKeepAlive(true);

        byte[] testBytePayload = TEST_BYTE_PAYLOAD;
        request.addPayload(testBytePayload);

        assertEquals(TEST_VERB, request.getVerb());
        assertEquals(TEST_BASE_URL, request.getUrl());
        assertEquals(TEST_QUERY_STRING_Params, request.getQueryStringParams());
        assertEquals(TEST_BODY_params, request.getBodyParams());
        assertEquals(TEST_HEADERS, request.getHeaders());
        assertEquals(TEST_CHARSET, request.getCharset());
        assertTrue(request.hasBytePayload());
        assertArrayEquals(testBytePayload, request.getByteBodyContents());
        assertNull(request.getPayload());
    }

    @Test
    public void testSend() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");
        request.addHeader("header1", "value1");
        request.addHeader("header2", "value2");

        connection = (HttpURLConnection) new URL(TEST_BASE_URL + "?" + URLUtils.mapToQueryString(TEST_QUERY_STRING_params)).openConnection();
        request.send();

        assertEquals(TEST_VERB.name(), connection.getRequestMethod());
        for (final String key : TEST_HEADERS.keySet()) {
            assertEquals(TEST_HEADERS.get(key), connection.getRequestProperty(key));
        }
        if (TEST_VERB.equals(Verb.PUT) || TEST_VERB.equals(Verb.POST)) {
            final InputStream inputStream = connection.getInputStream();
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                final byte[] responseBody = byteArrayOutputStream.toByteArray();
                assertEquals(TEST_PAYLOAD.length(), connection.getContentLength());
                assertEquals(TEST_PAYLOAD, new String(responseBody, TEST_CHARSET));
            }
        }
        new Response(connection);
    }

    @Test(expected = OAuthException.class)
    public void testSendWithNullUrl() throws Exception {
        request = new Request(TEST_VERB, null);
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithMalformedUrl() throws Exception {
        request = new Request(TEST_VERB, "http://test.com/");
        request.send();
    }

    @Test(expected = IOException.class)
    public void testSendWithIOException() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        final URL url = new URL("http://test.com/");
        request.connection = (HttpURLConnection) url.openConnection();
        request.send();
    }

    @Test
    public void testSetConnectTimeout() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
        assertEquals(500, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);
        assertEquals(500, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() throws Exception {
        request = new Request(TEST_VERB, TEST_BASE_URL);
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("@Request(GET http://test.com)", request.toString());
    }
}