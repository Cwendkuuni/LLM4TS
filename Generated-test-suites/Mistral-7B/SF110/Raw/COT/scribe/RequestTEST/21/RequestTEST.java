import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.*;
import java.net.URL;
import static org.mockito.Mockito.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithValidArguments() {
 // Test constructor
 Request expectedRequest = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(expectedRequest, request);

 // Test getters and setters
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertEquals(Collections.emptyMap(), request.getBodyParams());
 Assert.assertEquals(Collections.emptyMap(), request.getHeaders());
 Assert.assertNull(request.getPayload());
 Assert.assertNull(request.getBytePayload());
 Assert.assertFalse(request.isConnectionKeepAlive());
 Assert.assertNull(request.getCharset());
 Assert.assertNull(request.getConnection());

 // Test method addQuerystringParameter
 request.addQuerystringParameter("key", "value");
 Map<String, String> expectedParams = new HashMap<>();
 expectedParams.put("key", "value");
 Assert.assertEquals(expectedParams, request.getQueryStringParams());

 // Test method addBodyParameter
 request.addBodyParameter("key", "value");
 Map<String, String> expectedBodyParams = new HashMap<>();
 expectedBodyParams.put("key", "value");
 Assert.assertEquals(expectedBodyParams, request.getBodyParams());

 // Test method addHeader
 request.addHeader("key", "value");
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("key", "value");
 Assert.assertEquals(expectedHeaders, request.getHeaders());

 // Test method addPayload
 request.addPayload("payload");
 Assert.assertEquals("payload", request.getPayload());
 Assert.assertNull(request.getBytePayload());

 // Test method addPayload with byte array
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 Assert.assertNull(request.getPayload());
 Assert.assertEquals(bytePayload, request.getBytePayload());
 }

 @Test(expected = OAuthException.class)
 public void testIOExceptionDuringSend() throws Exception {
 when(request.createConnection()).thenThrow(new IOException("Connection creation failed"));
 request.send();
 }

 @Test
 public void testHeadersAndBodyAreSentCorrectly() throws Exception {
 // Arrange
 String url = "http://example.com";
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 Request request = new Request(Verb.POST, url);
 request.addHeaders(headers);
 request.addBodyParameter("key", "value");
 request.addPayload("{\"key\": \"value\"}");

 // Act
 Response response = request.send();
 HttpURLConnection connection = response.getConnection();

 // Assert
 verify(connection).setRequestMethod(Verb.POST.name());
 String contentType = connection.getRequestProperty("Content-Type");
 Assert.assertEquals("application/json", contentType);
 byte[] payload = request.getByteBodyContents();
 Assert.assertNotNull(payload);
 OutputStream outputStream = connection.getOutputStream();
 outputStream.write(payload);
 outputStream.flush();
 }

 @Test
 public void testSendWithEmptyRequest() {
 Request request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURLException() {
 request.setUrl("http://example.com:");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testUnsupportedEncodingException() {
 request.setCharset("InvalidCharset");
 request.addPayload("payload");
 }
}