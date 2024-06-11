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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithNullUrl() {
 Assert.assertNull(new Request(Verb.GET, null));
 }

 @Test
 public void testRequestWithNullVerb() {
 Assert.assertNull(new Request(null, "http://example.com"));
 }

 @Test
 public void testConstructors() {
 Request request1 = new Request(Verb.GET, "http://example.com");
 Request request2 = new Request(new Verb("PUT"), "http://example.com");
 Assert.assertEquals(request1.getVerb(), Verb.GET);
 Assert.assertEquals(request2.getVerb(), new Verb("PUT"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals(params.get("key"), "value");
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals(bodyParams.get("key"), "value");
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getBodyContents();
 Assert.assertEquals(payload, "payload");
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] bytePayload = "payload".getBytes(StandardCharsets.UTF_8);
 request.addPayload(bytePayload);
 byte[] actualBytePayload = request.getByteBodyContents();
 Assert.assertArrayEquals(bytePayload, actualBytePayload);
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals(headers.get("key"), "value");
 }

 @Test
 public void testSendWithoutBody() throws IOException {
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test
 public void testSendWithBody() throws IOException {
 String payload = "payload";
 request.addPayload(payload);
 Response response = request.send();
 Assert.assertNotNull(response);
 String actualPayload = new String(response.getResponseBody(), StandardCharsets.UTF_8);
 Assert.assertEquals(payload, actualPayload);
 }

 @Test(expected = IOException.class)
 public void testSendWithNullConnection() throws IOException {
 request.setConnection(null);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionWithMalformedURL() throws IOException {
 request.setUrl("http://example.com:/invalid");
 try {
 request.createConnection();
 } catch (MalformedURLException e) {
 // Expected
 }
 request.send();
 }

 @Test
 public void testCreateConnectionWithKeepAliveTrue() throws IOException {
 request.setUrl("http://example.com");
 request.setConnectionKeepAlive(true);
 HttpURLConnection connection = (HttpURLConnection) request.createConnection();
 Assert.assertEquals(connection.getDoOutput(), false);
 }

 @Test
 public void testCreateConnectionWithKeepAliveFalse() throws IOException {
 request.setUrl("http://example.com");
 request.setConnectionKeepAlive(false);
 HttpURLConnection connection = (HttpURLConnection) request.createConnection();
 Assert.assertEquals(connection.getDoOutput(), true);
 }

 @Test
 public void testDoSendWithGETRequest() throws IOException {
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testDoSendWithPUTOrPOSTRequest() throws IOException {
 request.setVerb(Verb.PUT);
 byte[] body = "body".getBytes(StandardCharsets.UTF_8);
 request.setPayload(body);
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testAddHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("key", "value");
 request.addHeaders(headers);
 HttpURLConnection connection = (HttpURLConnection) request.createConnection();
 Map<String, List<String>> actualHeaders = connection.getHeaderFields();
 List<String> headerValues = actualHeaders.get("key");
 Assert.assertNotNull(headerValues);
 Assert.assertEquals(1, headerValues.size());
 Assert.assertEquals("value", headerValues.get(0));
 }

 @Test
 public void testAddBodyWithEmptyBody() throws IOException {
 byte[] emptyBody = new byte[0];
 request.addBody(emptyBody);
 try (InputStream is = new ByteArrayInputStream(emptyBody)) {
 request.setConnection(new HttpURLConnection(new URL("http://example.com")));
 request.addBody(is);
 }
 }

 @Test
 public void testAddBodyWithLargeBody() throws IOException {
 final int MAX_BODY_SIZE = 1024 * 1024;
 byte[] largeBody = new byte[MAX_BODY_SIZE];
 request.addPayload(largeBody);
 try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
 request.setConnection(new HttpURLConnection(new URL("http://example.com")));
 request.addBody(largeBody);
 request.createConnection();
 }
 }

 @Test
 public void testAddBodyWithNonUTF8Body() throws IOException, UnsupportedEncodingException {
 String payload = "payload";
 String charsetName = "ISO-8859-1";
 byte[] bytePayload = payload.getBytes(charsetName);
 request.addPayload(bytePayload);
 String actualPayload = new String(request.getByteBodyContents(), charsetName);
 Assert.assertEquals(payload, actualPayload);
 }

 @Test
 public void testAddBodyWithEmptyCharset() throws IOException, UnsupportedEncodingException {
 String payload = "payload";
 request.addPayload(payload);
 try {
 request.setCharset(null);
 request.send();
 } catch (OAuthException e) {
 // Expected
 }
 }
}