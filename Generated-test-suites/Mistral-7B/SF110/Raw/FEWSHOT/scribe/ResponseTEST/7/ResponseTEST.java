package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
            final URL url = new URL("http://example.com");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            final Response response = new Response(connection);
            assertNotNull(response);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void testUnknownHostException() {
        try {
            final URL url = new URL("http://nonexistentserver.com");
            new Response(url.openConnection());
        } catch (IOException e) {
            throw e;
        }
    }

    @Test
    public void testGetters() {
        try {
            final URL url = new URL("http://example.com");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            final Response response = new Response(connection);
            assertEquals(200, response.getCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeaders());
            assertNull(response.getHeader("Invalid-Header"));
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyContents() throws IOException {
        final String expectedBody = "Hello, World!";
        final ByteArrayInputStream testStream = new ByteArrayInputStream(expectedBody.getBytes());

        try {
            final URL url = new URL("http://example.com");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(expectedBody.getBytes());
            connection.getOutputStream().flush();

            final Response response = new Response(connection);
            assertEquals(200, response.getCode());
            assertEquals(expectedBody, response.getBody());
            final InputStream inputStream = response.getStream();
            assertEquals(expectedBody, StreamUtils.getStreamContents(inputStream));
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseHeaders() {
        try {
            final String testContentType = "text/plain";
            final String testContentLength = "5";

            final URL url = new URL("http://example.com");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", testContentType);
            connection.setRequestProperty("Content-Length", testContentLength);
            connection.connect();

            final Response response = new Response(connection);
            assertEquals(200, response.getCode());
            assertEquals(testContentType, response.getHeader("Content-Type"));
            assertEquals(Integer.parseInt(testContentLength), response.getHeaders().size());
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(new Response(new HttpURLConnection(new URL("http://example.com")) {
            {
                setResponseCode(200);
            }
        }).wasSuccessful());

        assertFalse(new Response(new HttpURLConnection(new URL("http://example.com")) {
            {
                setResponseCode(404);
            }
        }).wasSuccessful());
    }

}