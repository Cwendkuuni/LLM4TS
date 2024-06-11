package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.model.utils.StreamUtils;
import org.scribe.utils.TestHttpURLConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = new TestHttpURLConnection(url.toExternalForm(), 200, "OK", new byte[0]);
        response = new Response(connection);
    }

    @Test
    public void testResponseConstructor() {
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
    }

    @Test
    public void testParseBodyContents() {
        response.parseBodyContents();
        assertNotNull(response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html;charset=UTF-8");
        expectedHeaders.put("Date", "Sun, 29 Mar 2015 01:23:45 GMT");

        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody_normal() {
        String expectedBody = "This is the response body.";
        response.setBody(expectedBody);
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetBody_empty() {
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testGetStream_normal() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is the response stream".getBytes());
        response.setStream(inputStream);
        InputStream actualStream = response.getStream();
        assertEquals(inputStream, actualStream);
    }

    @Test
    public void testGetStream_null() {
        response.setStream(null);
        assertNull(response.getStream());
    }

    @Test
    public void testGetCode_normal() {
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetCode_error() {
        TestHttpURLConnection errorConnection = new TestHttpURLConnection(new URL("http://example.com"), 404, "Not Found", new byte[0]);
        Response errorResponse = new Response(errorConnection);
        assertEquals(404, errorResponse.getCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCode_negative() {
        Response response = new Response(new TestHttpURLConnection(new URL("http://example.com"), -1, EMPTY, new byte[0]));
        response.getCode();
    }

    @Test(expected = IOException.class)
    public void testGetHeader_nullName() {
        response.getHeader(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetHeader_invalidEncoding() {
        response.setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "application/x-foo-bar; charset=invalidEncoding");
        }});
        response.getHeader("Content-Type");
    }

    @Test
    public void testGetHeader_normal() {
        response.setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }});
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testGetHeader_empty() {
        assertEquals(null, response.getHeader(null));
    }
}