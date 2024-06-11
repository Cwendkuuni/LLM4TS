package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;
 private HttpURLConnection connection;

 @Before
 public void setup() throws Exception {
 request = new Request(Verb.GET, "http://example.com/api?param1=1&param2=2");
 connection = (HttpURLConnection)new URL("http://example.com/api?param1=1&param2=2").openConnection();
 }

 @Test
 public void testRequestConstruction() {
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com/api?param1=1&param2=2", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertEquals(0, request.getBytePayload().length);
 assertEquals(false, request.isConnectionKeepAlive());
 assertNull(request.getConnection());
 assertEquals(new HashMap<String, String>(), request.getHeaders());
 assertEquals(new HashMap<String, String>(), request.getQuerystringParams());
 assertEquals(new HashMap<String, String>(), request.getBodyParams());
 }

 @Test
 public void testSend_GET() throws Exception {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertEquals("OK", response.getStatusMessage());
 }

 @Test(expected = OAuthException.class)
 public void testSend_GET_IOException() throws Exception {
 request = new Request(Verb.GET, "http://example.com/api?param1=1&param2=2");
 connection = (HttpURLConnection)new URL("http://example.com/api?param1=1&param2=2").openConnection();
 connection.setThrowsExceptionOnREAD(true);
 request.send();
 }

 @Test
 public void testCreateConnection() throws Exception {
 request.createConnection();
 assertEquals(connection, request.getConnection());
 }

 @Test
 public void testDoSend_GET() throws Exception {
 request.setConnection(connection);
 Response response = request.doSend();
 assertEquals(200, response.getStatusCode());
 assertEquals("OK", response.getStatusMessage());
 }

 @Test(expected = IOException.class)
 public void testDoSend_GET_IOException() throws Exception {
 request.setConnection(connection);
 connection.setDoOutput(true);
 request.doSend();
 }

 @Test
 public void testAddHeaders() throws Exception {
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("Value1", headers.get("Header1"));
 assertEquals("Value2", headers.get("Header2"));
 }

 @Test
 public void testAddBodyParameter() throws Exception {
 request.addBodyParameter("param1", "value1");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value1", bodyParams.get("param1"));
 }

 @Test
 public void testAddQuerystringParameter() throws Exception {
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> querystringParams = request.getQueryStringParams();
 assertEquals("value1", querystringParams.get("param1"));
 }

 @Test
 public void testAddPayload() throws Exception {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayload_byteArray() throws Exception {
 byte[] payload = "payload".getBytes(StandardCharsets.UTF_8);
 request.addPayload(payload);
 assertEquals(payload, request.getBytePayload());
 assertEquals(payload.length, request.getBytePayload().length);
 }

 @Test
 public void testGetByteBodyContents_String() throws Exception {
 request.addPayload("payload");
 byte[] bytePayload = request.getByteBodyContents();
 assertEquals("payload", new String(bytePayload, StandardCharsets.UTF_8));
 }

 @Test
 public void testGetByteBodyContents_byteArray() throws Exception {
 request.addPayload("payload".getBytes(StandardCharsets.UTF_8));
 byte[] bytePayload = request.getByteBodyContents();
 assertArrayEquals(request.getBytePayload(), bytePayload);
 }

 @Test
 public void testGetCharset() throws Exception {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() throws Exception {
 request.setConnectTimeout(10, TimeUnit.SECONDS);
 assertEquals(10000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() throws Exception {
 request.setReadTimeout(10, TimeUnit.SECONDS);
 assertEquals(10000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetConnectionKeepAlive() throws Exception {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testToString() throws Exception {
 assertEquals("@Request(GET http://example.com/api?param1=1&param2=2)", request.toString());
 }

 @Test
 public void testGetBodyContents_String() throws Exception {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetBodyContents_InputStream() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("payload".getBytes(StandardCharsets.UTF_8));
 request.setInputStream(inputStream);
 assertEquals("payload", new String(request.getByteBodyContents(), StandardCharsets.UTF_8));
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetBodyContents_byteArrayInputStream() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytePayload());
 assertEquals("payload", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetUrl() throws Exception {
 assertEquals("http://example.com/api?param1=1&param2=2", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() throws Exception {
 assertEquals("http://example.com/", request.getSanitizedUrl());
 }
}