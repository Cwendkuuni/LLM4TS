package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("Header1", "Value1");
 }

 @Test
 public void testConstructor() {
 assertEquals("http://example.com", request.getUrl());
 assertEquals(Verb.GET, request.getVerb());
 assertNotNull(request.getHeaders());
 assertNotNull(request.getQuerystringParams());
 }

 @Test
 public void testSend() throws IOException, OAuthException {
 final HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 Request realRequest = request;
 request = new Request(Verb.GET, "http://example.com") {
 @Override
 HttpURLConnection createConnection() throws IOException {
 return connection;
 }
 };
 Response response = realRequest.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendIOException() throws IOException {
 final HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenThrow(new IOException("Test IOException"));
 request.send();
 }

 @Test
 public void testCreateConnection() throws IOException {
 final String effectiveUrl = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQuerystringParams());
 assertEquals(effectiveUrl, request.createConnection().getURL().toString());
 }

 @Test
 public void testDoSend() throws IOException {
 final HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 request.addHeader("Header1", "Value1");
 request.addBodyParameter("param1", "value1");
 request.setCharset("UTF-8");
 Response response = request.doSend();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test(expected = IOException.class)
 public void testDoSendIOException() throws IOException {
 final HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenThrow(new IOException("Test IOException"));
 request.doSend();
 }

 @Test
 public void testAddBody() throws IOException {
 request.addPayload("Test payload");
 assertEquals("Test payload", new String(request.getByteBodyContents(), Charset.defaultCharset()));
 }

 @Test
 public void testAddBodyByte() throws IOException {
 final byte[] testBytePayload = "Test byte payload".getBytes(Charset.defaultCharset());
 request.addPayload(testBytePayload);
 assertArrayEquals(testBytePayload, request.getByteBodyContents());
 }

 @Test
 public void testGetQueryStringParams() {
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("param1=value1", params.toString());
 }

 @Test
 public void testGetBodyParams() {
 Map<String, String> bodyParams = request.getBodyParams();
 assertTrue(bodyParams.isEmpty());
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() {
 request.addPayload("Test payload");
 assertEquals("Test payload", request.getBodyContents());
 }

 @Test
 public void testGetByteBodyContents() throws UnsupportedEncodingException {
 request.addPayload("Test payload");
 byte[] byteContents = request.getByteBodyContents();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(byteContents, 0, byteContents.length);
 String contents = new String(outputStream.toByteArray(), Charset.defaultCharset());
 assertEquals("Test payload", contents);
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = request.getHeaders();
 headers.put("Header1", "Value1");
 assertEquals("Header1=Value1", headers.toString());
 }

 @Test
 public void testGetCharset() {
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.connection.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.connection.getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.connectionKeepAlive);
 }

 @Test
 public void testSetConnection() {
 final HttpURLConnection connection = mock(HttpURLConnection.class);
 request.setConnection(connection);
 assertEquals(connection, request.connection);
 }

 @Test
 public void testToString() {
 assertEquals("@Request(GET http://example.com)", request.toString());
 }
}