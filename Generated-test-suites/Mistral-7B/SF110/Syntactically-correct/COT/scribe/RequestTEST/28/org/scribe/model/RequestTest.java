package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 Verb verb = Verb.GET;
 String url = "http://example.com";
 request = new Request(verb, url);
 }

 @Test
 public void testRequestConstructor() {
 assertEquals("org.scribe.model.Request@", request.toString());
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertNull(request.getConnection());
 assertNotNull(request.getHeaders());
 assertNotNull(request.getQuerystringParams());
 assertNotNull(request.getBodyParams());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Accept", "text/plain");
 Map<String, String> headers = request.getHeaders();
 assertEquals("text/plain", headers.get("Accept"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("value", queryStringParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getPayload();
 assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadByteArray() throws UnsupportedEncodingException {
 request.addPayload(new byte[]{"a", "b", "c"});
 byte[] bytePayload = request.getBytePayload();
 assertArrayEquals(new byte[]{"a", "b", "c"}, bytePayload);
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetQuerystringParams() {
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertTrue(queryStringParams.isEmpty());
 }

 @Test
 public void testGetBodyParams() {
 Map<String, String> bodyParams = request.getBodyParams();
 assertTrue(bodyParams.isEmpty());
 }

 @Test
 public void testGetBodyContents() {
 request.addPayload("Hello World");
 String bodyContents = request.getBodyContents();
 assertEquals("Hello World", bodyContents);
 }

 @Test(expected = OAuthException.class)
 public void testGetByteBodyContentsUnsupportedEncoding() throws UnsupportedEncodingException {
 request.setCharset("unsupported-charset");
 request.addPayload("test");
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.connection.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.connection.getReadTimeout());
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionIOException() throws IOException {
 request.setUrl("invalid-url");
 request.createConnection();
 }

 @Test(expected = OAuthException.class)
 public void testSendOAuthException() throws IOException {
 request.addHeader("Accept", "text/plain");
 request.addBodyParameter("key", "value");
 request.addQuerystringParameter("key", "value");
 request.addPayload("payload");
 request.setUrl("http://example.com");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 Request requestSpy = spy(request);
 doThrow(new IOException("IOException")).when(requestSpy).createConnection();
 Response response = requestSpy.send();
 fail("Exception not thrown");
 }

 @Test
 public void testDoSend() throws IOException {
 request.setUrl("http://example.com");
 request.setVerb(Verb.POST);
 request.addHeader("Accept", "text/plain");
 request.addBodyParameter("key", "value");
 request.addQuerystringParameter("key", "value");
 request.addPayload("payload");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 Response response = request.send();
 assertNotNull(response);
 }

}