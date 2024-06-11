import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HTTPURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HTTPURLConnection connectionMock;

 @Before
 public void setup() {
 connectionMock = Mockito.mock(HTTPURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessCodeAndBody() throws IOException {
 // Given
 String expectedBody = "This is a successful response body.";
 ByteArrayInputStream bodyStream = new ByteArrayInputStream(expectedBody.getBytes());
 Mockito.when(connectionMock.getInputStream()).thenReturn(bodyStream);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);

 // When
 response.parseBodyContents();

 // Then
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testResponseWithErrorCodeAndEmptyBody() {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenReturn(404);

 // When
 // No need to call parseBodyContents(), since getBody() calls it if body is null

 // Then
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 // Given
 String headerKey = "Content-Type";
 String headerValue = "application/json";
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>(Arrays.asList(new AbstractMap.SimpleEntry<>("Content-Type", new ArrayList<>(Arrays.asList(new AbstractMap.SimpleEntry<>("Content-Type", headerValue)))))));

 // When

 // Then
 assertEquals(headerValue, response.getHeader(headerKey));
 }

 @Test(expected = IOException.class)
 public void testResponseWithUnknownHostException() {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When
 // Testing parseBodyContents() method leads to IOException in case of UnknownHostException
 response.parseBodyContents();
 }
}