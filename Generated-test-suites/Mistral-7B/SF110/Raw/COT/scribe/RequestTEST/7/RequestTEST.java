import org.junit.Test;
import org.junit.Assert;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://api.example.com/");
 }

 @Test
 public void testRequestWithValidParameters() {
 Request expectedRequest = new Request(Verb.GET, "https://api.example.com/");
 Assert.assertEquals(expectedRequest.toString(), request.toString());
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("paramKey", "paramValue");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("paramValue", params.get("paramKey"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("paramKey", "paramValue");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("paramValue", bodyParams.get("paramKey"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getBodyContents();
 Assert.assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadWithByteArray() {
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 String payload = request.getBodyContents();
 Assert.assertEquals(new String(bytePayload), payload);
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Content-Type", "application/json");
 String contentType = request.getHeaders().get("Content-Type");
 Assert.assertEquals("application/json", contentType);
 }

 @Test
 public void testSendRequestWithGETVerb() throws OAuthException {
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidURL() {
 request.setUrl("https://api.example.com:/");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidVerb() {
 request.setVerb(new Verb("INVALID"));
 request.send();
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionThrowsIOException() throws Exception {
 request.setUrl("https://api.example.com/");
 MockURLConnection mockConnection = new MockURLConnection("https://api.example.com/") {
 @Override
 public void connect() throws IOException {
 throw new IOException("Connection error");
 }
 };
 request.createConnection();
 }

 @Test
 public void testCreateConnectionWithExistingConnection() throws IOException {
 request.setUrl("https://api.example.com/");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 request.setConnection(connection);
 request.createConnection();
 verify(connection).connect();
 }

 @Test(expected = OAuthException.class)
 public void testDoSendThrowsIOException() throws Exception {
 request.setUrl("https://api.example.com/");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 doReturn(500).when(connection).getResponseCode();
 request.doSend();
 }

 @Test
 public void testDoSendWithGETVerb() throws IOException {
 request.setUrl("https://api.example.com/");
 String expectedResponse = "{\"status\":\"success\",\"data\": \"data\"}";
 HttpURLConnection connection = mock(HttpURLConnection.class);
 doReturn(200).when(connection).getResponseCode();
 doReturn(expectedResponse.getBytes()).when(connection).getInputStream();
 request.doSend();
 String response = request.getBodyContents();
 Assert.assertEquals(expectedResponse, response);
 }

 @Test
 public void testDoSendWithPUTVerbAndBody() throws IOException {
 request.setUrl("https://api.example.com/");
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 String body = "{\"key\": \"value\"}";
 byte[] byteBody = body.getBytes();
 request.addPayload(byteBody);
 String expectedResponse = "{\"status\":\"success\",\"data\": \"data\"}";
 HttpURLConnection connection = mock(HttpURLConnection.class);
 doReturn(200).when(connection).getResponseCode();
 doReturn(expectedResponse.getBytes()).when(connection).getInputStream();
 request.doSend();
 String response = request.getBodyContents();
 Assert.assertEquals(expectedResponse, response);
 }
}