import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private HttpURLConnection connectionMock;
 private Response response;

 @Before
 public void setup() {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testInitResponseWithSuccessfulConnection() throws IOException {
 // Given successful connection
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When creating new response object
 response.Response(connectionMock);

 // Then response code, body, stream and headers should be set correctly
 assert response.getCode() == 200;
 assert response.getBody() != null;
 assert response.getStream() != null;
 assert response.getHeaders().size() > 0;
 }

 @Test
 public void testInitResponseWithFailedConnection() {
 // Given failed connection with UnknownHostException
 Mockito.when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When creating new response object
 response.Response(connectionMock);

 // Then response code and body should be set with default values
 assert response.getCode() == 404;
 assert response.getBody().isEmpty();
 }

 @Test
 public void testGetBodyWithEmptyResponseBody() throws IOException {
 // Given response object with empty body
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 Mockito.when(connectionMock.getErrorStream()).thenReturn(StreamUtils.EMPTY_INPUT_STREAM);

 // When getting response body
 String expectedBody = "Expected body";
 response.parseBodyContents();
 response.setBody(expectedBody);
 String actualBody = response.getBody();

 // Then body should be set correctly
 assert actualBody.equals(expectedBody);
 }

 @Test
 public void testGetBodyWithNonEmptyResponseBody() throws IOException {
 // Given response object with non-empty body
 ByteArrayInputStream inputStream = new ByteArrayInputStream("Response body".getBytes());
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 Mockito.when(connectionMock.getInputStream()).thenReturn(inputStream);

 // When getting response body
 String expectedBody = "Response body";
 response.parseBodyContents();
 response.setBody(expectedBody);
 String actualBody = response.getBody();

 // Then body should be set correctly
 assert actualBody.equals(expectedBody);
 }

 @Test
 public void testGetHeadersWithEmptyHeaders() throws IOException {
 // Given response object with empty headers
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When getting response headers
 Map<String, String> expectedHeaders = new HashMap<>();
 response.parseHeaders(connectionMock);
 Map<String, String> actualHeaders = response.getHeaders();

 // Then headers should be set correctly
 assert actualHeaders.equals(expectedHeaders);
 }

 @Test
 public void testGetHeadersWithNonEmptyHeaders() throws IOException {
 // Given response object with non-empty headers
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Arrays.asList("application/json;charset=UTF-8"));
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFields);

 // When getting response headers
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json;charset=UTF-8");
 response.parseHeaders(connectionMock);
 Map<String, String> actualHeaders = response.getHeaders();

 // Then headers should be set correctly
 assert actualHeaders.equals(expectedHeaders);
 }
}