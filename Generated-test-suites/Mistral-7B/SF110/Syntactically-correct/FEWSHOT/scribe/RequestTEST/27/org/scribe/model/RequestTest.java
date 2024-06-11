package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;
import static org.mockito.Mockito.*;

public class RequestTest {

 Request request;
 Verb verb = Verb.GET;
 String url = "http://example.com";
 Map<String, String> querystringParams = new HashMap<>();
 Map<String, String> bodyParams = new HashMap<>();
 Map<String, String> headers = new HashMap<>();
 byte[] bytePayload = "test byte payload".getBytes();
 String charset = "UTF-8";
 HttpURLConnection connectionMock = mock(HttpURLConnection.class);

 @Before
 public void setUp() {
 request = new Request(verb, url);
 }

 @Test
 public void testRequestInstance() {
 assertEquals(verb, request.getVerb());
 assertEquals(url, request.getUrl());
 assertNull(request.getByteBodyContents());
 assertNull(request.getCharset());
 assertNull(request.getConnection());
 assertNull(request.getBytePayload());
 assertNull(request.getConnectionKeepAlive());
 assertEquals(0, request.getConnectTimeout());
 assertEquals(0, request.getReadTimeout());
 }

 @Test
 public void testAddHeaders() {
 request.addHeaders(connectionMock);
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), anyString());
 }

 @Test
 public void testAddBody() throws IOException {
 request.addBody(connectionMock, bytePayload);
 verify(connectionMock, times(1)).setDoOutput(true);
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq(bytePayload.length + ""));
 }

 @Test
 public void testSend() throws IOException {
 when(connectionMock.setRequestMethod(eq(verb.name()))).thenReturn(this);
 when(connectionMock.getResponseCode()).thenReturn(200);
 request.setConnection(connectionMock);
 Response response = request.send();
 assertEquals("200", response.getCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendWithIOException() throws IOException {
 when(connectionMock.setRequestMethod(eq(verb.name()))).thenThrow(new IOException());
 request.setConnection(connectionMock);
 request.send();
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("testKey", "testValue");
 assertEquals("testValue", request.getBodyParams().get("testKey"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("testKey", "testValue");
 assertEquals("testValue", request.getQueryStringParams().get("testKey"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("testPayload");
 assertEquals("testPayload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 request.addPayload(bytePayload);
 assertEquals(bytePayload, request.getBytePayload());
 }

 @Test
 public void testGetQueryStringParams() throws MalformedURLException {
 Map<String, String> expectedParams = new HashMap<>();
 expectedParams.put("testKey1", "testValue1");
 expectedParams.put("testKey2", "testValue2");
 expectedParams.putAll(request.getQueryStringParams());
 assertEquals(expectedParams, request.getQueryStringParams());
 }

 @Test
 public void testGetBodyParams() {
 assertEquals(request.getBodyParams(), request.getBodyParams());
 }

 @Test
 public void testGetUrl() {
 assertEquals(url, request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException {
 assertEquals("test byte payload", new String(request.getByteBodyContents(), charset));
 }

 @Test
 public void testGetByteBodyContents() {
 assertEquals(bytePayload, request.getByteBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(verb, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 assertEquals(headers, request.getHeaders());
 }

 @Test
 public void testGetCharset() {
 assertEquals(charset, request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("testCharset");
 assertEquals("testCharset", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 request.setConnection(connectionMock);
 assertSame(request, request.getConnection());
 }
}