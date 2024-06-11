import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithGetVerbAndEmptyQueryString() throws OAuthException {
 Request expectedRequest = new Request(Verb.GET, "http://example.com");
 Response expectedResponse = new Response(expectedRequest.send());

 assertEquals(expectedRequest, request);
 assertEquals(expectedResponse, request.send());
 }

 @Test
 public void testRequestWithGetVerbAndQueryStringParameters() throws OAuthException {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");

 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("param1=value1&param2=value2", queryStringParams.toString());
 }

 @Test
 public void testRequestWithGetVerbAndBodyParameters() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");

 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("param1=value1&param2=value2", bodyParams.toString());
 }

 @Test
 public void testRequestWithGetVerbAndPayload() {
 request.addPayload("payload");

 String payload = request.getBodyContents();
 assertEquals("payload", payload);
 }

 @Test
 public void testRequestWithPostVerbAndBody() throws OAuthException {
 request.setVerb(Verb.POST);
 request.addPayload("payload");

 byte[] bytePayload = request.getByteBodyContents();
 assertEquals("payload".getBytes(), bytePayload);
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidVerb() {
 request.setVerb(new Verb("INVALID"));
 }

 @Test
 public void testRequestWithHeaders() {
 request.addHeader("header1", "value1");
 request.addHeader("header2", "value2");

 Map<String, String> headers = request.getHeaders();
 assertEquals("header1=value1, header2=value2", headers.toString());
 }

 @Test
 public void testRequestWithSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.connection.getConnectTimeout());
 }

 @Test
 public void testRequestWithSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.connection.getReadTimeout());
 }
}