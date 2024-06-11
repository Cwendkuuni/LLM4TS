import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null) {
 connection.disconnect();
 }
 }

 @Test
 public void testRequestInstance() {
 Request request = new Request(Verb.GET, "http://example.com");
 assertEquals("@Request(GET http://example.com)", request.toString());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURL() {
 request = new Request(Verb.GET, "http://example.com:");
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
 String bodyContents = request.getBodyContents();
 assertEquals("payload", bodyContents);
 }

 @Test
 public void testAddPayloadByteArray() throws UnsupportedEncodingException {
 byte[] payload = "payload".getBytes("UTF-8");
 request.addPayload(payload);
 byte[] byteBodyContents = request.getByteBodyContents();
 assertEquals(payload, byteBodyContents);
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("value", headers.get("key"));
 }

 @Test
 public void testCreateConnection() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 assertTrue(request.connection != null);
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionTwice() throws Exception {
 request.createConnection();
 request.createConnection();
 }

 @Test
 public void testSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 response = request.send();
 assertTrue(response != null);
 }

 @Test(expected = OAuthException.class)
 public void testSendIOError() throws Exception {
 connection = (HttpURLConnection)new URL("http://example.com").openConnection();
 connection.setDoOutput(true);
 request = new Request(Verb.GET, "http://example.com");
 request.setConnection(connection);
 request.send();
 }

 @Test
 public void testDoSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 connection = (HttpURLConnection)new URL("http://example.com").openConnection();
 request.setConnection(connection);
 response = request.doSend();
 assertTrue(response != null);
 }

 @Test
 public void testAddBody() throws IOException {
 byte[] payload = "payload".getBytes("UTF-8");
 request.addBodyParameter("key", "value");
 request.addPayload(payload);
 InputStream inputStream = new ByteArrayInputStream(payload);
 request.addHeaders(connection);
 request.addBody(connection, payload);
 byte[] responseBody = Arrays.copyOfRange(IOUtils.toByteArray(connection.getInputStream()), 0, 10);
 assertEquals(payload, new String(responseBody, "UTF-8"));
 }

 @Test
 public void testAddHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("key", "value");
 request.addHeaders(headers);
 for (Map.Entry<String, String> entry : headers.entrySet()) {
 String key = entry.getKey();
 String value = entry.getValue();
 assertEquals(value, request.getHeaders().get(key));
 }
 }

}