import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithValidArguments() {
 Request expectedRequest = new Request(Verb.GET, "http://example.com");
 assertEquals(expectedRequest.getVerb(), request.getVerb());
 assertEquals(expectedRequest.getUrl(), request.getUrl());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRequestWithNullVerb() {
 request = new Request(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRequestWithNullUrl() {
 request = new Request(Verb.GET, null);
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertThat(headers.size(), is(1));
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertThat(bodyParams.size(), is(1));
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertThat(queryStringParams.size(), is(1));
 assertEquals("value", queryStringParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("test payload");
 String payload = request.getBodyContents();
 assertEquals("test payload", payload);
 }

 @Test
 public void testAddBinaryPayload() {
 byte[] bytePayload = "test byte payload".getBytes();
 request.addPayload(bytePayload);
 byte[] actualBytePayload = request.getByteBodyContents();
 assertArrayEquals(bytePayload, actualBytePayload);
 }

 @Test(expected = OAuthException.class)
 public void testSendThrowsIOException() throws Exception {
 request.setConnectionKeepAlive(true);
 mockHttpURLConnection(400);
 request.send();
 }

 @Test
 public void testSendReturnsResponse() throws Exception {
 request.setConnectionKeepAlive(true);
 mockHttpURLConnection(200);
 Response response = request.send();
 assertTrue(response.isSuccess());
 }

 @Test
 public void testCreateConnectionThrowsIOException() throws Exception {
 request.setConnectionKeepAlive(true);
 mockHttpURLConnection(IOException.class);
 request.createConnection();
 }

 @Test
 public void testDoSendThrowsIOException() throws Exception {
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 request.setConnectionKeepAlive(true);
 mockHttpURLConnection(IOException.class);
 request.doSend();
 }

 private void mockHttpURLConnection(Class<? extends IOException> exceptionClass) throws Exception {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenThrow(exceptionClass);
 request.setConnection(connection);
 }

}