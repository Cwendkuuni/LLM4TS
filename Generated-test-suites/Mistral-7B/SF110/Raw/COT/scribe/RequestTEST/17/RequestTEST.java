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
 Request newRequest = new Request(Verb.GET, "http://example.com");
 Assert.assertNotNull(newRequest);
 }

 @Test
 public void testSend_GET_200OK() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 Assert.assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException_GET() throws Exception {
 request = new Request(Verb.GET, "http://example.com/error");
 request.setConnectionKeepAlive(true); // So that createConnection() doesn't throw an exception
 try {
 request.send();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testCreateConnection() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 }

 @Test(expected = MalformedURLException.class)
 public void testGetQueryStringParams_MalformedURL() {
 request = new Request(Verb.GET, "http://example.com?");
 request.getQueryStringParams();
 }

 @Test
 public void testAddHeader() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Content-Type", "application/json");
 Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
 }

 @Test
 public void testAddBodyParameter() {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("key1", "value1");
 Assert.assertEquals("value1", request.getBodyParams().get("key1"));
 }

 @Test
 public void testAddPayload() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload1");
 Assert.assertEquals("payload1", request.getPayload());
 }

 @Test
 public void testAddPayload_byteArray() {
 request = new Request(Verb.POST, "http://example.com");
 byte[] bytePayload = "payload1".getBytes();
 request.addPayload(bytePayload);
 Assert.assertArrayEquals(bytePayload, request.getBytePayload());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContents_UnsupportedCharset() {
 request = new Request(Verb.POST, "http://example.com");
 request.setCharset("unsupportedCharset");
 request.addPayload("payload1");
 }

 @Test
 public void testGetVerb() {
 request = new Request(Verb.POST, "http://example.com");
 Assert.assertEquals(Verb.POST, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Content-Type", "application/json");
 Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
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
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(10, TimeUnit.SECONDS);
 }

 @Test
 public void testToString() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals("@Request(GET http://example.com)", request.toString());
 }
}