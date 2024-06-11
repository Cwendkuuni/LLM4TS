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
 public void setUp() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testConstructor_Success() throws IOException {
 // Given: A successful response
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);

 // When: The Response object is created
 response = new Response(connectionMock);

 // Then: The code, body, and headers are set correctly
 assertEquals(200, response.getCode());
 assertNull(response.getBody()); // body is empty by default
 assertNotNull(response.getHeaders());
 assertEquals(0, response.getHeaders().size());

 // And: Calling getBody() returns the body from the connection
 assertEquals(EMPTY, response.getBody());
 StreamUtils.copy(response.getStream(), new ByteArrayOutputStream()); // consume the stream
 String body = response.getBody();
 assertEquals(body, response.getBody());
 }

 @Test
 public void testConstructor_Failure() throws IOException {
 // Given: A failed response
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When: The Response object is created
 response = new Response(connectionMock);

 // Then: The code is set to 404, body is empty, and headers are empty
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 // Given: A response with a body
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Hello World".getBytes()));

 // When: The parseBodyContents() method is called
 response.parseBodyContents();

 // Then: The body is set to the contents of the stream
 assertEquals("Hello World", response.getBody());
 }

 @Test
 public void testParseHeaders() {
 // Given: A response with headers
 when(connectionMock.getHeaderFields()).thenReturn(new BasicHeaderElement[] {
 new BasicHeaderElement("Content-Type", "application/json"),
 new BasicHeaderElement("Cache-Control", "max-age=3600")
 });

 // When: The parseHeaders() method is called
 response.parseHeaders(connectionMock);

 // Then: The headers are set correctly
 assertEquals(2, response.getHeaders().size());
 assertEquals("application/json", response.getHeader("Content-Type"));
 assertEquals("max-age=3600", response.getHeader("Cache-Control"));
 }

 @Test
 public void testWasSuccessful() {
 // Given: A response with a successful status code
 when(connectionMock.getResponseCode()).thenReturn(200);

 // When: The wasSuccessful() method is called
 boolean result = response.wasSuccessful();

 // Then: The method returns true
 assertTrue(result);
 }

 @Test
 public void testWasSuccessful_Failure() {
 // Given: A response with an unsuccessful status code
 when(connectionMock.getResponseCode()).thenReturn(500);

 // When: The wasSuccessful() method is called
 boolean result = response.wasSuccessful();

 // Then: The method returns false
 assertFalse(result);
 }
}