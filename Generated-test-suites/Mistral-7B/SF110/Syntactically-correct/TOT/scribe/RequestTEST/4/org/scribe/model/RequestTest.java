import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testCreateAndSendRequestWithGETVerbAndEmptyQueryString() throws IOException {
 Response response = request.send();
 assertThat(response, notNullValue());
 }

 @Test
 public void testCreateAndSendRequestWithGETVerbAndQueryStringParameters() throws IOException {
 request.addQuerystringParameter("key1", "value1");
 request.addQuerystringParameter("key2", "value2");
 Response response = request.send();
 Map<String, String> params = request.getQueryStringParams();
 assertThat(params.get("key1"), is("value1"));
 assertThat(params.get("key2"), is("value2"));
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithGETVerbAndInvalidUrl() {
 request.setUrl("http://example:/");
 request.send();
 }

 @Test
 public void testCreateAndSendRequestWithPOSTVerbAndEmptyBody() throws IOException {
 request.setVerb(Verb.POST);
 request.send();
 }

 @Test
 public void testCreateAndSendRequestWithPOSTVerbAndBodyParameters() throws IOException {
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 request.setBytePayload(new byte[0]);
 request.setVerb(Verb.POST);
 Response response = request.send();
 Map<String, String> bodyParams = request.getBodyParams();
 assertThat(bodyParams.get("key1"), is("value1"));
 assertThat(bodyParams.get("key2"), is("value2"));
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithPOSTVerbAndNullBytePayload() throws IOException {
 request.addBodyParameter("key", "value");
 request.setBytePayload(null);
 request.setVerb(Verb.POST);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithPOSTVerbAndEmptyBodyAndHeaders() throws IOException {
 request.addHeader("key", "value");
 request.setBytePayload(new byte[0]);
 request.setVerb(Verb.POST);
 request.send();
 }

 @Test
 public void testCreateAndSendRequestWithPUTVerbAndEmptyBody() throws IOException {
 request.setVerb(Verb.PUT);
 Response response = request.send();
 assertThat(response, notNullValue());
 }

 @Test
 public void testCreateAndSendRequestWithPUTVerbAndBodyParameters() throws IOException {
 request.addBodyParameter("key1", "value1");
 request.addBodyParameter("key2", "value2");
 request.setBytePayload(new byte[0]);
 request.setVerb(Verb.PUT);
 Response response = request.send();
 Map<String, String> bodyParams = request.getBodyParams();
 assertThat(bodyParams.get("key1"), is("value1"));
 assertThat(bodyParams.get("key2"), is("value2"));
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithPUTVerbAndNullBytePayload() throws IOException {
 request.addBodyParameter("key", "value");
 request.setBytePayload(null);
 request.setVerb(Verb.PUT);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithPUTVerbAndEmptyBodyAndHeaders() throws IOException {
 request.addHeader("key", "value");
 request.setBytePayload(new byte[0]);
 request.setVerb(Verb.PUT);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithInvalidVerb() throws IOException {
 request.setVerb(new Verb("INVALID"));
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testCreateAndSendRequestWithNullVerb() throws IOException {
 request.setVerb(null);
 request.send();
 }

 @Test
 public void testSettingConnectionTimeout() throws IOException {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 request.send();
 }

 @Test
 public void testSettingReadTimeout() throws IOException {
 request.setReadTimeout(10, TimeUnit.MILLISECONDS);
 request.send();
 }

 @Test
 public void testSettingCharset() throws IOException {
 request.setCharset("UTF-8");
 String body = "test body";
 request.addPayload(body);
 String responseBody = request.getByteBodyContents().toString("UTF-8");
 assertThat(body, is(responseBody));
 }

 @Test(expected = OAuthException.class)
 public void testSettingUnsupportedCharset() throws IOException {
 request.setCharset("UNSUPPORTED_CHARSET");
 request.addPayload("test body");
 request.send();
 }

 @Test
 public void testSettingConnectionKeepAlive() throws IOException {
 request.setConnectionKeepAlive(true);
 request.send();
 }

 @Test
 public void testAddingValidHeader() throws IOException {
 request.addHeader("Content-Type", "application/json");
 request.send();
 String responseHeader = request.getHeaders().get("Content-Type");
 assertThat(responseHeader, is("application/json"));
 }

 @Test(expected = OAuthException.class)
 public void testAddingNullHeader() throws IOException {
 request.addHeader(null, "value");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testAddingEmptyHeaderName() throws IOException {
 request.addHeader("", "value");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testAddingEmptyHeaderValue() throws IOException {
 request.addHeader("key", "");
 request.send();
 }

 @Test
 public void testToString() throws IOException {
 String expected = "@Request(GET http://example.com)";
 String actual = request.toString();
 assertThat(actual, is(expected));
 }

 @Test
 public void testCreateConnection() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionTwice() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 request.createConnection();
 }

 @Test
 public void testDoSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Response response = request.doSend();
 }

 @Test(expected = IOException.class)
 public void testDoSendTwice() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Response response1 = request.doSend();
 Response response2 = request.doSend();
 }

}