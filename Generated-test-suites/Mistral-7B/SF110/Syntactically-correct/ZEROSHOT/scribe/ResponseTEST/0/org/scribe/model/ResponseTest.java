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

 private static final String EMPTY = "";
 private static final String TEST_HEADER_KEY = "Test-Header-Key";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 // Given
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 // When
 Response response = new Response(connection);
 // Then
 assertTrue(response.wasSuccessful());
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHost() throws IOException {
 // Given
 URL url = new URL("http://unknownhost.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 // When
 Response response = new Response(connection);
 // Then
 fail("Expected UnknownHostException but nothing was thrown.");
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 // Given
 URL url = new URL("http://error.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 // When
 Response response = new Response(connection);
 // Then
 assertFalse(response.wasSuccessful());
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseHeaders() throws IOException {
 // Given
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setRequestProperty(TEST_HEADER_KEY, TEST_HEADER_VALUE);
 // When
 Response response = new Response(connection);
 // Then
 Map<String, String> headers = response.getHeaders();
 assertEquals(1, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));
 }

 @Test
 public void testGetHeader() throws IOException {
 // Given
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setRequestProperty(TEST_HEADER_KEY, TEST_HEADER_VALUE);
 // When
 Response response = new Response(connection);
 // Then
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testParseBodyContents() throws IOException {
 // Given
 String expectedBody = "Expected Body";
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.getOutputStream().write(expectedBody.getBytes());
 connection.setRequestMethod("POST");
 // When
 Response response = new Response(connection);
 // Then
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 // When
 Response response = new Response(connection);
 // Then
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testResponseWithStream() throws IOException {
 // Given
 String expectedBody = "Expected Body";
 URL url = new URL("http://test.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.getOutputStream().write(expectedBody.getBytes());
 connection.setRequestMethod("POST");
 // When
 Response response = new Response(connection);
 // Then
 ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
 response.setStream(inputStream);
 assertEquals(expectedBody, response.getBody());
 }
}