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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://example.com");
 }

 @Test
 public void testSend_ValidGetRequest() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
 }

 @Test
 public void testSend_ValidPostRequestWithBody() throws IOException {
 request.setVerb(Verb.POST);
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] byteBody = "sample body".getBytes(StandardCharsets.UTF_8);
 request.addPayload(byteBody);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
 }

 @Test(expected = OAuthException.class)
 public void testSend_InvalidUrl() {
 request.setUrl("http://example:/");
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test(expected = OAuthException.class)
 public void testSend_InvalidVerb() {
 request.setVerb(new Verb("INVALID"));
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test(expected = IOException.class)
 public void testSend_IOException() throws IOException {
 request.setConnectTimeout(1, TimeUnit.SECONDS);
 request.setReadTimeout(1, TimeUnit.SECONDS);
 URL url = new URL("https://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoInput(true);
 connection.setDoOutput(true);
 request.setConnection(connection);
 request.addQuerystringParameter("param1", "value1");
 try {
 request.send();
 fail("IOException expected");
 } catch (IOException ioe) {
 // Expected
 }
 }

 @Test
 public void testSend_ValidPutRequestWithBody() throws IOException {
 request.setVerb(Verb.PUT);
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] byteBody = "sample body".getBytes(StandardCharsets.UTF_8);
 request.addPayload(byteBody);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
 }

 @Test
 public void testCreateConnection_ValidUrl() throws IOException {
 request.addQuerystringParameter("param1", "value1");
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateConnection_InvalidUrl() {
 request.setUrl("http://example:/");
 request.createConnection();
 }

 @Test
 public void testAddHeaders_ValidHeaders() {
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testAddBody_ValidBody() throws UnsupportedEncodingException {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] byteBody = "sample body".getBytes(StandardCharsets.UTF_8);
 request.addPayload(byteBody);
 String payload = "{\"param1\":\"value1\",\"param2\":\"value2\"}";
 request.setCharset("UTF-8");
 InputStream inputStream = new ByteArrayInputStream(byteBody);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int length;
 while ((length = inputStream.read(buffer)) > 0) {
 outputStream.write(buffer, 0, length);
 }
 String requestBody = outputStream.toString(StandardCharsets.UTF_8.name());
 assertEquals(payload, requestBody);
 }

 @Test
 public void testAddPayload_ValidPayload() {
 request.addPayload("sample payload");
 String payload = request.getPayload();
 assertEquals("sample payload", payload);
 }

 @Test
 public void testAddPayload_NullPayload() {
 request.addPayload(null);
 assertNull(request.getPayload());
 }

 @Test
 public void testAddPayload_BytePayload() {
 request.addPayload(new byte[0]);
 assertNull(request.getPayload());
 }

 @Test
 public void testGetQueryStringParams_ValidQueryString() throws MalformedURLException {
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(2, params.size());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyParams_EmptyBodyParams() {
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(0, bodyParams.size());
 }

 @Test
 public void testGetBodyParams_ValidBodyParams() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(2, bodyParams.size());
 assertEquals("value1", bodyParams.get("param1"));
 assertEquals("value2", bodyParams.get("param2"));
 }

 @Test
 public void testGetUrl() {
 assertEquals("https://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("https://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents_ValidBodyContents() throws UnsupportedEncodingException {
 byte[] byteBody = "sample body".getBytes(StandardCharsets.UTF_8);
 request.addPayload(byteBody);
 String bodyContents = request.getBodyContents();
 assertEquals("sample body", bodyContents);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetBodyContents_InvalidCharset() throws UnsupportedEncodingException {
 request.setCharset("INVALID_CHARSET");
 request.addPayload("sample payload");
 assertThrows(UnsupportedEncodingException.class, () -> request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = request.getHeaders();
 assertEquals(0, headers.size());
 }

 @Test
 public void testGetCharset() {
 assertEquals(StandardCharsets.DEFAULT.name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(2, TimeUnit.SECONDS);
 assertEquals(2000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(2, TimeUnit.SECONDS);
 assertEquals(2000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }
}