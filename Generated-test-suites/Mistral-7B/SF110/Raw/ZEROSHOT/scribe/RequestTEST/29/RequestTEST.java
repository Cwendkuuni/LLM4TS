import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @After
 public void tearDown() {
 request = null;
 }

 @Test
 public void testRequestCreation() {
 assertEquals("http://example.com", request.getUrl());
 assertEquals(Verb.GET, request.getVerb());
 assertNotNull(request.getHeaders());
 assertNotNull(request.getBodyParams());
 assertNotNull(request.getQuerystringParams());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Content-Type", "application/json");
 assertEquals("application/json", request.getHeaders().get("Content-Type"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 assertEquals("value", request.getQuerystringParams().get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadBytes() {
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 assertEquals(bytePayload, request.getBytePayload());
 }

 @Test
 public void testCreateConnection() throws OAuthException, IOException {
 request.setUrl("http://example.com?key=value");
 request.addQuerystringParameter("key", "value");
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 Request newRequest = new Request(Verb.POST, request.getUrl());
 newRequest.addHeaders(request.getHeaders());
 newRequest.addBodyParams(request.getBodyParams());
 newRequest.createConnection();
 assertNotNull(newRequest.getConnection());
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionWithMalformedUrl() throws OAuthException, IOException {
 request.setUrl("http://example.com:");
 request.createConnection();
 fail("Expected OAuthException");
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionWithInvalidConnectionKeepAlive() throws OAuthException, IOException {
 request.setConnectionKeepAlive("invalid");
 request.createConnection();
 fail("Expected OAuthException");
 }
}