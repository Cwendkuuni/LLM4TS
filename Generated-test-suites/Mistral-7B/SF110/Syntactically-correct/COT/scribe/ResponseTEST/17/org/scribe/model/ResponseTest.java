import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import static java.util.Collections.singletonMap;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws IOException {
 connection = openConnection("http://example.com");
 response = new Response(connection);
 }

 @After
 public void tearDown() throws Exception {
 connection.disconnect();
 }

 @Test
 public void testResponseWithSuccessfulStatus() {
 // Test for constructor with successful status
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() {
 // Test for parseBodyContents when body is empty
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.getOutputStream().write("{}".getBytes("UTF-8"));
 connection.getOutputStream().flush();
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 }

 @Test(expected = IOException.class)
 public void testResponseWithIOException() {
 // Test for constructor with IOException
 connection = openConnection("http://example.com/notfound");
 response = new Response(connection);
 fail("An IOException should be thrown but was not.");
 }

 @Test
 public void testResponseWithEmptyHeaders() {
 // Test for parseHeaders with empty headers
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestProperty("Content-Type", "application/json");
 connection.disconnect();
 response = new Response(connection);
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testResponseWithSingleHeader() {
 // Test for parseHeaders with single header
 connection.setRequestProperty("Cache-Control", "max-age=0");
 connection.disconnect();
 response = new Response(connection);
 assertEquals(singletonMap("Cache-Control", "max-age=0"), response.getHeaders());
 }

 @Test
 public void testResponseWithMultipleHeaders() {
 // Test for parseHeaders with multiple headers
 connection.setRequestProperty("Cache-Control", "max-age=0");
 connection.setRequestProperty("Content-Type", "application/json");
 connection.disconnect();
 response = new Response(connection);
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Cache-Control", "max-age=0");
 expectedHeaders.put("Content-Type", "application/json");
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test(expected = NullPointerException.class)
 public void testNullHeaders() {
 // Test for getHeader with null headers
 response.headers = null;
 try {
 response.getHeader("Cache-Control");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetHeaderWithInvalidName() {
 // Test for getHeader with invalid name
 response.headers = Arrays.asList("Cache-Control: max-age=0", "Content-Type: application/json").stream().collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
 try {
 response.getHeader("Invalid-Name");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetHeaderWithValidName() {
 // Test for getHeader with valid name
 response.headers = Arrays.asList("Cache-Control: max-age=0", "Content-Type: application/json").stream().collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
 assertEquals("max-age=0", response.getHeader("Cache-Control"));
 }

 @Test(expected = IOException.class)
 public void testResponseWithErrorStream() throws UnsupportedEncodingException {
 // Test for getBody with error stream
 connection.setDoOutput(true);
 connection.getOutputStream().write("{}".getBytes("UTF-8"));
 connection.getOutputStream().flush();
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setInstanceFollowRedirects(false);
 connection.connect();
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertEquals("", response.getHeader("Content-Type"));
 assertNotNull(response.getErrorStream());
 try (InputStream errorStream = response.getErrorStream()) {
 String errorBody = StreamUtils.getStreamContents(errorStream);
 assertEquals("This resource could not be found.", errorBody);
 }
 }

 @Test
 public void testResponseWithEmptyErrorStream() throws IOException {
 // Test for getBody with empty error stream
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setInstanceFollowRedirects(false);
 connection.connect();
 assertEquals(404, response.getCode());
 assertEquals("", response.getBody());
 assertNull(response.getErrorStream());
 }

 private URL openConnection(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 return url;
 }

}