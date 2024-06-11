package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseWithSuccessCode() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("HTTP/1.1 200 OK", response.getHeaders().get("Status"));
    }

    @Test
    public void testResponseWithErrorCode() throws IOException {
        URL url = new URL("http://nonexistent.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("HTTP/1.1 404 Not Found", response.getHeaders().get("Status"));
    }

    @Test
    public void testParseHeaders() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Server", "Apache/2.4.41 (Win64) OpenSSL/1.1.1d Java/8.0_31");
        connection.setRequestProperty("X-Powered-By", "Servlet/3.1 JSP/2.3");
        connection.setDoOutput(true);

        Response response = new Response(connection);

        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("Apache/2.4.41 (Win64) OpenSSL/1.1.1d Java/8.0_31", headers.get("Server"));
        assertEquals("Servlet/3.1 JSP/2.3", headers.get("X-Powered-By"));
    }

    @Test
    public void testGetBody() throws IOException {
        String testBody = "This is a test response body.";
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(testBody.length()));
        connection.getOutputStream().write(testBody.getBytes("UTF-8"));
        connection.getOutputStream().close();
        Response response = new Response(connection);

        String responseBody = response.getBody();
        assertEquals(testBody, responseBody);
    }

    @Test
    public void testGetStream() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        ByteArrayInputStream expectedStream = new ByteArrayInputStream(new byte[10]);
        Response response = new Response(connection);
        response.setStream(expectedStream);

        InputStream responseStream = response.getStream();
        InputStream originalStream = response.getStream();

        assertSame(expectedStream, responseStream);
        assertSame(originalStream, response.getStream());
    }

    @Test
    public void testGetHeader() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-My-Custom-Header", "Test Value");
        connection.setDoOutput(true);

        Response response = new Response(connection);

        String headerValue = response.getHeader("X-My-Custom-Header");
        assertEquals("Test Value", headerValue);
    }
}