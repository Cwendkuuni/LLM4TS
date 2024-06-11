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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testCreateRequest() {
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertNull(request.getConnection());
 assertNull(request.getCharset());
 assertNull(request.connectionKeepAlive);
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
 public void testAddPayloadByteArray() {
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 assertEquals(payload.length, request.getByteBodyContents().length);
 }

 @Test
 public void testAddHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("key", "value");
 request.addHeaders(headers);
 assertEquals("value", request.getHeaders().get("key"));
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.setUrl("http://example.com?key=value");
 URL url = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 request.createConnection();
 assertTrue(request.getConnection() == connection);
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionIOException() throws IOException {
 request.setUrl("http://example.com?key=value");
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 request.createConnection();
 throw new IOException("Mock IOException");
 }

 @Test
 public void testDoSend() throws IOException {
 request.addHeader("Accept", "application/json");
 request.addBodyParameter("key", "value");
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 InputStream inputStream = new ByteArrayInputStream(payload);
 request.setConnectionKeepAlive(true);
 request.setCharset("UTF-8");
 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testDoSendIOException() throws IOException {
 request.addHeader("Accept", "application/json");
 request.addBodyParameter("key", "value");
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 InputStream inputStream = new ByteArrayInputStream(payload);
 request.setConnectionKeepAlive(true);
 request.setCharset("UTF-8");
 request.setUrl("http://example.com/");
 Response response = request.send();
 throw new IOException("Mock IOException");
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException {
 request.addBodyParameter("key", "value");
 byte[] payload = "payload".getBytes("UTF-8");
 request.addPayload(payload);
 String bodyContents = request.getBodyContents();
 assertEquals("value", bodyContents);
 }
}