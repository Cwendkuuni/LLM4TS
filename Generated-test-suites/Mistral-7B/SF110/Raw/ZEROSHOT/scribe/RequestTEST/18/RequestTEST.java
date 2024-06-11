package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.After;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestConstruction() {
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertNotNull(request.getVerb());
 assertNotNull(request.getQuerystringParams());
 assertNotNull(request.getBodyParams());
 assertNotNull(request.getHeaders());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerbNullConstruction() {
 request = new Request(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUrlNullConstruction() {
 request = new Request(Verb.GET, null);
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetQueryStringParams() {
 Map<String, String> expected = new HashMap<>();
 expected.put("param1", "value1");
 expected.put("param2", "value2");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 assertEquals(expected, request.getQueryStringParams());
 }

 @Test
 public void testGetBodyParams() {
 Map<String, String> expected = new HashMap<>();
 expected.put("param1", "value1");
 expected.put("param2", "value2");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 assertEquals(expected, request.getBodyParams());
 }

 @Test
 public void testGetBodyContents() throws IOException, UnsupportedEncodingException {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetByteBodyContents() throws IOException, UnsupportedEncodingException {
 request.addPayload("payload");
 byte[] bytePayload = request.getByteBodyContents();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(bytePayload);
 assertEquals("payload", outputStream.toString());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> expected = new HashMap<>();
 expected.put("header1", "value1");
 expected.put("header2", "value2");
 request.addHeader("header1", "value1");
 request.addHeader("header2", "value2");
 assertEquals(expected, request.getHeaders());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnectionKeepAliveFalse() {
 request.setConnectionKeepAlive(false);
 assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testSend() throws IOException, OAuthException {
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("header1", "value1");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertTrue(response.isSuccess());
 }

 @Test(expected = OAuthException.class)
 public void testSendWithIOException() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("header1", "value1");when (request.connection.getInputStream()).thenThrow(new IOException());
 Response response = request.send();
 fail("Expected OAuthException to be thrown");
 }

 @Test
 public void testAddPayloadString() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] bytePayload = new byte[10];
 request.addPayload(bytePayload);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(bytePayload);
 assertEquals(outputStream.toString(), request.getPayload());
 }

 @Test
 public void testAddBodyParameterString() {
 request.addBodyParameter("param1", "value1");
 assertEquals("value1", request.getBodyParams().get("param1"));
 }

 @Test
 public void testAddBodyParameterNullValue() {
 request.addBodyParameter("param1", null);
 assertNull(request.getBodyParams().get("param1"));
 }

 @Test
 public void testAddBodyParameterMap() {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("param1", "value1");
 bodyParams.put("param2", "value2");
 request.addBodyParams(bodyParams);
 assertEquals(bodyParams, request.getBodyParams());
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("header1", "value1");
 request.createConnection();
 assertTrue(request.connection != null);
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionIOException() throws IOException {
 when (new URL("http://example.com").openConnection()).thenThrow(new IOException());
 request.createConnection();
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testDoSend() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("header1", "value1");
 Response response = new Response(HttpURLConnection.HTTP_OK, "Success", new ByteArrayInputStream(new byte[0]));
 when (request.connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when (request.connection.getResponseMessage()).thenReturn("Success");
 when (request.connection.getInputStream()).thenReturn(response.getInputStream());
 Response testResponse = request.doSend();
 assertEquals(response.getStatusCode(), testResponse.getStatusCode());
 assertEquals(response.isSuccess(), testResponse.isSuccess());
 }

 @Test(expected = IOException.class)
 public void testDoSendIOException() throws IOException {
 when (request.connection.getResponseCode()).thenThrow(new IOException());
 Response testResponse = request.doSend();
 fail("Expected IOException to be thrown");
 }

 @After
 public void tearDown() {
 request = null;
 }
}