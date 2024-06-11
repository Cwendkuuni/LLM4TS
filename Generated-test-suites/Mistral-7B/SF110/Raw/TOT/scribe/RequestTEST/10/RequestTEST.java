import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.io.UnsupportedEncodingException;
import java.net.HTTPURLConnection;
import java.util.*;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("Content-Type", "application/json");
 }

 @Test
 public void testSend_GET_Success() throws OAuthException, UnsupportedEncodingException, IOException {
 Response response = request.send();
 assertEquals(HTTPURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test
 public void testSend_GET_MalformedURL() {
 try {
 request = new Request(Verb.GET, "://example.com");
 request.send();
 fail("Should throw MalformedURLException");
 } catch (MalformedURLException e) {
 // Expected
 }
 }

 @Test
 public void testSend_GET_InvalidVerb() {
 request = new Request(null, "http://example.com");
 try {
 request.send();
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSend_GET_ConnectionError() throws OAuthException, UnsupportedEncodingException {
 request.setConnectTimeout(0, TimeUnit.MILLISECONDS);
 try {
 request.send();
 fail("Should throw IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testSend_POST_Success() throws OAuthException, UnsupportedEncodingException, IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("key1", "value1");
 request.addHeader("Content-Type", "application/x-www-form-urlencoded");
 request.addPayload("key2=value2");
 Response response = request.send();
 assertEquals(HTTPURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test
 public void testSend_POST_MalformedURL() {
 try {
 request = new Request(Verb.POST, "://example.com");
 request.send();
 fail("Should throw MalformedURLException");
 } catch (MalformedURLException e) {
 // Expected
 }
 }

 @Test
 public void testSend_POST_InvalidVerb() {
 request = new Request(null, "http://example.com");
 try {
 request.send();
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSend_POST_ConnectionError() throws OAuthException, UnsupportedEncodingException, IOException {
 request.setConnectTimeout(0, TimeUnit.MILLISECONDS);
 try {
 request.send();
 fail("Should throw IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testSend_PUT_Success() throws OAuthException, UnsupportedEncodingException, IOException {
 request = new Request(Verb.PUT, "http://example.com");
 request.addBodyParameter("key1", "value1");
 request.addHeader("Content-Type", "application/json");
 request.addPayload("{\"key\":\"value\"}");
 Response response = request.send();
 assertEquals(HTTPURLConnection.HTTP_OK, response.getStatusCode());
 }
}