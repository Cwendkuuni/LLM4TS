import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com/api?param1=value1&param2=value2");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("Authorization", "Bearer token123");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testSend_GET_ValidRequest() throws OAuthException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_GET_InvalidURL() {
 request.setUrl("http://example.com:");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSend_GET_InvalidVerb() {
 request.setVerb(null);
 request.send();
 }

 @Test
 public void testSend_GET_WithBody() {
 request.addBodyParameter("paramKey", "paramValue");
 request.setPayload("test payload");
 try {
 Response response = request.send();
 fail("Should not reach this point as GET request should not have a body");
 } catch (OAuthException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testCreateConnection_IOException() throws OAuthException {
 request.setUrl("http://example.com/invalidUrl");
 try {
 request.createConnection();
 fail("Should not reach this point as IOException should be thrown");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testAddHeaders_ValidHeaders() {
 request.addHeader("Accept", "application/json");
 request.addHeader("Content-Type", "application/xml");
 Map<String, String> headers = request.getHeaders();
 assertEquals("application/json", headers.get("Accept"));
 assertEquals("application/xml", headers.get("Content-Type"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddHeaders_NullHeaders() {
 request.addHeaders(null);
 }

 @Test
 public void testAddBodyParameter_ValidParameter() {
 request.addBodyParameter("paramKey", "paramValue");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("paramValue", bodyParams.get("paramKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddBodyParameter_NullParameter() {
 request.addBodyParameter(null, "value");
 }

 @Test
 public void testAddQuerystringParameter_ValidParameter() {
 request.addQuerystringParameter("paramKey", "paramValue");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("paramValue", queryStringParams.get("paramKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddQuerystringParameter_NullParameter() {
 request.addQuerystringParameter(null, "value");
 }

 @Test
 public void testAddPayload_ValidPayload() {
 request.addPayload("test payload");
 String payload = request.getPayload();
 assertEquals("test payload", payload);
 }

 @Test
 public void testGetByteBodyContents_ValidPayload() throws UnsupportedEncodingException {
 request.addPayload("test payload");
 byte[] bytePayload = request.getByteBodyContents();
 String payload = new String(bytePayload, request.getCharset());
 assertEquals("test payload", payload);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContents_UnsupportedCharset() {
 request.setCharset("invalid-charset");
 request.addPayload("test payload");
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getReadTimeout());
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