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
    public void testResponse() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertEquals("Content-Type", response.getHeader("Content-Type"));
    }

    @Test(expected = IOException.class)
    public void testUnknownHostException() throws IOException {
        URL url = new URL("http://non-existent-host.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
    }

    @Test
    public void testEmptyBody() throws IOException {
        URL url = new URL("http://example.com/empty");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.getOutputStream().write(new byte[0]);
        connection.getOutputStream().flush();
        connection.getInputStream().close();
        Response response = new Response(connection);
        assertEquals(204, response.getCode());
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        URL url = new URL("http://example.com/headers");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "max-age=0");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.getOutputStream().write(new byte[0]);
        connection.getOutputStream().flush();
        connection.getInputStream().close();
        Response response = new Response(connection);
        Map<String, String> parsedHeaders = response.getHeaders();
        assertEquals(headers.size(), parsedHeaders.size());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            assertEquals(entry.getValue(), parsedHeaders.get(entry.getKey()));
        }
    }

    @Test
    public void testWasSuccessful() throws IOException {
        URL url = new URL("http://example.com/success");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setDoOutput(true);
        connection.getOutputStream().write(new byte[0]);
        connection.getOutputStream().flush();
        connection.getInputStream().close();
        Response response = new Response(connection);
        assertTrue(response.wasSuccessful());

        URL url2 = new URL("http://example.com/error");
        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
        connection2.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection2.setDoOutput(true);
        connection2.getOutputStream().write(new byte[0]);
        connection2.getOutputStream().flush();
        connection2.setResponseCode(401);
        connection2.getInputStream().close();
        Response response2 = new Response(connection2);
        assertFalse(response2.wasSuccessful());
    }

    @Test
    public void testGetBodyFromStream() throws IOException, UnsupportedEncodingException {
        String expectedBody = "{\"success\":true,\"message\":\"Welcome!\"}";
        URL url = new URL("http://example.com/json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.getOutputStream().write("application/json".getBytes());
        connection.getOutputStream().flush();
        connection.getInputStream().close();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStreamFromErrorConnection() throws IOException {
        URL url = new URL("http://example.com/error");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setDoOutput(true);
        connection.setRequestMethod("HEAD");
        connection.getOutputStream().write(new byte[0]);
        connection.getOutputStream().flush();
        connection.setResponseCode(401);
        InputStream inputStream = response.getStream();
        byte[] expectedBytes = new byte[4096];
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(expectedBytes);
        try {
            StreamUtils.copy(inputStream, testOutputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            inputStream.close();
        }
    }
}