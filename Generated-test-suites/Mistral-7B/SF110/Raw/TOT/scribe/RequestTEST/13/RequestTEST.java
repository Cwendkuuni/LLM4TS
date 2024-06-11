import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.*;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("Content-Type", "application/json");
 }

 @Test
 public void testSend_GET() throws OAuthException {
 final Response response = request.send();
 assertEquals(200, response.getResponseCode());
 }

 @Test
 public void testSend_GET_withQuerystringParams() throws OAuthException {
 final Map<String, String> params = request.getQueryStringParams();
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testSend_GET_withHeaders() throws OAuthException {
 final Response response = request.send();
 assertEquals("application/json", response.getHeaders().get("Content-Type"));
 }

 @Test
 public void testSend_POST_withBody() throws OAuthException {
 request.setVerb(Verb.POST);
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 request.addHeader("Content-Type", "application/json");
 request.addPayload("{\"key3\":\"value3\"}");
 final Response response = request.send();
 assertEquals(200, response.getResponseCode());
 }

 @Test
 public void testSend_PUT_withBody() throws OAuthException {
 request.setVerb(Verb.PUT);
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 request.addHeader("Content-Type", "application/json");
 request.addPayload("{\"key3\":\"value3\"}");
 final Response response = request.send();
 assertEquals(200, response.getResponseCode());
 }

 @Test(expected = IOException.class)
 public void testSend_withInvalidURL() throws OAuthException {
 request = new Request(Verb.GET, "http://example.com:/");
 request.send();
 fail("An IOException should be thrown");
 }

 @Test(expected = OAuthException.class)
 public void testSend_withInvalidCharset() throws OAuthException {
 request.setCharset("InvalidCharset");
 try {
 request.send();
 fail("An OAuthException should be thrown");
 } catch (OAuthException e) {
 assertEquals("Unsupported Charset: InvalidCharset", e.getMessage());
 }
 }
}