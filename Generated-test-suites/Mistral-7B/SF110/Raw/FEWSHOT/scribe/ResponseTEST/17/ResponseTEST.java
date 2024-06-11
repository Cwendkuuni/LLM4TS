package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ResponseTest {

 private HttpURLConnection connectionMock;
 private Response response;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithValidConnection() throws IOException {
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Body".getBytes()));

 response.parseHeaders(connectionMock);

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getHeader("Content-Type"));
 }

 @Test
 public void testResponseWithErrorConnection() {
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 response.parseHeaders(connectionMock);

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Body".getBytes()));
 response.parseBodyContents();

 assertEquals("Body", response.getBody());
 }

 @Test
 public void testParseHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", " text/plain;charset=UTF-8 ");
 when(connectionMock.getHeaderFields()).thenReturn(headers);

 Map<String, String> parsedHeaders = response.parseHeaders(connectionMock);

 assertEquals(headers.get("Content-Type"), parsedHeaders.get("Content-Type"));
 }

 @Test
 public void testWasSuccessful() {
 assertTrue(response.wasSuccessful());
 response.code = 300;
 assertFalse(response.wasSuccessful());
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWithNullName() {
 response.headers = null;
 response.getHeader(null);
 }
}