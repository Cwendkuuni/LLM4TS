package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
 }

 @Test
 public void testResponseWithSuccessfulStatus() throws IOException {
 response = new Response(connection);
 assertTrue(response.wasSuccessful());
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorStatus() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");
 connection.setDoOutput(true);
 connection.getOutputStream().write("GET /test HTTP/1.1\r\nHost: example.com\r\nConnection: keep-alive\r\nAccept-Encoding: gzip, deflate\r\nAccept-Language: en-US,en;q=0.8\r\nUser-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36\r\n\r\n".getBytes());
 connection.disconnect();
 connection.connect();
 response = new Response(connection);
 assertFalse(response.wasSuccessful());
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseHeaders() {
 connection.setRequestProperty("Set-Cookie", "test=value; Path=/");
 connection.setRequestProperty("X-Custom-Header", "custom value");
 response = new Response(connection);
 Map<String, String> headers = response.getHeaders();
 assertEquals("value", headers.get("Set-Cookie"));
 assertEquals("custom value", headers.get("X-Custom-Header"));
 }

 @Test
 public void testGetHeader() {
 connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
 response = new Response(connection);
 assertEquals("application/json; charset=UTF-8", response.getHeader("Content-Type"));
 }

 @Test
 public void testGetEmptyResponse() throws IOException {
 connection.disconnect();
 response = new Response(connection);
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 connection.setDoOutput(true);
 connection.getOutputStream().write("Hello World".getBytes());
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.disconnect();
 response = new Response(connection);
 assertEquals("Hello World", response.getBody());
 }

 @Test
 public void testGetStream() throws IOException {
 ByteArrayInputStream expectedStream = new ByteArrayInputStream("Hello World".getBytes());
 connection.setDoOutput(true);
 connection.getOutputStream().write("Hello World".getBytes());
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.disconnect();
 connection.setDoInput(true);
 connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
 response = new Response(connection);
 InputStream actualStream = response.getStream();
 byte[] buffer = new byte[1024];
 int length;
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 while ((length = actualStream.read(buffer, 0, buffer.length)) > 0) {
 out.write(buffer, 0, length);
 }
 out.flush();
 assertEquals(expectedStream.readAllBytes(), out.toByteArray());
 }

 @Test
 public void testWasSuccessful() {
 response = new Response(null); // null connection
 assertFalse(response.wasSuccessful());
 }
}