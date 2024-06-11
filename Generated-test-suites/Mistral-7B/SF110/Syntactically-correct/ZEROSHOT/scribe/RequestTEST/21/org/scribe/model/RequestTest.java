package org.scribe.model;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObject;
import org.jmock.lib.legacy.Mockery;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    private static final String TEST_URL = "http://example.com";
    private static final String TEST_PAYLOAD = "Test payload";
    private static final Verb TEST_VERB = Verb.GET;
    private static final String TEST_CHARSET = "UTF-8";

    private Mockery context;
    private HttpURLConnection connectionMock;
    private Request request;

    @Before
    public void setUp() {
        context = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        connectionMock = context.mock(HttpURLConnection.class);
        request = new Request(TEST_VERB, TEST_URL);
    }

    @Test
    public void testRequestConstruction() {
        assertEquals(TEST_VERB, request.getVerb());
        assertEquals(TEST_URL, request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertEquals(false, request.isConnectionKeepAlive());
        assertNull(request.getCharset());
        assertTrue(request.getHeaders().isEmpty());
        assertTrue(request.getBodyParams().isEmpty());
        assertTrue(request.getQueryStringParams().isEmpty());
    }

    @Test
    public void testSendMethodCallsCreateConnectionAndDoSend() throws IOException {
        context.checking(new Expectations() {{
            oneOf(connectionMock).setRequestMethod(TEST_VERB.name());
            oneOf(connectionMock).setDoOutput(true);
            oneOf(connectionMock).setRequestProperty(with(equal(CONTENT_LENGTH)), with(any(String.class)));
        }});

        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendMethodThrowsIOException() throws Exception {
        context.checking(new Expectations() {{
            oneOf(connectionMock).setRequestMethod(TEST_VERB.name());
            will(throwException(new IOException()));
        }});

        request.send();
    }

    @Test
    public void testAddHeaderMethodSetsRequestHeader() throws IOException {
        String headerKey = "Test-Header";
        String headerValue = "Test Value";

        request.addHeader(headerKey, headerValue);

        context.checking(new Expectations() {{
            oneOf(connectionMock).setRequestProperty(headerKey, headerValue);
        }});

        request.send();
    }

    @Test
    public void testAddBodyParameterMethodSetsBodyParamMap() {
        String bodyParamKey = "Test-Param";
        String bodyParamValue = "Test Value";

        request.addBodyParameter(bodyParamKey, bodyParamValue);

        assertEquals(bodyParamValue, request.getBodyParams().get(bodyParamKey));
    }

    @Test
    public void testAddQuerystringParameterMethodSetsQuerystringParamMap() {
        String querystringParamKey = "Test-Param";
        String querystringParamValue = "Test Value";

        request.addQuerystringParameter(querystringParamKey, querystringParamValue);

        assertEquals(querystringParamValue, request.getQueryStringParams().get(querystringParamKey));
    }

    @Test
    public void testAddPayloadMethodSetsPayloadString() {
        request.addPayload(TEST_PAYLOAD);

        assertEquals(TEST_PAYLOAD, request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddPayloadMethodSetsBytePayload() {
        byte[] payloadBytes = TEST_PAYLOAD.getBytes(Charset.forName(TEST_CHARSET));

        request.addPayload(payloadBytes);

        assertEquals(TEST_PAYLOAD, request.getPayload());
        assertArrayEquals(payloadBytes, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParamsMethodReturnsMapWithQueryStringAndRequestParams() throws MalformedURLException {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("testParam1", "testValue1");
        expectedParams.put("testParam2", "testValue2");

        String urlWithParams = "http://example.com?testParam1=testValue1&testParam2=testValue2";
        Request requestWithParams = new Request(TEST_VERB, urlWithParams);
        Map<String, String> actualParams = requestWithParams.getQueryStringParams();

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetBodyParamsMethodReturnsMapWithBodyParams() {
        Request requestWithBodyParams = new Request(TEST_VERB, TEST_URL);
        requestWithBodyParams.addBodyParameter("testParam1", "testValue1");
        requestWithBodyParams.addBodyParameter("testParam2", "testValue2");

        Map<String, String> actualParams = requestWithBodyParams.getBodyParams();

        assertEquals(2, actualParams.size());
        assertTrue(actualParams.containsKey("testParam1"));
        assertTrue(actualParams.containsKey("testParam2"));
    }

    @Test
    public void testGetUrlMethodReturnsUrl() {
        assertEquals(TEST_URL, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrlMethodReturnsSanitizedUrl() {
        assertEquals(TEST_URL.replaceAll("\\?.*", "").replace("\\:\\d{4}", ""), request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContentsMethodReturnsBodyContents() throws IOException {
        request.addPayload(TEST_PAYLOAD);

        String expectedBodyContents = TEST_PAYLOAD;

        InputStream inputStream = new ByteArrayInputStream(TEST_PAYLOAD.getBytes(Charset.forName(TEST_CHARSET)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, TEST_CHARSET);
        try (InputStreamReader reader = new InputStreamReader(inputStream, TEST_CHARSET)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                writer.write(ch);
            }
        }
        writer.flush();

        request.setCharset(TEST_CHARSET);
        request.setPayload(outputStream.toString());

        String actualBodyContents = request.getBodyContents();

        assertEquals(expectedBodyContents, actualBodyContents);
    }

    @Test
    public void testGetByteBodyContentsMethodReturnsByteBodyContents() throws IOException {
        request.addPayload(TEST_PAYLOAD);

        byte[] expectedByteBodyContents = TEST_PAYLOAD.getBytes(Charset.forName(TEST_CHARSET));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, TEST_CHARSET);
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(expectedByteBodyContents), TEST_CHARSET)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                writer.write(ch);
            }
        }
        writer.flush();

        byte[] actualByteBodyContents = request.getByteBodyContents();

        assertArrayEquals(expectedByteBodyContents, actualByteBodyContents);
    }

    @Test
    public void testSetConnectTimeoutMethod() {
        int timeout = 1000;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        request.setConnectTimeout(timeout, timeUnit);

        assertEquals(timeout, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeoutMethod() {
        int timeout = 1000;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        request.setReadTimeout(timeout, timeUnit);

        assertEquals(timeout, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharsetMethodSetsRequestCharset() {
        request.setCharset(TEST_CHARSET);

        assertEquals(TEST_CHARSET, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAliveMethodSetsRequestKeepAlive() {
        request.setConnectionKeepAlive(true);

        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testToStringMethodReturnsStringRepresentationOfRequest() {
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}