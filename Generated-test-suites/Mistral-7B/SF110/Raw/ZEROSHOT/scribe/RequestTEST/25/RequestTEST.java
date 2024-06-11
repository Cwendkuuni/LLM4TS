import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithNoArguments() {
 Request request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 assertNotNull(response);
 }

 @Test
 public void testRequestWithQueryStringParameters() {
 Map<String, String> queryStringParams = new HashMap<>();
 queryStringParams.put("param1", "value1");
 queryStringParams.put("param2", "value2");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addQuerystringParameter("param3", "value3"); //should be ignored as it's not in the initial map
 String url = request.getUrl();
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(queryStringParams, params);
 assertEquals("http://example.com?param1=value1&param2=value2", url);
 }

 @Test
 public void testRequestWithBodyParameters() {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("param1", "value1");
 bodyParams.put("param2", "value2");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] byteBody = "param1=value1&param2=value2".getBytes();
 request.addPayload(byteBody);
 Response response = request.send();
 assertEquals(200, response.getStatusCode()); //Assuming a successful response has status code 200
 }

 @Test
 public void testRequestWithHeader() {
 request.addHeader("Content-Type", "application/json");
 assertEquals("application/json", request.getHeaders().get("Content-Type"));
 }

 @Test
 public void testRequestWithPayload() {
 String payload = "{\"name\":\"John\", \"age\":30}";
 request.addPayload(payload);
 byte[] bytePayload = payload.getBytes();
 request.addPayload(bytePayload);
 Response response = request.send();
 assertEquals(200, response.getStatusCode()); //Assuming a successful response has status code 200
 }

 @Test
 public void testRequestWithInvalidUrl() {
 request = new Request(Verb.GET, "http://example.com:/");
 assertThrows(OAuthException.class, () -> request.send());
 }

 @Test
 public void testRequestWithInvalidVerb() {
 request = new Request(null, "http://example.com");
 assertThrows(IllegalArgumentException.class, () -> request.send());
 }

 @Test
 public void testRequestWithEmptyPayload() {
 request.addPayload("");
 Response response = request.send();
 assertNull(response); //Assuming an empty payload doesn't result in a response
 }
}