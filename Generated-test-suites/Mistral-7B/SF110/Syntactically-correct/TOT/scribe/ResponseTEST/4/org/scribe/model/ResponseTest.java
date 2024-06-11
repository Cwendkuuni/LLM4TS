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
 public void testResponseWithSuccessfulStatusCode() {
 // Given: Response is created with successful HTTP status code
 when(connectionMock.getResponseCode()).thenReturn(200);

 // When: Response is accessed
 then(connectionMock).should().connect();
 response.parseBodyContents();

 // Then: Response has a body and valid headers
 assertEquals("Expected body", "Actual body", response.getBody());
 assertNotNull(response.getHeaders());
 assertFalse(response.getHeaders().isEmpty());

 // Verify: HTTP connection was made and stream was read
 verify(connectionMock).connect();
 verify(connectionMock).getInputStream();
 }

 @Test
 public void testResponseWithErrorStatusCode() {
 // Given: Response is created with error HTTP status code
 when(connectionMock.getResponseCode()).thenReturn(404);

 // When: Response is accessed
 then(connectionMock).should().connect();
 response.parseBodyContents();

 // Then: Response has an empty body and valid headers
 assertEquals(Response.EMPTY, response.getBody());
 assertNotNull(response.getHeaders());
 assertFalse(response.getHeaders().isEmpty());

 // Verify: HTTP connection was made but error stream was read instead
 verify(connectionMock).connect();
 verify(connectionMock).getErrorStream();
 }

 @Test
 public void testResponseWithUnknownHost() {
 // Given: Response is created with UnknownHostException
 doThrow(new UnknownHostException("")).when(connectionMock).connect();

 // When: Response is accessed
 response.parseBodyContents();

 // Then: Response has an empty body and valid headers with 404 status code
 assertEquals(Response.EMPTY, response.getBody());
 assertNotNull(response.getHeaders());
 assertFalse(response.getHeaders().isEmpty());
 assertEquals(404, response.getCode());

 // Verify: No HTTP connection was made
 verify(connectionMock, never()).getInputStream();
 verify(connectionMock, never()).getErrorStream();
 }
}