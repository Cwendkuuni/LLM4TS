package org.scribe.model;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

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
        int expectedCode = 200;
        String expectedBody = "Successful response";
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        URL url = new URL("http://example.com/api");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);

        assertEquals(expectedCode, response.getCode());
        assertEquals(expectedBody, response.getBody());
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertNull(response.getStream());
    }

    @Test
    public void testEmptyResponse() throws IOException {
        int expectedCode = 204;
        String expectedBody = "";
        Map<String, String> expectedHeaders = new HashMap<>();
        URL url = new URL("http://example.com/api/empty");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Response response = new Response(connection);

        assertEquals(expectedCode, response.getCode());
        assertEquals(expectedBody, response.getBody());
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty());
        assertNull(response.getStream());
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostException() throws IOException {
        String urlString = "http://non-existent-host.com";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        new Response(connection);
    }

    @Test
    public void testErrorResponse() throws IOException {
        int expectedCode = 500;
        String expectedBody = "Internal Server Error";
        Map<String, String> expectedHeaders = new HashMap<>();
        URL url = new URL("http://example.com/api/error");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setResponseCode(expectedCode);

        Response response = new Response(connection);

        assertEquals(expectedCode, response.getCode());
        assertNotNull(response.getBody());
        assertFalse(response.wasSuccessful());
        assertNull(response.getStream());

        JSONObject jsonObject = new JSONObject(response.getBody());
        String errorMessage = jsonObject.getString("error");
        assertEquals("Error message", errorMessage);
    }

    @Test
    public void testStreamResponse() throws IOException {
        int expectedCode = 200;
        String expectedBody = "Streamed response";
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/plain");

        byte[] bodyBytes = "Streamed response".getBytes();

        URL url = new URL("http://example.com/api/stream");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("User-Agent", "MyUserAgent");

        connection.setFixedLengthStreamingMode(bodyBytes.length);
        connection.setOutputStream(output -> output.write(bodyBytes));

        Response response = new Response(connection);

        assertEquals(expectedCode, response.getCode());
        assertEquals(expectedBody, response.getBody());
        assertNotNull(response.getHeaders());
        assertEquals("text/plain", response.getHeader("Content-Type"));
        assertNotNull(response.getStream());

        String streamContent = StreamUtils.getStreamContents(response.getStream());
        assertEquals(expectedBody, streamContent);
    }

    private ByteArrayInputStream output;

    @Test
    public void testSetOutput() throws IOException {
        int expectedCode = 200;
        String expectedBody = "Set Output Test";

        URL url = new URL("http://example.com/api/set-output");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setFixedLengthStreamingMode(expectedBody.getBytes().length);
        connection.setOutputStream(output = new ByteArrayOutputStream());

        Response response = new Response(connection);

        assertEquals(expectedCode, response.getCode());
        assertEquals(expectedBody, response.getBody());
        assertTrue(output.size() > 0);
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, List<String>> headersMap = new HashMap<>();
        headersMap.put("Content-Type", Collections.singletonList("application/json"));
        headersMap.put("Cache-Control", Collections.singletonList("max-age=0"));
        headersMap.put("X-Foo", Collections.singletonList("Bar"));

        URL url = new URL("http://example.com/api");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("User-Agent", "MyUserAgent");

        connection.setDoOutput(true);
        connection.setDoInput(true);

        for (Map.Entry<String, List<String>> header : headersMap.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue().get(0));
        }

        Response response = new Response(connection);

        Map<String, String> headers = response.getHeaders();

        assertEquals(headersMap.size(), headers.size());

        for (Map.Entry<String, List<String>> header : headersMap.entrySet()) {
            assertEquals(header.getValue().get(0), headers.get(header.getKey()));
        }
    }

}