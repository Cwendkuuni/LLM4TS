package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;

public class RequestTest {

 Request request;
 Verb verb = Verb.GET;
 Map<String, String> querystringParams = new HashMap<>();
 Map<String, String> bodyParams = new HashMap<>();
 Map<String, String> headers = new HashMap<>();
 String url = "http://example.com";

 @Test
 public void testRequestInstance() {
     request = new Request(verb, url);
     assertNull(request.connection);
     assertEquals(verb, request.getVerb());
     assertEquals(url, request.getUrl());
     assertEquals(new HashMap<>(), request.getBodyParams());
     assertEquals(new HashMap<>(), request.getHeaders());
     assertNull(request.payload);
     assertNull(request.bytePayload);
     assertFalse(request.connectionKeepAlive);
 }

 @Test
 public void testAddHeaders() {
     request = new Request(verb, url);
     String key = "Test-Header";
     String value = "Test-Value";
     request.addHeader(key, value);
     assertEquals(Arrays.asList(key, value), request.headers.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()));
 }

 @Test
 public void testAddBodyParameters() {
     request = new Request(verb, url);
     String key = "Test-Key";
     String value = "Test-Value";
     request.addBodyParameter(key, value);
     assertEquals(value, request.bodyParams.get(key));
 }

 @Test
 public void testAddQuerystringParameters() {
     request = new Request(verb, url);
     String key = "Test-Key";
     String value = "Test-Value";
     request.addQuerystringParameter(key, value);
     assertEquals(url + "?" + key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8), request.getUrl());
 }

 @Test
 public void testAddPayload() {
     request = new Request(verb, url);
     String payload = "Test-Payload";
     request.addPayload(payload);
     assertEquals(payload, request.payload);
 }

 @Test
 public void testAddBytePayload() throws UnsupportedEncodingException {
     request = new Request(verb, url);
     String payload = "Test-Payload";
     byte[] bytePayload = payload.getBytes(StandardCharsets.UTF_8);
     request.addPayload(bytePayload);
     assertEquals(bytePayload, request.bytePayload);
 }

 @Test
 public void testGetQueryStringParams() {
     request = new Request(verb, url);
     Map<String, String> expectedParams = new HashMap<>();
     expectedParams.put("testParam1", "testValue1");
     expectedParams.put("testParam2", "testValue2");
     request.querystringParams = expectedParams;
     Map<String, String> actualParams = request.getQueryStringParams();
     assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testGetBodyParams() {
     request = new Request(verb, url);
     Map<String, String> expectedParams = new HashMap<>();
     expectedParams.put("testParam1", "testValue1");
     expectedParams.put("testParam2", "testValue2");
     request.bodyParams = expectedParams;
     Map<String, String> actualParams = request.getBodyParams();
     assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testCreateConnection() throws IOException, URISyntaxException {
     HttpURLConnection mockConnection = mock(HttpURLConnection.class);
     when(mockConnection.getResponseCode()).thenReturn(HTTP_OK);
     request = new Request(verb, url);
     doReturn(mockConnection).when(request).createConnection();
     request.send();
     verify(mockConnection, times(1)).connect();
 }

 @Test(expected = OAuthException.class)
 public void testSendWithException() throws IOException {
     HttpURLConnection mockConnection = mock(HttpURLConnection.class);
     when(mockConnection.getResponseCode()).thenThrow(new IOException());
     request = new Request(verb, url);
     request.send();
 }

 @Test
 public void testDoSend() throws IOException {
     HttpURLConnection mockConnection = mock(HttpURLConnection.class);
     Map<String, String> expectedHeaders = new HashMap<>();
     expectedHeaders.put("Test-Header", "Test-Value");
     when(mockConnection.getHeaderFields()).thenReturn(expectedHeaders);
     doReturn(mockConnection).when(request).createConnection();
     Response response = request.doSend();
     assertEquals("Test-Value", response.getHeader("Test-Header"));
 }

 @Test
 public void testAddHeadersToConnection() throws IOException {
     HttpURLConnection mockConnection = mock(HttpURLConnection.class);
     String key = "Test-Header";
     String value = "Test-Value";
     request = new Request(verb, url);
     request.addHeader(key, value);
     doReturn(mockConnection).when(request).createConnection();
     request.doSend();
     verify(mockConnection, times(1)).setRequestProperty(eq(key), eq(value));
 }

 @Test
 public void testAddBodyToConnection() throws IOException {
     HttpURLConnection mockConnection = mock(HttpURLConnection.class);
     byte[] bytePayload = "Test-Payload".getBytes(StandardCharsets.UTF_8);
     request = new Request(verb, url);
     request.addPayload(bytePayload);
     doReturn(mockConnection).when(request).createConnection();
     doReturn(new OutputStream(){
         @Override
         public void write(int b) throws IOException {
             // Do nothing
         }
     }).when(mockConnection).getOutputStream();
     request.doSend();
     verify(mockConnection, times(1)).setDoOutput(true);
     verify(mockConnection, times(1)).setRequestProperty("Content-Length", eq(String.valueOf(bytePayload.length)));
 }

 @Test
 public void testGetByteBodyContents() throws IOException, UnsupportedEncodingException {
     String payload = "Test-Payload";
     request = new Request(verb, url);
     request.payload = payload;
     byte[] bytePayload = request.getByteBodyContents();
     assertEquals(payload.getBytes(StandardCharsets.UTF_8), bytePayload);
 }

 @Test
 public void testGetByteBodyContentsWithBodyParams() throws IOException, UnsupportedEncodingException {
     String payload = "Test-Payload";
     Map<String, String> bodyParams = new HashMap<>();
     bodyParams.put("testParam1", "testValue1");
     request = new Request(verb, url);
     request.addBodyParameter("testParam1", "testValue1");
     request.bytePayload = payload.getBytes(StandardCharsets.UTF_8);
     byte[] actualBytePayload = request.getByteBodyContents();
     assertEquals(payload, new String(actualBytePayload, StandardCharsets.UTF_8) + "&testParam1=" + URLEncoder.encode("testValue1", StandardCharsets.UTF_8));
 }
}