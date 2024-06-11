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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RequestTest {

 private Request request;
 private final String url = "http://example.com/api/endpoint?param1=value1&param2=value2";
 private final Verb verb = Verb.GET;

 @Before
 public void setUp() {
 request = new Request(verb, url);
 }

 @After
 public void tearDown() throws Exception {
 request = null;
 }

 @Test
 public void testRequestInstance() {
 // Test basic Request instance creation
 Request request = new Request(Verb.GET, "http://example.com");
 assertEquals("@Request(GET http://example.com)", request.toString());
 }

 @Test
 public void testRequestWithUrlAndVerb() {
 // Test Request instance creation with URL and verb
 assertEquals("@Request(GET http://example.com)", request.toString());
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("param1=value1", params.get("param1"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("param1", "value1");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value1", bodyParams.get("param1"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String bodyContents = request.getBodyContents();
 assertEquals("payload", bodyContents);
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 byte[] byteBodyContents = request.getByteBodyContents();
 assertEquals(payload, byteBodyContents);
 }

 @Test
 public void testAddHeaders() {
 request.addHeader("Header1", "value1");
 request.addHeader("Header2", "value2");
 Map<String, String> headers = request.getHeaders();
 assertEquals("value1", headers.get("Header1"));
 assertEquals("value2", headers.get("Header2"));
 }

 @Test
 public void testSendRequestEmpty() throws IOException {
 Request request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSendRequestWithQuerystringParameters() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("param1=value1&param2=value2", response.getResponseText());
 }

 @Test
 public void testSendRequestWithBody() throws IOException {
 request.addBodyParameter("param1", "value1");
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 request.setCharset("UTF-8");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 String bodyContents = request.getBodyContents();
 assertEquals("param1=value1", bodyContents);
 }

 @Test
 public void testSendRequestWithHeaders() throws IOException {
 request.addHeader("Header1", "value1");
 request.addHeader("Header2", "value2");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 Map<String, List<String>> headersMap = response.getHeaders();
 assertEquals(Arrays.asList("value1"), headersMap.get("Header1"));
 assertEquals(Arrays.asList("value2"), headersMap.get("Header2"));
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidUrl() throws IOException {
 request.setUrl("http://example.com:port/api/endpoint?param1=value1&param2=value2");
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidCharset() throws IOException {
 request.setCharset("InvalidCharset");
 assertThrows(UnsupportedEncodingException.class, () -> request.getByteBodyContents());
 }

 @Test
 public void testSetConnectTimeout() throws IOException {
 request.setUrl(url);
 request.setConnectTimeout(2000, TimeUnit.MILLISECONDS);
 URL urlObj = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
 connection.setConnectTimeout(2000);
 connection.disconnect();
 assertEquals(true, request.isConnectionSet());
 }

 @Test
 public void testSetReadTimeout() throws IOException {
 request.setUrl(url);
 request.setReadTimeout(2000, TimeUnit.MILLISECONDS);
 URL urlObj = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
 connection.setReadTimeout(2000);
 connection.disconnect();
 assertEquals(true, request.isReadTimeoutSet());
 }

 @Test
 public void testSetConnectionKeepAlive() throws IOException {
 request.setUrl(url);
 request.setConnectionKeepAlive(true);
 URL urlObj = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
 assertEquals(true, connection.getDoOutput());
 request.setConnection(connection);
 assertEquals("true", System.getProperty("http.keepAlive"));
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com/api/endpoint", request.getSanitizedUrl());
 }
}