package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private HttpURLConnection connectionMock;
 private Response response;

 @Before
 public void setUp() throws Exception {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithValidCodeAndContent() throws IOException {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Valid content".getBytes()));

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 int code = response.getCode();

 // Then
 assertEquals(200, code);
 assertEquals("Valid content", body);
 assertNotNull(headers);
 assertFalse(headers.isEmpty());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 int code = response.getCode();

 // Then
 assertEquals(200, code);
 assertEquals(EMPTY, body);
 assertNotNull(headers);
 assertFalse(headers.isEmpty());
 }

 @Test
 public void testResponseWithErrorStream() throws IOException {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenReturn(404);

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 InputStream stream = response.getStream();
 int code = response.getCode();

 // Then
 assertEquals(404, code);
 assertNull(body);
 assertNull(headers);
 assertEquals("N/A", stream.toString());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testParseBodyContentsWithInvalidEncoding() throws IOException {
 // Given
 response = new Response(connectionMock);
 response.setBody(Mockito.mock(InputStream.class));

 // When
 response.parseBodyContents();

 // Then (UnsupportedEncodingException should be thrown)
 }

 @Test
 public void testParseHeaders() {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Arrays.asList("application/json; charset=UTF-8"));
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFields);

 // When
 Map<String, String> headers = response.getHeaders();
 String contentType = headers.get("Content-Type");

 // Then
 assertEquals("application/json; charset=UTF-8", contentType);
 assertNotNull(headers);
 assertFalse(headers.isEmpty());
 }

 @Test
 public void testWasSuccessfulWithValidCode() {
 // Given
 int code = 200;

 // When
 boolean wasSuccessful = response.wasSuccessful();

 // Then
 assertTrue(wasSuccessful);
 }

 @Test
 public void testWasSuccessfulWithInvalidCode() {
 // Given
 int code = 401;

 // When
 boolean wasSuccessful = response.wasSuccessful();

 // Then
 assertFalse(wasSuccessful);
 }
}