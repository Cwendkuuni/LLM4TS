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
 public void testGettersAndSetters_validValues() {
 // Test all getters and setters with valid values
 setUp();

 event.setTimeStamp(1L);
 assertEquals(1L, event.getTimeStamp());
 event.setLoggerContext(loggerContext);
 assertEquals(loggerContext, event.getLoggerContext());
 event.setRequestURI("/test");
 assertEquals("/test", event.getRequestURI());
 event.setRequestURL("http://localhost:8080/test");
 assertEquals("http://localhost:8080/test", event.getRequestURL());
 event.setRemoteHost("127.0.0.1");
 assertEquals("127.0.0.1", event.getRemoteHost());
 event.setRemoteUser("testUser");
 assertEquals("testUser", event.getRemoteUser());
 event.setProtocol("HTTP/1.1");
 assertEquals("HTTP/1.1", event.getProtocol());
 event.setMethod("GET");
 assertEquals("GET", event.getMethod());
 event.setServerName("localhost");
 assertEquals("localhost", event.getServerName());
 event.setRemoteAddress("127.0.0.1");
 assertEquals("127.0.0.1", event.getRemoteAddress());
 Map<String, String> requestHeaders = new HashMap<>();
 requestHeaders.put("Content-Type", "application/json");
 event.setRequestHeaders(requestHeaders);
 assertEquals(requestHeaders, event.getRequestHeaders());
 Map<String, String> responseHeaders = new HashMap<>();
 responseHeaders.put("Content-Type", "application/json");
 event.setResponseHeaders(responseHeaders);
 Map<String, String[]> requestParameters = new HashMap<>();
 requestParameters.put("param1", new String[]{"value1", "value2"});
 event.setRequestParameters(requestParameters);
 assertEquals(requestParameters, event.getRequestParameters());
 event.setLocalPort(8080);
 assertEquals(8080, event.getLocalPort());
 event.setStatusCode(200);
 assertEquals(200, event.getStatusCode());
 }

 @Test
 public void testGettersAndSetters_nullValues() {
 // Test all getters and setters with null values
 setUp();

 event.setTimeStamp(null);
 assertNull(event.getTimeStamp());
 event.setLoggerContext(null);
 assertNull(event.getLoggerContext());
 event.setRequestURI(null);
 assertNull(event.getRequestURI());
 event.setRequestURL(null);
 assertNull(event.getRequestURL());
 event.setRemoteHost(null);
 assertNull(event.getRemoteHost());
 event.setRemoteUser(null);
 assertNull(event.getRemoteUser());
 event.setProtocol(null);
 assertNull(event.getProtocol());
 event.setMethod(null);
 assertNull(event.getMethod());
 event.setServerName(null);
 assertNull(event.getServerName());
 event.setRemoteAddress(null);
 assertNull(event.getRemoteAddress());
 event.setRequestHeaders(null);
 assertNull(event.getRequestHeaders());
 event.setResponseHeaders(null);
 assertNull(event.getResponseHeaders());
 event.setRequestParameters(null);
 assertNull(event.getRequestParameters());
 event.setLocalPort(0);
 assertEquals(0, event.getLocalPort());
 event.setStatusCode(0);
 assertEquals(0, event.getStatusCode());
 }

 @Test
 public void testEquals() {
 // Test equals method with equal and different AccessEvent objects
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();
 AccessEvent event3 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(loggerContext);
 event1.setRequestURI("/test");
 event1.setRequestURL("http://localhost:8080/test");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("testUser");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("localhost");
 event1.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders1 = new HashMap<>();
 requestHeaders1.put("Content-Type", "application/json");
 event1.setRequestHeaders(requestHeaders1);
 Map<String, String> responseHeaders1 = new HashMap<>();
 responseHeaders1.put("Content-Type", "application/json");
 event1.setResponseHeaders(responseHeaders1);
 Map<String, String[]> requestParameters1 = new HashMap<>();
 requestParameters1.put("param1", new String[]{"value1", "value2"});
 event1.setRequestParameters(requestParameters1);
 event1.setLocalPort(8080);
 event1.setStatusCode(200);

 event2.setTimeStamp(1L);
 event2.setLoggerContext(loggerContext);
 event2.setRequestURI("/test");
 event2.setRequestURL("http://localhost:8080/test");
 event2.setRemoteHost("127.0.0.1");
 event2.setRemoteUser("testUser");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("localhost");
 event2.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders2 = new HashMap<>();
 requestHeaders2.put("Content-Type", "application/json");
 event2.setRequestHeaders(requestHeaders2);
 Map<String, String> responseHeaders2 = new HashMap<>();
 responseHeaders2.put("Content-Type", "application/json");
 event2.setResponseHeaders(responseHeaders2);
 Map<String, String[]> requestParameters2 = new HashMap<>();
 requestParameters2.put("param1", new String[]{"value1", "value2"});
 event2.setRequestParameters(requestParameters2);
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 event3.setTimeStamp(2L);
 event3.setLoggerContext(loggerContext);
 event3.setRequestURI("/test");
 event3.setRequestURL("http://localhost:8080/test");
 event3.setRemoteHost("127.0.0.1");
 event3.setRemoteUser("testUser");
 event3.setProtocol("HTTP/1.1");
 event3.setMethod("GET");
 event3.setServerName("localhost");
 event3.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders3 = new HashMap<>();
 requestHeaders3.put("Content-Type", "application/json");
 event3.setRequestHeaders(requestHeaders3);
 Map<String, String> responseHeaders3 = new HashMap<>();
 responseHeaders3.put("Content-Type", "application/json");
 event3.setResponseHeaders(responseHeaders3);
 Map<String, String[]> requestParameters3 = new HashMap<>();
 requestParameters3.put("param1", new String[]{"value1", "value2"});
 event3.setRequestParameters(requestParameters3);
 event3.setLocalPort(8080);
 event3.setStatusCode(200);

 assertTrue(event1.equals(event2));
 assertFalse(event1.equals(event3));
 }

 @Test
 public void testHashCode() {
 // Test hashCode method with equal and different AccessEvent objects
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();
 AccessEvent event3 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(loggerContext);
 event1.setRequestURI("/test");
 event1.setRequestURL("http://localhost:8080/test");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("testUser");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("localhost");
 event1.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders1 = new HashMap<>();
 requestHeaders1.put("Content-Type", "application/json");
 event1.setRequestHeaders(requestHeaders1);
 Map<String, String> responseHeaders1 = new HashMap<>();
 responseHeaders1.put("Content-Type", "application/json");
 event1.setResponseHeaders(responseHeaders1);
 Map<String, String[]> requestParameters1 = new HashMap<>();
 requestParameters1.put("param1", new String[]{"value1", "value2"});
 event1.setRequestParameters(requestParameters1);
 event1.setLocalPort(8080);
 event1.setStatusCode(200);

 event2.setTimeStamp(1L);
 event2.setLoggerContext(loggerContext);
 event2.setRequestURI("/test");
 event2.setRequestURL("http://localhost:8080/test");
 event2.setRemoteHost("127.0.0.1");
 event2.setRemoteUser("testUser");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("localhost");
 event2.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders2 = new HashMap<>();
 requestHeaders2.put("Content-Type", "application/json");
 event2.setRequestHeaders(requestHeaders2);
 Map<String, String> responseHeaders2 = new HashMap<>();
 responseHeaders2.put("Content-Type", "application/json");
 event2.setResponseHeaders(responseHeaders2);
 Map<String, String[]> requestParameters2 = new HashMap<>();
 requestParameters2.put("param1", new String[]{"value1", "value2"});
 event2.setRequestParameters(requestParameters2);
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 event3.setTimeStamp(2L);
 event3.setLoggerContext(loggerContext);
 event3.setRequestURI("/test");
 event3.setRequestURL("http://localhost:8080/test");
 event3.setRemoteHost("127.0.0.1");
 event3.setRemoteUser("testUser");
 event3.setProtocol("HTTP/1.1");
 event3.setMethod("GET");
 event3.setServerName("localhost");
 event3.setRemoteAddress("127.0.0.1");
 Map<String, String> requestHeaders3 = new HashMap<>();
 requestHeaders3.put("Content-Type", "application/json");
 event3.setRequestHeaders(requestHeaders3);
 Map<String, String> responseHeaders3 = new HashMap<>();
 responseHeaders3.put("Content-Type", "application/json");
 event3.setResponseHeaders(responseHeaders3);
 Map<String, String[]> requestParameters3 = new HashMap<>();
 requestParameters3.put("param1", new String[]{"value1", "value2"});
 event3.setRequestParameters(requestParameters3);
 event3.setLocalPort(8080);
 event3.setStatusCode(200);

 int hashCode1 = event1.hashCode();
 int hashCode2 = event2.hashCode();
 int hashCode3 = event3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}