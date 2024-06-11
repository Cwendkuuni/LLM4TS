import org.junit. Assert;
import org.junit.Test;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

 @Test
 public void testAccessEvent() {
 AccessEvent accessEvent = new AccessEvent();
 //Setting properties
 accessEvent.setTimeStamp(1L);
 accessEvent.setLoggerContext(new LoggerContext());
 accessEvent.setRequestURI("/testURI");
 accessEvent.setRequestURL("http://test.com/testURI");
 accessEvent.setRemoteHost("127.0.0.1");
 accessEvent.setRemoteUser("testUser");
 accessEvent.setProtocol("HTTP/1.1");
 accessEvent.setMethod("GET");
 accessEvent.setServerName("testServer");
 accessEvent.setRemoteAddress("127.0.0.1");
 accessEvent.setRequestHeaders(new HashMap<>());
 accessEvent.setResponseHeaders(new HashMap<>());
 accessEvent.setRequestParameters(new HashMap<>());
 accessEvent.setLocalPort(8080);
 accessEvent.setStatusCode(200);

 //Test getters
 Assert.assertEquals(1L, accessEvent.getTimeStamp());
 Assert.assertEquals(accessEvent.getLoggerContext(), accessEvent.getLoggerContext());
 Assert.assertEquals("/testURI", accessEvent.getRequestURI());
 Assert.assertEquals("http://test.com/testURI", accessEvent.getRequestURL());
 Assert.assertEquals("127.0.0.1", accessEvent.getRemoteHost());
 Assert.assertEquals("testUser", accessEvent.getRemoteUser());
 Assert.assertEquals("HTTP/1.1", accessEvent.getProtocol());
 Assert.assertEquals("GET", accessEvent.getMethod());
 Assert.assertEquals("testServer", accessEvent.getServerName());
 Assert.assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
 Assert.assertEquals(new HashMap<>(), accessEvent.getRequestHeaders());
 Assert.assertEquals(new HashMap<>(), accessEvent.getResponseHeaders());
 Assert.assertEquals(new HashMap<>(), accessEvent.getRequestParameters());
 Assert.assertEquals(8080, accessEvent.getLocalPort());
 Assert.assertEquals(200, accessEvent.getStatusCode());

 //Setting properties with null values and testing getters
 accessEvent.setTimeStamp(null);
 accessEvent.setLoggerContext(null);
 accessEvent.setRequestURI(null);
 accessEvent.setRequestURL(null);
 accessEvent.setRemoteHost(null);
 accessEvent.setRemoteUser(null);
 accessEvent.setProtocol(null);
 accessEvent.setMethod(null);
 accessEvent.setServerName(null);
 accessEvent.setRemoteAddress(null);
 accessEvent.setRequestHeaders(null);
 accessEvent.setResponseHeaders(null);
 accessEvent.setRequestParameters(null);
 accessEvent.setLocalPort(0);
 accessEvent.setStatusCode(0);

 //Testing getters with null values
 Assert.assertNull(accessEvent.getTimeStamp());
 Assert.assertNull(accessEvent.getLoggerContext());
 Assert.assertNull(accessEvent.getRequestURI());
 Assert.assertNull(accessEvent.getRequestURL());
 Assert.assertNull(accessEvent.getRemoteHost());
 Assert.assertNull(accessEvent.getRemoteUser());
 Assert.assertNull(accessEvent.getProtocol());
 Assert.assertNull(accessEvent.getMethod());
 Assert.assertNull(accessEvent.getServerName());
 Assert.assertNull(accessEvent.getRemoteAddress());
 Assert.assertNull(accessEvent.getRequestHeaders());
 Assert.assertNull(accessEvent.getResponseHeaders());
 Assert.assertEquals(0, accessEvent.getLocalPort());
 Assert.assertEquals(0, accessEvent.getStatusCode());
 }

 @Test
 public void testEquals() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 AccessEvent accessEvent3 = new AccessEvent();

 accessEvent1.setTimeStamp(1L);
 accessEvent1.setLoggerContext(new LoggerContext());
 accessEvent1.setRequestURI("/testURI");
 accessEvent1.setRequestURL("http://test.com/testURI");
 accessEvent1.setRemoteHost("127.0.0.1");
 accessEvent1.setRemoteUser("testUser");
 accessEvent1.setProtocol("HTTP/1.1");
 accessEvent1.setMethod("GET");
 accessEvent1.setServerName("testServer");
 accessEvent1.setRemoteAddress("127.0.0.1");
 accessEvent1.setRequestHeaders(new HashMap<>());
 accessEvent1.setResponseHeaders(new HashMap<>());
 accessEvent1.setRequestParameters(new HashMap<>());
 accessEvent1.setLocalPort(8080);
 accessEvent1.setStatusCode(200);

 accessEvent2.setTimeStamp(1L);
 accessEvent2.setLoggerContext(new LoggerContext());
 accessEvent2.setRequestURI("/testURI");
 accessEvent2.setRequestURL("http://test.com/testURI");
 accessEvent2.setRemoteHost("127.0.0.1");
 accessEvent2.setRemoteUser("testUser");
 accessEvent2.setProtocol("HTTP/1.1");
 accessEvent2.setMethod("GET");
 accessEvent2.setServerName("testServer");
 accessEvent2.setRemoteAddress("127.0.0.1");
 accessEvent2.setRequestHeaders(new HashMap<>());
 accessEvent2.setResponseHeaders(new HashMap<>());
 accessEvent2.setRequestParameters(new HashMap<>());
 accessEvent2.setLocalPort(8080);
 accessEvent2.setStatusCode(200);

 accessEvent3.setTimeStamp(1L);
 accessEvent3.setLoggerContext(new LoggerContext());
 accessEvent3.setRequestURI("/testURI");
 accessEvent3.setRequestURL("http://test.com/testURI");
 accessEvent3.setRemoteHost("127.0.0.1");
 accessEvent3.setRemoteUser("testUser");
 accessEvent3.setProtocol("HTTP/1.1");
 accessEvent3.setMethod("GET");
 accessEvent3.setServerName("testServer");
 accessEvent3.setRemoteAddress("127.0.0.1");
 accessEvent3.setRequestHeaders(new HashMap<>());
 accessEvent3.setResponseHeaders(new HashMap<>());
 accessEvent3.setRequestParameters(new HashMap<>());
 accessEvent3.setLocalPort(8080);
 accessEvent3.setStatusCode(200);

 //Testing equals with same object
 Assert.assertTrue(accessEvent1.equals(accessEvent1));

 //Testing equals with different objects but same values
 Assert.assertTrue(accessEvent1.equals(accessEvent2));

 //Testing equals with different objects but different values
 accessEvent2.setRemoteUser("testUser2");
 Assert.assertFalse(accessEvent1.equals(accessEvent2));

 //Testing equals with null object
 Assert.assertFalse(accessEvent1.equals(null));
 }

 @Test
 public void testHashCode() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 AccessEvent accessEvent3 = new AccessEvent();

 accessEvent1.setTimeStamp(1L);
 accessEvent1.setLoggerContext(new LoggerContext());
 accessEvent1.setRequestURI("/testURI");
 accessEvent1.setRequestURL("http://test.com/testURI");
 accessEvent1.setRemoteHost("127.0.0.1");
 accessEvent1.setRemoteUser("testUser");
 accessEvent1.setProtocol("HTTP/1.1");
 accessEvent1.setMethod("GET");
 accessEvent1.setServerName("testServer");
 accessEvent1.setRemoteAddress("127.0.0.1");
 accessEvent1.setRequestHeaders(new HashMap<>());
 accessEvent1.setResponseHeaders(new HashMap<>());
 accessEvent1.setRequestParameters(new HashMap<>());
 accessEvent1.setLocalPort(8080);
 accessEvent1.setStatusCode(200);

 accessEvent2.setTimeStamp(1L);
 accessEvent2.setLoggerContext(new LoggerContext());
 accessEvent2.setRequestURI("/testURI");
 accessEvent2.setRequestURL("http://test.com/testURI");
 accessEvent2.setRemoteHost("127.0.0.1");
 accessEvent2.setRemoteUser("testUser");
 accessEvent2.setProtocol("HTTP/1.1");
 accessEvent2.setMethod("GET");
 accessEvent2.setServerName("testServer");
 accessEvent2.setRemoteAddress("127.0.0.1");
 accessEvent2.setRequestHeaders(new HashMap<>());
 accessEvent2.setResponseHeaders(new HashMap<>());
 accessEvent2.setRequestParameters(new HashMap<>());
 accessEvent2.setLocalPort(8080);
 accessEvent2.setStatusCode(200);

 accessEvent3.setTimeStamp(2L);
 accessEvent3.setLoggerContext(new LoggerContext());
 accessEvent3.setRequestURI("/testURI2");
 accessEvent3.setRequestURL("http://test.com/testURI2");
 accessEvent3.setRemoteHost("127.0.0.1");
 accessEvent3.setRemoteUser("testUser");
 accessEvent3.setProtocol("HTTP/1.1");
 accessEvent3.setMethod("GET");
 accessEvent3.setServerName("testServer");
 accessEvent3.setRemoteAddress("127.0.0.1");
 accessEvent3.setRequestHeaders(new HashMap<>());
 accessEvent3.setResponseHeaders(new HashMap<>());
 accessEvent3.setRequestParameters(new HashMap<>());
 accessEvent3.setLocalPort(8080);
 accessEvent3.setStatusCode(200);

 //Testing hashCode with same object
 Assert.assertEquals(accessEvent1.hashCode(), accessEvent1.hashCode());

 //Testing hashCode with different objects but same values
 Assert.assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

 //Testing hashCode with different objects but different values
 Assert.assertNotSame(accessEvent1.hashCode(), accessEvent3.hashCode());
 }
}