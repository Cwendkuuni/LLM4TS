package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.utils.StreamUtils;

public class ResponseTest {

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testResponseWithSuccessfulStatus() throws Exception {
 // Given
 URL url = new URL("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("success body".getBytes()));
 when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Collections.singletonList("text/plain"));
 put("X-Custom-Header", Collections.singletonList("value"));
 }});

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertEquals("text/plain", response.getHeader("Content-Type"));
 assertEquals("value", response.getHeader("X-Custom-Header"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithErrorStatus() throws Exception {
 // Given
 URL url = new URL("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(404);
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("error body".getBytes()));

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testResponseWithEmptyBody() throws Exception {
 // Given
 URL url = new URL("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Collections.singletonList("text/plain"));
 put("X-Custom-Header", Collections.singletonList("value"));
 }});

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertEquals("text/plain", response.getHeader("Content-Type"));
 assertEquals("value", response.getHeader("X-Custom-Header"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testParseBodyContents() throws Exception {
 // Given
 URL url = new URL("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("success body".getBytes()));
 String body = "expected body";
 Response response = new Response(connection);

 // When
 response.parseBodyContents();

 // Then
 assertEquals("success body", response.getBody());
 }

 @Test(expected = IOException.class)
 public void testParseBodyContentsWithErrorStream() throws Exception {
 // Given
 URL url = new URL("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("error body".getBytes()));
 Response response = new Response(connection);

 // Then
 response.parseBodyContents();
 }

 @Test
 public void testParseHeaders() {
 // Given
 HttpURLConnection connection = mock(HttpURLConnection.class);
 Map<String, List<String>> headers = new HashMap<String, List<String>>() {{
 put("Content-Type", Collections.singletonList("text/plain"));
 put("X-Custom-Header", Collections.singletonList("value"));
 }};
 when(connection.getHeaderFields()).thenReturn(headers);

 // When
 Response response = new Response(connection);
 Map<String, String> parsedHeaders = response.getHeaders();

 // Then
 assertEquals(2, parsedHeaders.size());
 assertEquals("text/plain", parsedHeaders.get("Content-Type"));
 assertEquals("value", parsedHeaders.get("X-Custom-Header"));
 }

 @Test
 public void testWasSuccessfulWithSuccessfulStatus() {
 // Given
 Response response = new Response(new URL("http://example.com").openConnection());

 // Then
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasSuccessfulWithErrorStatus() {
 // Given
 Response response = new Response(new URL("http://example.com/error").openConnection());

 // Then
 assertFalse(response.wasSuccessful());
 }
}