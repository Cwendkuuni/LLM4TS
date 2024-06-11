import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestConstructor() {
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertNotNull(request.getConnection());
 assertFalse(request.isConnectionKeepAlive());
 assertNotNull(request.getHeaders());
 assertEmpty(request.getBodyParams());
 assertEmpty(request.getQuerystringParams());
 }

 @Test
 public void testSetAndGetQuerystringParameters() {
 request.addQuerystringParameter("key1", "value1");
 request.addQuerystringParameter("key2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(2, params.size());
 assertEquals("value1", params.get("key1"));
 assertEquals("value2", params.get("key2"));
 }

 @Test
 public void testSetAndGetBodyParameters() {
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(2, bodyParams.size());
 assertEquals("value1", bodyParams.get("key1"));
 assertEquals("value2", bodyParams.get("key2"));
 }

 @Test
 public void testSetAndGetPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 assertNull(request.getBytePayload());
 }

 @Test
 public void testSetAndGetBytePayload() {
 request.addPayload("payload".getBytes());
 assertEquals(11, request.getBytePayload().length);
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionFailure() throws OAuthException {
 request = new Request(Verb.GET, null);
 try {
 request.createConnection();
 fail("Should throw exception for null URL");
 }
 catch (OAuthException e) {
 assertEquals("URL cannot be null", e.getMessage());
 }
 }

 @Test
 public void testAddHeaders() throws IOException {
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 request.addHeaders(connection);
 for (String key : request.getHeaders().keySet()) {
 assertEquals(request.getHeaders().get(key), connection.getRequestProperty(key));
 }
 }

 @Test
 public void testAddBodyWithNullPayload() throws IOException {
 request.addBodyParameter("key", "value");
 request.addBody(null, request.getByteBodyContents());
 assertFalse(request.getConnection().getOutputStream().isWritable());
 }

 @Test(expected = IOException.class)
 public void testAddBodyFailure() throws IOException {
 request.addBodyParameter("key", "value");
 byte[] payload = new byte[1024 * 1024 * 10]; // 10MB
 request.addBody(request.getConnection(), payload);
 fail("Should throw exception for large payload");
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetCharsetFailure() {
 request.addPayload("invalid_charset");
 try {
 request.getByteBodyContents();
 fail("Should throw exception for unsupported charset");
 }
 catch (UnsupportedEncodingException e) {
 assertEquals("Unsupported Charset: invalid_charset", e.getMessage());
 }
 }

}