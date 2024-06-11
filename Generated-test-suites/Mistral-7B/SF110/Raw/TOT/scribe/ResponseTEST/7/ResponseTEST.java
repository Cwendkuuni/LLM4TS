import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testResponseWithSuccessCode() throws IOException {
 // Given: A successful response
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getHeaderFields()).thenReturn(new BasicNameValuePair[0].getClass());

 // When: Parse the response
 response.parseBodyContents();
 response.parseHeaders(connection);

 // Then: Assert the response attributes are set correctly
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 // Given: An error response
 when(connection.getResponseCode()).thenReturn(404);
 when(connection.getErrorStream()).thenReturn(Mockito.mock(InputStream.class));
 when(connection.getHeaderFields()).thenReturn(new BasicNameValuePair[0].getClass());

 // When: Parse the response
 response.parseBodyContents();
 response.parseHeaders(connection);

 // Then: Assert the response attributes are set correctly for error case
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseHeaders() throws IOException {
 // Given: A response with headers
 when(connection.getHeaderFields()).thenReturn(new BasicNameValuePair[] { new BasicNameValuePair("Content-Type", "application/json"), new BasicNameValuePair("Cache-Control", "no-cache") });

 // When: Parse the headers
 Map<String, String> headers = response.parseHeaders(connection);

 // Then: Assert the headers are parsed correctly
 assertEquals("application/json", headers.get("Content-Type"));
 assertEquals("no-cache", headers.get("Cache-Control"));
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() throws IOException {
 // Given: A response with UnknownHostException
 when(connection.getResponseCode()).thenThrow(new UnknownHostException());

 // When: Parse the response
 response.parseBodyContents();
 response.parseHeaders(connection);

 // Then: Assert an UnknownHostException is thrown
 }
}