import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setup() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessfulStatus() throws IOException {
 // Given: A successful HTTP response
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(StreamUtils.createMockInputStream("Successful response body"));

 // When: The Response object is created
 Response expectedResponse = new Response(connectionMock);

 // Then: The code, body, and headers are set correctly
 assertEquals(200, expectedResponse.getCode());
 assertEquals("Successful response body", expectedResponse.getBody());
 assertNotNull(expectedResponse.getHeaders());
 assertFalse(expectedResponse.getStream().equals(response.getErrorStream()));

 // Verify: The parseBodyContents and parseHeaders methods are called
 verify(connectionMock, times(1)).getInputStream();
 verify(connectionMock, times(1)).getHeaderFields();

 // The test response should match the created one
 assertEquals(expectedResponse, response);
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithNotFoundError() throws IOException {
 // Given: A not found error
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When: The Response object is created
 Response response = new Response(connectionMock);

 // Then: The code and body are set to default values, and the stream is an error stream
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 }

 @Test
 public void testGetBody() throws IOException {
 // Given: A Response object with a body
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(StreamUtils.createMockInputStream("Test body"));

 // When: The getBody method is called
 String body = response.getBody();

 // Then: The body is returned
 assertEquals("Test body", body);
 }

 @Test
 public void testGetStream() throws IOException {
 // Given: A Response object with a stream
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(StreamUtils.createMockInputStream("Test body"));

 // When: The getStream method is called
 InputStream stream = response.getStream();

 // Then: The stream is returned
 assertSame(stream, response.getStream());
 }

 @Test
 public void testGetCode() throws IOException {
 // Given: A Response object with a code
 when(connectionMock.getResponseCode()).thenReturn(200);

 // When: The getCode method is called
 int code = response.getCode();

 // Then: The code is returned
 assertEquals(200, code);
 }

 @Test
 public void testGetHeaders() throws IOException {
 // Given: A Response object with headers
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new BasicHeaderElement[] { new BasicHeaderElement("Content-Type", "text/plain"), new BasicHeaderElement("Content-Length", "10") });

 // When: The getHeaders method is called
 Map<String, String> headers = response.getHeaders();

 // Then: The headers are returned
 assertEquals("text/plain", headers.get("Content-Type"));
 assertEquals("10", headers.get("Content-Length"));
 }

 @Test
 public void testGetHeader() throws IOException {
 // Given: A Response object with a header
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new BasicHeaderElement[] { new BasicHeaderElement("Content-Type", "text/plain") });

 // When: The getHeader method is called
 String header = response.getHeader("Content-Type");
 // Then: The header is returned
 assertEquals("text/plain", header);
 }
}