import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithValidConnection() throws IOException {
 // Given: A valid HttpURLConnection
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputSteam()).thenReturn(new ByteArrayInputStream("Valid response body".getBytes()));

 // When: Instantiating Response object with valid connection
 Response actualResponse = response;

 // Then: Asserting Response object has valid code, body and headers
 assertEquals(200, actualResponse.getCode());
 assertEquals("Valid response body", actualResponse.getBody());
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("HeaderKey1", "HeaderValue1");
 expectedHeaders.put("HeaderKey2", "HeaderValue2");
 assertEquals(expectedHeaders, actualResponse.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithInvalidUrl() throws IOException {
 // Given: An invalid URL
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When: Instantiating Response object with invalid URL
 Response actualResponse = response;

 // Then: Asserting Response object has invalid code and empty body
 assertEquals(404, actualResponse.getCode());
 assertEquals(EMPTY, actualResponse.getBody());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 // Given: A Response object with empty body
 response = new Response(connectionMock);
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 // When: Calling parseBodyContents method
 String actualBody = response.parseBodyContents();

 // Then: Asserting parsed body is empty
 assertEquals(EMPTY, actualBody);
 }

 @Test
 public void testParseHeaders() {
 // Given: A HttpURLConnection with valid headers
 Map<String, List<String>> headers = new HashMap<>();
 headers.put("HeaderKey1", Arrays.asList("HeaderValue1", "HeaderValue2"));
 headers.put("HeaderKey2", Arrays.asList("HeaderValue3"));
 when(connectionMock.getHeaderFields()).thenReturn(headers);

 // When: Instantiating Response object
 Response actualResponse = response;

 // Then: Asserting headers are correctly parsed
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("HeaderKey1", "HeaderValue1");
 expectedHeaders.put("HeaderKey2", "HeaderValue3");
 assertEquals(expectedHeaders, actualResponse.getHeaders());
 }

 @Test
 public void testWasSuccessful() {
 // Given: A Response object with valid code
 response = new Response(connectionMock);
 when(connectionMock.getResponseCode()).thenReturn(201);

 // When: Calling wasSuccessful method
 boolean actualResult = response.wasSuccessful();

 // Then: Asserting method returns true
 assertTrue(actualResult);
 }

 @Test
 public void testWasSuccessfulWithInvalidCode() {
 // Given: A Response object with invalid code
 response = new Response(connectionMock);
 when(connectionMock.getResponseCode()).thenReturn(404);

 // When: Calling wasSuccessful method
 boolean actualResult = response.wasSuccessful();

 // Then: Asserting method returns false
 assertFalse(actualResult);
 }
}