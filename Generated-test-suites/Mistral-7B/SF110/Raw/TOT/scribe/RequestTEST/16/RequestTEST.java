package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.createConnection();
 assertNotNull(request.connection);
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionWithMalformedURL() throws IOException {
 request = new Request(Verb.GET, "http://example.com:");
 try {
 request.createConnection();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testSendGetRequest() throws IOException {
 final Response response = request.send();
 assertNotNull(response);
 assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithNullVerb() {
 request.verb = null;
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 assertEquals("value", request.getQueryStringParams().get("key"));
 }

 @Test(expected = OAuthException.class)
 public void testAddQuerystringParameterWithEmptyKey() {
 request.addQuerystringParameter("", "value");
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testAddQuerystringParameterWithEmptyValue() {
 request.addQuerystringParameter("key", "");
 assertEquals("", request.getQueryStringParams().get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test(expected = OAuthException.class)
 public void testAddBodyParameterWithEmptyKey() {
 request.addBodyParameter("", "value");
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test(expected = OAuthException.class)
 public void testAddPayloadWithNullPayload() {
 request.addPayload(null);
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testAddHeaders() {
 request.addHeader("key", "value");
 assertEquals("value", request.getHeaders().get("key"));
 }

 @Test(expected = OAuthException.class)
 public void testAddHeadersWithEmptyKey() {
 request.addHeader("", "value");
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testAddHeadersWithEmptyValue() {
 request.addHeader("key", "");
 assertEquals("", request.getHeaders().get("key"));
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test(expected = OAuthException.class)
 public void testSetCharsetWithInvalidCharset() {
 request.setCharset("InvalidCharset");
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnectionKeepAliveWithFalse() {
 request.setConnectionKeepAlive(false);
 assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(10, TimeUnit.SECONDS);
 assertEquals(10000, request.connection.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(10, TimeUnit.SECONDS);
 assertEquals(10000, request.connection.getReadTimeout());
 }

 @Test
 public void testSendPostRequestWithEmptyBody() throws IOException {
 request.verb = Verb.POST;
 final String body = "";
 request.addBody(body);
 final Response response = request.send();
 assertNotNull(response);
 assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
 }

 @Test
 public void testSendPostRequestWithByteBody() throws IOException {
 request.verb = Verb.POST;
 final byte[] byteBody = "byte body".getBytes();
 request.addBody(byteBody);
 final Response response = request.send();
 assertNotNull(response);
 assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
 }

 @Test
 public void testSendPutRequestWithByteBody() throws IOException {
 request.verb = Verb.PUT;
 final byte[] byteBody = "byte body".getBytes();
 request.addBody(byteBody);
 final Response response = request.send();
 assertNotNull(response);
 assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
 }

 @Test
 public void testSendRequestWithContentLengthHeader() throws IOException {
 final String body = "body";
 request.addBody(body);
 request.addHeader(CONTENT_LENGTH, String.valueOf(body.length()));
 final Response response = request.send();
 assertNotNull(response);
 assertEquals(HttpURLConnection.HTTP_OK, response.getResponseCode());
 }

 @Test
 public void testSendRequestWithInvalidContentLengthHeader() throws IOException {
 final String body = "body";
 request.addBody(body);
 request.addHeader(CONTENT_LENGTH, "invalid length");
 try {
 request.send();
 fail("Should have thrown an exception");
 } catch (IOException e) {
 // Expected
 }
 }
}