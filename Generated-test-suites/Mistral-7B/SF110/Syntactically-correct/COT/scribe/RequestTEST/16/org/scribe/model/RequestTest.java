import org.junit.Test;
import org.junit.Assert;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestWithValidArguments() {
 request = new Request(Verb.GET, "http://www.example.com");
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://www.example.com", request.getUrl());
 Assert.assertNull(request.getByteBodyContents());
 Assert.assertNull(request.getCharset());
 Assert.assertNull(request.getConnection());
 Assert.assertNull(request.getConnectionKeepAlive());
 Assert.assertNull(request.getHeaders());
 Assert.assertNull(request.getPayload());
 }

 @Test
 public void testSendMethodWithGETRequest() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 Response response = request.send();
 Assert.assertNotNull(response);
 verify(request, times(1)).createConnection();
 verify(request, times(1)).doSend();
 }

 @Test(expected = OAuthException.class)
 public void testSendMethodThrowsIOException() throws Exception {
 request = spy(Request.class);
 doThrow(new IOException()).when(request).createConnection();
 request.send();
 }

 @Test
 public void testAddHeaderMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addHeader("Accept", "application/json");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("application/json", headers.get("Accept"));
 }

 @Test
 public void testAddBodyParameterMethod() {
 request = new Request(Verb.POST, "http://www.example.com");
 request.addBodyParameter("param1", "value1");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("value1", bodyParams.get("param1"));
 }

 @Test
 public void testAddQuerystringParameterMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 Assert.assertEquals("value1", queryStringParams.get("param1"));
 }

 @Test
 public void testAddPayloadMethodWithStringPayload() {
 request = new Request(Verb.POST, "http://www.example.com");
 request.addPayload("payload");
 String payload = request.getPayload();
 Assert.assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadMethodWithByteArrayPayload() {
 request = new Request(Verb.POST, "http://www.example.com");
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 byte[] payload = request.getByteBodyContents();
 Assert.assertArrayEquals(bytePayload, payload);
 }

 @Test
 public void testGetCharsetMethodWithDefaultCharset() {
 request = new Request(Verb.GET, "http://www.example.com");
 String charset = request.getCharset();
 Assert.assertEquals(Charset.defaultCharset().name(), charset);
 }

 @Test
 public void testGetCharsetMethodWithCustomCharset() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setCharset("UTF-8");
 String charset = request.getCharset();
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testSetConnectTimeout() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 int connectTimeout = request.connection.getConnectTimeout();
 Assert.assertEquals(1000, connectTimeout);
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 int readTimeout = request.connection.getReadTimeout();
 Assert.assertEquals(1000, readTimeout);
 }

 @Test
 public void testSetConnectionKeepAliveMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setConnectionKeepAlive(true);
 boolean connectionKeepAlive = request.connectionKeepAlive;
 Assert.assertTrue(connectionKeepAlive);
 }

 @Test
 public void testSetConnectionMethod() {
 Request requestSpy = spy(Request.class);
 HttpURLConnection connection = mock(HttpURLConnection.class);
 doReturn(connection).when(requestSpy).createConnection();
 requestSpy.setConnection(connection);
 HttpURLConnection storedConnection = requestSpy.connection;
 Assert.assertEquals(connection, storedConnection);
 }
}