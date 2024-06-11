import org.junit.Assert;import org.junit.Test;import de.huxhorn.lilith.data.access.AccessEvent;import de.huxhorn.lilith.data.access.LoggerContext;import java.util.*;

public class AccessEventTest {

 @Test
 public void testAccessEventWithValidData() {
 AccessEvent event = new AccessEvent();
 event.setTimeStamp(1L);
 event.setLoggerContext(new LoggerContext());
 event.setRequestURI("/test/path");
 event.setRequestURL("http://test.com/test/path");
 event.setRemoteHost("127.0.0.1");
 event.setRemoteUser("testuser");
 event.setProtocol("HTTP/1.1");
 event.setMethod("GET");
 event.setServerName("testserver.com");
 event.setRemoteAddress("127.0.0.1");
 event.setRequestHeaders(new HashMap<>());
 event.setResponseHeaders(new HashMap<>());
 event.setRequestParameters(new HashMap<>());
 event.setLocalPort(8080);
 event.setStatusCode(200);

 Assert.assertEquals("1", Long.toString(event.getTimeStamp()));
 Assert.assertNotNull(event.getLoggerContext());
 Assert.assertEquals("/test/path", event.getRequestURI());
 Assert.assertEquals("http://test.com/test/path", event.getRequestURL());
 Assert.assertEquals("127.0.0.1", event.getRemoteHost());
 Assert.assertEquals("testuser", event.getRemoteUser());
 Assert.assertEquals("HTTP/1.1", event.getProtocol());
 Assert.assertEquals("GET", event.getMethod());
 Assert.assertEquals("testserver.com", event.getServerName());
 Assert.assertEquals("127.0.0.1", event.getRemoteAddress());
 Assert.assertTrue(event.getRequestHeaders() instanceof Map);
 Assert.assertTrue(event.getResponseHeaders() instanceof Map);
 Assert.assertTrue(event.getRequestParameters() instanceof Map);
 Assert.assertEquals(8080, event.getLocalPort());
 Assert.assertEquals(200, event.getStatusCode());
 }

 @Test
 public void testAccessEventWithNullTimeStamp() {
 AccessEvent event = new AccessEvent();
 event.setLoggerContext(new LoggerContext());
 event.setRequestURI("/test/path");
 event.setRequestURL("http://test.com/test/path");
 event.setRemoteHost("127.0.0.1");
 event.setRemoteUser("testuser");
 event.setProtocol("HTTP/1.1");
 event.setMethod("GET");
 event.setServerName("testserver.com");
 event.setRemoteAddress("127.0.0.1");
 event.setRequestHeaders(new HashMap<>());
 event.setResponseHeaders(new HashMap<>());
 event.setRequestParameters(new HashMap<>());
 event.setLocalPort(8080);
 event.setStatusCode(200);
 event.setTimeStamp(null);

 Assert.assertNull(event.getTimeStamp());
 }

 @Test
 public void testAccessEventWithEmptyLoggerContext() {
 AccessEvent event = new AccessEvent();
 event.setTimeStamp(1L);
 event.setLoggerContext(new LoggerContext());
 event.setRequestURI("/test/path");
 event.setRequestURL("http://test.com/test/path");
 event.setRemoteHost("127.0.0.1");
 event.setRemoteUser("testuser");
 event.setProtocol("HTTP/1.1");
 event.setMethod("GET");
 event.setServerName("testserver.com");
 event.setRemoteAddress("127.0.0.1");
 event.setRequestHeaders(new HashMap<>());
 event.setResponseHeaders(new HashMap<>());
 event.setRequestParameters(new HashMap<>());
 event.setLocalPort(8080);
 event.setStatusCode(200);
 event.setLoggerContext(new LoggerContext());

 Assert.assertNotSame(event.getLoggerContext(), new LoggerContext());
 event.setLoggerContext(new LoggerContext());

 Assert.assertNull(event.getLoggerContext().getLoggerName());
 Assert.assertNull(event.getLoggerContext().getLevel());
 }

 @Test
 public void testAccessEventWithEmptyMaps() {
 AccessEvent event = new AccessEvent();
 event.setTimeStamp(1L);
 event.setLoggerContext(new LoggerContext());
 event.setRequestURI("/test/path");
 event.setRequestURL("http://test.com/test/path");
 event.setRemoteHost("127.0.0.1");
 event.setRemoteUser("testuser");
 event.setProtocol("HTTP/1.1");
 event.setMethod("GET");
 event.setServerName("testserver.com");
 event.setRemoteAddress("127.0.0.1");
 event.setRequestHeaders(new HashMap<>());
 event.setResponseHeaders(new HashMap<>());
 event.setRequestParameters(new HashMap<>());
 event.setLocalPort(8080);
 event.setStatusCode(200);

 Assert.assertTrue(event.getRequestHeaders().isEmpty());
 Assert.assertTrue(event.getResponseHeaders().isEmpty());
 Assert.assertTrue(event.getRequestParameters().isEmpty());
 }

 @Test
 public void testAccessEventEquals() {
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(new LoggerContext());
 event1.setRequestURI("/test/path");
 event1.setRequestURL("http://test.com/test/path");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("testuser");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("testserver.com");
 event1.setRemoteAddress("127.0.0.1");
 event1.setRequestHeaders(new HashMap<>());
 event1.setResponseHeaders(new HashMap<>());
 event1.setRequestParameters(new HashMap<>());
 event1.setLocalPort(8080);
 event1.setStatusCode(200);

 event2.setTimeStamp(1L);
 event2.setLoggerContext(new LoggerContext());
 event2.setRequestURI("/test/path");
 event2.setRequestURL("http://test.com/test/path");
 event2.setRemoteHost("127.0.0.1");
 event2.setRemoteUser("testuser");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("testserver.com");
 event2.setRemoteAddress("127.0.0.1");
 event2.setRequestHeaders(new HashMap<>());
 event2.setResponseHeaders(new HashMap<>());
 event2.setRequestParameters(new HashMap<>());
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 Assert.assertTrue(event1.equals(event2));
 Assert.assertTrue(event2.equals(event1));
 }

 @Test
 public void testAccessEventHashCode() {
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(new LoggerContext());
 event1.setRequestURI("/test/path");
 event1.setRequestURL("http://test.com/test/path");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("testuser");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("testserver.com");
 event1.setRemoteAddress("127.0.0.1");
 event1.setRequestHeaders(new HashMap<>());
 event1.setResponseHeaders(new HashMap<>());
 event1.setRequestParameters(new HashMap<>());
 event1.setLocalPort(8080);
 event1.setStatusCode(200);

 event2.setTimeStamp(1L);
 event2.setLoggerContext(new LoggerContext());
 event2.setRequestURI("/test/path");
 event2.setRequestURL("http://test.com/test/path");
 event2.setRemoteHost("127.0.0.1");
 event2.setRemoteUser("testuser");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("testserver.com");
 event2.setRemoteAddress("127.0.0.1");
 event2.setRequestHeaders(new HashMap<>());
 event2.setResponseHeaders(new HashMap<>());
 event2.setRequestParameters(new HashMap<>());
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 Assert.assertEquals(event1.hashCode(), event2.hashCode());
 }
}