package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private HttpURLConnection connectionMock;
 private Response response;

 @Before
 public void setUp() {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 // Given: A successful HTTP response
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());

 // When: Response is created
 Response actualResponse = response;

 // Then: Response should have code, body, stream and headers
 assertEquals(200, actualResponse.getCode());
 assertNotNull(actualResponse.getBody());
 assertNotNull(actualResponse.getStream());
 assertNotNull(actualResponse.getHeaders());

 // Verify: parseBodyContents and parseHeaders were called
 Mockito.verify(connectionMock, Mockito.times(1)).disconnect();
 Mockito.verify(connectionMock, Mockito.times(1)).getInputStream();
 Mockito.verify(connectionMock, Mockito.times(1)).getHeaderFields();
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 // Given: A failed HTTP response
 Mockito.when(connectionMock.getResponseCode()).thenReturn(500);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());

 // When: Response is created
 Response actualResponse = response;

 // Then: Response should have error code and empty body and stream
 assertEquals(500, actualResponse.getCode());
 assertNull(actualResponse.getBody());
 assertNull(actualResponse.getStream());

 // Verify: errorStream was called instead of getInputStream()
 Mockito.verify(connectionMock, Mockito.times(1)).getErrorStream();
 }

 @Test
 public void testGetHeader() throws IOException {
 // Given: A successful HTTP response with a header "Content-Type: text/plain"
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("text/plain"));
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFields);

 // When: Response is created and get header is called
 String actualHeaderValue = response.getHeader("Content-Type");

 // Then: Header value should match expected value
 assertEquals("text/plain", actualHeaderValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWithNullName() {
 // Given: A Response instance

 // When: getHeader is called with null name
 response.getHeader(null);
 }
}