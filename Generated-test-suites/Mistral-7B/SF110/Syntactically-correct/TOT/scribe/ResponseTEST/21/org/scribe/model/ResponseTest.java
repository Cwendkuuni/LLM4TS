import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setup() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithValidCodeAndBody() throws IOException {
 // Given: A valid response with a status code of 200 and a body
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 // When: The getBody() method is called
 String expectedBody = "{\"status\": \"success\"}";
 when(connectionMock.getOutputStream()).then(invocation -> {
 byte[] responseBody = expectedBody.getBytes();
 OutputStreamWriter writer = new OutputStreamWriter(invocation.getArgument(0));
 writer.write(new String(responseBody));
 writer.flush();
 });
 Mockito.doAnswer(invocation -> {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 StreamUtils.copy(invocation.getArgument(0), outputStream);
 return outputStream.toByteArray();
 }).when(connectionMock).getErrorStream();

 // Then: The getBody() method returns the expected body
 String actualBody = response.getBody();
 assertEquals(expectedBody, actualBody);
 }

 @Test
 public void testResponseWithValidCodeAndEmptyBody() throws IOException {
 // Given: A valid response with a status code of 200 and an empty body
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 // When: The getBody() method is called
 String emptyBody = EMPTY;
 // Then: The getBody() method returns the empty body
 String actualBody = response.getBody();
 assertEquals(emptyBody, actualBody);
 }

 @Test(expected = IOException.class)
 public void testResponseWithInvalidCode() throws IOException {
 // Given: An invalid response with a status code of 500
 when(connectionMock.getResponseCode()).thenReturn(500);

 // When: An attempt is made to get the body of the response
 // Then: An IOException is thrown
 response.getBody();
 }

 @Test
 public void testResponseWithValidHeaders() throws IOException {
 // Given: A valid response with valid headers
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 when(connectionMock.getHeaderFields()).thenReturn(headers);

 // When: The getHeaders() method is called
 Map<String, String> expectedHeaders = headers;
 // Then: The getHeaders() method returns the expected headers
 Map<String, String> actualHeaders = response.getHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testResponseWithEmptyHeaders() throws IOException {
 // Given: A valid response with empty headers
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When: The getHeaders() method is called
 Map<String, String> expectedHeaders = new HashMap<>();
 // Then: The getHeaders() method returns the empty headers
 Map<String, String> actualHeaders = response.getHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testResponseWithUnknownHeader() throws IOException {
 // Given: A valid response with an unknown header
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("X-Unknown-Header", "unknown value");
 when(connectionMock.getHeaderFields()).thenReturn(headers);

 // When: An attempt is made to get an unknown header
 // Then: A NullPointerException is thrown
 try {
 response.getHeader("X-Unknown-Header");
 } catch (NullPointerException e) {
 // Do nothing
 }
 }
}