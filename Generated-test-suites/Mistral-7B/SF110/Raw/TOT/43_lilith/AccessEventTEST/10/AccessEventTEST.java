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
 public void setup() {
 loggerContext = new LoggerContext();
 event = new AccessEvent();
 event.setLoggerContext(loggerContext);
 event.setTimeStamp(1L);
 event.setRequestURI("/test");
 event.setRequestURL("http://test.com/test");
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
 }

 @Test
 public void testGettersAndSetters() {
 // Testing getters and setters for all fields
 Long expectedTimeStamp = 1L;
 assertEquals("TimeStamp getter should return correct value", expectedTimeStamp, event.getTimeStamp());
 event.setTimeStamp(2L);
 assertEquals("TimeStamp setter should set correct value", 2L, event.getTimeStamp());

 assertEquals("LoggerContext getter should return correct value", loggerContext, event.getLoggerContext());
 String expectedLoggerContextName = "defaultLoggerContext";
 loggerContext.setName(expectedLoggerContextName);
 assertEquals("LoggerContext getter should return correct value after setter call", expectedLoggerContextName, event.getLoggerContext().getName());

 assertEquals("RequestURI getter should return correct value", "/test", event.getRequestURI());
 event.setRequestURI("/test2");
 assertEquals("RequestURI setter should set correct value", "/test2", event.getRequestURI());

 assertEquals("RequestURL getter should return correct value", "http://test.com/test", event.getRequestURL());
 event.setRequestURL("http://test.com/test2");
 assertEquals("RequestURL setter should set correct value", "http://test.com/test2", event.getRequestURL());

 assertEquals("RemoteHost getter should return correct value", "127.0.0.1", event.getRemoteHost());
 event.setRemoteHost("127.0.0.2");
 assertEquals("RemoteHost setter should set correct value", "127.0.0.2", event.getRemoteHost());

 assertEquals("RemoteUser getter should return correct value", "testuser", event.getRemoteUser());
 event.setRemoteUser("testuser2");
 assertEquals("RemoteUser setter should set correct value", "testuser2", event.getRemoteUser());

 assertEquals("Protocol getter should return correct value", "HTTP/1.1", event.getProtocol());
 event.setProtocol("HTTP/1.2");
 assertEquals("Protocol setter should set correct value", "HTTP/1.2", event.getProtocol());

 assertEquals("Method getter should return correct value", "GET", event.getMethod());
 event.setMethod("POST");
 assertEquals("Method setter should set correct value", "POST", event.getMethod());

 assertEquals("ServerName getter should return correct value", "testserver.com", event.getServerName());
 event.setServerName("testserver2.com");
 assertEquals("ServerName setter should set correct value", "testserver2.com", event.getServerName());

 assertEquals("RemoteAddress getter should return correct value", "127.0.0.1", event.getRemoteAddress());
 event.setRemoteAddress("127.0.0.2");
 assertEquals("RemoteAddress setter should set correct value", "127.0.0.2", event.getRemoteAddress());

 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 assertEquals("RequestHeaders getter should return correct value", expectedHeaders, event.getRequestHeaders());
 event.setRequestHeaders(expectedHeaders);
 assertEquals("RequestHeaders setter should set correct value", expectedHeaders, event.getRequestHeaders());

 Map<String, String> expectedResponseHeaders = new HashMap<>();
 expectedResponseHeaders.put("Content-Type", "application/json");
 assertEquals("ResponseHeaders getter should return correct value", expectedResponseHeaders, event.getResponseHeaders());
 event.setResponseHeaders(expectedResponseHeaders);
 assertEquals("ResponseHeaders setter should set correct value", expectedResponseHeaders, event.getResponseHeaders());

 Map<String, String[]> expectedRequestParameters = new HashMap<>();
 expectedRequestParameters.put("param1", new String[]{"value1", "value2"});
 assertEquals("RequestParameters getter should return correct value", expectedRequestParameters, event.getRequestParameters());
 event.setRequestParameters(expectedRequestParameters);
 assertEquals("RequestParameters setter should set correct value", expectedRequestParameters, event.getRequestParameters());

 int expectedLocalPort = 8080;
 assertEquals("LocalPort getter should return correct value", expectedLocalPort, event.getLocalPort());
 event.setLocalPort(8081);
 assertEquals("LocalPort setter should set correct value", 8081, event.getLocalPort());

 int expectedStatusCode = 200;
 assertEquals("StatusCode getter should return correct value", expectedStatusCode, event.getStatusCode());
 event.setStatusCode(201);
 assertEquals("StatusCode setter should set correct value", 201, event.getStatusCode());
 }

 @Test
 public void testEqualsAndHashcode() {
 AccessEvent event1 = new AccessEvent();
 event1.setTimeStamp(1L);
 event1.setLoggerContext(loggerContext);
 event1.setRequestURI("/test");
 event1.setRequestURL("http://test.com/test");
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

 AccessEvent event2 = new AccessEvent();
 event2.setTimeStamp(1L);
 event2.setLoggerContext(loggerContext);
 event2.setRequestURI("/test");
 event2.setRequestURL("http://test.com/test");
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

 AccessEvent event3 = new AccessEvent();
 event3.setTimeStamp(2L);
 event3.setLoggerContext(loggerContext);
 event3.setRequestURI("/test2");
 event3.setRequestURL("http://test.com/test2");
 event3.setRemoteHost("127.0.0.2");
 event3.setRemoteUser("testuser2");
 event3.setProtocol("HTTP/1.2");
 event3.setMethod("POST");
 event3.setServerName("testserver2.com");
 event3.setRemoteAddress("127.0.0.2");
 event3.setRequestHeaders(new HashMap<>());
 event3.setResponseHeaders(new HashMap<>());
 event3.setRequestParameters(new HashMap<>());
 event3.setLocalPort(8081);
 event3.setStatusCode(201);

 AccessEvent event4 = new AccessEvent();
 event4.setTimeStamp(1L);
 event4.setLoggerContext(loggerContext);
 event4.setRequestURI("/test");
 event4.setRequestURL("http://test.com/test");
 event4.setRemoteHost("127.0.0.1");
 event4.setRemoteUser("testuser");
 event4.setProtocol("HTTP/1.1");
 event4.setMethod("GET");
 event4.setServerName("testserver.com");
 event4.setRemoteAddress("127.0.0.1");
 event4.setRequestHeaders(new HashMap<>());
 event4.setResponseHeaders(new HashMap<>());
 event4.setRequestParameters(new HashMap<>());
 event4.setLocalPort(8080);
 event4.setStatusCode(200);

 AccessEvent event5 = new AccessEvent();
 event5.setTimeStamp(1L);
 event5.setLoggerContext(loggerContext);
 event5.setRequestURI("/test");
 event5.setRequestURL("http://test.com/test");
 event5.setRemoteHost("127.0.0.1");
 event5.setRemoteUser("testuser");
 event5.setProtocol("HTTP/1.1");
 event5.setMethod("GET");
 event5.setServerName("testserver.com");
 event5.setRemoteAddress("127.0.0.2");
 event5.setRequestHeaders(new HashMap<>());
 event5.setResponseHeaders(new HashMap<>());
 event5.setRequestParameters(new HashMap<>());
 event5.setLocalPort(8080);
 event5.setStatusCode(200);

 assertTrue("Equals should return true for equal objects", event1.equals(event4));
 assertTrue("Equals should return true for equal objects after hashCode call", event1.equals(event4) && event1.hashCode() == event4.hashCode());

 assertFalse("Equals should return false for different objects", event1.equals(event2));
 assertFalse("Equals should return false for different objects after hashCode call", event1.equals(event2) && event1.hashCode() == event2.hashCode());

 assertFalse("Equals should return false for different objects with different fields", event1.equals(event3));
 assertFalse("Equals should return false for different objects with different fields after hashCode call", event1.equals(event3) && event1.hashCode() == event3.hashCode());

 assertFalse("Equals should return false for null object", event1.equals(null));
 }
}