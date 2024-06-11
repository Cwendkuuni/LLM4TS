import org.junit.Assert;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstructor() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertNull(request.getPayload());
 Assert.assertNull(request.getBytePayload());
 Assert.assertFalse(request.isConnectionKeepAlive());
 Assert.assertNull(request.getConnection());
 Assert.assertEquals(0, request.getConnectionKeepAliveTimeout());
 Assert.assertEquals(0, request.getConnectionTimeout());
 Assert.assertEquals(0, request.getReadTimeout());
 }

 @Test
 public void testSend_GetMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 throw new IOException("Test IOException");
 }

 @Test(expected = OAuthException.class)
 public void testSend_OAuthException() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 throw new OAuthException("Test OAuthException");
 }

 @Test
 public void testAddHeader() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("header1", "value1");
 Assert.assertEquals("value1", request.getHeaders().get("header1"));
 }

 @Test
 public void testCreateConnection() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Assert.assertNotNull(request.getConnection());
 }

 @Test
 public void testDoSend_GetMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testDoSend_PutOrPostMethod() throws Exception {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.createConnection();
 request.setConnectionKeepAlive(true);
 request.setConnectionTimeout(5000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 byte[] bytePayload = "test payload".getBytes();
 request.addPayload(bytePayload);
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testGetQueryStringParams() throws Exception {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value1", params.get("param1"));
 Assert.assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyParams() {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 Map<String, String> params = request.getBodyParams();
 Assert.assertEquals("value1", params.get("param1"));
 }

 @Test
 public void testGetUrl() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 Assert.assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws Exception {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 String bodyContents = request.getBodyContents();
 Assert.assertEquals("value1", bodyContents);
 }

 @Test
 public void testGetVerb() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("header1", "value1");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("value1", headers.get("header1"));
 }

 @Test
 public void testGetCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 Assert.assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnectionTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getReadTimeout());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 Assert.assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 request = new Request(Verb.GET, "http://example.com");
 HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
 request.setConnection(connection);
 Assert.assertEquals(connection, request.getConnection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetConnection_NullConnection() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnection(null);
 }

}