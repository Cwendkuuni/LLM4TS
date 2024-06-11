package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
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
    public void testResponseInstance() {
        try {
            Response response = new Response(createConnection(200));
            assertEquals(200, response.getCode());
            assertNull(response.getBody());
            assertNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertEmpty(response.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyResponse() {
        try {
            Response response = new Response(createConnection(404));
            assertEquals(404, response.getCode());
            assertEquals(EMPTY, response.getBody());
            assertNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertEmpty(response.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSuccessfulResponseWithBody() {
        String responseBody = "This is a response body";
        try {
            Response response = new Response(createConnectionWithBody(200, responseBody.getBytes()));
            assertEquals(200, response.getCode());
            assertEquals(responseBody, response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertFalse(response.getHeaders().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFailedResponseWithBody() {
        String responseBody = "This is a response body";
        try {
            Response response = new Response(createConnectionWithBody(500, responseBody.getBytes()));
            assertEquals(500, response.getCode());
            assertEquals(responseBody, response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertFalse(response.getHeaders().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void testUnknownHostException() {
        try {
            Response response = new Response(createUnknownHostConnection());
        } catch (IOException e) {
            assertEquals(404, e.getResponseCode());
        }
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "max-age=60");

        try {
            Response response = new Response(createConnectionWithHeaders(headers));
            Map<String, String> parsedHeaders = response.getHeaders();
            assertEquals(headers.size(), parsedHeaders.size());
            for (Map.Entry<String, String> header : headers.entrySet()) {
                assertEquals(header.getValue(), parsedHeaders.get(header.getKey()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            Response response = new Response(createConnectionWithHeaders(headers));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertNull(response.getHeader("Content-Length"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWasSuccessful() {
        try {
            Response response1 = new Response(createConnection(200));
            assertTrue(response1.wasSuccessful());
            Response response2 = new Response(createConnection(401));
            assertFalse(response2.wasSuccessful());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection createConnection(int statusCode) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("User-Agent", "TestClient/1.0");
        conn.setResponseCode(statusCode);
        return conn;
    }

    private HttpURLConnection createConnectionWithBody(int statusCode, byte[] body) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("User-Agent", "TestClient/1.0");
        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes("data=" + body.toString());
        wr.flush();
        wr.close();
        conn.setResponseCode(statusCode);
        return conn;
    }

    private HttpURLConnection createUnknownHostConnection() throws IOException {
        URL url = new URL("http://non-existent-host.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn;
    }

    private HttpURLConnection createConnectionWithHeaders(Map<String, String> headers) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("User-Agent", "TestClient/1.0");
        conn.setRequestProperty("Content-Type", headers.get("Content-Type"));

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        return conn;
    }
}