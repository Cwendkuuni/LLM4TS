import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

 private AccessEvent event;
 private LoggerContext loggerContext;

 @Before
 public void setUp() {
 loggerContext = new LoggerContext();
 event = new AccessEvent();
 event.setLoggerContext(loggerContext);
 event.setTimeStamp(System.currentTimeMillis());
 }

 @Test
 public void testGetTimeStamp() {
 Long expected = event.getTimeStamp();
 event.setTimeStamp(expected + 1000L);
 Long actual = event.getTimeStamp();
 assertEquals(expected + 1000L, actual);
 }

 @Test
 public void testSetTimeStamp() {
 Long expected = 1234567890123L;
 event.setTimeStamp(expected);
 Long actual = event.getTimeStamp();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLoggerContext() {
 assertEquals(loggerContext, event.getLoggerContext());
 }

 @Test
 public void testSetLoggerContext() {
 LoggerContext newLoggerContext = new LoggerContext();
 event.setLoggerContext(newLoggerContext);
 assertEquals(newLoggerContext, event.getLoggerContext());
 }

 @Test
 public void testGetRequestURI() {
 String expected = "/test";
 event.setRequestURI(expected);
 String actual = event.getRequestURI();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestURI() {
 String expected = "/test2";
 event.setRequestURI(expected);
 String actual = event.getRequestURI();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRequestURL() {
 String expected = "http://example.com/test";
 event.setRequestURL(expected);
 String actual = event.getRequestURL();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestURL() {
 String expected = "http://example.com/test2";
 event.setRequestURL(expected);
 String actual = event.getRequestURL();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRemoteHost() {
 String expected = "127.0.0.1";
 event.setRemoteHost(expected);
 String actual = event.getRemoteHost();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteHost() {
 String expected = "192.168.0.1";
 event.setRemoteHost(expected);
 String actual = event.getRemoteHost();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRemoteUser() {
 String expected = "admin";
 event.setRemoteUser(expected);
 String actual = event.getRemoteUser();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteUser() {
 String expected = "guest";
 event.setRemoteUser(expected);
 String actual = event.getRemoteUser();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetProtocol() {
 String expected = "HTTP/1.1";
 event.setProtocol(expected);
 String actual = event.getProtocol();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetProtocol() {
 String expected = "HTTPS/1.1";
 event.setProtocol(expected);
 String actual = event.getProtocol();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMethod() {
 String expected = "GET";
 event.setMethod(expected);
 String actual = event.getMethod();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMethod() {
 String expected = "POST";
 event.setMethod(expected);
 String actual = event.getMethod();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetServerName() {
 String expected = "example.com";
 event.setServerName(expected);
 String actual = event.getServerName();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetServerName() {
 String expected = "www.example.com";
 event.setServerName(expected);
 String actual = event.getServerName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRemoteAddress() {
 String expected = "127.0.0.1";
 event.setRemoteAddress(expected);
 String actual = event.getRemoteAddress();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteAddress() {
 String expected = "192.168.0.1";
 event.setRemoteAddress(expected);
 String actual = event.getRemoteAddress();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRequestHeaders() {
 Map<String, String> expected = new HashMap<>();
 expected.put("Content-Type", "application/json");
 event.setRequestHeaders(expected);
 Map<String, String> actual = event.getRequestHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestHeaders() {
 Map<String, String> expected = new HashMap<>();
 expected.put("Content-Type", "application/xml");
 event.setRequestHeaders(expected);
 Map<String, String> actual = event.getRequestHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetResponseHeaders() {
 Map<String, String> expected = new HashMap<>();
 expected.put("Content-Type", "application/json");
 event.setResponseHeaders(expected);
 Map<String, String> actual = event.getResponseHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetResponseHeaders() {
 Map<String, String> expected = new HashMap<>();
 expected.put("Content-Type", "application/xml");
 event.setResponseHeaders(expected);
 Map<String, String> actual = event.getResponseHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRequestParameters() {
 Map<String, String[]> expected = new HashMap<>();
 expected.put("param1", new String[]{"value1", "value2"});
 event.setRequestParameters(expected);
 Map<String, String[]> actual = event.getRequestParameters();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestParameters() {
 Map<String, String[]> expected = new HashMap<>();
 expected.put("param1", new String[]{"value3", "value4"});
 event.setRequestParameters(expected);
 Map<String, String[]> actual = event.getRequestParameters();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPort() {
 int expected = 8080;
 event.setLocalPort(expected);
 int actual = event.getLocalPort();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetLocalPort() {
 int expected = 80;
 event.setLocalPort(expected);
 int actual = event.getLocalPort();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode() {
 int expected = 200;
 event.setStatusCode(expected);
 int actual = event.getStatusCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetStatusCode() {
 int expected = 404;
 event.setStatusCode(expected);
 int actual = event.getStatusCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();
 assertFalse(event1.equals(event2));

 event1.setLoggerContext(loggerContext);
 event1.setTimeStamp(System.currentTimeMillis());
 event2.setLoggerContext(loggerContext);
 event2.setTimeStamp(System.currentTimeMillis());
 assertTrue(event1.equals(event2));

 event2.setLocalPort(event1.getLocalPort() + 1);
 assertFalse(event1.equals(event2));

 event2.setStatusCode(event1.getStatusCode());
 assertTrue(event1.equals(event2));
 }

 @Test
 public void testHashCode() {
 AccessEvent event1 = new AccessEvent();
 int hashCode1 = event1.hashCode();

 AccessEvent event2 = new AccessEvent();
 event2.setLoggerContext(loggerContext);
 event2.setTimeStamp(System.currentTimeMillis());
 int hashCode2 = event2.hashCode();
 assertEquals(hashCode1, hashCode2);

 event2.setLocalPort(event1.getLocalPort() + 1);
 int hashCode3 = event2.hashCode();
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 AccessEvent event = new AccessEvent();
 event.setLoggerContext(new LoggerContext());
 event.setTimeStamp(System.currentTimeMillis());
 String expected = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + event.getTimeStamp() + "]";
 String actual = event.toString();
 assertEquals(expected, actual);
 }
}