package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.Test;
import org.scribe.utils.*;
import org.scribe.model.*;

public class ResponseTest {

 private HttpURLConnection mockHttpURLConnection;
 private Response response;

 @Before
 public void setUp() {
 mockHttpURLConnection = mock(HttpURLConnection.class);
 response = new Response(mockHttpURLConnection);
 }

 @Test
 public void testResponseWithSuccessfulStatusCode() throws Exception {
 // Given
 when(mockHttpURLConnection.getResponseCode()).thenReturn(200);
 when(mockHttpURLConnection.getInputSteam()).thenReturn(new ByteArrayInputStream("Success".getBytes()));

 // When
 Response actualResponse = new Response(mockHttpURLConnection);

 // Then
 assertEquals(200, actualResponse.getCode());
 assertNotNull(actualResponse.getStream());
 assertNotNull(actualResponse.getBody());
 assertEquals("Success", actualResponse.getBody());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws Exception {
 // Given
 when(mockHttpURLConnection.getResponseCode()).thenReturn(404);
 when(mockHttpURLConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("Error".getBytes()));

 // When
 Response actualResponse = new Response(mockHttpURLConnection);

 // Then
 assertEquals(404, actualResponse.getCode());
 assertNull(actualResponse.getStream());
 assertEquals("", actualResponse.getBody());
 }

 @Test
 public void testParseHeaders() throws Exception {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Arrays.asList("application/json"));
 when(mockHttpURLConnection.getHeaderFields()).thenReturn(headerFields);

 // When
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 Response actualResponse = new Response(mockHttpURLConnection);
 Map<String, String> actualHeaders = actualResponse.getHeaders();

 // Then
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testWasSuccessful() throws Exception {
 // Given
 when(mockHttpURLConnection.getResponseCode()).thenReturn(201);

 // When
 boolean actualResult = new Response(mockHttpURLConnection).wasSuccessful();

 // Then
 assertTrue(actualResult);
 }

 @Test
 public void testWasUnsuccessful() throws Exception {
 // Given
 when(mockHttpURLConnection.getResponseCode()).thenReturn(405);

 // When
 boolean actualResult = new Response(mockHttpURLConnection).wasSuccessful();

 // Then
 assertFalse(actualResult);
 }

 @Test
 public void testParseBodyContents() throws Exception {
 // Given
 when(mockHttpURLConnection.getInputStream()).thenReturn(new ByteArrayInputStream("{ \"name\": \"John\", \"age\": 30 }".getBytes()));

 // When
 String actualBody = new Response(mockHttpURLConnection).parseBodyContents();

 // Then
 assertEquals("{ \"name\": \"John\", \"age\": 30 }", actualBody);
 }
}