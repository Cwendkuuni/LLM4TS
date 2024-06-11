package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {

 private HttpURLConnection connection;
 private Response response;

 @Before
 public void setUp() throws IOException {
 URL url = new URL("http://example.com");
 connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 }

 @Test
 public void testResponseWithSuccessfulCode() throws IOException {
 response = new Response(connection);
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0");
 connection.disconnect();
 connection.setInstanceFollowRedirects(true);
 connection.connect();
 response = new Response(connection);
 assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 connection.disconnect();
 connection.setDoOutput(true);
 connection.getOutputStream().close();
 connection.connect();
 response = new Response(connection);
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertTrue(response.getBody().isEmpty());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithHeaders() throws IOException, UnsupportedEncodingException {
 connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
 connection.disconnect();
 connection.setDoOutput(true);
 connection.getOutputStream().write("{}".getBytes("UTF-8"));
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.connect();
 response = new Response(connection);
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals("application/json; charset=UTF-8", response.getHeader("Content-Type"));
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHostException() throws IOException {
 connection.disconnect();
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(true);
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0");
 connection.connect();
 connection.disconnect();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 connection.connect();
 response = new Response(connection);
 }
}