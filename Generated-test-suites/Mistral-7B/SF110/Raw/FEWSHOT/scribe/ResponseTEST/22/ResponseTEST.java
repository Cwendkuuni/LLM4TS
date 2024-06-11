package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testResponseInstance() {
        try {
            URL url = new URL("http://example.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            assertNotNull(response);
            assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
            assertNull(response.getBody());
            assertNull(response.getStream());
            assertTrue(response.getHeaders().isEmpty());
        } catch (IOException e) {
            //ignore
        }
    }

    @Test
    public void testResponseSuccess() {
        try {
            URL url = new URL("http://example.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Response response = new Response(connection);
            assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
            String body = "{\"name\":\"John\", \"age\":30}";
            assertEquals(body, response.getBody());
            assertNotNull(response.getStream());
            Map<String, String> headers = response.getHeaders();
            assertEquals(8, headers.size());
            assertEquals("HTTP/1.1", headers.get("Server"));
            assertEquals("application/json; charset=UTF-8", headers.get("Content-Type"));
            assertEquals("text/plain", headers.get("Content-Language"));
            // other assertions for headers
        } catch (IOException e) {
            //ignore
        }
    }

    @Test
    public void testResponseFailure() {
        try {
            URL url = new URL("http://example.com/notfound");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getCode());
            assertNull(response.getBody());
            assertNull(response.getStream());
            assertTrue(response.getHeaders().isEmpty());
        } catch (IOException e) {
            //ignore
        }
    }

    @Test
    public void testParseBodyContents() {
        try {
            String body = "{\"name\":\"John\", \"age\":30}";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
            Response response = new Response(null);
            response.setBody(inputStream);
            assertEquals(body, response.parseBodyContents());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
    }

    @Test
    public void testParseHeaders() {
        try {
            Map<String, String> expectedHeaders = new HashMap<>();
            expectedHeaders.put("Server", "Apache/2.4.46 (Win64) OpenSSL/1.1.1c");
            expectedHeaders.put("Content-Type", "text/html; charset=UTF-8");
            expectedHeaders.put("Content-Length", "1234");
            expectedHeaders.put("Date", "Thu, 15 Oct 2020 19:47:36 GMT");
            expectedHeaders.put("Connection", "keep-alive");
            expectedHeaders.put("Transfer-Encoding", "chunked");

            URL url = new URL("http://example.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();
            Map<String, String> headers = response.parseHeaders(connection);
            assertEquals(expectedHeaders, headers);
        } catch (IOException e) {
            //ignore
        }
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(new Response(null).wasSuccessful());
        assertFalse(new Response(null).wasSuccessful());
        Response response = new Response(null);
        response.setCode(HttpURLConnection.HTTP_OK);
        assertTrue(response.wasSuccessful());
        response.setCode(HttpURLConnection.HTTP_BAD_REQUEST);
        assertFalse(response.wasSuccessful());
    }

}