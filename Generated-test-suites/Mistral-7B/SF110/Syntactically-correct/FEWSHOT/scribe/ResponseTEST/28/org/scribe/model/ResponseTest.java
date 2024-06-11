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
    public void testResponseInstance() {
        try {
            Response response = new Response(createConnection(200));
            assertEquals(200, response.getCode());
            assertNull(response.getBody());
            assertNull(response.getStream());
            assertNull(response.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResponseInstanceWithEmptyBody() {
        try {
            Map<String, List<String>> headers = new HashMap<>();
            headers.put("Content-Length", Collections.singletonList("0"));
            HttpURLConnection connection = createConnectionWithResponse(200, EMPTY, headers);
            Response response = new Response(connection);
            assertEquals(200, response.getCode());
            assertEquals(EMPTY, response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertEquals(headers.size(), response.getHeaders().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResponseInstanceWithBody() {
        String body = "Hello World";
        try {
            Map<String, List<String>> headers = new HashMap<>();
            HttpURLConnection connection = createConnectionWithResponse(200, body, headers);
            Response response = new Response(connection);
            assertEquals(200, response.getCode());
            assertEquals(body, response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertEquals(headers.size(), response.getHeaders().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResponseInstanceWithError() {
        try {
            Response response = new Response(createConnection(404));
            assertEquals(404, response.getCode());
            assertEquals(EMPTY, response.getBody());
            assertNull(response.getStream());
            assertNotNull(response.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String body = "Hello World";
        HttpURLConnection connection = createConnectionWithResponse(200, body, null);
        Response response = new Response(connection);
        response.parseBodyContents();
        assertEquals(body, response.getBody());
    }

    @Test
    public void testParseHeaders() {
        String contentType = "text/html";
        String contentLength = "32";

        Map<String, List<String>> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", Collections.singletonList(contentType));
        expectedHeaders.put("Content-Length", Collections.singletonList(contentLength));

        HttpURLConnection connection = createConnectionWithResponse(200, EMPTY, expectedHeaders);
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();

        assertEquals(expectedHeaders.size(), headers.size());
        for (Map.Entry<String, List<String>> entry : expectedHeaders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().get(0);
            assertEquals(value, headers.get(key));
        }
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(createConnection(200));
        assertTrue(response.wasSuccessful());

        response = new Response(createConnection(401));
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetHeader() {
        String contentType = "text/html";
        String expectedContentType = "text/html";

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList(contentType));

        HttpURLConnection connection = createConnectionWithResponse(200, EMPTY, headers);
        Response response = new Response(connection);

        assertEquals(expectedContentType, response.getHeader("Content-Type"));
        assertEquals(null, response.getHeader("Content-Length"));
    }

    private HttpURLConnection createConnection(int statusCode) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        connection.setConnectTimeout(1000);
        connection.disconnect();
        connection.setResponseCode(statusCode);
        return connection;
    }

    private HttpURLConnection createConnectionWithResponse(int statusCode, String body, Map<String, List<String>> headers) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        connection.setConnectTimeout(1000);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoInput(true);

        if (body != null) {
            byte[] bodyBytes = body.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(bodyBytes, 0, bodyBytes.length);
            os.flush();
            os.close();
        }

        connection.setRequestProperty("Content-Length", String.valueOf(body.getBytes("UTF-8").length));
        connection.disconnect();
        connection.setResponseCode(statusCode);
        connection.setDoInput(true);

        if (statusCode == 200) {
            connection.setDoOutput(true);
            InputStream inputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
            connection.setInputStream(inputStream);
        }

        return connection;
    }

}