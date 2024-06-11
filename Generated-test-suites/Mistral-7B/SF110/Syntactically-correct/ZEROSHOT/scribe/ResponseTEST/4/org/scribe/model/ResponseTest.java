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
 public void setup() throws IOException {
 URL url = new URL("http://example.com");
 connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 }

 @Test
 public void testResponseWithSuccessStatusCode() throws IOException {
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertFalse(response.getStream().available() > 0);

 Map<String, String> headers = response.getHeaders();
 assertEquals(4, headers.size());
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Content-Length"));
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
 connection.connect();
 response = new Response(connection);
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testParseHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("Content-Length", "1234");
 connection.setRequestProperty("Set-Cookie", "test=value");
 connection.setRequestProperty("Cache-Control", "max-age=3600");
 response = new Response(connection);
 Map<String, String> responseHeaders = response.getHeaders();
 assertEquals(5, responseHeaders.size());
 assertTrue(responseHeaders.containsKey("Set-Cookie"));
 assertEquals("value", responseHeaders.get("Set-Cookie"));
 assertEquals("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8, application/json", responseHeaders.get("Accept"));
 assertEquals("max-age=3600", responseHeaders.get("Cache-Control"));
 }

 @Test
 public void testParseBodyContents() throws IOException {
 connection.setDoOutput(true);
 String body = "{\"name\":\"John Doe\",\"age\":30}";
 OutputStream os = connection.getOutputStream();
 os.write(body.getBytes("UTF-8"));
 os.flush();
 os.close();
 response = new Response(connection);
 assertEquals("{\"name\":\"John Doe\",\"age\":30}", response.getBody());
 }

 @Test
 public void testWasSuccessful() {
 response = new Response(connection);
 assertFalse(response.wasSuccessful());
 connection.setResponseCode(200);
 response = new Response(connection);
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testGetHeader() throws IOException, UnsupportedEncodingException {
 response = new Response(connection);
 assertEquals("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8", response.getHeader("Accept"));
 connection.setRequestProperty("X-Custom-Header", "test");
 response = new Response(connection);
 assertEquals("test", response.getHeader("X-Custom-Header"));
 }

 @Test
 public void testGetEmptyResponse() throws IOException {
 connection.disconnect();
 response = new Response(connection);
 assertEquals(0, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test(expected = IOException.class)
 public void testExceptionOnInvalidUrl() {
 response = new Response(new URL("http://example.com/this_is_invalid"));
 fail("Should throw IOException on invalid URL");
 }
}