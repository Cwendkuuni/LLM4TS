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
 public void testRequestWithDefaults() {
 // Test initial values
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertEquals(0, request.getConnection().getConnectTimeout());
 assertEquals(0, request.getConnection().getReadTimeout());
 assertNull(request.getCharset());
 assertFalse(request.isConnectionKeepAlive());

 // Test getters and setters
 request.setVerb(Verb.POST);
 assertEquals(Verb.POST, request.getVerb());
 request.setUrl("http://example.org");
 assertEquals("http://example.org", request.getUrl());
 request.addHeader("Content-Type", "application/json");
 assertEquals(1, request.getHeaders().size());
 request.addBodyParameter("param1", "value1");
 assertEquals(1, request.getBodyParams().size());
 request.addQuerystringParameter("param2", "value2");
 assertEquals(1, request.getQueryStringParams().size());
 request.addPayload("payload1");
 assertEquals("payload1", request.getPayload());
 request.addPayload(new byte[]{"payload1".getBytes()});
 assertTrue(request.getBytePayload() instanceof byte[]);
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 request.setReadTimeout(10000, TimeUnit.MILLISECONDS);
 assertEquals(10000, request.getConnection().getReadTimeout());
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSendRequest() throws IOException, OAuthException {
 final String urlWithParams = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQueryStringParams());
 final URL url = new URL(urlWithParams);
 final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 final Request testRequest = new Request(Verb.GET, url.toString());
 testRequest.addHeader("Content-Type", "application/json");
 testRequest.addBodyParameter("param1", "value1");
 testRequest.addQuerystringParameter("param2", "value2");
 testRequest.addPayload("payload1");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final InputStream inputStream = new ByteArrayInputStream(testRequest.getByteBodyContents());
 try {
 testRequest.send().getResponseCode();
 testRequest.setCharset("UTF-8");
 testRequest.setConnectionKeepAlive(true);
 final String expectedResponse = "Expected response";
 final String actualResponse = testRequest.send().getResponseBody().trim();
 assertEquals(expectedResponse, actualResponse);
 } finally {
 connection.disconnect();
 }
 }

 @Test(expected = OAuthException.class)
 public void testIOExceptionDuringSend() throws IOException, OAuthException {
 final String urlWithParams = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQueryStringParams());
 final URL url = new URL(urlWithParams);
 final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 final Request testRequest = new Request(Verb.GET, url.toString());
 testRequest.addHeader("Content-Type", "application/json");
 testRequest.addBodyParameter("param1", "value1");
 testRequest.addQuerystringParameter("param2", "value2");
 testRequest.addPayload("payload1");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final InputStream inputStream = new ByteArrayInputStream(testRequest.getByteBodyContents());
 try {
 testRequest.send();
 fail("An IOException should have been thrown.");
 } catch (IOException ioe) {
 // Expected
 } catch (OAuthException oe) {
 fail("An OAuthException should not have been thrown.");
 } finally {
 connection.disconnect();
 }
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURLDuringSend() throws IOException, OAuthException {
 final String invalidUrl = "invalid:/url";
 final Request testRequest = new Request(Verb.GET, invalidUrl);
 try {
 testRequest.send();
 fail("An OAuthException should have been thrown.");
 } catch (OAuthException oe) {
 assertEquals("Malformed URL", oe.getMessage());
 }
 }
}