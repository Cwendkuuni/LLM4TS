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
 Response response = new Response(createConnectionWithSuccessStatusCode(200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String expectedBody = "Expected body content";
 Response response = new Response(createConnectionWithExpectedBody(expectedBody.getBytes()));

 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() throws IOException {
 String expectedKey = "Content-Type";
 String expectedValue = "text/plain";
 Response response = new Response(createConnectionWithExpectedHeader(expectedKey, expectedValue));

 assertEquals(expectedValue, response.getHeader(expectedKey));
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response = new Response(createConnectionWithSuccessStatusCode(200));

 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasNotSuccessful() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(404));

 assertFalse(response.wasSuccessful());
 }

 private HttpURLConnection createConnectionWithSuccessStatusCode(int statusCode) throws IOException {
 HttpURLConnection connection = createConnection();
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithErrorStatusCode(int statusCode) throws IOException {
 HttpURLConnection connection = createConnection();
 connection.setResponseCode(statusCode);
 connection.disconnect();
 return connection;
 }

 private HttpURLConnection createConnectionWithExpectedBody(byte[] body) throws IOException {
 HttpURLConnection connection = createConnection();
 OutputStream outputStream = connection.getOutputStream();
 outputStream.write(body);
 outputStream.flush();
 outputStream.close();
 return connection;
 }

 private HttpURLConnection createConnectionWithExpectedHeader(String key, String value) throws IOException {
 HttpURLConnection connection = createConnection();
 connection.setRequestProperty(key, value);
 return connection;
 }

 private HttpURLConnection createConnection() throws IOException {
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 return connection;
 }

}