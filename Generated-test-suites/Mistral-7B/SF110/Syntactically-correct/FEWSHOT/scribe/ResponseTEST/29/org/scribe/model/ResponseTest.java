package org.scribe.model;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ResponseTest {

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createURLWithSuccessfulStatusCode(200));
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createURLWithSuccessfulStatusCode(404));
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String body = "This is the body of the response";
 Response response = new Response(createURLWithSuccessfulStatusCode(200));
 response.parseBodyContents();
 assertEquals(body, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 Response response = new Response(createMockHttpURLConnectionWithHeaders(headers));
 Map<String, String> parsedHeaders = response.getHeaders();
 assertEquals(headers, parsedHeaders);
 }

 @Test
 public void testWasSuccessful() {
 Response response = new Response(createURLWithSuccessfulStatusCode(200));
 assertTrue(response.wasSuccessful());

 Response responseWithErrorStatusCode = new Response(createURLWithSuccessfulStatusCode(401));
 assertFalse(responseWithErrorStatusCode.wasSuccessful());
 }

 @Test
 public void testGetBody() throws IOException {
 String body = "This is the body of the response";
 Response response = new Response(createURLWithSuccessfulStatusCode(200), body.getBytes());
 assertEquals(body, response.getBody());
 }

 @Test
 public void testGetStream() throws IOException {
 String body = "This is the body of the response";
 byte[] bodyBytes = body.getBytes();
 Response response = new Response(createURLWithSuccessfulStatusCode(200), new ByteArrayInputStream(bodyBytes));
 assertEquals(bodyBytes, response.getStream().readAllBytes());
 }

 @Test
 public void testGetHeader() {
 Response response = new Response(createMockHttpURLConnectionWithHeaders(Map.of("Content-Type", "application/json")));
 assertEquals("application/json", response.getHeader("Content-Type"));
 assertNull(response.getHeader("Content-Length"));
 }

 @Test
 public void testGetNullResponse() {
 Response response = new Response(null);
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNull(response.getHeaders());
 assertNull(response.getCode());
 }

 private URL createURLWithSuccessfulStatusCode(int statusCode) throws IOException {
 URL url = new URL("http://example.com");
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setDoOutput(false);
 conn.setInstanceFollowRedirects(false);
 conn.setRequestProperty("User-Agent", "Test");
 conn.setRequestProperty("Accept", "*/*");
 conn.setRequestProperty("Connection", "keep-alive");
 conn.setRequestProperty("Host", url.getHost());
 conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 conn.disconnect();
 conn.setResponseCode(statusCode);
 return url;
 }

 private HttpURLConnection createMockHttpURLConnectionWithHeaders(Map<String, String> headers) {
 HttpURLConnection conn = mock(HttpURLConnection.class);
 when(conn.getHeaderFields()).thenReturn(new Header[] { new BasicHeader("Content-Type", headers.get("Content-Type")) });
 return conn;
 }

}