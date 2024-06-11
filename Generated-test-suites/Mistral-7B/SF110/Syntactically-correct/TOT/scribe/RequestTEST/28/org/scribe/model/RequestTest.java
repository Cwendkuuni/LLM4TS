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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() throws Exception {
 request = new Request(Verb.GET, "https://example.com");
 }

 @Test
 public void testConstructor() {
 assertNotNull(request);
 assertEquals(Verb.GET, request.getVerb());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertNull(request.getConnection());
 assertNull(request.getCharset());
 assertNull(request.getByteBodyContents());
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
 assertNotNull(request.getPayload());
 assertNull(request.getBytePayload());
 }

 @Test
 public void testAddPayloadByte() throws IOException {
 request.addPayload(new byte[0]);
 assertNull(request.getPayload());
 assertNotNull(request.getBytePayload());
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException, IOException {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetByteBodyContents() throws UnsupportedEncodingException, IOException {
 request.addPayload("payload");
 byte[] byteBody = request.getByteBodyContents();
 assertEquals(request.getPayload().getBytes().length, byteBody.length);
 }

 @Test
 public void testGetHeaders() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("value", headers.get("key"));
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.addQuerystringParameter("key", "value");
 request.createConnection();
 URL url = new URL(request.getUrl());
 assertEquals(request.getUrl(), url.toString());
 }

 @Test
 public void testSendNormalCase() throws IOException {
 request.addQuerystringParameter("key", "value");
 Response response = request.send();
 assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendFailure() throws IOException {
 request.addQuerystringParameter("key", "value");
 HttpURLConnection connection = (HttpURLConnection)request.createConnection();
 connection.disconnect();
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertEquals(true, request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnectTimeout() throws IOException {
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() throws IOException {
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetConnection() throws IOException {
 HttpURLConnection connection = (HttpURLConnection)new URL("https://example.com").openConnection();
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }

 @Test
 public void testToString() {
 assertEquals("@Request(GET https://example.com)", request.toString());
 }
}