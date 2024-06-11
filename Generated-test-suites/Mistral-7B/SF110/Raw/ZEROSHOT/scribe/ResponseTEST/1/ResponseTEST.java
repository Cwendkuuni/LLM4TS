package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HTTPURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {

 private Response response;
 private HTTPURLConnection connection;

 @Before
 public void setup() throws IOException {
 connection = (HTTPURLConnection) new URL("http://example.com").openConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseWithSuccessCode() throws IOException {
 // Given a successful response
 response = new Response(connection);

 // When getting the response code
 int code = response.getCode();

 // Then the code should be equal to 200
 assertEquals(200, code);

 // When getting the response body
 String body = response.getBody();

 // Then the body should be an empty string as it hasn't been set yet
 assertNull(body);

 // When getting the response stream
 InputStream stream = response.getStream();

 // Then the stream should not be null
 assertNotNull(stream);
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 // Given an error response
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(true);
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "application/json");
 connection.setRequestProperty("Content-Type", "application/json");
 connection.disconnect();
 connection.connect();

 // When creating a response instance
 response = new Response(connection);

 // Then the code should be equal to 404
 assertEquals(404, response.getCode());

 // When getting the response body
 body = response.getBody();

 // Then the body should be an empty string
 assertNotNull(body);
 assertEquals(EMPTY, body);
 }

 @Test
 public void testResponseWithValidHeaders() throws IOException {
 // Given a response with valid headers
 connection.setRequestProperty("X-Test-Header", "Test Value");
 connection.setRequestProperty("Content-Type", "application/json");
 connection.disconnect();
 connection.connect();

 // When creating a response instance
 response = new Response(connection);

 // When getting the headers map
 Map<String, String> headers = response.getHeaders();

 // Then the map should not be null
 assertNotNull(headers);

 // Then the headers should contain the expected key-value pair
 assertEquals("Test Value", headers.get("X-Test-Header"));
 }

 @Test
 public void testResponseWithEmptyHeaders() throws IOException {
 // Given a response with empty headers
 connection.disconnect();
 connection.connect();

 // When creating a response instance
 response = new Response(connection);

 // When getting the headers map
 headers = response.getHeaders();

 // Then the map should not be null
 assertNotNull(headers);

 // Then the map should be empty
 assertTrue(headers.isEmpty());
 }

 @Test
 public void testResponseWithNullInputStream() throws IOException {
 // Given a response with a null input stream
 connection.disconnect();
 connection.connect();
 connection.setDoOutput(false);
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "application/json");
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setDoInput(false);

 // When creating a response instance
 response = new Response(connection);
 connection.disconnect();

 // When getting the response stream
 stream = response.getStream();

 // Then the stream should be null
 assertNull(stream);
 }

 @Test
 public void testResponseWithValidBody() throws IOException {
 // Given a response with a valid body
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setDoInput(true);
 connection.setRequestMethod("POST");
 connection.getOutputStream().write("{\"name\":\"John\",\"age\":30}".getBytes("UTF-8"));
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.connect();

 // When creating a response instance
 response = new Response(connection);

 // When parsing the body
 body = response.getBody();

 // Then the body should not be null
 assertNotNull(body);

 // Then the body should be equal to the expected JSON string
 assertEquals("{\"name\":\"John\",\"age\":30}", body);
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given a response with an empty body
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.connect();

 // When creating a response instance
 response = new Response(connection);

 // When parsing the body
 body = response.getBody();
 // Then the body should be an empty string
 assertNotNull(body);
 assertEquals(EMPTY, body);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testResponseWithInvalidEncoding() throws IOException {
 // Given a response with an invalid encoding
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json; charset=ISO-8859-1");
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.connect();
 response = new Response(connection);
 // Then an UnsupportedEncodingException should be thrown when trying to parse the body
 response.getBody();
 }
}