import org.junit.Assert;
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
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("Content-Type", "application/json");
 }

 @Test
 public void testSend_GET_Success() throws IOException, InterruptedException {
 final Response response = request.send();
 Assert.assertNotNull(response);
 Assert.assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_GET_MalformedURL() throws IOException {
 request.setUrl("http://example:/");
 try {
 request.send();
 fail("Should throw MalformedURLException");
 } catch (OAuthException e) {
 Assert.assertEquals("Malformed URL", e.getMessage());
 }
 }

 @Test
 public void testSend_GET_IOException() throws IOException {
 request.setUrl("https://example.com/not_existing");
 try {
 request.send();
 fail("Should throw IOException");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testSend_GET_InvalidContentType() throws IOException {
 request.addHeader("Content-Type", "text/plain");
 try {
 request.send();
 fail("Should throw OAuthException");
 } catch (OAuthException e) {
 Assert.assertEquals("Problems while creating connection", e.getMessage());
 }
 }

 @Test
 public void testSend_POST_Success() throws IOException, InterruptedException {
 final String payload = "{\"key\":\"value\"}";
 request.setPayload(payload);
 request.setVerb(Verb.POST);
 final Response response = request.send();
 Assert.assertNotNull(response);
 Assert.assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_POST_EmptyBody() throws IOException, InterruptedException {
 request.setPayload("");
 request.setVerb(Verb.POST);
 final Response response = request.send();
 Assert.assertNotNull(response);
 Assert.assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_POST_InvalidContentType() throws IOException, InterruptedException {
 request.setPayload("Invalid content");
 request.setVerb(Verb.POST);
 request.addHeader("Content-Type", "text/plain");
 try {
 request.send();
 fail("Should throw OAuthException");
 } catch (OAuthException e) {
 Assert.assertEquals("Problems while creating connection", e.getMessage());
 }
 }

 @Test
 public void testSend_PUT_Success() throws IOException, InterruptedException {
 final String payload = "{\"key\":\"value\"}";
 request.setPayload(payload);
 request.setVerb(Verb.PUT);
 request.addBodyParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("Content-Type", "application/json");
 final Response response = request.send();
 Assert.assertNotNull(response);
 Assert.assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testAddBodyParameter_NullValue() {
 request.addBodyParameter(null, "value");
 Assert.assertEquals(0, request.getBodyParams().size());
 }

 @Test
 public void testAddQuerystringParameter_NullValue() {
 request.addQuerystringParameter(null, "value");
 try {
 request.send();
 fail("Should throw MalformedURLException");
 } catch (OAuthException e) {
 // Expected
 }
 }

 @Test
 public void testGetQueryStringParams_Empty() {
 final Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals(0, params.size());
 }

 @Test
 public void testGetQueryStringParams() {
 final Map<String, String> expectedParams = new HashMap<String, String>() {{
 put("param1", "value1");
 put("param2", "value2");
 }};Assert.assertEquals(expectedParams, request.getQueryStringParams());
 }

 @Test
 public void testGetBodyParams() {
 final Map<String, String> expectedParams = new HashMap<String, String>() {{
 put("key", "value");
 }};
 Assert.assertEquals(expectedParams, request.getBodyParams());
 }

 @Test
 public void testGetCharset() {
 Assert.assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() throws IOException {
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 request.setConnectTimeout(500, TimeUnit.MILLISECONDS); // should be ignored
 request.send();
 }

 @Test
 public void testSetReadTimeout() throws IOException {
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(500, TimeUnit.MILLISECONDS); // should be ignored
 request.send();
 }

 @Test
 public void testSetConnectionKeepAlive() throws IOException {
 request.setConnectionKeepAlive(true);
 request.send();
 request.setConnectionKeepAlive(false);
 request.send();
 }

 @Test
 public void testToString() {
 final String expected = "@Request(GET https://example.com)";
 Assert.assertEquals(expected, request.toString());
 }
}