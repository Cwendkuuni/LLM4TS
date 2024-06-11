package org.scribe.model;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRule;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectFactory;
import org.jmock.lib.legacy.Test;
import org.jmock.lib.legacy.TestCase;
import org.jmock.lib.legacy.mock.MockHttpURLConnection;
import org.jmock.lib.legacy.mock.MockInputStream;
import org.jmock.util.concurrent.MockClock;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest extends TestCase {

    private MockClock clock;
    private MockObjectFactory mockFactory;
    private MockHttpURLConnection connection;
    private Response response;

    {
        MockObjectFactory mockFactory = new MockObjectFactory();
        this.mockFactory = mockFactory;
        this.clock = new MockClock();
        ClassImposteriser.install(this.getClass().getClassLoader());
    }

    @Test
    public void testResponseWithSuccessfulConnection() throws IOException {
        // Given
        int expectedCode = HttpURLConnection.HTTP_OK;
        String expectedBody = "Success";
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json; charset=UTF-8");
        String expectedContentType = expectedHeaders.get("Content-Type");

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getResponseCode").will(returnValue(expectedCode));
        connection.expects(once()).method("getHeaderFields").will(returnValue(expectedHeaders));
        connection.expects(once()).method("getInputStream").andReturn(new MockInputStream(new ByteArrayInputStream(expectedBody.getBytes("UTF-8"))));

        // When
        URL url = new URL("http://example.com");
        response = new Response(connection);

        // Then
        assertEquals(expectedCode, response.getCode());
        assertEquals(expectedBody, response.getBody());
        assertEquals(expectedContentType, response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithInvalidHost() throws IOException {
        // Given
        int expectedCode = HttpURLConnection.HTTP_NOT_FOUND;

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getResponseCode").will(returnValue(expectedCode));

        // When
        URL url = new URL("http://non-existing-host");
        response = new Response(connection);

        // Then
        assertEquals(expectedCode, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals(0, response.getHeaders().size());
    }

    @Test
    public void testParseHeaders() throws IOException {
        // Given
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Length", "123");
        expectedHeaders.put("Content-Type", "application/json; charset=UTF-8");
        expectedHeaders.put("Date", "Mon, 27 Jul 2009 09:28:53 GMT");

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getHeaderFields").will(returnValue(expectedHeaders));

        // When
        Map<String, String> headers = response.getHeaders();

        // Then
        assertEquals(expectedHeaders, headers);
    }

    @Test
    public void testWasSuccessful() {
        // Given
        int expectedCode = HttpURLConnection.HTTP_OK;

        // When
        boolean result = response.wasSuccessful();

        // Then
        assertTrue(result);

        // Given
        int notSuccessfulCode = HttpURLConnection.HTTP_BAD_REQUEST;

        // When
        response.code = notSuccessfulCode;

        // Then
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetStream() throws IOException {
        // Given
        String expectedBody = "Expected body";

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getInputStream").andReturn(new MockInputStream(new ByteArrayInputStream(expectedBody.getBytes("UTF-8"))));

        // When
        URL url = new URL("http://example.com");
        response = new Response(connection);

        // Then
        InputStream actualStream = response.getStream();
        InputStream expectedStream = connection.getInputStream();
        assertEquals(expectedStream, actualStream);
    }

    @Test
    public void testGetBody() throws IOException, UnsupportedEncodingException {
        // Given
        String expectedBody = "Expected body";

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getInputStream").andReturn(new MockInputStream(new ByteArrayInputStream(expectedBody.getBytes("UTF-8"))));

        // When
        URL url = new URL("http://example.com");
        response = new Response(connection);

        // Then
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetHeader() throws IOException {
        // Given
        String expectedHeaderName = "Content-Type";
        String expectedHeaderValue = "application/json; charset=UTF-8";

        Map<String, String> headers = new HashMap<>();
        headers.put(expectedHeaderName, expectedHeaderValue);

        connection = (MockHttpURLConnection) mockFactory.mock(HttpURLConnection.class);
        connection.expects(once()).method("getHeaderFields").will(returnValue(headers));

        // When
        URL url = new URL("http://example.com");
        response = new Response(connection);

        // Then
        assertEquals(expectedHeaderValue, response.getHeader(expectedHeaderName));
    }

    @Test
    public void testGetHeaderWithNullName() {
        // Given
        String nullName = null;

        // When
        URL url = new URL("http://example.com");
        response = new Response(connection);

        // Then
        assertNull(response.getHeader(nullName));
    }
}