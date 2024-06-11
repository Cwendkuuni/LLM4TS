package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testEmptyRequest() {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertNull(request.getCharset());
 assertNull(request.getHeaders());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("value", headers.get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 assertNull(request.getBytePayload());
 }

 @Test
 public void testAddPayloadByte() {
 byte[] bytePayload = "payload".getBytes(StandardCharsets.UTF_8);
 request.addPayload(bytePayload);
 assertEquals("payload", new String(request.getBytePayload(), StandardCharsets.UTF_8));
 }

 @Test
 public void testSetVerb() {
 request.setVerb(Verb.POST);
 assertEquals(Verb.POST, request.getVerb());
 }

 @Test
 public void testSetUrl() {
 request.setUrl("http://example.com/newUrl");
 assertEquals("http://example.com/newUrl", request.getUrl());
 }

 @Test
 public void testSetSanitizedUrl() {
 request.setSanitizedUrl("http://example.com/newUrl?param=value");
 assertEquals("http://example.com/newUrl", request.getSanitizedUrl());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test
 public void testCreateConnectionTwice() throws IOException {
 request.createConnection();
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test
 public void testDoSendGet() throws IOException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testDoSendGetWithHeaders() throws IOException {
 request.addHeader("key", "value");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertEquals("value", response.getHeaders().get("key"));
 }

 @Test
 public void testDoSendPostWithBody() throws IOException {
 byte[] body = "payload".getBytes(StandardCharsets.UTF_8);
 request.addBodyParameter("key", "value");
 request.addPayload(body);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertNotNull(response.getHeaders().get("Content-Length"));
 }

 @Test
 public void testDoSendPutWithBody() throws IOException {
 byte[] body = "payload".getBytes(StandardCharsets.UTF_8);
 request.setVerb(Verb.PUT);
 request.addBodyParameter("key", "value");
 request.addPayload(body);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertNotNull(response.getHeaders().get("Content-Length"));
 }

 @Test
 public void testThrowOAuthExceptionOnIOException() throws IOException {
 IOException ioException = new IOException();
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test
 public void testSetConnectTimeout() throws IOException {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assert(request.getConnection().getConnectTimeout() > 0);
 }

 @Test
 public void testSetReadTimeout() throws IOException {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assert(request.getConnection().getReadTimeout() > 0);
 }

 @Test
 public void testSendWithInvalidUrl() {
 assertThrows(MalformedURLException.class, () -> request.send());
 }

 @Test
 public void testSendWithInvalidCharset() {
 request.setCharset("invalidCharset");
 assertThrows(UnsupportedEncodingException.class, () -> request.send());
 }
}