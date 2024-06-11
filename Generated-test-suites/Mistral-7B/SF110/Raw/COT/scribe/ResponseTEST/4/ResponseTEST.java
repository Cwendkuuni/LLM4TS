package org.scribe.model.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

    @Test
    public void testResponse() throws IOException {
        int code = 200;
        String body = "Success";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        InputStream expectedStream = new ByteArrayInputStream(body.getBytes());

        Response response = new Response(createMockConnection(code, headers, expectedStream));

        assertEquals(code, response.getCode());
        assertEquals(body, response.getBody());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream()); // stream should be wrapped

        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testResponseWithEmptyBody() throws IOException {
        int code = 200;
        Map<String, String> headers = new HashMap<String, String>();

        Response response = new Response(createMockConnection(code, headers, null));

        assertEquals(code, response.getCode());
        assertEquals(EMPTY, response.getBody());
        assertNotNull(response.getHeaders());
        assertNull(response.getStream());
    }

    @Test
    public void testResponseWithError() throws IOException {
        int code = 404;
        String body = "Not Found";

        Response response = new Response(createMockConnection(code, null, null));

        assertEquals(code, response.getCode());
        assertEquals(body, response.getBody());
        assertNull(response.getHeaders());
        assertNull(response.getStream());
    }

    @Test(expected = UnknownHostException.class)
    public void testResponseWithUnknownHostException() throws IOException {
        int code = 404;

        Response response = new Response(createMockConnectionWithUnknownHostException());

        assertEquals(code, response.getCode());
        assertEquals(EMPTY, response.getBody());
        assertNull(response.getHeaders());
        assertNull(response.getStream());
    }

    private HttpURLConnection createMockConnection(int code, Map<String, String> headers, InputStream stream) throws IOException {
        URL url = new URL("http://test.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "Scribe Java Library");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Connection", "keep-alive");

        connection.setResponseCode(code);
        connection.setRequestProperty("Content-Type", "application/json"); // for headers test
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (stream != null) {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            byte[] input = stream.readAllBytes();
            os.write(input, 0, input.length);
            os.flush();
        }

        return connection;
    }

    private HttpURLConnection createMockConnectionWithUnknownHostException() throws IOException {
        URL url = new URL("http://non-existent-host.com");
        return (HttpURLConnection) url.openConnection();
    }
}