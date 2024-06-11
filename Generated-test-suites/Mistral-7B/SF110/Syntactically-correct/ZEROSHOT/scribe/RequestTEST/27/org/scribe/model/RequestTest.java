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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;
 private static final String TEST_URL = "http://example.com";
 private static final Verb TEST_VERB = Verb.GET;

 @Before
 public void setUp() {
 request = new Request(TEST_VERB, TEST_URL);
 }

 @After
 public void tearDown() {
 request = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(request);
 assertEquals(TEST_VERB, request.getVerb());
 assertEquals(TEST_URL, request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertNull(request.getConnection());
 assertNull(request.getCharset());
 assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("HeaderKey", "HeaderValue");
 Map<String, String> headers = request.getHeaders();
 assertEquals("HeaderValue", headers.get("HeaderKey"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("ParamKey", "ParamValue");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("ParamValue", bodyParams.get("ParamKey"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("ParamKey", "ParamValue");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("ParamValue", params.get("ParamKey"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("PayloadValue");
 assertEquals("PayloadValue", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 String payload = "PayloadValue";
 byte[] bytePayload = payload.getBytes();
 request.addPayload(bytePayload);
 assertEquals(bytePayload, request.getBytePayload());
 }

 @Test
 public void testSend() throws IOException {
 request.addQuerystringParameter("ParamKey", "ParamValue");
 Response response = request.send();
 assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendInvalidUrl() {
 request.setUrl("http://example.com:");
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test(expected = OAuthException.class)
 public void testSendInvalidVerb() {
 request.setVerb(new Verb("INVALID"));
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionThrowsIOException() throws IOException {
 URL url = new URL("http://example.com/");
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 assertNotNull(connection);
 connection.disconnect();
 request = new Request(TEST_VERB, url.toString());
 assertThrows(IOException.class, () -> request.createConnection());
 }

 @Test
 public void testDoSend() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 request.addHeaders(headers);
 request.addBodyParameter("ParamKey", "ParamValue");
 byte[] bytePayload = "{\"key\": \"value\"}".getBytes();
 request.addPayload(bytePayload);
 request.setConnectionKeepAlive(true);

 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 assertEquals("application/json", response.getHeaderField("Content-Type"));
 }
}