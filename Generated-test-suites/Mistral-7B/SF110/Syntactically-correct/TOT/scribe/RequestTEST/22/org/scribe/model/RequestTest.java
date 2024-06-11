import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;
 private HttpURLConnection connectionMock;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://api.example.com?param1=value1&param2=value2");
 connectionMock = mock(HttpURLConnection.class);
 }

 @Test
 public void testCreateConnection() throws Exception {
 request.setConnection(connectionMock);
 request.createConnection();
 verify(connectionMock).openConnection();
 }

 @Test
 public void testAddHeaders() throws Exception {
 request.addHeader("Content-Type", "application/json");
 request.addHeader("Authorization", "Bearer token123");
 request.addHeaders(connectionMock);

 assertEquals("application/json", connectionMock.getRequestProperty("Content-Type"));
 assertEquals("Bearer token123", connectionMock.getRequestProperty("Authorization"));
 }

 @Test
 public void testSend_GET_NoBody() throws Exception {
 when(connectionMock.setRequestMethod(eq("GET"))).thenReturn(true);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(Verb.GET, response.getVerb());
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq("0"));
 }

 @Test
 public void testSend_GET_WithBody() {
 request.addPayload("{\"key\": \"value\"}");

 when(connectionMock.setRequestMethod(eq("GET"))).thenThrow(new IOException());

 try {
 request.send();
 fail("expected IOException");
 } catch (IOException e) {
 // expected
 }

 verify(connectionMock, never()).setDoOutput(true);
 verify(connectionMock, never()).getOutputStream();
 }

 @Test
 public void testSend_POST_NoBody() throws Exception {
 when(connectionMock.setRequestMethod(eq("POST"))).thenReturn(true);
 when(connectionMock.getResponseCode()).thenReturn(200);

 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(Verb.POST, response.getVerb());
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq("0"));
 }

 @Test
 public void testSend_POST_WithBody() throws Exception {
 final String body = "{\"key\": \"value\"}";
 request.addPayload(body);
 request.setCharset("UTF-8");

 when(connectionMock.setRequestMethod(eq("POST"))).thenThrow(new IOException());

 try {
 request.send();
 fail("expected IOException");
 } catch (IOException e) {
 // expected
 }

 try {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 request.setCharset("UTF-8");
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 request.send();
 } finally {
 verify(connectionMock, times(1)).setDoOutput(true);
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq(body.getBytes("UTF-8").length + ""));
 verify(connectionMock, times(1)).getOutputStream().write(body.getBytes("UTF-8"));
 }
 }

 @Test
 public void testSend_PUT() throws Exception {
 final String body = "{\"key\": \"value\"}";
 request.addPayload(body);
 request.setCharset("UTF-8");

 when(connectionMock.setRequestMethod(eq("PUT"))).thenThrow(new IOException());

 try {
 request.send();
 fail("expected IOException");
 } catch (IOException e) {
 // expected
 }

 try {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 request.setCharset("UTF-8");
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 request.send();
 } finally {
 verify(connectionMock, times(1)).setDoOutput(true);
 verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq(body.getBytes("UTF-8").length + ""));
 verify(connectionMock, times(1)).getOutputStream().write(body.getBytes("UTF-8"));
 }
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException() throws Exception {
 when(connectionMock.setRequestMethod(eq("GET"))).thenThrow(new IOException());
 request.send();
 }
}