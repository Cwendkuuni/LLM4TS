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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.addHeader("header1", "value1");
 request.setCharset("UTF-8");
 }

 @Test
 public void testSend_GET() throws OAuthException, IOException {
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_GET_withQuerystringParameters() throws OAuthException, IOException {
 final Map<String, String> params = request.getQueryStringParams();
 assertEquals("value1", params.get("param1"));
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_GET_withBodyParameters() throws OAuthException, IOException {
 request.addBodyParameter("param3", "value3");
 final String payload = request.getPayload();
 assertEquals("param2=value2&param3=value3", payload);
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_GET_withHeaders() throws OAuthException, IOException {
 request.addHeader("header2", "value2");
 final Map<String, String> headers = request.getHeaders();
 assertEquals("value1", headers.get("header1"));
 assertEquals("value2", headers.get("header2"));
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_GET_withError() throws OAuthException, IOException {
 request.setUrl("http://example.com:8080");
 final Exception exception = assertThrows(OAuthException.class, () -> request.send());
 assertEquals("Problems while creating connection", exception.getMessage());
 }

 @Test
 public void testSend_PUT() throws OAuthException, IOException {
 final String payload = "{\"param1\":\"value1\",\"param2\":\"value2\"}";
 final byte[] bytePayload = payload.getBytes("UTF-8");
 request = new Request(Verb.PUT, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.addPayload(payload);
 request.setBytePayload(bytePayload);
 request.setConnectionKeepAlive(true);
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_POST() throws OAuthException, IOException {
 final String payload = "param1=value1&param2=value2";
 final byte[] bytePayload = payload.getBytes("UTF-8");
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.addPayload(payload);
 request.setBytePayload(bytePayload);
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_POST_withPayloadFile() throws OAuthException, IOException {
 final String payloadFile = "param1=value1\nparam2=value2";
 final InputStream inputStream = new ByteArrayInputStream(payloadFile.getBytes("UTF-8"));
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload(inputStream);
 final Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testCreateConnection() throws MalformedURLException, IOException {
 final String effectiveUrl = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQuerystringParams());
 final URL url = new URL(effectiveUrl);
 final Class<?> clazz = url.openConnection().getClass();
 assertTrue(clazz.equals(HttpURLConnection.class));
 }
}