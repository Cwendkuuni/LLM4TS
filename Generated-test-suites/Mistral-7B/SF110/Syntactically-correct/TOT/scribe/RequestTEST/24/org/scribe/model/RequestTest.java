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
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testSend_GET_NoBody_NoHeaders_NoQueryStringParams() {
 try {
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testSend_GET_WithQueryStringParams() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");

 try {
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());

 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("value1", queryStringParams.get("param1"));
 assertEquals("value2", queryStringParams.get("param2"));
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testSend_POST_WithBody_NoHeaders() {
 String body = "{\"name\":\"John\",\"age\":30}";
 request.addPayload(body);

 try {
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testSend_PUT_WithBody_WithHeaders() {
 String body = "{\"name\":\"John\",\"age\":30}";
 request.addPayload(body);
 request.addHeader("Content-Type", "application/json");

 try {
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());

 ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream in = request.getConnection().getInputStream();
 byte[] buffer = new byte[1024];
 int length;

 while ((length = in.read(buffer)) > 0) {
 outputStream.write(buffer, 0, length);
 }

 String responseBody = outputStream.toString();
 assertEquals(body, responseBody);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException() {
 try {
 request.setConnectionKeepAlive(false);
 request.send();
 fail("IOException should be thrown when keepAlive is false");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = OAuthException.class)
 public void testSend_MalformedURL() {
 request = new Request(Verb.GET, "http://example::com");
 try {
 request.send();
 fail("MalformedURLException should be thrown");
 } catch (OAuthException e) {
 // expected
 }
 }
}