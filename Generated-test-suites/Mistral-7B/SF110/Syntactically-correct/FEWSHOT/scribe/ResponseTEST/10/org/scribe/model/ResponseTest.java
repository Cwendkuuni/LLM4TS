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
    public void testResponseWithSuccessfulStatusCode() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("application/json", response.getHeader("Content-Type"));

        connection.disconnect();
    }

    @Test
    public void testResponseWithErrorStatusCode() throws Exception {
        URL url = new URL("http://example.com/notfound");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());

        connection.disconnect();
    }

    @Test
    public void testResponseWithEmptyBody() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("text/html; charset=ISO-8859-1", response.getHeader("Content-Type"));

        String expectedBody = "";
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(expectedBody.getBytes("UTF-8"));
        os.flush();
        os.close();

        response = new Response(connection);
        assertEquals(200, response.getCode());
        assertEquals(expectedBody, response.getBody());

        connection.disconnect();
    }

    @Test
    public void testResponseWithHeadersContainingMultipleValues() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("X-Foo", "bar1, bar2, baz");

        Response response = new Response(connection);
        Map<String, List<String>> headers = connection.getHeaderFields();
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("bar1, bar2, baz", response.getHeader("X-Foo"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromNullConnection() {
        Response response = new Response(null);
        assertNull(response.getHeaders());
    }

    @Test
    public void testParseHeadersFromConnectionWithEmptyHeaderFields() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("text/plain", response.getHeader("Content-Type"));
        assertNull(response.getHeader("Set-Cookie"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithHeadersContainingNullValues() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nX-Foo: null\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNull(response.getHeader("X-Foo"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithInvalidCharsetInContentType() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: application/json; charset=x-invalid\r\nTransfer-Encoding: chunked\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        try {
            Response response = new Response(connection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid charset x-invalid", e.getMessage());
        }

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithHeadersContainingEmptyValue() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nX-Foo: \r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNull(response.getHeader("X-Foo"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithMultilineHeaders() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nX-Foo:\r\n  bar1\r\n  bar2\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("bar1\r\n  bar2", response.getHeader("X-Foo"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithTrailingWhitespace() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nX-Foo: bar1\r\n \r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("bar1", response.getHeader("X-Foo"));

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithInvalidContentType() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain; text/html\r\nTransfer-Encoding: chunked\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        try {
            Response response = new Response(connection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid content type: text/plain; text/html", e.getMessage());
        }

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithInvalidTransferEncoding() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: text/plain\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        try {
            Response response = new Response(connection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid transfer encoding: text/plain", e.getMessage());
        }

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithInvalidContentLength() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nContent-Length: text/plain\r\n\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        try {
            Response response = new Response(connection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid content length: text/plain", e.getMessage());
        }

        connection.disconnect();
    }

    @Test
    public void testParseHeadersFromConnectionWithInvalidChunkedSize() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\n\r\n100\r\nfoo\r\n0\r\nbar\r\n-1\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        try {
            Response response = new Response(connection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid chunked size: -1", e.getMessage());
        }

        connection.disconnect();
    }

    @Test
    public void testResponseWithStream() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        String expectedBody = "Hello, World!";
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(expectedBody.getBytes("UTF-8"));
        os.flush();
        os.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes("UTF-8"));
        connection.setInputStream(inputStream);

        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertEquals(expectedBody, response.getBody());

        connection.disconnect();
    }

    @Test
    public void testResponseWithErrorStream() throws IOException {
        URL url = new URL("http://example.com/notfound");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write("HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\n".getBytes("UTF-8"));
        os.flush();
        os.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream("Error message".getBytes("UTF-8"));
        connection.setErrorStream(inputStream);

        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertEquals("Error message", response.getBody());

        connection.disconnect();
    }
}