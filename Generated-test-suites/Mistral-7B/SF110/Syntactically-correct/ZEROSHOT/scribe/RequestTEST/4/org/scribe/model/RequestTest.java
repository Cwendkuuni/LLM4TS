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
 request = new Request(Verb.GET, "http://example.com/api/endpoint?param1=value1&param2=value2");
 }

 @Test
 public void testRequestWithGetVerbAndEmptyQueryParams() {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testRequestWithGetVerbAndQueryParams() {
 request.addQuerystringParameter("param1", "newValue1");
 request.addQuerystringParameter("param2", "newValue2");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertEquals("newValue1", request.getQueryStringParams().get("param1"));
 assertEquals("newValue2", request.getQueryStringParams().get("param2"));
 }

 @Test
 public void testRequestWithPostVerbAndBodyParams() {
 request.setVerb(Verb.POST);
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 request.addPayload("{\"key3\":\"value3\"}");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertEquals("value1", request.getBodyParams().get("key1"));
 assertEquals("value2", request.getBodyParams().get("key2"));
 assertEquals("{\"key3\":\"value3\"}", request.getBodyContents());
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidUrl() {
 request = new Request(Verb.GET, "http://example.com:/api/endpoint?param1=value1&param2=value2");
 request.send();
 fail("An OAuthException should have been thrown");
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidSetCharset() {
 request.setCharset("invalidCharset");
 try {
 request.send();
 fail("An OAuthException should have been thrown");
 } catch (OAuthException e) {
 // expected
 }
 }

 @Test
 public void testRequestWithSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testRequestWithSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 }

 @After
 public void tearDown() {
 request = null;
 }
}