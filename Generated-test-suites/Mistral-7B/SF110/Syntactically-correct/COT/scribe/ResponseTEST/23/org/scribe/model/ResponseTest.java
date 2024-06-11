package org.scribe.model.test;

import org.junit.Assert;
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
    public void testResponseConstructorWithValidConnection() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);

        Assert.assertEquals(200, response.getCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getHeaders());
        Assert.assertNotNull(response.getStream());
    }

    @Test
    public void testResponseConstructorWithUnknownHostException() {
        URL url = new URL("http://non-existent-host.com");
        Response response = new Response(url.openConnection());

        Assert.assertEquals(404, response.getCode());
        Assert.assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String expectedBody = "Expected body content";
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.getOutputStream().write(expectedBody.getBytes("UTF-8"));

        Response response = new Response(connection);
        Assert.assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "max-age=0");

        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setHeaders(expectedHeaders.toArray(new String[expectedHeaders.size()]));

        Response response = new Response(connection);

        Assert.assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testWasSuccessfulWithValidResponseCode() {
        Response response = new Response(new URL("http://example.com").openConnection());

        Assert.assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWithInvalidResponseCode() {
        Response response = new Response(new URL("http://example.com/error").openConnection());

        Assert.assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetHeaderWithValidName() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setHeaders(headers.toArray(new String[headers.size()]));

        Response response = new Response(connection);
        String contentType = response.getHeader("Content-Type");

        Assert.assertEquals("application/json", contentType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetHeaderWithNullName() throws IOException {
        Response response = new Response(new URL("http://example.com").openConnection());
        response.getHeader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHeaderWithEmptyName() throws IOException {
        Response response = new Response(new URL("http://example.com").openConnection());
        response.getHeader(Strings.EMPTY);
    }
}