package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() throws IOException {
 connection = new URL("http://example.com").openConnection(); asHttpURLConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructor() throws IOException {
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getStream());
 assertNull(response.getBody());
 }

 @Test
 public void testGetBody() throws IOException {
 String body = "Hello World";
 connection.setDoInput(true);
 StreamUtils.printStream(connection.getInputStream(), body.getBytes());
 response = new Response(connection);
 assertEquals(body, response.getBody());
 }

 @Test
 public void testGetStream() throws IOException {
 InputStream expectedStream = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o'});
 connection.setDoInput(true);
 connection.setDoOutput(true);
 StreamUtils.printStream(connection.getOutputStream(), "Hello".getBytes());
 connection.getResponseCode(); // read response code to trigger setting the stream
 response = new Response(connection);
 assertSame(expectedStream, response.getStream());
 }

 @Test
 public void testGetCode() {
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = response.getHeaders();
 assertEquals(3, headers.size());
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Content-Length"));
 assertTrue(headers.containsKey("Date"));
 }

 @Test
 public void testGetHeader() {
 response.getHeaders().put("X-Test", "Test Value");
 assertEquals("Test Value", response.getHeader("X-Test"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullHeader() {
 response.getHeader(null);
 }

 @Test
 public void testUnsuccessfulConnection() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.setDoInput(false);
 connection.disconnect();
 response = new Response(connection);
 assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHost() throws IOException {
 connection = new URL("http://non-existent-host.com").openConnection();
 response = new Response(connection);
 }
}