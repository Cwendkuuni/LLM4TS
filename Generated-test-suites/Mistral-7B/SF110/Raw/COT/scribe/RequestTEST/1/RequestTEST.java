import org.junit.Assert;
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
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithGetVerbAndEmptyParameters() {
 Request expectedRequest = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(expectedRequest, request);
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> querystringParams = request.getQueryStringParams();
 Assert.assertEquals("value", querystringParams.get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getBodyContents();
 Assert.assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 String contents = request.getBodyContents();
 Assert.assertEquals("payload", contents);
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("value", headers.get("key"));
 }

 @Test
 public void testAddVerb() {
 request.setVerb(Verb.POST);
 Verb verb = request.getVerb();
 Assert.assertEquals(Verb.POST, verb);
 }

 @Test
 public void testSendRequestWithGetVerbAndEmptyParameters() throws IOException, OAuthException {
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithMalformedURL() {
 request.setUrl(":/invalid");
 request.send();
 }

 @Test(expected = IOException.class)
 public void testSendRequestWithIOException() throws OAuthException {
 request.setUrl("http://example.com/path/to/nonexistent/resource");
 request.send();
 }

 @Test
 public void testSetConnectTimeout() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 URL url = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 int timeout = connection.getConnectTimeout();
 Assert.assertEquals(1000, timeout);
 }

 @Test
 public void testSetReadTimeout() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 URL url = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 int timeout = connection.getReadTimeout();
 Assert.assertEquals(1000, timeout);
 }

 @Test
 public void testSetCharset() {
 request.setUrl("http://example.com");
 request.setCharset("UTF-8");
 String charset = request.getCharset();
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setUrl("http://example.com");
 request.setConnectionKeepAlive(true);
 boolean connectionKeepAlive = request.isConnectionKeepAlive();
 Assert.assertTrue(connectionKeepAlive);
 }

 @Test
 public void testCreateConnectionWithEmptyParameters() throws IOException {
 request.setUrl("http://example.com");
 request.createConnection();
 HttpURLConnection connection = request.getConnection();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionWithMalformedURL() throws IOException {
 request.setUrl(":/invalid");
 request.createConnection();
 }

 @Test
 public void testDoSendWithGetVerbAndEmptyParameters() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testAddBodyWithGetVerbAndEmptyParameters() throws IOException {
 request.setUrl("http://example.com");
 request.addBody("body");
 byte[] byteBody = request.getByteBodyContents();
 InputStream inputStream = new ByteArrayInputStream(byteBody);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[4096];
 int bytesRead;
 while ((bytesRead = inputStream.read(buffer)) != -1) {
 outputStream.write(buffer, 0, bytesRead);
 }
 String body = outputStream.toString();
 Assert.assertEquals("body", body);
 }

 @Test(expected = IOException.class)
 public void testAddBodyWithInvalidByteArray() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 byte[] byteBody = null;
 request.addBody(byteBody);
 }

 @Test
 public void testAddHeadersWithEmptyHeaders() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 request.addHeader("header1", "value1");
 request.addHeader("header2", "value2");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals(2, headers.size());
 }

 @Test(expected = OAuthException.class)
 public void testAddHeaderWithInvalidKey() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 request.addHeader(null, "value");
 }

 @Test(expected = OAuthException.class)
 public void testAddHeaderWithInvalidValue() throws IOException, OAuthException {
 request.setUrl("http://example.com");
 request.addHeader("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBodyWithNegativeContentLength() {
 request.setUrl("http://example.com");
 request.addBody("body".getBytes());
 request.setRequestProperty("Content-Length", "-1");
 }
}