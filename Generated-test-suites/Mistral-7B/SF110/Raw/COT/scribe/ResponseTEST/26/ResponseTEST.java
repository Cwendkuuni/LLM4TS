package org.scribe.model;

import static org.junit.Assert.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

 private static final String EMPTY = "";

 @Test
 public void testResponseConstructorWithValidConnection() throws Exception {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, EMPTY, new HashMap<>()));
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseConstructorWithInvalidHost() throws Exception {
 Response response = new Response(createMockHttpURLConnection(404, EMPTY, EMPTY, new HashMap<>()));
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 HttpURLConnection mockConnection = createMockHttpURLConnection(200, EMPTY, EMPTY, headers);
 Response response = new Response(mockConnection);
 Map<String, String> parsedHeaders = response.getHeaders();
 assertEquals(1, parsedHeaders.size());
 assertEquals("application/json", parsedHeaders.get("Content-Type"));
 }

 @Test
 public void testWasSuccessful() {
 Response response = new Response(createMockHttpURLConnection(201, EMPTY, EMPTY, new HashMap<>()));
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasNotSuccess() {
 Response response = new Response(createMockHttpURLConnection(401, EMPTY, EMPTY, new HashMap<>()));
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetBody() throws Exception {
 String expectedBody = "{\"key\":\"value\"}";
 InputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
 HttpURLConnection mockConnection = createMockHttpURLConnection(200, EMPTY, inputStream, new HashMap<>());
 Response response = new Response(mockConnection);
 String responseBody = response.getBody();
 assertEquals(expectedBody, responseBody);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetBodyWhenNoStream() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, null, new HashMap<>()));
 response.getBody();
 }

 @Test
 public void testGetStream() throws Exception {
 InputStream expectedStream = new ByteArrayInputStream("expected stream content".getBytes());
 HttpURLConnection mockConnection = createMockHttpURLConnection(200, EMPTY, expectedStream, new HashMap<>());
 Response response = new Response(mockConnection);
 InputStream actualStream = response.getStream();
 assertSame(expectedStream, actualStream);
 }

 @Test
 public void testGetCode() {
 Response response = new Response(createMockHttpURLConnection(403, EMPTY, EMPTY, new HashMap<>()));
 assertEquals(403, response.getCode());
 }

 @Test
 public void testGetHeader() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 HttpURLConnection mockConnection = createMockHttpURLConnection(200, EMPTY, EMPTY, headers);
 Response response = new Response(mockConnection);
 String expectedHeader = "application/json";
 String actualHeader = response.getHeader("Content-Type");
 assertEquals(expectedHeader, actualHeader);
 }

 @Test
 public void testGetHeaderWithNullName() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, EMPTY, new HashMap<>()));
 assertNull(response.getHeader(null));
 }

 private HttpURLConnection createMockHttpURLConnection(int responseCode, String responseMessage, InputStream responseStream, Map<String, String> headers) throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(responseCode);
 when(connection.getResponseMessage()).thenReturn(responseMessage);
 when(connection.getInputStream()).thenReturn(responseStream);
 when(connection.getErrorStream()).thenReturn(null);
 when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);
 if (headers != null) {
 for (Map.Entry<String, String> entry : headers.entrySet()) {
 givenThat(connection).willReturn(entry.getValue()).givenInput("getHeaderField", eq(entry.getKey()));
 }
 }
 return connection;
 }

}