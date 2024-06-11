package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody_NormalCase() {
        Response response = new Response(createMockHttpURLConnection(200, "OK", new byte[0][]));
        String expectedBody = "OK";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    // ... (other test cases for getBody())

    @Test
    public void testGetHeaders_NormalCase() {
        Response response = new Response(createMockHttpURLConnection(200, "OK", new byte[0][]));
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json; charset=UTF-8");
        // Add other headers as necessary
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    // ... (other test cases for getHeaders())

    @Test(expected = IOException.class)
    public void testResponseCreation_UnknownHostException() {
        Response response = new Response(createMockHttpURLConnection(0, null, null));
        assertNotNull(response);
        // Add more checks as necessary
    }

    @Test
    public void testGetBody_EmptyBody() {
        Response response = new Response(createMockHttpURLConnection(204, "No Content", new byte[0][]));
        String expectedBody = "";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    // ... (test cases for other methods: getStream(), getCode(), getHeader(), and wasSuccessful())

    private HttpURLConnection createMockHttpURLConnection(int code, String reasonPhrase, byte[] responseBody) throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setResponseCode(code);
        connection.setDoInput(true);
        if (responseBody != null) {
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(responseBody);
            }
        }
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }

}